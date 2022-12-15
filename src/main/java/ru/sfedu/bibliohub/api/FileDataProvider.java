package ru.sfedu.bibliohub.api;

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
     * @param type          bean to work with
     * @param <T>           generic class of bean
     * @return full filename string
     */
    protected <T> String getFileName(Class<T> type) {
        return String.format(fileNamePattern, type.getSimpleName());
    }
}
