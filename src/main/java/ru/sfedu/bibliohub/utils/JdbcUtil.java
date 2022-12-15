package ru.sfedu.bibliohub.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil {
    public static final String tablePrefix = "table_";

    // COMMANDS
    private static final String SELECT_ALL_FROM_TABLE = "SELECT * FROM %s;";
    private static final String SELECT_FROM_TABLE_BY_ID = "SELECT * FROM %s WHERE id = %d;";
    private static final String INSERT_INTO_TABLE_VALUES = "INSERT INTO %s VALUES (%s);";
    private static final String DELETE_FROM_TABLE_BY_ID = "DELETE FROM %s WHERE id = %d;";
    private static final String UPDATE_TABLE_SET_BY_ID = "UPDATE %s SET %s WHERE id = %d;";

    // SQL
    public static String selectAllFromTable(String tableName) {
        return String.format(SELECT_ALL_FROM_TABLE, tableName);
    }

    public static String selectFromTableById(String tableName, long id) {
        return String.format(SELECT_FROM_TABLE_BY_ID, tableName, id);
    }

    public static <T> String insertIntoTableValues(String tableName, String values) {
        return String.format(INSERT_INTO_TABLE_VALUES, tableName, values);
    }

    public static String deleteFromTableById(String tableName, long id) {
        return String.format(DELETE_FROM_TABLE_BY_ID, tableName, id);
    }

    public static <T> String updateTableSetById(String tableName, String values, long id) {
        return String.format(UPDATE_TABLE_SET_BY_ID, tableName, values, id);
    }

    // WRITE
//    public static <T> String beanToInsertString(T bean) {
//        if (bean.getClass().equals(User.class))
//            return ((User) bean).toInsertString();
//        if (bean.getClass().equals(Product.class))
//            return ((Product) bean).toInsertString();
//        if (bean.getClass().equals(CreationKit.class))
//            return ((CreationKit) bean).toInsertString();
//        if (bean.getClass().equals(EndProduct.class))
//            return ((EndProduct) bean).toInsertString();
//        if (bean.getClass().equals(Order.class))
//            return ((Order) bean).toInsertString();
//        else return "";
//    }
//
//    public static <T> String beanToUpdateString(T bean) {
//        if (bean.getClass().equals(User.class))
//            return ((User) bean).toUpdateString();
//        if (bean.getClass().equals(Product.class))
//            return ((Product) bean).toUpdateString();
//        if (bean.getClass().equals(CreationKit.class))
//            return ((CreationKit) bean).toUpdateString();
//        if (bean.getClass().equals(EndProduct.class))
//            return ((EndProduct) bean).toUpdateString();
//        if (bean.getClass().equals(Order.class))
//            return ((Order) bean).toUpdateString();
//        else return "";
//    }
//
//    // READ
//    public static <T> List<T> readData(Class<T> type, ResultSet resultSet) throws SQLException {
//        List list = new ArrayList<>();
//        if (type.equals(User.class))
//            list = readUser(resultSet);
//        else if (type.equals(Product.class))
//            list = readProduct(resultSet);
//        else if (type.equals(CreationKit.class))
//            list = readCreationKit(resultSet);
//        else if (type.equals(EndProduct.class))
//            list = readEndProduct(resultSet);
//        else if (type.equals(Order.class))
//            list = readOrder(resultSet);
//        return list;
//    }
//
//    public static List<User> readUser(ResultSet resultSet) throws SQLException {
//        List<User> list = new ArrayList<>();
//        while (resultSet.next()) {
//            User user = new User();
//            user.setId(resultSet.getLong(1));
//            user.setName(resultSet.getString(2));
//            user.setPhone(resultSet.getString(3));
//            user.setEmail(resultSet.getString(4));
//            user.setAddress(resultSet.getString(5));
//            list.add(user);
//        }
//        return list;
//    }
//
//    public static List<Product> readProduct(ResultSet resultSet) throws SQLException {
//        List<Product> list = new ArrayList<>();
//        while (resultSet.next()) {
//            Product product = new Product();
//            product.setId(resultSet.getLong(1));
//            product.setName(resultSet.getString(2));
//            product.setPrice(resultSet.getDouble(3));
//            list.add(product);
//        }
//        return list;
//    }
//
//    public static List<CreationKit> readCreationKit(ResultSet resultSet) throws SQLException {
//        List<CreationKit> list = new ArrayList<>();
//        while (resultSet.next()) {
//            CreationKit creationKit = new CreationKit();
//            creationKit.setId(resultSet.getLong(1));
//            creationKit.setName(resultSet.getString(2));
//            creationKit.setPrice(resultSet.getDouble(3));
//            creationKit.setCategory(resultSet.getString(4));
//            creationKit.setComposition(resultSet.getString(5));
//            list.add(creationKit);
//        }
//        return list;
//    }
//
//    public static List<EndProduct> readEndProduct(ResultSet resultSet) throws SQLException {
//        List<EndProduct> list = new ArrayList<>();
//        while (resultSet.next()) {
//            EndProduct endProduct = new EndProduct();
//            endProduct.setId(resultSet.getLong(1));
//            endProduct.setName(resultSet.getString(2));
//            endProduct.setPrice(resultSet.getDouble(3));
//            endProduct.setWarranty(resultSet.getInt(4));
//            list.add(endProduct);
//        }
//        return list;
//    }
//
//    public static List<Order> readOrder(ResultSet resultSet) throws SQLException {
//        List<Order> list = new ArrayList<>();
//        while (resultSet.next()) {
//            Order order = new Order();
//            order.setId(resultSet.getLong(1));
//            order.setCustomer(new UserConverter().convert(resultSet.getString(2)));
//            order.setProduct(new ProductConverter().convert(resultSet.getString(3)));
//            list.add(order);
//        }
//        return list;
//    }
}
