package ru.sfedu.bibliohub.utils;

import ru.sfedu.bibliohub.model.bean.Book;
import ru.sfedu.bibliohub.model.bean.PerpetualCard;
import ru.sfedu.bibliohub.model.bean.Rent;
import ru.sfedu.bibliohub.model.bean.TemporaryCard;

public class TestData {
    public final Book b1 = new Book(11, "Edgar Allan Poe", "A Descent into the Maelström", 1841);
    public final Book b2 = new Book(12, "Ernest Miller Hemingway", "For Whom the Bell Tolls", 1940);
    public final Book b3 = new Book(13, "John Ronald Reuel Tolkien", "The Lord of the Rings", 1954);
    public final Book b4 = new Book(14, "Andrzej Sapkowski", "Saga o wiedźminie", 1986);

    public final PerpetualCard p1 = new PerpetualCard(21, "Same", "Person", "20.02.2014", "Google", "Good Boy");
    public final PerpetualCard p2 = new PerpetualCard(22, "Jonh", "Doe", "13.05.1998", "Netflix", "Our sponsor");

    public final TemporaryCard t1 = new TemporaryCard(31, "Maxim", "Mokhov", "12.09.1984", "SFedU", "15.12.2022", "15.05.2023");
    public final TemporaryCard t2 = new TemporaryCard(32, "Kirill", "Svalkin", "05.02.2000", "GreenAtom", "15.03.2022", "15.02.2021");

    public final Rent r1 = new Rent(41, b1, t1, "14.09.2022", "14.01.2023");
    public final Rent r2 = new Rent(42, b2, t2, "06.12.2022", "06.02.2023");
    public final Rent r3 = new Rent(43, b3, p1, "10.04.2022", "10.10.2022");
    public final Rent r4 = new Rent(44, b4, p2, "11.05.2022", "11.06.2022");
}
