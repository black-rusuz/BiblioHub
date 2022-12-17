package ru.sfedu.bibliohub.api;

import ru.sfedu.bibliohub.model.bean.Book;
import ru.sfedu.bibliohub.model.bean.PerpetualCard;
import ru.sfedu.bibliohub.model.bean.Rent;
import ru.sfedu.bibliohub.model.bean.TemporaryCard;
import ru.sfedu.bibliohub.utils.ConfigurationUtil;
import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.JdbcUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public  class DataProviderJdbc extends AbstractDataProvider {
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

    // CRUD

    @Override
    public List<Book> getBooks() {
        return read(Book.class);
    }

    @Override
    public Book getBook(long id) {
        List<Book> list = read(Book.class, id);
        return list.isEmpty() ? new Book() : list.get(0);
    }

    @Override
    public long insertBook(Book book) {
        long id = book.getId();
        if (getBook(id).getId() != 0)
            book.setId(System.currentTimeMillis());
        write(Constants.METHOD_NAME_APPEND, book, book.getId());
        return book.getId();
    }

    @Override
    public boolean deleteBook(long id) {
        Book oldBook = getBook(id);
        if (oldBook.getId() == 0) {
            log.warn(Constants.NOT_FOUND);
            return false;
        }
        return write(Constants.METHOD_NAME_DELETE, oldBook, id);
    }

    @Override
    public boolean updateBook(Book book) {
        long id = book.getId();
        Book oldBook = getBook(id);
        if (oldBook.getId() == 0) {
            log.warn(Constants.NOT_FOUND);
            return false;
        }
        return write(Constants.METHOD_NAME_UPDATE, book, id);
    }

    @Override
    public List<PerpetualCard> getPerpetualCards() {
        return null;
    }

    @Override
    public PerpetualCard getPerpetualCard(long id) {
        return null;
    }

    @Override
    public long insertPerpetualCard(PerpetualCard perpetualCard) {
        return 0;
    }

    @Override
    public boolean deletePerpetualCard(long id) {
        return false;
    }

    @Override
    public boolean updatePerpetualCard(PerpetualCard perpetualCard) {
        return false;
    }

    @Override
    public List<TemporaryCard> getTemporaryCards() {
        return null;
    }

    @Override
    public TemporaryCard getTemporaryCard(long id) {
        return null;
    }

    @Override
    public long insertTemporaryCard(TemporaryCard temporaryCard) {
        return 0;
    }

    @Override
    public boolean deleteTemporaryCard(long id) {
        return false;
    }

    @Override
    public boolean updateTemporaryCard(TemporaryCard temporaryCard) {
        return false;
    }

    @Override
    public List<Rent> getRents() {
        return null;
    }

    @Override
    public Rent getRent(long id) {
        return null;
    }

    @Override
    public long insertRent(Rent rent) {
        return 0;
    }

    @Override
    public boolean deleteRent(long id) {
        return false;
    }

    @Override
    public boolean updateRent(Rent rent) {
        return false;
    }
}
