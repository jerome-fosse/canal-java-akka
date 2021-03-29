package fr.jfo.examples.canal.infra.imdb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class TitlePrincipalsImporter extends Importer {

    public TitlePrincipalsImporter() {
        super ("title-principals", "target/imdb/title.principals.tsv.gz");
    }

    @Override
    PreparedStatement processLines(List<Map<String, String>> lines, Connection connection) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO title_principals VALUES ");
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
            statement.setString(base + 1, values.get("tconst"));
            statement.setInt(base + 2, Integer.parseInt(values.get("ordering")));
            statement.setString(base + 3, values.get("nconst"));
            statement.setString(base + 4, values.get("category"));
            if (values.get("job").equals("\\N")) {
                statement.setNull(base + 5, Types.CHAR);
            } else {
                statement.setString(base + 5, values.get("job"));
            }
            if (values.get("characters").equals("\\N")) {
                statement.setNull(base + 6, Types.CHAR);
            } else {
                statement.setString(base + 6, values.get("characters"));
            }
        }

        return statement;
    }
}
