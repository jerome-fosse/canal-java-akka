package fr.jfo.examples.canal.application.http;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import fr.jfo.examples.canal.application.http.adapters.TitlesAdapter;
import fr.jfo.examples.canal.domain.TitleNotFoundException;
import fr.jfo.examples.canal.domain.model.Person;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static akka.http.javadsl.model.HttpRequest.POST;

public class HttpRoutesTest extends JUnitRouteTest {

    TestRoute routes = testRoute(new SimpleHTTPServer(
        new TitlesAdapter(
            limit -> null,
            name -> {
                if (name.equals("The Last of the Mohicans")) {
                    return CompletableFuture.completedFuture(List.of(
                        Person.builder().id("1").name("Daniel Day-Lewis").birthYear(1957).professions(List.of("actor", "music_department", "soundtrack")).build(),
                        Person.builder().id("2").name("Eric Schweig").birthYear(1967).professions(List.of("actor")).build(),
                        Person.builder().id("3").name("Madeleine Stowe").birthYear(1958).professions(List.of("actress", "director", "writer")).build(),
                        Person.builder().id("4").name("Maurice Tourneur").birthYear(1876).deathYear(1961).professions(List.of("director", "writer", "producer")).build(),
                        Person.builder().id("5").name("Michael Mann").birthYear(1943).professions(List.of("producer", "writer", "director")).build()
                    ));
                } else {
                    return CompletableFuture.failedFuture(new TitleNotFoundException("Title " + name + " not found."));
                }
            }
        )
    ).createRoutes());

    //@Test
    @Ignore("java.lang.NullPointerException: Cannot invoke \"akka.actor.ClassicActorSystemProvider.classicSystem()\" because \"system\" is null")
    public void when_a_movie_exist_it_should_return_the_list_of_the_principals_and_status_code_is_OK() {
        routes.run(POST("/movies/principals/search").withEntity("{movieTitle:\"The Last of the Mohicans\"}"))
            .assertStatusCode(StatusCodes.OK)
        ;
    }

    //@Test
    @Ignore("java.lang.NullPointerException: Cannot invoke \"akka.actor.ClassicActorSystemProvider.classicSystem()\" because \"system\" is null")
    public void when_a_movie_does_not_exist_status_code_should_be_NOT_FOUND() {
        routes.run(POST("/movies/principals/search").withEntity("{movieTitle:\"The Matrix\"}"))
            .assertStatusCode(StatusCodes.NOT_FOUND);
    }

}
