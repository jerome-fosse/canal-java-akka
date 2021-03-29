package fr.jfo.examples.canal;

import fr.jfo.examples.canal.infra.imdb.data.*;

public class DataImporter {
    public static void main(String[] args) {
        NameBasicsImporter nameBasicsImporter = new NameBasicsImporter();
        TitleBasicsImporter titleBasicsSqlImporter = new TitleBasicsImporter();
        TitleEpisodesImporter titleEpisodesImporter = new TitleEpisodesImporter();
        TitlePrincipalsImporter titlePrincipalsImporter = new TitlePrincipalsImporter();
        TitleRatingsImporter titleRatingsImporter = new TitleRatingsImporter();

        nameBasicsImporter.start().thenCompose(ioResult ->
                titleBasicsSqlImporter.start().thenCompose(ioResult1 ->
                        titleEpisodesImporter.start().thenCompose(ioResult2 ->
                                titlePrincipalsImporter.start().thenCompose(ioResult3 ->
                                        titleRatingsImporter.start()
                                    )
                            )
                    )
            );
    }
}
