package ru.sfedu.artsale.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import ru.sfedu.bibliohub.api.AbstractDataProvider;
import ru.sfedu.bibliohub.api.DataProviderXml;
import ru.sfedu.bibliohub.model.bean.Book;

import java.io.IOException;


public class TestArea {
    protected final Logger log = LogManager.getLogger(TestArea.class);
    protected final AbstractDataProvider dp = new DataProviderXml();

    public TestArea() throws IOException {
    }

    @Test
    void test() {
        Book b = new Book(123, "qwe", "asd", 1);
        dp.insertBook(b);
        log.info("Hello");
        Book bb = dp.getBook(123);
        log.info(bb);
    }
}
