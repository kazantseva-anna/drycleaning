package drycleaning;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.HashMap;

class Schedule {
    private EnumMap<DayOfWeek, WorkingTime> weekDayWorkingHours;
    private HashMap<LocalDate, WorkingTime> dateWorkingHours;

    Schedule(String defaultOpeningTime, String defaultClosingTime) {
        this.weekDayWorkingHours = new EnumMap<>(DayOfWeek.class);
        this.dateWorkingHours = new HashMap<>();

        // setting default opening time for all week days
        for (DayOfWeek day : DayOfWeek.values()) {
            weekDayWorkingHours.put(day, WorkingTime.opening(defaultOpeningTime, defaultClosingTime));
        }
    }

    public void setOpeningHours(DayOfWeek day, String openingTime, String closingTime) {
        weekDayWorkingHours.put(day, WorkingTime.opening(openingTime, closingTime));
    }

    public void setOpeningHours(String day, String openingTime, String closingTime) {
        dateWorkingHours.put(DateTimeHelper.parseDate(day), WorkingTime.opening(openingTime, closingTime));
    }

    public void setClosed(DayOfWeek... days) {
        for (DayOfWeek day : days) {
            weekDayWorkingHours.put(day, WorkingTime.closed());
        }
    }

    public void setClosed(String... days) {
        for (String day : days) {
            dateWorkingHours.put(DateTimeHelper.parseDate(day), WorkingTime.closed());
        }
    }

    public LocalTime getOpeningTime(LocalDate day) {
        WorkingTime workingTime = dateWorkingHours.get(day);
        if (workingTime != null && workingTime.isOpen())
            return workingTime.getOpeningTime();

        workingTime = weekDayWorkingHours.get(DayOfWeek.from(day));
        return workingTime.isOpen() ? workingTime.getOpeningTime() : null;
    }

    public LocalTime getClosingTime(LocalDate day) {
        WorkingTime workingTime = dateWorkingHours.get(day);
        if (workingTime != null && workingTime.isOpen())
            return workingTime.getClosingTime();

        workingTime = weekDayWorkingHours.get(DayOfWeek.from(day));
        return workingTime.isOpen() ? workingTime.getClosingTime() : null;
    }

    public long getSecondsTillClosing(LocalTime start, LocalDate day) {
        LocalTime closingTime = getClosingTime(day);
        return closingTime != null ? ChronoUnit.SECONDS.between(start, closingTime) : 0;
    }

    public long getWorkingSecondsPerDay(LocalDate day) {
        WorkingTime workingTime = dateWorkingHours.get(day);
        if (workingTime != null)
            return workingTime.getDurationInSeconds();

        workingTime = weekDayWorkingHours.get(DayOfWeek.from(day));
        return workingTime.getDurationInSeconds();
    }
    
    public boolean isWorkingDay(LocalDate day) {
        return getWorkingSecondsPerDay(day) > 0;
    }

    @Override
    public String toString() {
        return String.format("Schedule:%nWeek: %s%nDate: %s", weekDayWorkingHours, dateWorkingHours);
    }
}
