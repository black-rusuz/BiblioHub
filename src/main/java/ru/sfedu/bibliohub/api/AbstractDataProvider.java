package ru.sfedu.bibliohub.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.bibliohub.model.HistoryContent;
import ru.sfedu.bibliohub.model.bean.*;
import ru.sfedu.bibliohub.utils.ConfigurationUtil;
import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.MongoUtil;
import ru.sfedu.bibliohub.utils.ReflectUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("UnusedReturnValue")
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

    protected <T> String getNotFoundMessage(Class<T> type, long id) {
        return String.format(Constants.NOT_FOUND, type.getSimpleName(), id);
    }

    // USE CASES

    /**
     * Формат даты
     *
     * @param date Дата
     * @return Строка формата "дд.мм.гггг"
     */
    String formatDate(LocalDate date) {
        return String.format(Constants.DATE_FORMAT, date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    /**
     * Получить LocalDate из форматированной строки
     *
     * @param date Строка формата "дд.мм.гггг"
     * @return Дата
     */
    private LocalDate dateFromString(String date) {
        if (date.length() == 0) return LocalDate.MIN;
        List<Integer> numbers = Arrays.stream(date.split("\\.")).map(Integer::parseInt).toList();
        return LocalDate.of(numbers.get(2), numbers.get(1), numbers.get(0));
    }

    private Optional<LibraryCard> getCard(long id) {
        LibraryCard card = getPerpetualCard(id);
        if (card.getId() != 0) return Optional.of(card);
        card = getTemporaryCard(id);
        if (card.getId() != 0) return Optional.of(card);
        log.info(getNotFoundMessage(LibraryCard.class, id));
        return Optional.empty();
    }

    /**
     * Выдача книги читателю
     *
     * @param bookId ID книги
     * @param cardId ID читательского билета
     * @return Запись об аренде книги
     */
    public Optional<Rent> giveBook(long bookId, long cardId) {
        if (!validateCard(cardId)) {
            return Optional.empty();
        }

        Book book = getBook(bookId);
        if (book.getId() == 0) {
            log.warn(getNotFoundMessage(Book.class, bookId));
            return Optional.empty();
        }

        Optional<LibraryCard> optionalCard = getCard(cardId);
        if (optionalCard.isEmpty()) {
            log.warn(getNotFoundMessage(LibraryCard.class, cardId));
            return Optional.empty();
        }

        LocalDate today = LocalDate.now();
        String todayString = formatDate(today);
        LocalDate ret = calculateReturnDate(today.getYear(), today.getMonthValue(), today.getDayOfMonth()).get();
        String retString = formatDate(ret);

        Rent rent = new Rent(System.currentTimeMillis(), book, optionalCard.get(), todayString, retString);
        log.info(Constants.NEW_RENT + rent);

        insertRent(rent);
        return Optional.of(rent);
    }

    /**
     * Проверка актуальности читательского билета
     *
     * @param cardId ID билета
     * @return true если билет актуален
     */
    public boolean validateCard(long cardId) {
        PerpetualCard pCard = getPerpetualCard(cardId);
        if (pCard.getId() != 0) {
            log.info(Constants.CARD_PERPETUAL + pCard.getReason());
            return true;
        };

        TemporaryCard tCard = getTemporaryCard(cardId);
        LocalDate expireDate = dateFromString(tCard.getEndDate());
        LocalDate today = LocalDate.now();

        if (expireDate.isBefore(today)) {
            log.info(Constants.CARD_EXPIRED);
            return false;
        } else {
            log.info(Constants.CARD_NOT_EXPIRED);
            return true;
        }
    }

    /**
     * Расчёт даты возврата книги
     *
     * @param startYear  Текущий год
     * @param startMonth Текущий месяц
     * @param startDay   Текущий день
     * @return Дата возврата книги
     */
    public Optional<LocalDate> calculateReturnDate(int startYear, int startMonth, int startDay) {
        LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
        LocalDate returnDate = startDate.plusDays(14);
        log.info(Constants.RETURN_DATE + formatDate(returnDate));
        return Optional.of(returnDate);
    }

    /**
     * Проверить входит ли дата в диапазон от текущего момента до следующих двух недель
     *
     * @param rentDate дата для проверки
     * @return true если осталось более 2 недель до наступления даты
     */
    boolean dateFilter(String rentDate) {
        LocalDate today = LocalDate.now();
        LocalDate expireDate = today.plusWeeks(2);
        LocalDate date = dateFromString(rentDate);
        return date.isAfter(today) && date.isBefore(expireDate);
    }

    /**
     * Просмотр записей об аренды со сроком возврата менее 2 недель
     *
     * @param rentId ID аренды
     * @return Список записей
     */
    public List<Rent> watchExpiringRents(long rentId) {
        List<Rent> rents = getRents();
        rents = rents.stream().filter((e) -> dateFilter(e.getReturnDate())).toList();
        log.info(Constants.EXPIRING_RENTS
                + rents.stream().map(Object::toString).collect(Collectors.joining("\n")));
        if (rentId != 0) expireRentPeriod(rentId);
        return rents;
    }

    /**
     * Продлить срок аренды книги
     *
     * @param rentId ID аренды
     * @return Обновлённая запись об аренде
     */
    public Optional<Rent> expireRentPeriod(long rentId) {
        Rent rent = getRent(rentId);
        if (rent.getId() != 0) {
            LocalDate endDate = LocalDate.now().plusMonths(1);
            rent.setReturnDate(formatDate(endDate));
            updateRent(rent);
            log.info(Constants.EXPIRED_PERIOD + rent);
            return Optional.of(rent);
        } else {
            getNotFoundMessage(Rent.class, rentId);
            return Optional.empty();
        }
    }

    /**
     * Просмотр временных читательских билетов с оставшимся сроком действия менее 2 недель
     *
     * @param cardId ID билета
     * @return Список записей
     */
    public List<TemporaryCard> watchExpiringCards(long cardId) {
        List<TemporaryCard> cards = getTemporaryCards();
        cards = cards.stream().filter((e) -> dateFilter(e.getEndDate())).toList();
        log.info(Constants.EXPIRING_CARDS
                + cards.stream().map(Object::toString).collect(Collectors.joining("\n")));
        if (cardId != 0) expireCardPeriod(cardId);
        return cards;
    }

    /**
     * Продлить срок действия временного билета
     *
     * @param cardId ID аренды
     * @return Обновлённый билет
     */
    public Optional<LibraryCard> expireCardPeriod(long cardId) {
        TemporaryCard card = getTemporaryCard(cardId);
        if (card.getId() != 0) {
            LocalDate endDate = LocalDate.now().plusMonths(6);
            card.setEndDate(formatDate(endDate));
            updateTemporaryCard(card);
            log.info(Constants.EXPIRED_PERIOD + card);
            return Optional.of(card);
        } else {
            getNotFoundMessage(TemporaryCard.class, cardId);
            return Optional.empty();
        }
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
