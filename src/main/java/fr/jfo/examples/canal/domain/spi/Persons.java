package fr.jfo.examples.canal.domain.spi;


import fr.jfo.examples.canal.domain.model.Person;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface Persons {

    CompletionStage<List<Person>> findPersonsWorkingInATitle(String titleName);
}
