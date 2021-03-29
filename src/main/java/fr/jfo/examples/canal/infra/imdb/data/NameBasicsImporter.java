package fr.jfo.examples.canal.infra.imdb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class NameBasicsImporter extends Importer {

    public NameBasicsImporter() {
        super("name-basics", "target/imdb/name.basics.tsv.gz");
    }

    @Override
    PreparedStatement processLines(List<Map<String, String>> lines, Connection connection) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO name_basics VALUES ");
        for (int i = 0; i < lines.size(); i++) {
            sql.append("(?, ?, ?, ?, ?, ?)");
            if (i + 1 < lines.size()) {
                sql.append(", ");
            }
        }
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        for (int i = 0; i < lines.size(); i++) {
            int base = i == 0 ? i : i * 6;
            var values = lines.get(i);
            statement.setString(base + 1, values.get("nconst"));
            statement.setString(base + 2, values.get("primaryName"));
            if (values.get("birthYear").equals("\\N")) {
                statement.setNull(base + 3, Types.INTEGER);
            } else {
                statement.setInt(base + 3, Integer.parseInt(values.get("birthYear")));
            }
            if (values.get("deathYear").equals("\\N")) {
                statement.setNull(base + 4, Types.INTEGER);
            } else {
                statement.setInt(base + 4, Integer.parseInt(values.get("deathYear")));
            }
            statement.setString(base + 5, values.get("primaryProfession"));
            if (values.get("knownForTitles").equals("\\N")) {
                statement.setNull(base + 6, Types.CHAR);
            } else {
                statement.setString(base + 6, values.get("knownForTitles"));
            }
        }

        return statement;
    }
}
