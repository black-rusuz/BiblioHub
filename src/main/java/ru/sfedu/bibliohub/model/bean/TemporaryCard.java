package ru.sfedu.bibliohub.model.bean;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;

import java.util.Objects;

public class TemporaryCard extends LibraryCard {
    @Attribute
    @CsvBindByPosition(position = 5)
    private String startDate = "";
    @Attribute
    @CsvBindByPosition(position = 6)
    private String endDate = "";

    public TemporaryCard() {
    }

    public TemporaryCard(long id, String firstName, String lastName, String birthDate, String work, String startDate, String endDate) {
        super(id, firstName, lastName, birthDate, work);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemporaryCard that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getStartDate(), that.getStartDate())
                && Objects.equals(getEndDate(), that.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        return "TemporaryCard{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", birthDate='" + getBirthDate() + '\'' +
                ", work='" + getWork() + '\'' +
                ", startDate='" + getStartDate() + '\'' +
                ", endDate='" + getEndDate() + '\'' +
                '}';
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
