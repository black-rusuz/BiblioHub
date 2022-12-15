package ru.sfedu.bibliohub.api;

import ru.sfedu.bibliohub.model.bean.Book;
import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.ReflectUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

abstract public class FileDataProvider extends AbstractDataProvider {
    protected String fileNamePattern;

    public FileDataProvider() throws IOException {
    }

    /**
     * Reads bean list from file.
     *
     * @param type class that needed to read
     * @param <T>  generic class of list entries
     * @return list of read beans
     */
    protected abstract <T> List<T> read(Class<T> type);

    /**
     * Writes list of any beans to file.
     *
     * @param list list of beans to write
     * @param <T>  generic class of list entries
     * @return writing Result (Success/Warning/Error and message)
     */
    protected abstract <T> boolean write(List<T> list, Class<T> type, String methodName);

    /**
     * Creates File variable to read from/write in. Creates file in filesystem if not exists.
     */
    protected File initFile(String fullFileName) throws IOException {
        File file = new File(fullFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    /**
     * Generates full file name by bean.
     *
     * @param type bean to work with
     * @param <T>  generic class of bean
     * @return full filename string
     */
    protected <T> String getFileName(Class<T> type) {
        return String.format(fileNamePattern, type.getSimpleName());
    }

    private <T> boolean hasSavedId(Class<T> type, long id) {
        T oldBean = getById(type, id);
        return ReflectUtils.getId(oldBean) != 0;
    }

    private <T> List<T> getAll(Class<T> type) {
        return read(type);
    }

    private <T> T getById(Class<T> type, long id) {
        List<T> list = getAll(type).stream().filter(e -> ReflectUtils.getId(e) == id).toList();
        if (list.isEmpty()) {
            log.warn(Constants.NOT_FOUND);
            return ReflectUtils.getEmptyObject(type);
        }
        else return list.get(0);
    }

    private <T> long insert(Class<T> type, T bean) {
        long id = ReflectUtils.getId(bean);
        if (hasSavedId(type, id)) {
            ReflectUtils.setId(bean, System.currentTimeMillis());
        }

        List<T> list = getAll(type);
        list.add(bean);
        write(list, type, Constants.METHOD_NAME_APPEND);

        return ReflectUtils.getId(bean);
    }

    private <T> boolean delete(Class<T> type, long id) {
        if (!hasSavedId(type, id)) {
            log.warn(Constants.NOT_FOUND);
            return false;
        }

        List<T> list = getAll(type);
        list.removeIf(e -> ReflectUtils.getId(e) == id);
        return write(list, type, Constants.METHOD_NAME_DELETE);
    }

    private <T> boolean update(Class<T> type, T bean) {
        long id = ReflectUtils.getId(bean);
        if (!hasSavedId(type, id)) {
            log.warn(Constants.NOT_FOUND);
            return false;
        }

        List<T> list = getAll(type);
        list.set(list.indexOf(getById(type, id)), bean);
        return write(list, type, Constants.METHOD_NAME_UPDATE);
    }

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
}
