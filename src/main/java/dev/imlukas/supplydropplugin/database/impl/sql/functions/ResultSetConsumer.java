package dev.imlukas.supplydropplugin.database.impl.sql.functions;

import java.sql.ResultSet;

@FunctionalInterface
public interface ResultSetConsumer {

    void accept(ResultSet resultSet) throws Exception;
}