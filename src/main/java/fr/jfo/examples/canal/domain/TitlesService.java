package fr.jfo.examples.canal.domain;

import fr.jfo.examples.canal.domain.api.TitlesApi;
import fr.jfo.examples.canal.domain.model.Person;
import fr.jfo.examples.canal.domain.model.Title;
import fr.jfo.examples.canal.domain.spi.Persons;
import fr.jfo.examples.canal.domain.spi.Titles;

import java.util.List;
import java.util.concurrent.CompletionStage;


public class TitlesService implements TitlesApi {

    private final Titles titles;

    public TitlesService(Titles titles) {
        this.titles = titles;
    }

    public CompletionStage<List<Title>> findTvSeriesWithMostEpisodesLimitedBy(int limit) {
        return titles.findTvSeriesSortedByNumberOfEpisodesAndLimitBy(limit);
    }
}
