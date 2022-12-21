package ru.sfedu.bibliohub.model.bean;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;
import java.util.Objects;

public abstract class LibraryCard implements Serializable {
    @Attribute
    @CsvBindByPosition(position = 0)
    private long id;

    @Attribute
    @CsvBindByPosition(position = 1)
    private String firstName = "";

    @Attribute
    @CsvBindByPosition(position = 2)
    private String lastName = "";

    @Attribute
    @CsvBindByPosition(position = 3)
    private String birthDate = "";

    @Attribute
    @CsvBindByPosition(position = 4)
    private String work = "";

    public LibraryCard() {
    }

    public LibraryCard(long id, String firstName, String lastName, String birthDate, String work) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
        setWork(work);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LibraryCard that)) return false;
        return getId() == that.getId()
                && Objects.equals(getFirstName(), that.getFirstName())
                && Objects.equals(getLastName(), that.getLastName())
                && Objects.equals(getBirthDate(), that.getBirthDate())
                && Objects.equals(getWork(), that.getWork());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getBirthDate(), getWork());
    }

    @Override
    public String toString() {
        return "LibraryCard{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", birthDate='" + getBirthDate() + '\'' +
                ", work='" + getWork() + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}
