package drycleaning;

import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A calculator that determines an expected business time of dry-cleaning requests given a business schedule. 
 */
public class BusinessHourCalculator {
    private static final int PROCESSING_DAYS_LIMIT = 365;

    private final Schedule schedule;

    /**
     * Constructs a BusinessHourCalculator with default working time for all 7 week days.
     * 
     * @param defaultOpeningTime String     the default opening time in the 24h format
     * @param defaultClosingTime String     the default closing time in the 24h format
     */
    public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
        this.schedule = new Schedule(defaultOpeningTime, defaultClosingTime);
    }

    /**
     * Changes opening hours for a given day of the week.
     * 
     * @param day         DayOfWeek the day of the week
     * @param openingTime String    the local opening time in the 24h format
     * @param closingTime String    the local closing time in the 24h format
     */
    public void setOpeningHours(DayOfWeek day, String openingTime, String closingTime) {
        schedule.setOpeningHours(day, openingTime, closingTime);
    }

    /**
     * Changes opening hours for a given date.
     * 
     * @param date        String    the date in the 'yyyy-MM-dd' format
     * @param openingTime String    the local opening time in the 24h format
     * @param closingTime String    the local closing time in the 24h format
     */
    public void setOpeningHours(String date, String openingTime, String closingTime) {
        schedule.setOpeningHours(date, openingTime, closingTime);
    }

    /**
     * Specifies days of the week when the store is closed.
     * 
     * @param days DayOfWeek    the days of the week when it is closed
     */
    public void setClosed(DayOfWeek... days) {
        schedule.setClosed(days);
    }

    /**
     * Specifies dates when the store is closed.
     * 
     * @param dates String      the dates when is it closed
     */
    public void setClosed(String... dates) {
        schedule.setClosed(dates);
    }

    /**
     * Determines the resulting business time given a time interval (in seconds) and a starting local datetime (as a string).
     * 
     * @param interval            int        the expected duration of cleaning in seconds
     * @param startingDateTimeStr String     the local datetime when a cleaning request is made (as a string in the 'yyy-MM-dd H:mm' format)
     * @return the resulting business time when the request can be fulfilled as an instance of {@link java.util.Date}
     */
    public Date calculateDeadline(int interval, String startingDateTimeStr) {
        LocalDateTime startingDateTime = DateTimeHelper.parseDateTime(startingDateTimeStr);
        LocalDate startingDate = startingDateTime.toLocalDate();
        LocalTime startingTime = startingDateTime.toLocalTime();

        if (schedule.isWorkingDay(startingDate)) {
            LocalTime openingTime = schedule.getOpeningTime(startingDate);
            if (startingTime.isBefore(openingTime)) {
                startingTime = openingTime;
            }
        }

        int days = 0;
        long workingSecondsPerDay = schedule.getSecondsTillClosing(startingTime, startingDate);
        int remainingIntervalInSeconds = interval;
        while (remainingIntervalInSeconds > workingSecondsPerDay) {
            days++;
            if (days == PROCESSING_DAYS_LIMIT) {
                throw new IllegalStateException(String.format(
                        "We are not able to finish in %d days.%nPlease, come back later.", PROCESSING_DAYS_LIMIT));
            }
            remainingIntervalInSeconds -= workingSecondsPerDay;
            workingSecondsPerDay = schedule.getWorkingSecondsPerDay(startingDate.plusDays(days));
            startingTime = schedule.getOpeningTime(startingDate.plusDays(days));
        }

        LocalDateTime deadline = LocalDateTime.of(startingDate.plusDays(days),
                startingTime.plusSeconds(remainingIntervalInSeconds));

        return DateTimeHelper.localDateTimeToDate(deadline);
    }

    @Override
    public String toString() {
        return schedule.toString();
    }
}
