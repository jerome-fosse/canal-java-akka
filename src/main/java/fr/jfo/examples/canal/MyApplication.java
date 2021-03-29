package fr.jfo.examples.canal;

import fr.jfo.examples.canal.application.http.SimpleHTTPServer;
import fr.jfo.examples.canal.application.http.adapters.TitlesAdapter;
import fr.jfo.examples.canal.domain.PersonsService;
import fr.jfo.examples.canal.domain.TitlesService;
import fr.jfo.examples.canal.infra.imdb.adapter.ImdbAdapter;
import fr.jfo.examples.canal.infra.imdb.repository.NameBasicsRepository;
import fr.jfo.examples.canal.infra.imdb.repository.TitleBasicsRepository;

public class MyApplication {

    public static void main(String[] args) throws Exception {
        ImdbAdapter imdbAdapter = new ImdbAdapter(
            new NameBasicsRepository(),
            new TitleBasicsRepository()
        );

        TitlesAdapter titlesAdapter = new TitlesAdapter(
            new TitlesService(imdbAdapter),
            new PersonsService(imdbAdapter)
        );

        SimpleHTTPServer server = new SimpleHTTPServer(titlesAdapter);
        server.start();
    }
}
