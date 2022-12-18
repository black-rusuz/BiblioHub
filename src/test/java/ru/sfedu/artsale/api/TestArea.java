package ru.sfedu.artsale.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import ru.sfedu.bibliohub.api.AbstractDataProvider;
import ru.sfedu.bibliohub.api.DataProviderJdbc;
import ru.sfedu.bibliohub.model.bean.Book;
import ru.sfedu.bibliohub.model.bean.PerpetualCard;
import ru.sfedu.bibliohub.model.bean.Rent;
import ru.sfedu.bibliohub.model.bean.TemporaryCard;
import ru.sfedu.bibliohub.utils.TestData;


public class TestArea extends TestData {
    protected final Logger log = LogManager.getLogger(TestArea.class);
    protected final AbstractDataProvider dp = new DataProviderJdbc();

    @Test
    void books() {
        log.info(dp.getBooks());

        Book b = new Book(123, "qwe", "asd", 1);
        dp.insertBook(b);
        log.info(dp.getBooks());

        Book bb = dp.getBook(b.getId());
        log.info(dp.getBooks());

        Book bbb = dp.getBook(321);
        log.info(bbb);

        bb.setAuthor("aASDasdasdasdasdasd");
        dp.updateBook(bb);
        log.info(dp.getBooks());

        dp.deleteBook(bb.getId());
        log.info(dp.getBooks());
    }

    @Test
    void pCards() {
        log.info(dp.getPerpetualCards());

        PerpetualCard card = new PerpetualCard(12, "Same", "Person", "20.02.2014", "Work", "Good Boy");
        dp.insertPerpetualCard(card);
        log.info(dp.getPerpetualCards());

        PerpetualCard card2 = dp.getPerpetualCard(card.getId());
        log.info(dp.getPerpetualCards());

        PerpetualCard card3 = dp.getPerpetualCard(321);
        log.info(card3);

        card2.setWork("Rubium");
        dp.updatePerpetualCard(card2);
        log.info(dp.getPerpetualCards());

        dp.deletePerpetualCard(card2.getId());
        log.info(dp.getPerpetualCards());
    }

    @Test
    void tCards() {
        log.info(dp.getTemporaryCards());

        TemporaryCard card = new TemporaryCard(12, "Same", "Person", "20.02.2014", "Work", "15.12.2022", "15.01.2023");
        dp.insertTemporaryCard(card);
        log.info(dp.getTemporaryCards());

        TemporaryCard card2 = dp.getTemporaryCard(card.getId());
        log.info(dp.getTemporaryCards());

        TemporaryCard card3 = dp.getTemporaryCard(321);
        log.info(card3);

        card2.setWork("Rubium");
        dp.updateTemporaryCard(card2);
        log.info(dp.getTemporaryCards());

        dp.deleteTemporaryCard(card2.getId());
        log.info(dp.getTemporaryCards());
    }

    @Test
    void rents() {
        log.info(dp.getRents());

        Book b = new Book(123, "qwe", "asd", 1);
        PerpetualCard card = new PerpetualCard(12, "Same", "Person", "20.02.2014", "Work", "Good Boy");
        Rent rent = new Rent(1245, b, card, "16.12.2022", "16.01.2023");
        dp.insertRent(rent);
        log.info(dp.getRents());

        Rent rent2 = dp.getRent(rent.getId());
        log.info(rent2);

        Rent rent3 = dp.getRent(123123);
        log.info(rent3);

        rent2.setReturnDate("16.02.2023");
        dp.updateRent(rent2);
        log.info(dp.getRents());

        dp.deleteRent(rent2.getId());
        log.info(dp.getRents());
    }
}
