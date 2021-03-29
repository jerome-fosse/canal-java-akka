package fr.jfo.examples.canal.domain;

import fr.jfo.examples.canal.domain.api.PersonApi;
import fr.jfo.examples.canal.domain.model.Person;
import fr.jfo.examples.canal.domain.spi.Persons;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class PersonsService implements PersonApi {
    private final Persons persons;

    public PersonsService(Persons persons) {
        this.persons = persons;
    }

    @Override
    public CompletionStage<List<Person>> findCrewMembersForTitleWithName(String name) {
        return persons.findPersonsWorkingInATitle(name)
            .thenApply(peoples -> {
                if (peoples.size() == 0) {
                    throw new TitleNotFoundException("The title " + name + " has not been found.");
                }

                return peoples;
            });
    }
}
