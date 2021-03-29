package fr.jfo.examples.canal.infra.imdb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class TitleRatingsImporter extends Importer {

    public TitleRatingsImporter() {
        super ("title-ratings", "target/imdb/title.ratings.tsv.gz");
    }

    @Override
    PreparedStatement processLines(List<Map<String, String>> lines, Connection connection) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO title_ratings VALUES ");
        for (int i = 0; i < lines.size(); i++) {
            sql.append("(?, ?, ?)");
            if (i + 1 < lines.size()) {
                sql.append(", ");
            }
        }
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        for (int i = 0; i < lines.size(); i++) {
            int base = i == 0 ? i : i * 3;
            var values = lines.get(i);
            statement.setString(base + 1, values.get("tconst"));
            statement.setDouble(base + 2, Double.parseDouble(values.get("averageRating")));
            statement.setInt(base + 3, Integer.parseInt(values.get("numVotes")));
        }

        return statement;
    }
}
