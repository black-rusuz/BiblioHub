package ru.sfedu.bibliohub.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.bibliohub.model.HistoryContent;
import ru.sfedu.bibliohub.model.bean.Book;
import ru.sfedu.bibliohub.model.bean.PerpetualCard;
import ru.sfedu.bibliohub.model.bean.Rent;
import ru.sfedu.bibliohub.model.bean.TemporaryCard;
import ru.sfedu.bibliohub.utils.ConfigurationUtil;
import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.MongoUtil;
import ru.sfedu.bibliohub.utils.ReflectUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class AbstractDataProvider {
    protected final Logger log = LogManager.getLogger(this.getClass());
    private boolean MONGO_ENABLE = false;
    private String MONGO_ACTOR = "";

    public AbstractDataProvider() {
        try {
            MONGO_ENABLE = Boolean.parseBoolean(ConfigurationUtil.getConfigurationEntry(Constants.MONGO_ENABLE));
            MONGO_ACTOR = ConfigurationUtil.getConfigurationEntry(Constants.MONGO_ACTOR);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    // ABSTRACT GENERICS

    protected abstract <T> List<T> getAll(Class<T> type);

    protected abstract <T> T getById(Class<T> type, long id);

    protected abstract <T> long insert(Class<T> type, T bean);

    protected abstract <T> boolean delete(Class<T> type, long id);

    protected abstract <T> boolean update(Class<T> type, T bean);

    // SERVICE

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

    protected <T> boolean hasSavedId(Class<T> type, long id) {
        T oldBean = getById(type, id);
        return ReflectUtil.getId(oldBean) != 0;
    }
    // CRUD

    public List<Book> getBooks() {
        return getAll(Book.class);
    }

    public Book getBook(long id) {
        return getById(Book.class, id);
    }

    public long insertBook(Book book) {
        return insert(Book.class, book);
    }

    public boolean deleteBook(long id) {
        return delete(Book.class, id);
    }

    public boolean updateBook(Book book) {
        return update(Book.class, book);
    }


    public List<PerpetualCard> getPerpetualCards() {
        return getAll(PerpetualCard.class);
    }

    public PerpetualCard getPerpetualCard(long id) {
        return getById(PerpetualCard.class, id);
    }

    public long insertPerpetualCard(PerpetualCard perpetualCard) {
        return insert(PerpetualCard.class, perpetualCard);
    }

    public boolean deletePerpetualCard(long id) {
        return delete(PerpetualCard.class, id);
    }

    public boolean updatePerpetualCard(PerpetualCard perpetualCard) {
        return update(PerpetualCard.class, perpetualCard);
    }


    public List<TemporaryCard> getTemporaryCards() {
        return getAll(TemporaryCard.class);
    }

    public TemporaryCard getTemporaryCard(long id) {
        return getById(TemporaryCard.class, id);
    }

    public long insertTemporaryCard(TemporaryCard temporaryCard) {
        return insert(TemporaryCard.class, temporaryCard);
    }

    public boolean deleteTemporaryCard(long id) {
        return delete(TemporaryCard.class, id);
    }

    public boolean updateTemporaryCard(TemporaryCard temporaryCard) {
        return update(TemporaryCard.class, temporaryCard);
    }


    public List<Rent> getRents() {
        return getAll(Rent.class);
    }

    public Rent getRent(long id) {
        return getById(Rent.class, id);
    }

    public long insertRent(Rent rent) {
        return insert(Rent.class, rent);
    }

    public boolean deleteRent(long id) {
        return delete(Rent.class, id);
    }

    public boolean updateRent(Rent rent) {
        return update(Rent.class, rent);
    }
}
