package ru.sfedu.bibliohub.api;

import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.ReflectUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    // GENERICS

    @Override
    protected <T> List<T> getAll(Class<T> type) {
        return read(type);
    }

    @Override
    protected <T> T getById(Class<T> type, long id) {
        List<T> list = getAll(type).stream().filter(e -> ReflectUtil.getId(e) == id).toList();
        return list.isEmpty() ? ReflectUtil.getEmptyObject(type) : list.get(0);
    }

    @Override
    protected <T> long insert(Class<T> type, T bean) {
        long id = ReflectUtil.getId(bean);
        if (hasSavedId(type, id)) {
            ReflectUtil.setId(bean, System.currentTimeMillis());
        }

        List<T> list = getAll(type);
        list.add(bean);
        write(list, type, Constants.METHOD_NAME_APPEND);

        return ReflectUtil.getId(bean);
    }

    @Override
    protected <T> boolean delete(Class<T> type, long id) {
        if (!hasSavedId(type, id)) {
            log.warn(getNotFoundMessage(type, id));
            return false;
        }

        List<T> list = getAll(type);
        list.removeIf(e -> ReflectUtil.getId(e) == id);
        return write(list, type, Constants.METHOD_NAME_DELETE);
    }

    @Override
    protected <T> boolean update(Class<T> type, T bean) {
        long id = ReflectUtil.getId(bean);
        if (!hasSavedId(type, id)) {
            log.warn(getNotFoundMessage(type, id));
            return false;
        }

        List<T> list = getAll(type);
        list.set(list.indexOf(getById(type, id)), bean);
        return write(list, type, Constants.METHOD_NAME_UPDATE);
    }
}
