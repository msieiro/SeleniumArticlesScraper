package com.msieiro.SeleniumArticlesScraper.domain.utils;

import java.time.LocalDate;
import java.time.Month;

public final class DateUtils {

    /**
     * Date parser from String to LocalDate
     *
     * @param stringDate with format like: <b>August 01, 2020</b>
     * @return the parsed date from String to LocalDate
     */
    public static LocalDate parseStringDateToLocalDate3(final String stringDate) {
        final int day = Integer.parseInt(stringDate.split(" ")[1].split(",")[0]);
        final Month month = parseStringMonthToMonthClazz(stringDate.split(" ")[0]);
        final int year = Integer.parseInt(stringDate.split(" ")[2]);
        return LocalDate.of(year, month, day);
    }

    /**
     * Date parser from String to LocalDate
     *
     * @param stringDate with format like: <b>10th April 2022</b>
     * @return the parsed date from String to LocalDate
     */
    public static LocalDate parseStringDateToLocalDate2(final String stringDate) {
        final int day = Integer.parseInt(stringDate.split(" ")[0].replaceAll("[a-z]", ""));
        final Month month = parseStringMonthToMonthClazz(stringDate.split(" ")[1]);
        final int year = Integer.parseInt(stringDate.split(" ")[2]);
        return LocalDate.of(year, month, day);
    }

    /**
     * Date parser from String to LocalDate
     *
     * @param stringDate with format like: <b>26 julio 2022</b>
     * @return the parsed date from String to LocalDate
     */
    public static LocalDate parseStringDateToLocalDate(final String stringDate) {
        final int day = Integer.parseInt(stringDate.split(" ")[0]);
        final Month month = parseStringMonthToMonthClazz(stringDate.split(" ")[1]);
        final int year = Integer.parseInt(stringDate.split(" ")[2]);
        return LocalDate.of(year, month, day);
    }

    /**
     * Month parser from String to Month
     *
     * @param month in String
     * @return Month class
     */
    public static Month parseStringMonthToMonthClazz(final String month) {
        final String lowerMonth = month.toLowerCase();

        switch (lowerMonth) {
            case "enero":
            case "january":
            case "jan":
                return Month.JANUARY;
            case "febrero":
            case "february":
            case "feb":
                return Month.FEBRUARY;
            case "marzo":
            case "march":
            case "mar":
                return Month.MARCH;
            case "abril":
            case "april":
            case "apr":
                return Month.APRIL;
            case "mayo":
            case "may":
                return Month.MAY;
            case "junio":
            case "june":
            case "jun":
                return Month.JUNE;
            case "julio":
            case "july":
            case "jul":
                return Month.JULY;
            case "agosto":
            case "august":
            case "aug":
                return Month.AUGUST;
            case "septiembre":
            case "september":
            case "sept":
            case "sep":
                return Month.SEPTEMBER;
            case "octubre":
            case "october":
            case "oct":
                return Month.OCTOBER;
            case "noviembre":
            case "november":
            case "nov":
                return Month.NOVEMBER;
            case "diciembre":
            case "december":
            case "dec":
                return Month.DECEMBER;
        }

        return null;
    }

}
