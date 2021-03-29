package fr.jfo.examples.canal.infra.imdb.adapter;

import akka.actor.ActorSystem;
import akka.stream.alpakka.slick.javadsl.SlickSession;
import akka.stream.alpakka.slick.javadsl.SlickSession$;
import akka.stream.javadsl.Sink;
import fr.jfo.examples.canal.domain.model.Person;
import fr.jfo.examples.canal.domain.model.Title;
import fr.jfo.examples.canal.domain.model.TitleType;
import fr.jfo.examples.canal.domain.spi.Persons;
import fr.jfo.examples.canal.domain.spi.Titles;
import fr.jfo.examples.canal.infra.imdb.repository.NameBasicsRepository;
import fr.jfo.examples.canal.infra.imdb.repository.TitleBasicsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class ImdbAdapter implements Titles, Persons {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImdbAdapter.class);

    private NameBasicsRepository nameBasicsRepository;
    private TitleBasicsRepository titleBasicsRepository;

    public ImdbAdapter(NameBasicsRepository nameBasicsRepository, TitleBasicsRepository titleBasicsRepository) {
        this.nameBasicsRepository = nameBasicsRepository;
        this.titleBasicsRepository = titleBasicsRepository;
    }

    @Override
    public CompletionStage<List<Title>> findTvSeriesSortedByNumberOfEpisodesAndLimitBy(int limit) {
        SlickSession session = SlickSession$.MODULE$.forConfig("canal-imdb");
        ActorSystem system = ActorSystem.create();
        system.registerOnTermination(session::close);

        return titleBasicsRepository.session(session).findTvSeriesWithGreatestNumberOfEpisodes(10)
            .map(titleBasic -> Title.builder()
                .id(titleBasic.getTconst())
                .type(TitleType.valueOf(titleBasic.getTitleType()))
                .startYear(titleBasic.getStartYear().orElse(null))
                .endYear(titleBasic.getEndYear().orElse(null))
                .genres(titleBasic.getGenres())
                .adult(titleBasic.isAdult())
                .primaryTitle(titleBasic.getPrimaryTitle())
                .originalTitle(titleBasic.getOriginalTitle())
                .runtime(titleBasic.getRuntimeMinutes().orElse(null))
                .build()
            )
            .runWith(Sink.seq(), system)
            .whenComplete((titles, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("An error occurred.", throwable);
                }

                LOGGER.debug("Closing database session.");
                system.terminate();
            })
            ;
    }

    @Override
    public CompletionStage<List<Person>> findPersonsWorkingInATitle(String titleName) {
        SlickSession session = SlickSession$.MODULE$.forConfig("canal-imdb");
        ActorSystem system = ActorSystem.create();
        system.registerOnTermination(session::close);

        return nameBasicsRepository.session(session).findNamesForTitle(titleName)
            .map(name -> Person.builder()
                .id(name.getNconst())
                .name(name.getPrimaryName())
                .birthYear(name.getBirthYear().orElse(null))
                .deathYear(name.getDeathYear().orElse(null))
                .professions(name.getPrimaryProfession().orElse(List.of()))
                .build()
            )
            .runWith(Sink.seq(), system)
            .whenComplete((titles, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("An error occurred.", throwable);
                }

                LOGGER.debug("Closing database session.");
                system.terminate();
            })
            ;
    }
}
