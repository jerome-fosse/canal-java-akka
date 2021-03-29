package fr.jfo.examples.canal.infra.imdb.repository;

import akka.NotUsed;
import akka.stream.alpakka.slick.javadsl.Slick;
import akka.stream.alpakka.slick.javadsl.SlickSession;
import akka.stream.javadsl.Source;
import fr.jfo.examples.canal.NotImplementedException;
import fr.jfo.examples.canal.infra.imdb.model.NameBasic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NameBasicsRepository implements Repository<NameBasic> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NameBasicsRepository.class);

    private SlickSession session;

    public NameBasicsRepository session(SlickSession session) {
        this.session = session;
        return this;
    }

    @Override
    public Source<NameBasic, NotUsed> findById(String id) {
        throw new NotImplementedException();
    }

    @Override
    public void save(NameBasic nameBasic) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(NameBasic nameBasic) {
        throw new NotImplementedException();
    }

    public Source<NameBasic, NotUsed> findNamesForTitle(String movieTitle) {
        LOGGER.debug("Finding title with original title {}.", movieTitle);

        return Slick.source(
            session,
            String.format(
                """
                SELECT DISTINCT nb.nconst, primary_name, birth_year, death_year, primary_profession
                FROM name_basics nb
                JOIN title_principals tp ON nb.nconst = tp.nconst
                JOIN title_basics tb ON tp.tconst = tb.tconst
                WHERE tb.original_title = '%s'
                ORDER BY primary_name
                """
                , movieTitle
            ),
            row -> NameBasic.builder()
                .nconst(row.nextString())
                .primaryName(row.nextString())
                .birthYear(row.nextInt())
                .deathYear(row.nextInt())
                .primaryProfession(List.of(row.nextString().split(",")))
                .build()
        );
    }

}
