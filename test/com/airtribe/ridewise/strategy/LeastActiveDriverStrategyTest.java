package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeastActiveDriverStrategyTest {

    @Test
    void findDriver_returnsDriverWithFewestCompletedRides() {
        Rider rider = new Rider("RIDER-1", "Alice", new Location(0, 0));
        Driver busy = new Driver("DRIVER-1", "Bob", new Location(0, 0));
        Driver idle = new Driver("DRIVER-2", "Carl", new Location(0, 0));

        List<Ride> history = new ArrayList<>();
        Ride completedRide = new Ride("RIDE-1", rider, 5.0);
        completedRide.setDriver(busy);
        completedRide.setStatus(RideStatus.COMPLETED);
        history.add(completedRide);

        LeastActiveDriverStrategy strategy = new LeastActiveDriverStrategy(history);

        Driver result = strategy.findDriver(rider, Arrays.asList(busy, idle));

        assertEquals("DRIVER-2", result.getId());
    }

    @Test
    void findDriver_ignoresNonCompletedRides() {
        Rider rider = new Rider("RIDER-1", "Alice", new Location(0, 0));
        Driver driverA = new Driver("DRIVER-1", "Bob", new Location(0, 0));
        Driver driverB = new Driver("DRIVER-2", "Carl", new Location(0, 0));

        List<Ride> history = new ArrayList<>();
        Ride assignedRide = new Ride("RIDE-1", rider, 5.0);
        assignedRide.setDriver(driverA);
        assignedRide.setStatus(RideStatus.ASSIGNED); // not COMPLETED
        history.add(assignedRide);

        LeastActiveDriverStrategy strategy = new LeastActiveDriverStrategy(history);

        // Both drivers have 0 completed rides, so the first one in the list wins
        Driver result = strategy.findDriver(rider, Arrays.asList(driverA, driverB));

        assertEquals("DRIVER-1", result.getId());
    }
}