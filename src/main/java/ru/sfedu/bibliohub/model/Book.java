package ru.sfedu.bibliohub.model;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.util.Objects;

@Element
public class Book implements Serializable {
    @Attribute
    @CsvBindByPosition(position = 0)
    private long id;

    @Attribute
    @CsvBindByPosition(position = 1)
    private String author;

    @Attribute
    @CsvBindByPosition(position = 2)
    private String title;

    @Attribute
    @CsvBindByPosition(position = 3)
    private int year;

    public Book() {
    }

    public Book(long id, String author, String title, int year) {
        setId(id);
        setAuthor(author);
        setTitle(title);
        setYear(year);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return getId() == book.getId()
                && getYear() == book.getYear()
                && Objects.equals(getAuthor(), book.getAuthor())
                && Objects.equals(getTitle(), book.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAuthor(), getTitle(), getYear());
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", author='" + getAuthor() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", year=" + getYear() +
                '}';
    }
}
