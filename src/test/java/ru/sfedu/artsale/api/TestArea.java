package ru.sfedu.artsale.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.sfedu.bibliohub.api.AbstractDataProvider;
import ru.sfedu.bibliohub.api.DataProviderXml;
import ru.sfedu.bibliohub.model.bean.Book;

import java.io.IOException;
import java.util.List;


public class TestArea {
    protected final Logger log = LogManager.getLogger(TestArea.class);
    protected final AbstractDataProvider dp = new DataProviderXml(true);

    public TestArea() throws IOException {
    }

    @Test
    void test() {
        Book b = new Book(123, "qwe", "asd", 1);
        log.info(b);
        long id = dp.insertBook(b);
        log.info(id);
        Book bb = dp.getBook(123);
        log.info(bb);
    }

    @AfterEach
    void clear() {
        List<Book> books = dp.getBooks();
        books.forEach(e -> dp.deleteBook(e.getId()));
    }
}
