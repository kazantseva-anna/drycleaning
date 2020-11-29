package drycleaning;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;
import java.time.format.*;

import org.junit.Test;

public class BusinessHourCalculatorTest {
    private final BusinessHourCalculator businessHourCalculator;
    private final DateFormat format;

    public BusinessHourCalculatorTest() {
        businessHourCalculator = new BusinessHourCalculator("09:00", "15:00");
        format = new SimpleDateFormat("yyyy-MM-d H:mm:ss");

        businessHourCalculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHourCalculator.setOpeningHours("2010-12-24", "8:00", "13:00");
        businessHourCalculator.setClosed(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY);
        businessHourCalculator.setClosed("2010-12-25");
    }

    // Test cases from the assignment

    @Test
    public void testShouldFinishToday() throws ParseException {
        Date expectedDate = format.parse("2010-06-07 11:10:00");

        Date actualDate = businessHourCalculator.calculateDeadline(2 * 60 * 60, "2010-06-07 09:10");
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testLongerThanTodayAndNextWeekDayIsClosed() throws ParseException {
        Date expectedDate = format.parse("2010-06-10 09:03:00");

        Date actualDate = businessHourCalculator.calculateDeadline(15 * 60, "2010-06-08 14:48");
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testBeforeOpeningAndLongerThanTodayAndNextDateIsClosedAndNextWeekDayIsClosed() throws ParseException {
        Date expectedDate = format.parse("2010-12-27 11:00:00");

        Date actualDate = businessHourCalculator.calculateDeadline(7 * 60 * 60, "2010-12-24 6:45");
        assertEquals(expectedDate, actualDate);
    }

    // Additional tests

    @Test
    public void testTodayIsClosedShouldFinishTomorrow() throws ParseException {
        Date expectedDate = format.parse("2010-12-23 10:00:00");

        Date actualDate = businessHourCalculator.calculateDeadline(1 * 60 * 60, "2010-12-22 12:00"); // Wednesday
                                                                                                     // (closed)
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllWeekDaysAreClosed() {
        for (DayOfWeek day : DayOfWeek.values()) {
            businessHourCalculator.setClosed(day);
        }

        businessHourCalculator.calculateDeadline(7 * 60 * 60, "2010-12-24 6:45");
    }

    @Test(expected = DateTimeParseException.class)
    public void testIncorrectInput() {
        new BusinessHourCalculator("test123!@#$%%", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpeningAndClosingAreEqualInCalculatorConstructor() {
        new BusinessHourCalculator("00:00", "00:00");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpeningIsLaterThanClosing() {
        new BusinessHourCalculator("20:00", "8:00");
    }
}
