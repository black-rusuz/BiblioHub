package ru.sfedu.bibliohub.utils.converters;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.bibliohub.model.bean.Book;

import java.util.List;
import java.util.stream.Collectors;

public class BookConverter extends AbstractBeanField<Book, String> {
    public static final String fieldsDelimiter = "::";

    public static Book fromString(String string) {
        String[] parsed = string.split(fieldsDelimiter);
        return new Book(Long.parseLong(parsed[0]), parsed[1], parsed[2], Integer.parseInt(parsed[3]));
    }

    public static String toString(Object object) {
        Book book = (Book) object;
        List<Object> params = List.of(book.getId(),
                book.getAuthor(),
                book.getTitle(),
                book.getYear());
        return params.stream().map(Object::toString).collect(Collectors.joining(fieldsDelimiter));
    }

    @Override
    public Book convert(String string) {
        return fromString(string);
    }

    @Override
    public String convertToWrite(Object object) {
        return toString(object);
    }
}
