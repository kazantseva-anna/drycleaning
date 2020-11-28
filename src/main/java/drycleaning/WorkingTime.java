package drycleaning;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

class WorkingTime {
    private boolean isOpen;
    private LocalTime openingTime;
    private LocalTime closingTime;

    // the class can be only instantiated by static factory-methods
    private WorkingTime() {
    }

    public static WorkingTime opening(String openingTime, String closingTime) {
        LocalTime opening = DateTimeHelper.parseTime(openingTime);
        LocalTime closing = DateTimeHelper.parseTime(closingTime);

        if (!closing.isAfter(opening)) {
            throw new IllegalArgumentException("The closing time must be after the opening time.");
        }

        WorkingTime workingTime = new WorkingTime();
        workingTime.isOpen = true;
        workingTime.openingTime = opening;
        workingTime.closingTime = closing;
        return workingTime;
    }

    public static WorkingTime closed() {
        WorkingTime workingTime = new WorkingTime();
        workingTime.isOpen = false;
        return workingTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public long getDurationInSeconds() {
        return isOpen ? ChronoUnit.SECONDS.between(openingTime, closingTime) : 0;
    }

    @Override
    public String toString() {
        return String.format("{ open=%b, openingTime=%s, closingTime=%s }", isOpen, openingTime, closingTime);
    }
}
