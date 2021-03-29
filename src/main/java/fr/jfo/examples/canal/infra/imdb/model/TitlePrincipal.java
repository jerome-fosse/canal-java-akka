package fr.jfo.examples.canal.infra.imdb.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class TitlePrincipal {
    private final String tconst;
    private final Integer ordering;
    private final String nconst;
    private final String category;
    private final String job;
    private final List<String> characters;

    public Optional<String> getJob() {
        return Optional.ofNullable(job);
    }

    public Optional<List<String>> getCharacters() {
        return Optional.ofNullable(characters);
    }
}
