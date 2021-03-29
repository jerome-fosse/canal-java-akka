package fr.jfo.examples.canal.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class Title {
    private final String id;
    private final TitleType type;
    private final String primaryTitle;
    private final String originalTitle;
    private final boolean adult;
    private final Integer startYear;
    private final Integer endYear;
    private final Integer runtime;
    private final List<String> genres;
    private final List<CrewMember> crew;

    public Optional<Integer> getEndYear() {
        return Optional.ofNullable(endYear);
    }

}
