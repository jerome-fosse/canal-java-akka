package fr.jfo.examples.canal.infra.imdb.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class NameBasic {
    private final String nconst;
    private final String primaryName;
    private final Integer birthYear;
    private final Integer deathYear;
    private final List<String> primaryProfession;
    private final List<String> knowForTitles;

    public Optional<Integer> getBirthYear() {
        return Optional.ofNullable(birthYear);
    }

    public Optional<Integer> getDeathYear() {
        return Optional.ofNullable(deathYear);
    }

    public Optional<List<String>> getPrimaryProfession() {
        return Optional.ofNullable(primaryProfession);
    }
    public Optional<List<String>> getKnowForTitles() {
        return Optional.ofNullable(knowForTitles);
    }
}
