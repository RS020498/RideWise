package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    private final List<Ride> allRides;

    public LeastActiveDriverStrategy(List<Ride> allRides) {
        this.allRides = allRides;
    }

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Driver leastActive = null;
        int minRides = Integer.MAX_VALUE;

        for (Driver driver : drivers) {
            int completedRides = countCompletedRides(driver);
            if (completedRides < minRides) {
                minRides = completedRides;
                leastActive = driver;
            }
        }

        return leastActive;
    }

    private int countCompletedRides(Driver driver) {
        int count = 0;
        for (Ride ride : allRides) {
            if (ride.getDriver() != null
                    && ride.getDriver().getId().equals(driver.getId())
                    && ride.getStatus() == RideStatus.COMPLETED) {
                count++;
            }
        }
        return count;
    }
}