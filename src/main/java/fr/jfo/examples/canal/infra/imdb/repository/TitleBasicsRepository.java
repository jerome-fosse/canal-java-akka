package fr.jfo.examples.canal.infra.imdb.repository;

import akka.NotUsed;
import akka.stream.alpakka.slick.javadsl.Slick;
import akka.stream.alpakka.slick.javadsl.SlickSession;
import akka.stream.javadsl.Source;
import fr.jfo.examples.canal.NotImplementedException;
import fr.jfo.examples.canal.infra.imdb.model.TitleBasic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TitleBasicsRepository implements Repository<TitleBasic> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TitleBasicsRepository.class);

    private SlickSession session;

    public TitleBasicsRepository session(SlickSession session) {
        this.session = session;
        return this;
    }

    @Override
    public Source<TitleBasic, NotUsed> findById(String id) {
        throw new NotImplementedException();
    }

    @Override
    public void save(TitleBasic titleBasic) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(TitleBasic titleBasic) {
        throw new NotImplementedException();
    }

    public Source<TitleBasic, NotUsed> findTvSeriesWithGreatestNumberOfEpisodes(int limit) {
        LOGGER.debug("Finding {} first series with the greatest number of episodes.", limit);

        return Slick.source(
            session,
            String.format(
                """
                SELECT tconst, title_type, primary_title, original_title, is_adult, start_year, end_year, runtime_minutes, genres, te.episode_number_total
                FROM title_basics
                JOIN (
                    SELECT parent_tconst, COUNT(*) AS episode_number_total
                    FROM title_episodes
                    GROUP BY parent_tconst
                    ORDER BY episode_number_total DESC
                    limit %d
                ) te ON title_basics.tconst = te.parent_tconst
                """, limit),
            row -> TitleBasic.builder()
                .tconst(row.nextString())
                .titleType(row.nextString())
                .primaryTitle(row.nextString())
                .originalTitle(row.nextString())
                .adult(row.nextBoolean())
                .startYear(row.nextInt())
                .endYear(row.nextInt())
                .runtimeMinutes(row.nextInt())
                .genres(List.of(row.nextString().split(",")))
                .build()
        );
    }
}
