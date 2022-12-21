package ru.sfedu.bibliohub.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sfedu.bibliohub.utils.TestData;

import java.time.LocalDate;
import java.util.Optional;

public abstract class UseCaseTest extends TestData {
    AbstractDataProvider dp;

    @Test
    void test() {
    }


    @Test
    void validateCardPos() {
        Assertions.assertTrue(dp.validateCard(t1.getId()));
    }

    @Test
    void validateCardNeg() {
        Assertions.assertFalse(dp.validateCard(123));
    }


    @Test
    void calculateReturnDatePos() {
        Optional<LocalDate> expectedDate = Optional.of(LocalDate.of(2022, 12, 15));
        Optional<LocalDate> actualDate = dp.calculateReturnDate(2022, 12, 1);
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    void calculateReturnDateNeg() {
        Optional<LocalDate> expectedDate = Optional.of(LocalDate.now());
        Optional<LocalDate> actualDate = dp.calculateReturnDate(2022, 12, 1);
        Assertions.assertNotEquals(expectedDate, actualDate);
    }


    @Test
    void expireRentPeriodPos() {
        r1.setReturnDate("21.1.2023");
        Assertions.assertEquals(Optional.of(r1), dp.expireRentPeriod(r1.getId()));
    }

    @Test
    void expireRentPeriodNeg() {
        Assertions.assertNotEquals(Optional.of(r1), dp.expireRentPeriod(r1.getId()));
    }


    @Test
    void expireCardPeriodPos() {
        t1.setEndDate("22.1.2023");
        Assertions.assertEquals(Optional.of(t1), dp.expireCardPeriod(t1.getId()));
    }

    @Test
    void expireCardPeriodNeg() {
        Assertions.assertNotEquals(Optional.of(t1), dp.expireCardPeriod(t1.getId()));
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
