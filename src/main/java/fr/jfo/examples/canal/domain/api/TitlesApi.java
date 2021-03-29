package fr.jfo.examples.canal.domain.api;

import fr.jfo.examples.canal.domain.model.Title;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface TitlesApi {
    CompletionStage<List<Title>> findTvSeriesWithMostEpisodesLimitedBy(int limit);
}
