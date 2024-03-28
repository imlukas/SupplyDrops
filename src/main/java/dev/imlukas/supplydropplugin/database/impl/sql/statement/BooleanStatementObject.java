package dev.imlukas.supplydropplugin.database.impl.sql.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BooleanStatementObject implements StatementObject
{
    private final boolean bool;

    public BooleanStatementObject(boolean bool) {
        this.bool = bool;
    }

    public static BooleanStatementObject create(boolean bool) {
        return new BooleanStatementObject(bool);
    }

    @Override
    public void applyTo(PreparedStatement statement, int index) throws SQLException {
        statement.setBoolean(index, bool);
    }
}
