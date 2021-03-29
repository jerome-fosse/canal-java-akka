package fr.jfo.examples.canal.application.http.adapters;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import fr.jfo.examples.canal.application.http.model.Principal;
import fr.jfo.examples.canal.application.http.model.TvSerie;
import fr.jfo.examples.canal.domain.api.PersonApi;
import fr.jfo.examples.canal.domain.api.TitlesApi;
import fr.jfo.examples.canal.domain.model.Person;
import fr.jfo.examples.canal.domain.model.Title;

import java.util.ArrayList;
import java.util.List;

public class TitlesAdapter {
    private TitlesApi titlesApi;
    private PersonApi personApi;

    public TitlesAdapter(TitlesApi titlesApi, PersonApi personApi) {
        this.titlesApi = titlesApi;
        this.personApi = personApi;
    }

    public Source<Principal, NotUsed> principalsForTitleName(String titleName) {
        var future = personApi.findCrewMembersForTitleWithName(titleName)
            .thenApply(persons -> {
                List<Principal> principals = new ArrayList<>();
                for (Person person : persons) {
                    principals.add(new Principal(
                        person.getName(),
                        person.getBirthYear(),
                        person.getDeathYear().orElse(null),
                        person.getProfessions()
                    ));
                }
                return principals;
            }
        );

        return Source.fromCompletionStage(future)
            .flatMapConcat(Source::from)
            ;
    }

    public Source<TvSerie, NotUsed> tvSeriesWithGreatestNumberOfEpisodes() {
        var future =  titlesApi.findTvSeriesWithMostEpisodesLimitedBy(10)
            .thenApply(titles -> {
                List<TvSerie> tvSeries = new ArrayList<>();
                for (Title title : titles) {
                    tvSeries.add(
                        new TvSerie(
                            title.getOriginalTitle(),
                            title.getStartYear(),
                            title.getEndYear().orElse(null),
                            title.getGenres()
                        )
                    );
                }

                return tvSeries;
            });

        return Source.fromCompletionStage(future)
            .flatMapConcat(Source::from)
            ;
    }
}
