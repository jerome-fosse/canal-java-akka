package fr.jfo.examples.canal.infra.imdb.repository;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import fr.jfo.examples.canal.NotImplementedException;
import fr.jfo.examples.canal.infra.imdb.model.TitleRating;

public class TitleRatingRepository implements Repository<TitleRating> {
    @Override
    public Source<TitleRating, NotUsed> findById(String id) {
        throw new NotImplementedException();
    }

    @Override
    public void save(TitleRating titleRating) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(TitleRating titleRating) {
        throw new NotImplementedException();
    }
}
