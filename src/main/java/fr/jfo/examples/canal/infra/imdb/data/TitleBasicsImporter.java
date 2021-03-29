package fr.jfo.examples.canal.infra.imdb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class TitleBasicsImporter extends Importer {

    public TitleBasicsImporter() {
        super ("title-basics", "target/imdb/title.basics.tsv.gz");
    }

    @Override
    PreparedStatement processLines(List<Map<String, String>> lines, Connection connection) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO title_basics VALUES ");
        for (int i = 0; i < lines.size(); i++) {
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            if (i + 1 < lines.size()) {
                sql.append(", ");
            }
        }
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        for (int i = 0; i < lines.size(); i++) {
            int base = i == 0 ? i : i * 9;
            var values = lines.get(i);
            statement.setString(base + 1, values.get("tconst"));
            statement.setString(base + 2, values.get("titleType").toUpperCase());
            statement.setString(base + 3, values.get("primaryTitle"));
            statement.setString(base + 4, values.get("originalTitle"));
            statement.setBoolean(base + 5, values.get("isAdult").equals("1"));
            if (values.get("startYear").equals("\\N")) {
                statement.setNull(base + 6, Types.INTEGER);
            } else {
                statement.setInt(base + 6, Integer.parseInt(values.get("startYear")));
            }
            if (values.get("endYear").equals("\\N")) {
                statement.setNull(base + 7, Types.INTEGER);
            } else {
                statement.setInt(base + 7, Integer.parseInt(values.get("endYear")));
            }
            if (values.get("runtimeMinutes").equals("\\N")) {
                statement.setNull(base + 8, Types.INTEGER);
            } else {
                statement.setInt(base + 8, Integer.parseInt(values.get("runtimeMinutes")));
            }
            statement.setString(base + 9, values.get("genres"));
        }

        return statement;
    }
}
