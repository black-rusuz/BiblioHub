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
    protected final AbstractDataProvider dp = new DataProviderXml();

    public TestArea() throws IOException {
    }

    @Test
    void test() {
        log.info(dp.getBooks());

        Book b = new Book(123, "qwe", "asd", 1);
        dp.insertBook(b);
        log.info(dp.getBooks());

        Book bb = dp.getBook(b.getId());
        log.info(dp.getBooks());

        Book bbb = dp.getBook(321);
        log.info(bbb);

        bb.setAuthor("aASDasdasdasdasdasd");
        boolean u = dp.updateBook(bb);
        log.info(dp.getBooks());

        boolean d = dp.deleteBook(bb.getId());
        log.info(dp.getBooks());
    }

    @AfterEach
    void clear() {
        List<Book> books = dp.getBooks();
        books.forEach(e -> dp.deleteBook(e.getId()));
    }
}
