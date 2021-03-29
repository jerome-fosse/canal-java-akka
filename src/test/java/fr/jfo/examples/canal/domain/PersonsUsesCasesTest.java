package fr.jfo.examples.canal.domain;

import fr.jfo.examples.canal.domain.api.PersonApi;
import fr.jfo.examples.canal.domain.model.Person;
import fr.jfo.examples.canal.domain.spi.Persons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PersonsUsesCasesTest {

    private PersonApi api;

    @BeforeEach
    public void beforeEachTest() {
        api = new PersonsService(new Persons() {
            @Override
            public CompletionStage<List<Person>> findPersonsWorkingInATitle(String movieTitle) {
                if (movieTitle.equals("The Last of the Mohicans")) {
                    return CompletableFuture.completedFuture(List.of(
                        Person.builder().id("1").name("Daniel Day-Lewis").birthYear(1957).professions(List.of("actor", "music_department", "soundtrack")).build(),
                        Person.builder().id("2").name("Eric Schweig").birthYear(1967).professions(List.of("actor")).build(),
                        Person.builder().id("3").name("Madeleine Stowe").birthYear(1958).professions(List.of("actress", "director", "writer")).build(),
                        Person.builder().id("4").name("Maurice Tourneur").birthYear(1876).deathYear(1961).professions(List.of("director", "writer", "producer")).build(),
                        Person.builder().id("5").name("Michael Mann").birthYear(1943).professions(List.of("producer", "writer", "director")).build()
                    ));
                } else {
                    return CompletableFuture.failedFuture(new TitleNotFoundException(movieTitle + " does not exists"));
                }
            }
        });
    }

    @Test
    public void when_the_movie_exists_it_should_return_the_list_of_the_persons_of_the_crew() throws ExecutionException, InterruptedException {
        // Given an existing movie
        String movieTitle = "The Last of the Mohicans";

        // When requesting crew members
        List<Person> persons = api.findCrewMembersForTitleWithName(movieTitle).toCompletableFuture().get();

        // Then We should receive the list of the persons
        assertThat(persons).isNotEmpty();
        assertThat(persons).size().isEqualTo(5);
        assertThat(persons.get(0).getName()).isEqualTo("Daniel Day-Lewis");
        assertThat(persons.get(1).getName()).isEqualTo("Eric Schweig");
        assertThat(persons.get(2).getName()).isEqualTo("Madeleine Stowe");
        assertThat(persons.get(3).getName()).isEqualTo("Maurice Tourneur");
        assertThat(persons.get(4).getName()).isEqualTo("Michael Mann");
    }

    @Test
    public void when_the_movie_does_not_exists_it_should_raise_an_error() throws ExecutionException, InterruptedException {
        // Given a movie that does not exists
        String movieTitle = "The Last King of Scotland";

        // When requesting crew members
        var thrown = catchThrowable(() -> api.findCrewMembersForTitleWithName(movieTitle).toCompletableFuture().get());

        // Then we should receive an error
        assertThat(thrown).isNotNull();
        assertThat(thrown).isInstanceOf(ExecutionException.class);
        assertThat(thrown.getCause()).isInstanceOf(TitleNotFoundException.class);
    }
}
