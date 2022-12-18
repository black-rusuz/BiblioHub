package ru.sfedu.artsale.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sfedu.bibliohub.api.AbstractDataProvider;
import ru.sfedu.bibliohub.api.DataProviderXml;
import ru.sfedu.bibliohub.utils.TestData;

public class UseCaseTest extends TestData {
    AbstractDataProvider dp = new DataProviderXml();

    @Test
    void test() {
        dp.giveBook(b1.getId(), t1.getId());
    }

    @BeforeEach
    void setUp() {
        dp.insertBook(b1);
        dp.insertBook(b2);
        dp.insertBook(b3);
        dp.insertBook(b4);

        dp.insertTemporaryCard(t1);
        dp.insertTemporaryCard(t2);

        dp.insertPerpetualCard(p1);
        dp.insertPerpetualCard(p2);

        dp.insertRent(r1);
        dp.insertRent(r2);
        dp.insertRent(r3);
        dp.insertRent(r4);
    }

    @AfterEach
    void tearDown() {
        dp.deleteBook(b1.getId());
        dp.deleteBook(b2.getId());
        dp.deleteBook(b3.getId());
        dp.deleteBook(b4.getId());

        dp.deleteTemporaryCard(t1.getId());
        dp.deleteTemporaryCard(t2.getId());

        dp.deletePerpetualCard(p1.getId());
        dp.deletePerpetualCard(p2.getId());

        dp.deleteRent(r1.getId());
        dp.deleteRent(r2.getId());
        dp.deleteRent(r3.getId());
        dp.deleteRent(r4.getId());
    }
}
