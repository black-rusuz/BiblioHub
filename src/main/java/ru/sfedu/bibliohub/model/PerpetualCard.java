package ru.sfedu.bibliohub.model;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.util.Objects;

@Element
public class PerpetualCard extends LibraryCard {
    @Attribute
    @CsvBindByPosition(position = 2)
    private String reason = "";

    public PerpetualCard() {
    }

    public PerpetualCard(long id, String name, String reason) {
        super(id, name);
        setReason(reason);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PerpetualCard that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getReason(), that.getReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getReason());
    }

    @Override
    public String toString() {
        return "PerpetualCard{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}