package ru.sfedu.bibliohub.api;

import ru.sfedu.bibliohub.model.bean.Book;
import ru.sfedu.bibliohub.model.bean.PerpetualCard;
import ru.sfedu.bibliohub.model.bean.Rent;
import ru.sfedu.bibliohub.model.bean.TemporaryCard;
import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.ReflectUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

abstract public class FileDataProvider extends AbstractDataProvider {
    protected String fileNamePattern;

    protected abstract <T> List<T> read(Class<T> type);

    protected abstract <T> boolean write(List<T> list, Class<T> type, String methodName);

    protected File initFile(String fullFileName) throws IOException {
        File file = new File(fullFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    protected <T> String getFileName(Class<T> type) {
        return String.format(fileNamePattern, type.getSimpleName());
    }

    private <T> boolean hasSavedId(Class<T> type, long id) {
        T oldBean = getById(type, id);
        return ReflectUtil.getId(oldBean) != 0;
    }

    // GENERICS

    private <T> List<T> getAll(Class<T> type) {
        return read(type);
    }

    private <T> T getById(Class<T> type, long id) {
        List<T> list = getAll(type).stream().filter(e -> ReflectUtil.getId(e) == id).toList();
        return list.isEmpty() ? ReflectUtil.getEmptyObject(type) : list.get(0);
    }

    private <T> long insert(Class<T> type, T bean) {
        long id = ReflectUtil.getId(bean);
        if (hasSavedId(type, id)) {
            ReflectUtil.setId(bean, System.currentTimeMillis());
        }

        List<T> list = getAll(type);
        list.add(bean);
        write(list, type, Constants.METHOD_NAME_APPEND);

        return ReflectUtil.getId(bean);
    }

    private <T> boolean delete(Class<T> type, long id) {
        if (!hasSavedId(type, id)) {
            log.warn(Constants.NOT_FOUND);
            return false;
        }

        List<T> list = getAll(type);
        list.removeIf(e -> ReflectUtil.getId(e) == id);
        return write(list, type, Constants.METHOD_NAME_DELETE);
    }

    private <T> boolean update(Class<T> type, T bean) {
        long id = ReflectUtil.getId(bean);
        if (!hasSavedId(type, id)) {
            log.warn(Constants.NOT_FOUND);
            return false;
        }

        List<T> list = getAll(type);
        list.set(list.indexOf(getById(type, id)), bean);
        return write(list, type, Constants.METHOD_NAME_UPDATE);
    }


    // CRUD

    @Override
    public List<Book> getBooks() {
        return getAll(Book.class);
    }

    @Override
    public Book getBook(long id) {
        return getById(Book.class, id);
    }

    @Override
    public long insertBook(Book book) {
        return insert(Book.class, book);
    }

    @Override
    public boolean deleteBook(long id) {
        return delete(Book.class, id);
    }

    @Override
    public boolean updateBook(Book book) {
        return update(Book.class, book);
    }


    @Override
    public List<PerpetualCard> getPerpetualCards() {
        return getAll(PerpetualCard.class);
    }

    @Override
    public PerpetualCard getPerpetualCard(long id) {
        return getById(PerpetualCard.class, id);
    }

    @Override
    public long insertPerpetualCard(PerpetualCard perpetualCard) {
        return insert(PerpetualCard.class, perpetualCard);
    }

    @Override
    public boolean deletePerpetualCard(long id) {
        return delete(PerpetualCard.class, id);
    }

    @Override
    public boolean updatePerpetualCard(PerpetualCard perpetualCard) {
        return update(PerpetualCard.class, perpetualCard);
    }


    @Override
    public List<TemporaryCard> getTemporaryCards() {
        return getAll(TemporaryCard.class);
    }

    @Override
    public TemporaryCard getTemporaryCard(long id) {
        return getById(TemporaryCard.class, id);
    }

    @Override
    public long insertTemporaryCard(TemporaryCard temporaryCard) {
        return insert(TemporaryCard.class, temporaryCard);
    }

    @Override
    public boolean deleteTemporaryCard(long id) {
        return delete(TemporaryCard.class, id);
    }

    @Override
    public boolean updateTemporaryCard(TemporaryCard temporaryCard) {
        return update(TemporaryCard.class, temporaryCard);
    }


    @Override
    public List<Rent> getRents() {
        return getAll(Rent.class);
    }

    @Override
    public Rent getRent(long id) {
        return getById(Rent.class, id);
    }

    @Override
    public long insertRent(Rent rent) {
        return insert(Rent.class, rent);
    }

    @Override
    public boolean deleteRent(long id) {
        return delete(Rent.class, id);
    }

    @Override
    public boolean updateRent(Rent rent) {
        return update(Rent.class, rent);
    }
}
