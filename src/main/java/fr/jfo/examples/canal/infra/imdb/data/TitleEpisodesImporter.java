package fr.jfo.examples.canal.infra.imdb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class TitleEpisodesImporter extends Importer {

    public TitleEpisodesImporter() {
        super ("title-episodes", "target/imdb/title.episode.tsv.gz");
    }

    @Override
    PreparedStatement processLines(List<Map<String, String>> lines, Connection connection) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO title_episodes VALUES ");
        for (int i = 0; i < lines.size(); i++) {
            sql.append("(?, ?, ?, ?)");
            if (i + 1 < lines.size()) {
                sql.append(", ");
            }
        }
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        for (int i = 0; i < lines.size(); i++) {
            int base = i == 0 ? i : i * 4;
            var values = lines.get(i);
            statement.setString(base + 1, values.get("tconst"));
            statement.setString(base + 2, values.get("parentTconst"));
            if (values.get("seasonNumber").equals("\\N")) {
                statement.setNull(base + 3, Types.INTEGER);
            } else {
                statement.setInt(base + 3, Integer.parseInt(values.get("seasonNumber")));
            }
            if (values.get("episodeNumber").equals("\\N")) {
                statement.setNull(base + 4, Types.INTEGER);
            } else {
                statement.setInt(base + 4, Integer.parseInt(values.get("episodeNumber")));
            }
        }

        return statement;
    }
}
