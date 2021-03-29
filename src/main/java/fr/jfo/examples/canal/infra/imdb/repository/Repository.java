package fr.jfo.examples.canal.infra.imdb.repository;

import akka.NotUsed;
import akka.stream.javadsl.Source;

public interface Repository<E> {

    Source<E, NotUsed> findById(String id);

    void save(E entity);

    void delete (E entity);
}
