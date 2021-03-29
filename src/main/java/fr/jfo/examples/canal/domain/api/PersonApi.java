package fr.jfo.examples.canal.domain.api;

import fr.jfo.examples.canal.domain.model.Person;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface PersonApi {
    CompletionStage<List<Person>> findCrewMembersForTitleWithName(String name);
}
