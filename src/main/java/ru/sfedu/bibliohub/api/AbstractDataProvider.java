package ru.sfedu.bibliohub.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.bibliohub.model.HistoryContent;
import ru.sfedu.bibliohub.model.bean.Book;
import ru.sfedu.bibliohub.utils.ConfigurationUtil;
import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.MongoUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class AbstractDataProvider {
    protected final Logger log = LogManager.getLogger(this.getClass());
    private final boolean MONGO_ENABLE = Boolean.parseBoolean(ConfigurationUtil.getConfigurationEntry(Constants.MONGO_ENABLE));
    private final String MONGO_ACTOR = ConfigurationUtil.getConfigurationEntry(Constants.MONGO_ACTOR);

    public AbstractDataProvider() throws IOException {
    }

    // Service
    protected void sendLogs(String methodName, Object bean, boolean result) {
        HistoryContent historyContent = new HistoryContent(UUID.randomUUID(),
                this.getClass().getSimpleName(),
                LocalDateTime.now().toString(),
                MONGO_ACTOR,
                methodName,
                MongoUtil.objectToString(bean),
                result);
        if (MONGO_ENABLE) MongoUtil.saveToLog(historyContent);
    }

    public abstract List<Book> getBooks();

    public abstract Book getBook(long id);

    public abstract long insertBook(Book book);

    public abstract boolean deleteBook(long id);

    public abstract boolean updateBook(Book book);
}
