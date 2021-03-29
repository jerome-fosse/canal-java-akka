package fr.jfo.examples.canal.application.http.model;

import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
public class TvSerie {
    private final String original;
    private final Integer startYear;
    private final Integer endYear;
    private final List<String> genres;
}
