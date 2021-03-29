package fr.jfo.examples.canal.application.http;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.ExceptionHandler;
import akka.http.javadsl.server.Route;
import akka.stream.javadsl.Sink;
import fr.jfo.examples.canal.application.http.adapters.TitlesAdapter;
import fr.jfo.examples.canal.application.http.model.Principal;
import fr.jfo.examples.canal.application.http.model.TvSerie;
import fr.jfo.examples.canal.application.http.requests.FindPrincipalsRequest;
import fr.jfo.examples.canal.application.http.responses.FindPrincipalsResponse;
import fr.jfo.examples.canal.application.http.responses.TvSeriesTopTenResponse;
import fr.jfo.examples.canal.domain.TitleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.model.StatusCodes.*;
import static akka.http.javadsl.server.Directives.*;
import static akka.http.javadsl.server.PathMatchers.segment;

public class SimpleHTTPServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHTTPServer.class);

    private final TitlesAdapter titlesAdapter;

    public SimpleHTTPServer(TitlesAdapter titlesAdapter) {
        this.titlesAdapter = titlesAdapter;
    }

    public void start() throws Exception {
        ActorSystem system = ActorSystem.create("routes");
        Http http = Http.get(system);
        final CompletionStage<ServerBinding> binding = http.newServerAt("localhost", 8080)
            .bind(createRoutes());

        LOGGER.info("Server online at http://localhost:8080/");
        LOGGER.info("Press RETURN to stop...");
        System.in.read();

        binding
            .thenCompose(ServerBinding::unbind)
            .thenAccept(done -> system.terminate());
    }

    Route createRoutes() {
        return concat(
            post(() ->
                path(segment("titles").slash("principals").slash("search"), () ->
                    handleExceptions(moviePrincipalsExceptionHandler(), () ->
                        entity(Jackson.unmarshaller(FindPrincipalsRequest.class), request -> {
                            ActorSystem system = ActorSystem.create();
                            CompletionStage<FindPrincipalsResponse> response = titlesAdapter.principalsForTitleName(request.getTitleName())
                                .fold(new ArrayList<Principal>(), this::collectToList)
                                .map(principals -> new FindPrincipalsResponse(request.getTitleName(), principals))
                                .runWith(Sink.head(), system)
                                .whenComplete((findPrincipalsResponse, throwable) -> system.terminate())
                                ;

                            return onSuccess(response, done -> completeOKWithFuture(response.toCompletableFuture(), Jackson.marshaller()));
                        })
                    )
                )
            ),
            get(() ->
                path(segment("tvseries").slash(segment("topten")), () -> {
                    ActorSystem system = ActorSystem.create();
                    CompletionStage<TvSeriesTopTenResponse> response = titlesAdapter.tvSeriesWithGreatestNumberOfEpisodes()
                        .fold(new ArrayList<TvSerie>(), this::collectToList)
                        .map(TvSeriesTopTenResponse::new)
                        .runWith(Sink.head(), system)
                        .whenComplete((tvSeriesTopTenResponse, throwable) -> system.terminate())
                        ;

                    return onSuccess(response, done -> completeOKWithFuture(response.toCompletableFuture(), Jackson.marshaller()));
                })
            )
        );
    }

    private <E> ArrayList<E> collectToList(ArrayList<E> acc, E elem) {
        acc.add(elem);
        return acc;
    }

    private ExceptionHandler moviePrincipalsExceptionHandler() {
        return ExceptionHandler.newBuilder()
            .match(TitleNotFoundException.class, ex -> complete(NOT_FOUND, ex.getMessage()))
            .match(Throwable.class, ex -> complete(INTERNAL_SERVER_ERROR, ex.getMessage()))
            .build()
            ;
    }
}
