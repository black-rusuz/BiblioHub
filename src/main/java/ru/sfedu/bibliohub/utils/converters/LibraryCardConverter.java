package ru.sfedu.bibliohub.utils.converters;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.bibliohub.model.bean.LibraryCard;
import ru.sfedu.bibliohub.model.bean.PerpetualCard;
import ru.sfedu.bibliohub.model.bean.TemporaryCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryCardConverter extends AbstractBeanField<LibraryCard, String> {
    public static final String fieldsDelimiter = "::";

    @Override
    public LibraryCard convert(String s) {
        String[] parsed = s.split(fieldsDelimiter);
        return switch (parsed.length) {
            case (6) ->
                    new PerpetualCard(Long.parseLong(parsed[0]), parsed[1], parsed[2], parsed[3], parsed[4], parsed[5]);
            case (7) ->
                    new TemporaryCard(Long.parseLong(parsed[0]), parsed[1], parsed[2], parsed[3], parsed[4], parsed[5], parsed[6]);
            default -> new LibraryCard() {
            };
        };
    }

    @Override
    public String convertToWrite(Object object) {
        LibraryCard libraryCard = (LibraryCard) object;
        List<Object> params = new ArrayList<>(List.of(libraryCard.getId(),
                libraryCard.getFirstName(),
                libraryCard.getLastName(),
                libraryCard.getBirthDate(),
                libraryCard.getWork()));

        if (object instanceof PerpetualCard perpetualCard) {
            params.add(perpetualCard.getReason());
        } else if (object instanceof TemporaryCard temporaryCard) {
            params.add(temporaryCard.getStartDate());
            params.add(temporaryCard.getEndDate());
        }
        return params.stream().map(Object::toString).collect(Collectors.joining(fieldsDelimiter));
    }
}
