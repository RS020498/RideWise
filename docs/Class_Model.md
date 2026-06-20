# RideWise — Class Model

## model/
- **Rider** — id, name, location. Pure data, no behavior.
- **Driver** — id, name, currentLocation, available. Pure data, no behavior.
- **Location** — x, y coordinates + `distanceTo()`. Introduced to support
  `NearestDriverStrategy` without duplicating distance math (DRY). Immutable
  value object — locations are replaced, never mutated in place.
- **Ride** — id, rider, driver, distance, status. `driver` starts `null` and
  `status` starts `REQUESTED`; both are set later as the ride progresses.
- **FareReceipt** — rideId, amount, generatedAt. Immutable record of a
  completed transaction; `generatedAt` is set automatically at construction.
- **RideStatus** (enum) — REQUESTED, ASSIGNED, COMPLETED, CANCELLED.
- **VehicleType** (enum) — BIKE, AUTO, CAR. Defined per spec, currently unused
  by any entity (no requirement currently needs it).

## strategy/
- **RideMatchingStrategy** (interface) — `findDriver(Rider, List<Driver>)`
  - NearestDriverStrategy — picks the driver with the smallest `distanceTo()`
  - LeastActiveDriverStrategy — picks the driver with the fewest COMPLETED
    rides, computed from an injected `List<Ride>` (since `Driver` itself
    carries no ride-count field, per the original entity spec)
- **FareStrategy** (interface) — `calculateFare(Ride)`
  - DefaultFareStrategy — base fare + rate per km
  - PeakHourFareStrategy — same formula, ×1.5 during defined peak windows

## service/
- **RiderService** — register, get-by-id, get-all
- **DriverService** — register, get-by-id, get-available, update-availability
- **RideService** — request ride (uses RideMatchingStrategy), complete ride
  (uses FareStrategy), get-by-id, get-all. Depends only on the two strategy
  interfaces and `DriverService` — never on concrete strategy classes.

## exception/
- **NoDriverAvailableException** — unchecked, thrown by `RideService` when
  no driver can be matched for a ride request.

## util/
- **IdGenerator** — static helper producing readable sequential IDs
  (e.g. `RIDER-1`, `DRIVER-1`, `RIDE-1`).