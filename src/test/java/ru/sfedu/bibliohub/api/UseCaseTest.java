package ru.sfedu.bibliohub.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sfedu.bibliohub.model.bean.Rent;
import ru.sfedu.bibliohub.model.bean.TemporaryCard;
import ru.sfedu.bibliohub.utils.TestData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public abstract class UseCaseTest extends TestData {
    AbstractDataProvider dp;

    @Test
    void giveBookPos() {
        LocalDate today = LocalDate.now();
        String todayString = dp.formatDate(today);
        LocalDate ret = dp.calculateReturnDate(today.getYear(), today.getMonthValue(), today.getDayOfMonth()).get();
        String retString = dp.formatDate(ret);

        Optional<Rent> actualRent = dp.giveBook(b1.getId(), t1.getId());
        Rent expectedRent = new Rent(actualRent.get().getId(), b1, t1, todayString, retString);
        Assertions.assertEquals(Optional.of(expectedRent), actualRent);
        dp.deleteRent(expectedRent.getId());
    }

    @Test
    void giveBookNeg() {
        Assertions.assertEquals(Optional.empty(), dp.giveBook(b4.getId(), t2.getId()));
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
    void watchExpiringRentsPos() {
        List<Rent> rents = List.of(r1, r2, r3, r4);
        List<Rent> expectedRents = rents.stream().filter((e) -> dp.dateFilter(e.getReturnDate())).toList();
        Assertions.assertEquals(expectedRents, dp.watchExpiringRents(0));
    }

    @Test
    void watchExpiringRentsNeg() {
        List<Rent> rents = List.of(r3, r4);
        Assertions.assertNotEquals(rents, dp.watchExpiringRents(0));
    }


    @Test
    void expireRentPeriodPos() {
        LocalDate returnDate = LocalDate.now().plusMonths(1);
        r1.setReturnDate(dp.formatDate(returnDate));
        Assertions.assertEquals(Optional.of(r1), dp.expireRentPeriod(r1.getId()));
        r1.setReturnDate("14.01.2023");
    }

    @Test
    void expireRentPeriodNeg() {
        Assertions.assertNotEquals(Optional.of(r1), dp.expireRentPeriod(r1.getId()));
    }


    @Test
    void watchExpiringCardsPos() {
        List<TemporaryCard> cards = List.of(t1, t2);
        List<TemporaryCard> expectedCards = cards.stream().filter((e) -> dp.dateFilter(e.getEndDate())).toList();
        Assertions.assertEquals(expectedCards, dp.watchExpiringCards(0));
    }

    @Test
    void watchExpiringCardsNeg() {
        List<TemporaryCard> cards = List.of(t2);
        Assertions.assertNotEquals(cards, dp.watchExpiringCards(0));
    }


    @Test
    void expireCardPeriodPos() {
        LocalDate returnDate = LocalDate.now().plusMonths(6);
        t1.setEndDate(dp.formatDate(returnDate));
        Assertions.assertEquals(Optional.of(t1), dp.expireCardPeriod(t1.getId()));
        t1.setEndDate("15.05.2023");
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
