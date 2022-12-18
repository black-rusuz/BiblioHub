package ru.sfedu.bibliohub.api;

import ru.sfedu.bibliohub.model.bean.Book;
import ru.sfedu.bibliohub.model.bean.PerpetualCard;
import ru.sfedu.bibliohub.model.bean.Rent;
import ru.sfedu.bibliohub.model.bean.TemporaryCard;
import ru.sfedu.bibliohub.utils.ConfigurationUtil;
import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.JdbcUtil;
import ru.sfedu.bibliohub.utils.ReflectUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataProviderJdbc extends AbstractDataProvider {
    private String hostname;
    private String username;
    private String password;

    public DataProviderJdbc() {
        try {
            hostname = ConfigurationUtil.getConfigurationEntry(Constants.H2_HOSTNAME);
            username = ConfigurationUtil.getConfigurationEntry(Constants.H2_USERNAME);
            password = ConfigurationUtil.getConfigurationEntry(Constants.H2_PASSWORD);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        try {
            write(JdbcUtil.createTable(new Book()));
            write(JdbcUtil.createTable(new PerpetualCard()));
            write(JdbcUtil.createTable(new TemporaryCard()));
            write(JdbcUtil.createTable(new Rent()));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    // READ

    private <T> List<T> read(Class<T> type) {
        return read(type, JdbcUtil.selectAllFromTable(type));
    }

    private <T> List<T> read(Class<T> type, long id) {
        return read(type, JdbcUtil.selectFromTableById(type, id));
    }

    private <T> List<T> read(Class<T> type, String sql) {
        List<T> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(hostname, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            log.debug(sql);
            list = JdbcUtil.readData(type, resultSet);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    // WRITE

    private void write(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection(hostname, username, password);
        Statement statement = connection.createStatement();

        log.debug(sql);
        statement.executeUpdate(sql);

        connection.close();
        statement.close();
    }

    private <T> boolean write(String methodName, T bean, long id) {
        String sql = switch (methodName) {
            case Constants.METHOD_NAME_APPEND -> JdbcUtil.insertIntoTableValues(bean);
            case Constants.METHOD_NAME_DELETE -> JdbcUtil.deleteFromTableById(bean, id);
            case Constants.METHOD_NAME_UPDATE -> JdbcUtil.updateTableSetById(bean, id);
            default -> "";
        };

        try {
            write(sql);
            sendLogs(methodName, bean, true);
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            sendLogs(methodName, bean, false);
            return false;
        }
    }

    // GENERICS

    @Override
    protected <T> List<T> getAll(Class<T> type) {
        return read(type);
    }

    @Override
    protected <T> T getById(Class<T> type, long id) {
        List<T> list = read(type, id);
        return list.isEmpty() ? ReflectUtil.getEmptyObject(type) : list.get(0);
    }

    @Override
    protected <T> long insert(Class<T> type, T bean) {
        long id = ReflectUtil.getId(bean);
        if (hasSavedId(type, id)) {
            ReflectUtil.setId(bean, System.currentTimeMillis());
        }
        write(Constants.METHOD_NAME_APPEND, bean, id);
        return ReflectUtil.getId(bean);
    }

    @Override
    protected <T> boolean delete(Class<T> type, long id) {
        if (!hasSavedId(type, id)) {
            log.warn(getNotFoundMessage(type, id));
            return false;
        }
        return write(Constants.METHOD_NAME_DELETE, ReflectUtil.getEmptyObject(type), id);
    }

    @Override
    protected <T> boolean update(Class<T> type, T bean) {
        long id = ReflectUtil.getId(bean);
        if (!hasSavedId(type, id)) {
            log.warn(getNotFoundMessage(type, id));
            return false;
        }
        return write(Constants.METHOD_NAME_UPDATE, bean, id);
    }
}
