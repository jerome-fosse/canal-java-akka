package fr.jfo.examples.canal.infra.imdb.data;

import akka.Done;
import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.alpakka.csv.javadsl.CsvParsing;
import akka.stream.alpakka.csv.javadsl.CsvToMap;
import akka.stream.alpakka.slick.javadsl.Slick;
import akka.stream.alpakka.slick.javadsl.SlickSession;
import akka.stream.alpakka.slick.javadsl.SlickSession$;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.zip.GZIPInputStream;

abstract public class Importer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Importer.class);
    private static final Integer CHUNK_SIZE = 1000;

    private String name;
    private String sourceFile;

    private ActorSystem system;
    private Materializer materializer;
    private SlickSession session;

    protected Importer(String name, String sourceFile) {
        this.name = name;
        this.sourceFile = sourceFile;

        system = ActorSystem.create();
        materializer = Materializer.createMaterializer(system);
        session = SlickSession$.MODULE$.forConfig("canal-imdb");
        system.registerOnTermination(session::close);
    }

    protected void unzip(File source, File dest) {
        if (dest.exists()) {
            dest.delete();
        }

        try (
            InputStream input = new GZIPInputStream(new FileInputStream(source));
            OutputStream output = new FileOutputStream(dest);
        ) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }

        } catch (IOException e) {
            LOGGER.error("Error while importing " + source.getName() + " : " + e.getMessage());
            System.exit(-1);
        }
    }

    public CompletionStage<IOResult> start() {
        var ts1 = new Date().getTime();
        File source = new File(sourceFile);
        File dest = new File("target/imdb/" + name + ".tsv");
        unzip(source, dest);

        byte QUOTE = 0;
        var result = FileIO.fromFile(dest)
            .via(CsvParsing.lineScanner(CsvParsing.TAB, QUOTE, CsvParsing.BACKSLASH))
            .via(CsvToMap.toMapAsStrings(StandardCharsets.UTF_8))
            .grouped(CHUNK_SIZE)
            .via(Slick.flow(
                session,
                5,
                this::processLines
            ))
            .toMat(Sink.fold(0, (acc, inserted) -> {
                if (acc != 0 && acc % 10000 == 0) {
                    LOGGER.info(acc + inserted + " " + name + " imported...");
                }
                return acc + inserted;
            }), Keep.both())
            .run(materializer);

        result.second().whenComplete((inserted, throwable) ->  {
            if (throwable != null) {
                LOGGER.error("Error while importing " + name, throwable);
                system.terminate();
            } else {
                LOGGER.info(inserted + " " + name + " have been imported.");
            }
        });

        return result.first().whenComplete((ioResult, throwable) -> {
            var ts2 = new Date().getTime();
            LOGGER.info(name + " have been imported in " + (ts2 - ts1) + "ms.");
            system.terminate();
        });
    }

    abstract PreparedStatement processLines(List<Map<String, String>> lines, Connection connection) throws SQLException;
}
