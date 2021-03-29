package fr.jfo.examples.canal.domain.spi;

import fr.jfo.examples.canal.domain.model.Title;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface Titles {

    CompletionStage<List<Title>> findTvSeriesSortedByNumberOfEpisodesAndLimitBy(int limit);
}
