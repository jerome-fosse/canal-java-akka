package fr.jfo.examples.canal.application.http.model;

import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
public class Principal {
    private final String name;
    private final Integer birthYear;
    private final Integer deathYear;
    private final List<String> professions;
}
