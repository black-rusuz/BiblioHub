package ru.sfedu.bibliohub.model;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.util.Objects;

@Element
public class Rent implements Serializable {
    @Attribute
    @CsvBindByPosition(position = 0)
    private long id;

    @Element
    @CsvBindByPosition(position = 1)
    private Book book = new Book();

    @Element
    @CsvBindByPosition(position = 2)
    private LibraryCard card = new LibraryCard() {
    };

    @Attribute
    @CsvBindByPosition(position = 3)
    private String rentDate;

    @Attribute
    @CsvBindByPosition(position = 4)
    private String returnDate;

    public Rent() {
    }

    public Rent(long id, Book book, LibraryCard card, String rentDate, String returnDate) {
        setId(id);
        setBook(book);
        setCard(card);
        setRentDate(rentDate);
        setReturnDate(returnDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LibraryCard getCard() {
        return card;
    }

    public void setCard(LibraryCard card) {
        this.card = card;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rent rent)) return false;
        return getId() == rent.getId()
                && Objects.equals(getBook(), rent.getBook())
                && Objects.equals(getCard(), rent.getCard())
                && Objects.equals(getRentDate(), rent.getRentDate())
                && Objects.equals(getReturnDate(), rent.getReturnDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBook(), getCard(), getRentDate(), getReturnDate());
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + getId() +
                ", book=" + getBook() +
                ", card=" + getCard() +
                ", rentDate='" + getRentDate() + '\'' +
                ", returnDate='" + getReturnDate() + '\'' +
                '}';
    }
}
