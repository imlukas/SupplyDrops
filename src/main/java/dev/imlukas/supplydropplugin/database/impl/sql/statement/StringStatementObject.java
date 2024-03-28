package dev.imlukas.supplydropplugin.database.impl.sql.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringStatementObject implements StatementObject {

    private final String value;

    public StringStatementObject(String value) {
        this.value = value;
    }

    public static StringStatementObject create(String value) {
        return new StringStatementObject(value);
    }

    @Override
    public void applyTo(PreparedStatement statement, int index) throws SQLException {
        statement.setString(index, value);
    }
}