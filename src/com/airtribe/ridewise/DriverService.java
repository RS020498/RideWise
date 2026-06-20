package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class DriverService {

    private final List<Driver> drivers = new ArrayList<>();

    public Driver registerDriver(String name, Location location) {
        String id = IdGenerator.nextId("DRIVER");
        Driver driver = new Driver(id, name, location);
        drivers.add(driver);
        return driver;
    }

    public Driver getDriverById(String id) {
        for (Driver driver : drivers) {
            if (driver.getId().equals(id)) {
                return driver;
            }
        }
        return null;
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> available = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                available.add(driver);
            }
        }
        return available;
    }

    public void updateAvailability(String driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }
}