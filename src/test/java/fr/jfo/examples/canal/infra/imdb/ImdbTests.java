package fr.jfo.examples.canal.infra.imdb;

import com.typesafe.config.ConfigFactory;
import fr.jfo.examples.canal.domain.model.Person;
import fr.jfo.examples.canal.infra.imdb.adapter.ImdbAdapter;
import fr.jfo.examples.canal.infra.imdb.repository.NameBasicsRepository;
import fr.jfo.examples.canal.infra.imdb.repository.TitleBasicsRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;

public class ImdbTests {
    private static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:11.11")
            .withDatabaseName("canal")
            .withUsername("canal")
            .withPassword("canal")
            .withFileSystemBind("src/db/init", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
            .withExposedPorts(5432)
       ;

    @BeforeAll
    public static void beforeAllTests() throws SQLException, IOException {
        postgreSQLContainer.start();
        System.getProperties().setProperty("canal-imdb.db.properties.url", "jdbc:postgresql://localhost:" + postgreSQLContainer.getMappedPort(5432) + "/canal");
        ConfigFactory.invalidateCaches();
        ConfigFactory.load();
        try (
            var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("infra/imdb/imdb-data-tests.sql");
            var conn = postgreSQLContainer.createConnection("")
        ) {
            var sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            conn.createStatement().execute(sql);
        }
    }

    @AfterAll
    public static void afterAllTests() {
        postgreSQLContainer.stop();
    }

    @Test
    public void when_a_movie_exists_it_should_return_the_list_of_the_names_of_all_the_principals() throws ExecutionException, InterruptedException {
        var imdbAdapter = new ImdbAdapter(new NameBasicsRepository(), new TitleBasicsRepository());

        var persons = imdbAdapter.findPersonsWorkingInATitle("Mulholland Dr.").toCompletableFuture().get();

        assertThat(persons).isNotEmpty();
        assertThat(persons).size().isEqualTo(10);
        assertThat(persons).contains(Person.builder().id("nm0000186").name("David Lynch").build());
        assertThat(persons).contains(Person.builder().id("nm0857620").name("Justin Theroux").build());
        assertThat(persons).contains(Person.builder().id("nm0915208").name("Naomi Watts").build());
        assertThat(persons).doesNotContain(Person.builder().id("nm0000001").name("Fred Astaire").build());
    }

    @Test
    public void when_a_movie_does_not_exists_it_should_return_an_empty_list() throws ExecutionException, InterruptedException {
        var imdbAdapter = new ImdbAdapter(new NameBasicsRepository(), new TitleBasicsRepository());

        var persons = imdbAdapter.findPersonsWorkingInATitle("The Night of the Hunter").toCompletableFuture().get();

        assertThat(persons).isEmpty();
    }
}
