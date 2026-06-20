package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PeakHourFareStrategyTest {

    @Test
    void calculateFare_isAlwaysAtLeastTheBaseFare() {
        // Note: this test avoids asserting an exact value because peak-hour
        // detection depends on the real current time (LocalTime.now()),
        // which makes the result non-deterministic across test runs.
        Rider rider = new Rider("RIDER-1", "Alice", new Location(0, 0));
        Ride ride = new Ride("RIDE-1", rider, 10.0);

        PeakHourFareStrategy strategy = new PeakHourFareStrategy();
        double fare = strategy.calculateFare(ride);

        double minimumExpectedFare = 20.0 + (10.0 * 10.0); // base, no peak multiplier
        assertTrue(fare >= minimumExpectedFare);
    }
}