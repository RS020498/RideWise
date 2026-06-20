package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 20.0;
    private static final double RATE_PER_KM = 10.0;
    private static final double PEAK_MULTIPLIER = 1.5;

    private static final LocalTime MORNING_PEAK_START = LocalTime.of(8, 0);
    private static final LocalTime MORNING_PEAK_END = LocalTime.of(10, 0);
    private static final LocalTime EVENING_PEAK_START = LocalTime.of(17, 0);
    private static final LocalTime EVENING_PEAK_END = LocalTime.of(20, 0);

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = BASE_FARE + (RATE_PER_KM * ride.getDistance());

        if (isPeakHour()) {
            return baseFare * PEAK_MULTIPLIER;
        }

        return baseFare;
    }

    private boolean isPeakHour() {
        LocalTime now = LocalTime.now();
        boolean isMorningPeak = !now.isBefore(MORNING_PEAK_START)
                && now.isBefore(MORNING_PEAK_END);
        boolean isEveningPeak = !now.isBefore(EVENING_PEAK_START)
                && now.isBefore(EVENING_PEAK_END);
        return isMorningPeak || isEveningPeak;
    }
}