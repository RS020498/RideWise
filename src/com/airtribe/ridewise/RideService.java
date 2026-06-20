package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class RideService {

    private final RideMatchingStrategy matchingStrategy;
    private final FareStrategy fareStrategy;
    private final DriverService driverService;
    private final List<Ride> rides = new ArrayList<>();

    public RideService(RideMatchingStrategy matchingStrategy,
                       FareStrategy fareStrategy,
                       DriverService driverService) {
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
        this.driverService = driverService;
    }

    public Ride requestRide(Rider rider, double distance) {
        List<Driver> availableDrivers = driverService.getAvailableDrivers();

        Driver driver = matchingStrategy.findDriver(rider, availableDrivers);

        if (driver == null) {
            throw new NoDriverAvailableException(
                    "No drivers available for rider: " + rider.getName());
        }

        String id = IdGenerator.nextId("RIDE");
        Ride ride = new Ride(id, rider, distance);
        ride.setDriver(driver);
        ride.setStatus(RideStatus.ASSIGNED);

        driverService.updateAvailability(driver.getId(), false);
        rides.add(ride);

        return ride;
    }

    public FareReceipt completeRide(String rideId) {
        Ride ride = getRideById(rideId);

        if (ride == null) {
            throw new IllegalArgumentException("Ride not found: " + rideId);
        }

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException(
                    "Ride cannot be completed. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.COMPLETED);
        driverService.updateAvailability(ride.getDriver().getId(), true);

        double amount = fareStrategy.calculateFare(ride);
        return new FareReceipt(ride.getId(), amount);
    }

    public Ride getRideById(String rideId) {
        for (Ride ride : rides) {
            if (ride.getId().equals(rideId)) {
                return ride;
            }
        }
        return null;
    }

    public List<Ride> getAllRides() {
        return rides;
    }
}