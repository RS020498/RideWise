package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final DriverService driverService = new DriverService();
    private static final RiderService riderService = new RiderService();

    // strategies injected into RideService constructor — DIP in action
    private static final RideMatchingStrategy matchingStrategy =
            new NearestDriverStrategy();
    private static final FareStrategy fareStrategy =
            new DefaultFareStrategy();
    private static final RideService rideService =
            new RideService(matchingStrategy, fareStrategy, driverService);

    public static void main(String[] args) {
        System.out.println("Welcome to RideWise!");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> addRider();
                case 2 -> addDriver();
                case 3 -> viewAvailableDrivers();
                case 4 -> requestRide();
                case 5 -> completeRide();
                case 6 -> viewRides();
                case 7 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println(
                        "Invalid choice. Please enter a number between 1 and 7.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- RideWise Menu ---");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Exit");
    }

    // ── Option 1 ──────────────────────────────────────────────────────────────

    private static void addRider() {
        System.out.println("\n-- Add Rider --");
        String name = readString("Enter rider name: ");
        double x = readDouble("Enter rider location X: ");
        double y = readDouble("Enter rider location Y: ");

        Rider rider = riderService.registerRider(name, new Location(x, y));
        System.out.println("Rider registered successfully!");
        System.out.println("ID: " + rider.getId() + " | Name: " + rider.getName());
    }

    // ── Option 2 ──────────────────────────────────────────────────────────────

    private static void addDriver() {
        System.out.println("\n-- Add Driver --");
        String name = readString("Enter driver name: ");
        double x = readDouble("Enter driver location X: ");
        double y = readDouble("Enter driver location Y: ");

        Driver driver = driverService.registerDriver(name, new Location(x, y));
        System.out.println("Driver registered successfully!");
        System.out.println("ID: " + driver.getId() + " | Name: " + driver.getName());
    }

    // ── Option 3 ──────────────────────────────────────────────────────────────

    private static void viewAvailableDrivers() {
        System.out.println("\n-- Available Drivers --");
        List<Driver> drivers = driverService.getAvailableDrivers();

        if (drivers.isEmpty()) {
            System.out.println("No drivers available at the moment.");
            return;
        }

        for (Driver driver : drivers) {
            System.out.println("ID: " + driver.getId()
                    + " | Name: " + driver.getName()
                    + " | Location: (" + driver.getCurrentLocation().getX()
                    + ", " + driver.getCurrentLocation().getY() + ")");
        }
    }

    // ── Option 4 ──────────────────────────────────────────────────────────────

    private static void requestRide() {
        System.out.println("\n-- Request Ride --");
        String riderId = readString("Enter rider ID: ");

        Rider rider = riderService.getRiderById(riderId);
        if (rider == null) {
            System.out.println("Rider not found with ID: " + riderId);
            return;
        }

        double distance = readDouble("Enter ride distance (km): ");

        try {
            Ride ride = rideService.requestRide(rider, distance);
            System.out.println("Ride requested successfully!");
            System.out.println("Ride ID   : " + ride.getId());
            System.out.println("Rider     : " + ride.getRider().getName());
            System.out.println("Driver    : " + ride.getDriver().getName());
            System.out.println("Distance  : " + ride.getDistance() + " km");
            System.out.println("Status    : " + ride.getStatus());
        } catch (NoDriverAvailableException e) {
            System.out.println("Sorry! " + e.getMessage());
        }
    }

    // ── Option 5 ──────────────────────────────────────────────────────────────

    private static void completeRide() {
        System.out.println("\n-- Complete Ride --");
        String rideId = readString("Enter ride ID: ");

        try {
            FareReceipt receipt = rideService.completeRide(rideId);
            System.out.println("Ride completed successfully!");
            System.out.println("Fare Receipt: " + receipt);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── Option 6 ──────────────────────────────────────────────────────────────

    private static void viewRides() {
        System.out.println("\n-- All Rides --");
        List<Ride> rides = rideService.getAllRides();

        if (rides.isEmpty()) {
            System.out.println("No rides found.");
            return;
        }

        for (Ride ride : rides) {
            String driverName = ride.getDriver() != null
                    ? ride.getDriver().getName()
                    : "Unassigned";
            System.out.println("Ride ID  : " + ride.getId()
                    + " | Rider: " + ride.getRider().getName()
                    + " | Driver: " + driverName
                    + " | Distance: " + ride.getDistance() + " km"
                    + " | Status: " + ride.getStatus());
        }
    }

    // ── Input helpers ─────────────────────────────────────────────────────────

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}