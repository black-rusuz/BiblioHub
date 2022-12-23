package ru.sfedu.bibliohub.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import ru.sfedu.bibliohub.BiblioHubClient;
import ru.sfedu.bibliohub.utils.TestData;

import java.io.IOException;


public class TestArea extends TestData {
    protected final Logger log = LogManager.getLogger(TestArea.class);
    protected final AbstractDataProvider dp = new DataProviderJdbc();

    @Test
    void test() {
        BiblioHubClient.main("XML giveBook 11 21".split(" "));
        BiblioHubClient.main("XML validateCard 21".split(" "));
        BiblioHubClient.main("CSV calculateReturnDate 2022 12 23".split(" "));
        BiblioHubClient.main("CSV watchExpiringRents 41".split(" "));
        BiblioHubClient.main("CSV expireRentPeriod 41".split(" "));
        BiblioHubClient.main("JDBC watchExpiringCards 31".split(" "));
        BiblioHubClient.main("JDBC expireCardPeriod 31".split(" "));
    }
}
