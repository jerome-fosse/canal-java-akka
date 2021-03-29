package fr.jfo.examples.canal.infra.imdb.model;

import lombok.Value;

@Value
public class TitleRating {
    private final String tconst;
    private final Float averageRating;
    private final Integer numVotes;
}
