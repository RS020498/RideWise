package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Rider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NearestDriverStrategyTest {

    private final NearestDriverStrategy strategy = new NearestDriverStrategy();

    @Test
    void findDriver_returnsClosestDriver() {
        Rider rider = new Rider("RIDER-1", "Alice", new Location(0, 0));

        Driver near = new Driver("DRIVER-1", "Bob", new Location(1, 1));
        Driver far = new Driver("DRIVER-2", "Carl", new Location(10, 10));

        Driver result = strategy.findDriver(rider, Arrays.asList(far, near));

        assertEquals("DRIVER-1", result.getId());
    }

    @Test
    void findDriver_emptyDriverList_returnsNull() {
        Rider rider = new Rider("RIDER-1", "Alice", new Location(0, 0));

        Driver result = strategy.findDriver(rider, Collections.emptyList());

        assertNull(result);
    }
}