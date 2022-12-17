package ru.sfedu.bibliohub.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sfedu.bibliohub.model.bean.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked"})
public class JdbcUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String tablePrefix = "t_";
    public static final String columnPrefix = "c_";

    // COMMANDS

    private static final String SELECT_ALL_FROM_TABLE = "SELECT * FROM %s;";
    private static final String SELECT_FROM_TABLE_BY_ID = "SELECT * FROM %s WHERE " + columnPrefix + "id = %d;";
    private static final String INSERT_INTO_TABLE_VALUES = "INSERT INTO %s VALUES (%s);";
    private static final String DELETE_FROM_TABLE_BY_ID = "DELETE FROM %s WHERE " + columnPrefix + "id = %d;";
    private static final String UPDATE_TABLE_SET_BY_ID = "UPDATE %s SET %s WHERE " + columnPrefix + "id = %d;";

    private static final String SQL_COMMA = ", ";
    private static final String SQL_VALUE_WRAPPER = "'%s'";
    private static final String SQL_KEY_VALUE_WRAPPER = "%s = '%s'";

    public static <T> String getTableName(Class<T> type) {
        return tablePrefix + type.getSimpleName();
    }

    public static String getColumnName(String key) {
        return columnPrefix + key;
    }

    // SQL

    public static String selectAllFromTable(String tableName) {
        return String.format(SELECT_ALL_FROM_TABLE, tableName);
    }

    public static String selectFromTableById(String tableName, long id) {
        return String.format(SELECT_FROM_TABLE_BY_ID, tableName, id);
    }

    public static <T> String insertIntoTableValues(String tableName, T bean) {
        LinkedHashMap<String, Object> map = objectMapper.convertValue(bean, LinkedHashMap.class);
        return String.format(INSERT_INTO_TABLE_VALUES, tableName, mapToValues(map));
    }

    public static String deleteFromTableById(String tableName, long id) {
        return String.format(DELETE_FROM_TABLE_BY_ID, tableName, id);
    }

    public static <T> String updateTableSetById(String tableName, T bean, long id) {
        LinkedHashMap<String, Object> map = objectMapper.convertValue(bean, LinkedHashMap.class);
        return String.format(UPDATE_TABLE_SET_BY_ID, tableName, mapToKeyValues(map), id);
    }

    // READ

    public static <T> List<T> readData(Class<T> type, ResultSet resultSet) throws SQLException {
        List list = new ArrayList<>();
        if (type.equals(Book.class)) list = readBooks(resultSet);
        return list;
    }

    private static List<Book> readBooks(ResultSet resultSet) throws SQLException {
        List<Book> list = new ArrayList<>();
        while (resultSet.next()) {
            Book balance = new Book();
            balance.setId(resultSet.getLong(1));
            balance.setAuthor(resultSet.getString(2));
            balance.setTitle(resultSet.getString(3));
            balance.setYear(resultSet.getInt(4));
            list.add(balance);
        }
        return list;
    }

    // HELPERS

    private static String mapToValues(LinkedHashMap<String, Object> map) {
        return map.values().stream()
                .map(e -> String.format(SQL_VALUE_WRAPPER, e))
                .collect(Collectors.joining(SQL_COMMA));
    }

    private static String mapToKeyValues(LinkedHashMap<String, Object> map) {
        return map.entrySet().stream()
                .map(e -> String.format(SQL_KEY_VALUE_WRAPPER, getColumnName(e.getKey()), e.getValue()))
                .collect(Collectors.joining(SQL_COMMA));
    }

    private static final String SQL_CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS %s (%s);";

    // CREATE TABLES

    private static final String ID = "id";
    private static final String COLUMN_PRIMARY_KEY = " PRIMARY KEY";
    private static final String COLUMN_TYPE_LONG = " LONG";
    private static final String COLUMN_TYPE_STRING = " VARCHAR";
    private static final String COLUMN_TYPE_INT = " INTEGER";

    public static <T> String createTable(T bean) {
        LinkedHashMap<String, Object> map = objectMapper.convertValue(bean, LinkedHashMap.class);
        return String.format(SQL_CREATE_TABLE_IF_NOT_EXISTS, getTableName(bean.getClass()), mapToColumns(map));
    }

    private static String mapToColumns(LinkedHashMap<String, Object> map) {
        return map.entrySet().stream()
                .map(e -> {
                    if (e.getKey().equals(ID))
                        return getColumnName(e.getKey()) + COLUMN_TYPE_LONG + COLUMN_PRIMARY_KEY;
                    else if (e.getValue().getClass().equals(Integer.class))
                        return getColumnName(e.getKey()) + COLUMN_TYPE_INT;
                    else
                        return getColumnName(e.getKey()) + COLUMN_TYPE_STRING;
                })
                .collect(Collectors.joining(SQL_COMMA));
    }
}
