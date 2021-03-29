package fr.jfo.examples.canal.infra.imdb.repository;

import akka.NotUsed;
import akka.stream.alpakka.slick.javadsl.SlickSession;
import akka.stream.javadsl.Source;
import fr.jfo.examples.canal.NotImplementedException;
import fr.jfo.examples.canal.infra.imdb.model.TitlePrincipal;

public class TitlePrincipalsRepository implements Repository<TitlePrincipal> {
    private SlickSession session;

    public TitlePrincipalsRepository session(SlickSession session) {
        this.session = session;
        return this;
    }

    @Override
    public Source<TitlePrincipal, NotUsed> findById(String id) {
        throw new NotImplementedException();
    }

    @Override
    public void save(TitlePrincipal titlePrincipal) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(TitlePrincipal titlePrincipal) {
        throw new NotImplementedException();
    }
}
