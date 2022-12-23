package ru.sfedu.bibliohub;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.bibliohub.api.AbstractDataProvider;
import ru.sfedu.bibliohub.api.DataProviderCsv;
import ru.sfedu.bibliohub.api.DataProviderJdbc;
import ru.sfedu.bibliohub.api.DataProviderXml;
import ru.sfedu.bibliohub.utils.Constants;
import ru.sfedu.bibliohub.utils.TestData;

import java.util.Arrays;

public class BiblioHubClient {
    private static final Logger log = LogManager.getLogger(BiblioHubClient.class);
    private static AbstractDataProvider dp;

    public static void main(String[] args) {
        checkArgumentsCount(args);
        dp = getDataProvider(args[0]);
        if (dp.getRents().isEmpty()) loadSampleData();
        long[] params = Arrays.stream(Arrays.copyOfRange(args, 2, args.length))
                .mapToLong(Long::parseLong).toArray();

        switch (args[1].toUpperCase()) {
            case (Constants.GIVE_BOOK) -> dp.giveBook(params[0], params[1]);
            case (Constants.VALIDATE_CARD) -> dp.validateCard(params[0]);
            case (Constants.CALCULATE_RETURN_DATE) ->
                    dp.calculateReturnDate((int) params[0], (int) params[1], (int) params[2]);

            case (Constants.WATCH_EXPIRING_RENTS) -> dp.watchExpiringRents(params.length > 0 ? params[0] : 0);
            case (Constants.EXPIRE_RENT_PERIOD) -> dp.expireRentPeriod(params[0]);

            case (Constants.WATCH_EXPIRING_CARDS) -> dp.watchExpiringCards(params.length > 0 ? params[0] : 0);
            case (Constants.EXPIRE_CARD_PERIOD) -> dp.expireCardPeriod(params[0]);
            default -> log.error(Constants.WRONG_ARG);
        }
    }

    private static void checkArgumentsCount(String[] args) {
        if (args.length < 2) {
            log.error(Constants.FEW_ARGS);
            System.exit(0);
        }
    }

    private static AbstractDataProvider getDataProvider(String dataProviderSource) {
        if (dataProviderSource.equalsIgnoreCase(Constants.XML)) return new DataProviderXml();
        else if (dataProviderSource.equalsIgnoreCase(Constants.CSV)) return new DataProviderCsv();
        else if (dataProviderSource.equalsIgnoreCase(Constants.JDBC)) return new DataProviderJdbc();
        else {
            log.error(Constants.WRONG_DP);
            System.exit(0);
            return null;
        }
    }

    private static void loadSampleData() {
        TestData td = new TestData();

        dp.insertRent(td.r1);
        dp.insertBook(td.b1);
        dp.insertBook(td.b2);
        dp.insertBook(td.b3);
        dp.insertBook(td.b4);

        dp.insertTemporaryCard(td.t1);
        dp.insertTemporaryCard(td.t2);

        dp.insertPerpetualCard(td.p1);
        dp.insertPerpetualCard(td.p2);

        dp.insertRent(td.r1);
        dp.insertRent(td.r2);
        dp.insertRent(td.r3);
        dp.insertRent(td.r4);
    }
}