# Dry cleaning service

Note: the code doesn't containt the main function, all testing is done via unit tests (see `BusinessHourCalculatorTest`).

## Assumptions
1. What if working hours are specified for a date, but we are closed at this weekday? Let’s assume specific date schedule has higher priority.
2. The assignment's description says: "It guarantees to dry-clean
anything within two business hours or less", but there is an example with 7 hours. Let’s assume we need to return the deadline even if the processing takes longer than 2 hours.
3. But what if all week days are closed? Let's assume that if a request cannot be fulfilled within next 365 days (configured by `PROCESSING_DAYS_LIMIT`) we throw an exception.
4. What if day X is closed and then opening hours are set for this day. Should opening hours be used now? Let’s assume they should.

## Prerequisites
- Java 8+
- Maven

## Build & run tests
```
mvn clean test
```
