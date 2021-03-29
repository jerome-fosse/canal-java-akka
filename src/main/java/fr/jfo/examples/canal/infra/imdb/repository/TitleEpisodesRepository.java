package fr.jfo.examples.canal.infra.imdb.repository;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import fr.jfo.examples.canal.NotImplementedException;
import fr.jfo.examples.canal.infra.imdb.model.TitleEpisode;

public class TitleEpisodesRepository implements Repository<TitleEpisode> {
    @Override
    public Source<TitleEpisode, NotUsed> findById(String id) {
        throw new NotImplementedException();
    }

    @Override
    public void save(TitleEpisode titleEpisode) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(TitleEpisode titleEpisode) {
        throw new NotImplementedException();
    }
}
