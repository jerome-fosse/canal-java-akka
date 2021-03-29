package fr.jfo.examples.canal.infra.imdb.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class TitleBasic {
    private final String tconst;
    private final String titleType;
    private final String primaryTitle;
    private final String originalTitle;
    private final boolean adult;
    private final Integer startYear;
    private final Integer endYear;
    private final Integer runtimeMinutes;
    private final List<String> genres;

    public Optional<Integer> getStartYear() {
        return Optional.ofNullable(startYear);
    }

    public Optional<Integer> getEndYear() {
        return Optional.ofNullable(endYear);
    }

    public Optional<Integer> getRuntimeMinutes() {
        return Optional.ofNullable(runtimeMinutes);
    }
}
