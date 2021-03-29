package fr.jfo.examples.canal.infra.imdb.model;

import lombok.Value;

import java.util.Optional;

@Value
public class TitleEpisode {
    private final String tconst;
    private final String parentTconst;
    private final Integer seasonNumber;
    private final Integer episodeNumber;

    public Optional<Integer> getSeasonNumber() {
        return Optional.ofNullable(seasonNumber);
    }

    public Optional<Integer> getEpisodeNumber() {
        return Optional.ofNullable(episodeNumber);
    }
}
