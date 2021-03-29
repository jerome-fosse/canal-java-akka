package fr.jfo.examples.canal.application.http.responses;

import fr.jfo.examples.canal.application.http.model.TvSerie;
import lombok.Value;

import java.util.List;

@Value
public class TvSeriesTopTenResponse {
    private final List<TvSerie> tvSeries;
}
