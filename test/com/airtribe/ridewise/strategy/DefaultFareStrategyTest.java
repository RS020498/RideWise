package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultFareStrategyTest {

    @Test
    void calculateFare_returnsBaseFarePlusDistanceRate() {
        Rider rider = new Rider("RIDER-1", "Alice", new Location(0, 0));
        Ride ride = new Ride("RIDE-1", rider, 10.0); // 10 km

        DefaultFareStrategy strategy = new DefaultFareStrategy();
        double fare = strategy.calculateFare(ride);

        // BASE_FARE (20.0) + RATE_PER_KM (10.0) * distance (10.0) = 120.0
        assertEquals(120.0, fare, 0.0001);
    }
}