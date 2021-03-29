package fr.jfo.examples.canal.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder
@EqualsAndHashCode(of = {"id", "name"})
public class Person {
    private final String id;
    private final String name;
    private final Integer birthYear;
    private final Integer deathYear;
    private final List<String> professions;

    public Optional<Integer> getDeathYear() {
        return Optional.ofNullable(deathYear);
    }
}
