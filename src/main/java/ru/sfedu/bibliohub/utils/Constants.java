package ru.sfedu.bibliohub.utils;

import ru.sfedu.bibliohub.model.bean.PerpetualCard;

public class Constants {
    // FILE
    public static final String XML_PATH = "XML_PATH";
    public static final String CSV_PATH = "CSV_PATH";

    // JDBC
    public static final String H2_HOSTNAME = "H2_HOSTNAME";
    public static final String H2_USERNAME = "H2_USERNAME";
    public static final String H2_PASSWORD = "H2_PASSWORD";

    // MongoDB
    public static final String MONGO_ENABLE = "MONGO_ENABLE";
    public static final String MONGO_ACTOR = "MONGO_ACTOR";
    public static final String MONGO_CONNECTION = "MONGO_CONNECTION";
    public static final String MONGO_DATABASE = "MONGO_DATABASE";
    public static final String MONGO_COLLECTION = "MONGO_COLLECTION";


    // Internal
    public static final String ENVIRONMENT_VARIABLE = "env";
    public static final String GET_ID = "getId";
    public static final String SET_ID = "setId";
    public static final String XML_PATTERN = "%s.xml";
    public static final String CSV_PATTERN = "%s.csv";
    public static final String FIELDS_DELIMITER = "::";

    // CRUD
    public static final String METHOD_NAME_APPEND = "Append";
    public static final String METHOD_NAME_DELETE = "Delete";
    public static final String METHOD_NAME_UPDATE = "Update";

    // CLI
    public static final String XML = "XML";
    public static final String JDBC = "JDBC";
    public static final String CSV = "CSV";

    public static final String GIVE_BOOK = "GIVEBOOK";
    public static final String VALIDATE_CARD = "VALIDATECARD";
    public static final String CALCULATE_RETURN_DATE = "CALCULATERETURNDATE";
    public static final String WATCH_EXPIRING_RENTS = "WATCHEXPIRINGRENTS";
    public static final String EXPIRE_RENT_PERIOD = "EXPIRERENTPERIOD";
    public static final String WATCH_EXPIRING_CARDS = "WATCHEXPIRINGCARDS";
    public static final String EXPIRE_CARD_PERIOD = "EXPIRECARDPERIOD";

    // Info
    public static final String NOT_FOUND = "%s ID %d not found";
    public static final String DATE_FORMAT = "%d.%d.%d";
    public static final String NEW_RENT = "Created rent:\n";
    public static final String CARD_PERPETUAL = "Card is perpetual. Reason: ";
    public static final String CARD_EXPIRED = "Card is expired";
    public static final String CARD_NOT_EXPIRED = "Card is not expired";
    public static final String RETURN_DATE = "Return date: ";
    public static final String EXPIRING_RENTS = "Expiring rents:\n";
    public static final String EXPIRING_CARDS = "Expiring cards:\n";
    public static final String EXPIRED_PERIOD = "Expired period:\n";

    public static final String FEW_ARGS = "Few arguments";
    public static final String WRONG_DP = "Wrong type of DataProvider";
    public static final String WRONG_ARG = "Wrong argument";
}
