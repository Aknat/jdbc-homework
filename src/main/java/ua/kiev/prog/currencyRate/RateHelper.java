package ua.kiev.prog.currencyRate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static ua.kiev.prog.currencyRate.MultiScanner.scan;

public abstract class RateHelper {

    static final String[] rateTypes = {"AZN", "BYN", "CAD", "CHF", "CNY", "CZK", "DKK",
            "EUR", "GBP", "GEL", "HUF", "ILS", "JPY", "KZT", "MDL", "NOK",
            "PLN", "RUB", "SEK", "SGD", "TMT", "TRY", "UAH", "USD", "UZS"};


    public static int defineRateType() {
        System.out.println("Would you like to receive the currency rate for the definite DATE, " +
                "or to receive the currency average rate for the definite PERIOD?");

        String rateTypeIn; // 1 - currency rate for the date; 2 - the average currency rate for the period
        int rateType = 0;
        do {
            System.out.println("For the rate per date type: 1; for the average rate per period type: 2");

            try {
                rateTypeIn = scan();
                rateType = Integer.parseInt(rateTypeIn);
            } catch (NumberFormatException | IOException e) {
                System.out.println("try again)");
            }
        }
        while ((rateType != 2) && (rateType != 1));
        return rateType;
    }


    public static String defineCurrency() {
        System.out.println("What currency would you like to process?" + "\n" +
                "Available currencies: AZN, BYN, CAD, CHF, CNY, CZK, DKK, EUR, GBP, GEL, " +
                "HUF, ILS, JPY, KZT, MDL, NOK, PLN, RUB, SEK, SGD, TMT, TRY, UAH, USD, UZS");

        String currencyType = null;
        boolean currencyTypeValid = false;
        do {
            System.out.println("Enter a proper currency:");

            try {
                currencyType = scan();
                for (String rateType : rateTypes) {
                    if (currencyType.equals(rateType)) {
                        currencyTypeValid = true;
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("try again)");
            }
        }
        while (!currencyTypeValid);
        return currencyType;
    }


    public static String defineDate() {
        String dateStart = "";
        System.out.println("Enter the desired date in format: dd.MM.yyyy");
        try {
            dateStart = scan();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dateStart;
    }


    public static String[] definePeriod() {
        String dateStartIn;
        String dateEndIn;
        String[] period = new String[2];
//        Date dateStart = new Date();
//        Date dateEnd = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        System.out.println("Enter the desired period start date in format: dd.MM.yyyy");
        try {
            dateStartIn = scan();
            period[0] = dateStartIn;
//            dateStart = sdf.parse(dateStartIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enter the desired period end date in format: dd.MM.yyyy");
        try {
            dateEndIn = scan();
            period[1] = dateEndIn;
//            dateEnd = sdf.parse(dateEndIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return period;

        /*
         the code below is needed for further system improvement
         to request privat-API for all missing dates when the user requests the average rate,
         but rates for some dates are missing in the database


        ArrayList<String> period = new ArrayList<>();
        List<Date> dates = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(dateStart);
        Calendar end = Calendar.getInstance();
        end.setTime(dateEnd);
        end.add(Calendar.DAY_OF_YEAR, 1);

        while (start.before(end)) {
            dates.add(start.getTime());
            start.add(Calendar.DAY_OF_YEAR, 1);
        }

        for (Date value : dates) {
            String date = sdf.format(value);
            period.add(date);
        }
        System.out.println(dates.toString());
        System.out.println(period.toString());
        */
    }
}
