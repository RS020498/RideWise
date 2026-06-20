package com.airtribe.ridewise.service;

import com.airtribe.ridewise.DriverService;
import com.airtribe.ridewise.RideService;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RideServiceTest {

    private DriverService driverService;
    private RideService rideService;
    private Rider rider;
    private Driver driver;

    // simple fixed-return test doubles — no need for a mocking library here
    private final RideMatchingStrategy alwaysPicksFirstDriver =
            (r, drivers) -> drivers.isEmpty() ? null : drivers.get(0);

    private final FareStrategy fixedFareStrategy = ride -> 100.0;

    @BeforeEach
    void setUp() {
        driverService = new DriverService();
        rideService = new RideService(alwaysPicksFirstDriver, fixedFareStrategy, driverService);

        rider = new Rider("RIDER-1", "Alice", new Location(0, 0));
        driver = driverService.registerDriver("Bob", new Location(1, 1));
    }

    @Test
    void requestRide_assignsAvailableDriverAndMarksUnavailable() {
        Ride ride = rideService.requestRide(rider, 5.0);

        assertEquals(RideStatus.ASSIGNED, ride.getStatus());
        assertEquals(driver.getId(), ride.getDriver().getId());
        assertFalse(driver.isAvailable());
    }

    @Test
    void requestRide_noAvailableDrivers_throwsException() {
        driverService.updateAvailability(driver.getId(), false); // make unavailable

        assertThrows(NoDriverAvailableException.class,
                () -> rideService.requestRide(rider, 5.0));
    }

    @Test
    void completeRide_marksCompletedAndFreesDriver() {
        Ride ride = rideService.requestRide(rider, 5.0);

        FareReceipt receipt = rideService.completeRide(ride.getId());

        assertEquals(RideStatus.COMPLETED, ride.getStatus());
        assertTrue(driver.isAvailable());
        assertEquals(ride.getId(), receipt.getRideId());
        assertEquals(100.0, receipt.getAmount(), 0.0001);
    }

    @Test
    void completeRide_alreadyCompleted_throwsException() {
        Ride ride = rideService.requestRide(rider, 5.0);
        rideService.completeRide(ride.getId());

        assertThrows(IllegalStateException.class,
                () -> rideService.completeRide(ride.getId()));
    }

    @Test
    void completeRide_unknownRideId_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> rideService.completeRide("RIDE-DOES-NOT-EXIST"));
    }
}