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

    public void setOpeningHours(String date, String openingTime, String closingTime) {
        dateWorkingHours.put(DateTimeHelper.parseDate(date), WorkingTime.opening(openingTime, closingTime));
    }

    public void setClosed(DayOfWeek... days) {
        for (DayOfWeek day : days) {
            weekDayWorkingHours.put(day, WorkingTime.closed());
        }
    }

    public void setClosed(String... dates) {
        for (String date : dates) {
            dateWorkingHours.put(DateTimeHelper.parseDate(date), WorkingTime.closed());
        }
    }

    public LocalTime getOpeningTime(LocalDate date) {
        WorkingTime workingTime = dateWorkingHours.get(date);
        if (workingTime != null && workingTime.isOpen())
            return workingTime.getOpeningTime();

        workingTime = weekDayWorkingHours.get(DayOfWeek.from(date));
        return workingTime.isOpen() ? workingTime.getOpeningTime() : null;
    }

    public LocalTime getClosingTime(LocalDate date) {
        WorkingTime workingTime = dateWorkingHours.get(date);
        if (workingTime != null && workingTime.isOpen())
            return workingTime.getClosingTime();

        workingTime = weekDayWorkingHours.get(DayOfWeek.from(date));
        return workingTime.isOpen() ? workingTime.getClosingTime() : null;
    }

    public long getSecondsTillClosing(LocalTime start, LocalDate date) {
        LocalTime closingTime = getClosingTime(date);
        return closingTime != null ? ChronoUnit.SECONDS.between(start, closingTime) : 0;
    }

    public long getWorkingSecondsPerDay(LocalDate date) {
        WorkingTime workingTime = dateWorkingHours.get(date);
        if (workingTime != null)
            return workingTime.getDurationInSeconds();

        workingTime = weekDayWorkingHours.get(DayOfWeek.from(date));
        return workingTime.getDurationInSeconds();
    }
    
    public boolean isWorkingDay(LocalDate date) {
        return getWorkingSecondsPerDay(date) > 0;
    }

    @Override
    public String toString() {
        return String.format("Schedule:%nWeek: %s%nDate: %s", weekDayWorkingHours, dateWorkingHours);
    }
}
