package com.airtribe.ridewise.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationTest {

    @Test
    void distanceTo_returnsCorrectEuclideanDistance() {
        Location a = new Location(0, 0);
        Location b = new Location(3, 4);

        double distance = a.distanceTo(b);

        assertEquals(5.0, distance, 0.0001);
    }

    @Test
    void distanceTo_sameLocation_returnsZero() {
        Location a = new Location(2, 2);
        Location b = new Location(2, 2);

        assertEquals(0.0, a.distanceTo(b), 0.0001);
    }
}