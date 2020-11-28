package drycleaning;

import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BusinessHourCalculator {
    private static final int PROCESSING_DAYS_LIMIT = 365;

    private Schedule schedule;

    public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
        this.schedule = new Schedule(defaultOpeningTime, defaultClosingTime);
    }

    public void setOpeningHours(DayOfWeek day, String openingTime, String closingTime) {
        schedule.setOpeningHours(day, openingTime, closingTime);
    }

    public void setOpeningHours(String day, String openingTime, String closingTime) {
        schedule.setOpeningHours(day, openingTime, closingTime);
    }

    public void setClosed(DayOfWeek... days) {
        schedule.setClosed(days);
    }

    public void setClosed(String... days) {
        schedule.setClosed(days);
    }

    public Date calculateDeadline(int interval, String startingTimeString) {
        LocalDateTime startingDateTime = DateTimeHelper.parseDateTime(startingTimeString);
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
        int remainigIntervalInSeconds = interval;
        while (remainigIntervalInSeconds > workingSecondsPerDay) {
            days++;
            if (days == PROCESSING_DAYS_LIMIT) {
                throw new IllegalStateException(String.format(
                        "We are not able to finish in %d days.%nPlease, come back later.", PROCESSING_DAYS_LIMIT));
            }
            remainigIntervalInSeconds -= workingSecondsPerDay;
            workingSecondsPerDay = schedule.getWorkingSecondsPerDay(startingDate.plusDays(days));
            startingTime = schedule.getOpeningTime(startingDate.plusDays(days));
        }

        LocalDateTime deadline = LocalDateTime.of(startingDate.plusDays(days),
                startingTime.plusSeconds(remainigIntervalInSeconds));

        return DateTimeHelper.localDateTimeToDate(deadline);
    }

    @Override
    public String toString() {
        return schedule.toString();
    }
}
