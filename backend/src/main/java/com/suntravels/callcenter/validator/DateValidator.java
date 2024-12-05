package com.suntravels.callcenter.validator;

import java.time.LocalDate;

public class DateValidator {

    /**
     * Validates check-in and check-out dates to ensure they are not in the past
     * and that the check-out date is after the check-in date.
     *
     * @param checkinDate The check-in date.
     * @param checkoutDate The check-out date.
     * @throws IllegalStateException if the dates are invalid.
     */
    public static void validateDates(LocalDate checkinDate, LocalDate checkoutDate) {
        if (checkinDate.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Check-in Date cannot be in the past");
        }
        if (checkoutDate.isBefore(checkinDate)) {
            throw new IllegalStateException("Checkout Date must be after Check-in Date");
        }
        if (checkoutDate.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Checkout Date cannot be in the past");
        }
    }
}
