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
    void test() throws IOException {
        BiblioHubClient.main("XML GIVEBOOK 11 21".split(" "));
    }
}
