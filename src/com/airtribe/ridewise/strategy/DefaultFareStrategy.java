package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 20.0;
    private static final double RATE_PER_KM = 10.0;

    @Override
    public double calculateFare(Ride ride) {
        return BASE_FARE + (RATE_PER_KM * ride.getDistance());
    }
}