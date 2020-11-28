package drycleaning;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class DateTimeHelper {

    private DateTimeHelper() {
    }

    public static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    public static LocalTime parseTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return LocalTime.parse(time, formatter);
    }

    public static LocalDateTime parseDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        return java.sql.Timestamp.valueOf(dateTime);
    }
}
