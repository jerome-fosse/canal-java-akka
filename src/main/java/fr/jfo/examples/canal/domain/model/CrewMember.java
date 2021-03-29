package fr.jfo.examples.canal.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
public class CrewMember {
    private final String id;
    private final Integer order;
    private final Person person;
    private final String category;
    private final String profession;
    private final String character;

    public Optional<String> getProfession() {
        return Optional.ofNullable(profession);
    }

    public Optional<String> getCharacter() {
        return Optional.ofNullable(character);
    }
}
