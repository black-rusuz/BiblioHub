package ru.sfedu.bibliohub.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sfedu.bibliohub.model.bean.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("Unchecked")
public class JdbcUtil {

    // COMMON

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String tablePrefix = "T_";
    private static final String columnPrefix = "C_";

    private static <T> LinkedHashMap<String, Object> getMap(T bean) {
        return objectMapper.convertValue(bean, LinkedHashMap.class);
    }

    public static <T> String getTableName(Class<T> type) {
        return tablePrefix + type.getSimpleName();
    }

    public static String getColumnName(String key) {
        return columnPrefix + key;
    }

    // CREATE TABLES

    private static final String SQL_CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS %s (%s);";

    private static final String ID = "id";
    private static final String COLUMN_PRIMARY_KEY = " PRIMARY KEY";
    private static final String COLUMN_TYPE_LONG = " LONG";
    private static final String COLUMN_TYPE_STRING = " VARCHAR";
    private static final String COLUMN_TYPE_INT = " INTEGER";

    public static <T> String createTable(T bean) {
        LinkedHashMap<String, Object> map = getMap(bean);
        return String.format(SQL_CREATE_TABLE_IF_NOT_EXISTS, getTableName(bean.getClass()), mapToColumns(map));
    }

    private static String mapToColumns(LinkedHashMap<String, Object> map) {
        return map.entrySet().stream()
                .map(JdbcUtil::mapper)
                .collect(Collectors.joining(SQL_COMMA));
    }

    private static String mapper(Map.Entry<String, Object> entry) {
        if (entry.getKey().equals(ID))
            return getColumnName(entry.getKey()) + COLUMN_TYPE_LONG + COLUMN_PRIMARY_KEY;
        else if (entry.getValue().getClass() == Integer.class)
            return getColumnName(entry.getKey()) + COLUMN_TYPE_INT;
        else
            return getColumnName(entry.getKey()) + COLUMN_TYPE_STRING;
    }

    // COMMANDS

    private static final String SELECT_ALL_FROM_TABLE = "SELECT * FROM %s;";
    private static final String SELECT_FROM_TABLE_BY_ID = "SELECT * FROM %s WHERE " + getColumnName(ID) + " = %d;";
    private static final String INSERT_INTO_TABLE_VALUES = "INSERT INTO %s VALUES (%s);";
    private static final String DELETE_FROM_TABLE_BY_ID = "DELETE FROM %s WHERE " + getColumnName(ID) + " = %d;";
    private static final String UPDATE_TABLE_SET_BY_ID = "UPDATE %s SET %s WHERE " + getColumnName(ID) + " = %d;";

    // SQL

    public static <T> String selectAllFromTable(Class<T> type) {
        String tableName = JdbcUtil.getTableName(type);
        return String.format(SELECT_ALL_FROM_TABLE, tableName);
    }

    public static <T> String selectFromTableById(Class<T> type, long id) {
        String tableName = JdbcUtil.getTableName(type);
        return String.format(SELECT_FROM_TABLE_BY_ID, tableName, id);
    }

    public static <T> String insertIntoTableValues(T bean) {
        String tableName = JdbcUtil.getTableName(bean.getClass());
        LinkedHashMap<String, Object> map = getMap(bean);
        return String.format(INSERT_INTO_TABLE_VALUES, tableName, mapToValues(map));
    }

    public static <T> String deleteFromTableById(T bean, long id) {
        String tableName = JdbcUtil.getTableName(bean.getClass());
        return String.format(DELETE_FROM_TABLE_BY_ID, tableName, id);
    }

    public static <T> String updateTableSetById(T bean, long id) {
        String tableName = JdbcUtil.getTableName(bean.getClass());
        LinkedHashMap<String, Object> map = getMap(bean);
        return String.format(UPDATE_TABLE_SET_BY_ID, tableName, mapToKeyValues(map), id);
    }

    // HELPERS

    private static final String SQL_COMMA = ", ";
    private static final String SQL_VALUE_WRAPPER = "'%s'";
    private static final String SQL_KEY_VALUE_WRAPPER = "%s = '%s'";

    private static String toValues(String value) {
        return String.format(SQL_VALUE_WRAPPER, value);
    }

    private static String toKeyValues(Map.Entry<String, Object> entry) {
        return String.format(SQL_KEY_VALUE_WRAPPER, getColumnName(entry.getKey()), entry.getValue());
    }

    private static String mapToValues(LinkedHashMap<String, Object> map) {
        return map.values().stream()
                .map(Object::toString)
                .map(JdbcUtil::toValues)
                .collect(Collectors.joining(SQL_COMMA));
    }

    private static String mapToKeyValues(LinkedHashMap<String, Object> map) {
        return map.entrySet().stream()
                .map(JdbcUtil::toKeyValues)
                .collect(Collectors.joining(SQL_COMMA));
    }

    // READ

    public static <T> List<T> readData(Class<T> type, ResultSet resultSet) throws SQLException {
        List list = new ArrayList<>();
        if (type == Book.class) list = readBooks(resultSet);
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
}
