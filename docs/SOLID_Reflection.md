# RideWise — SOLID & Design Principles Reflection

## SRP — Single Responsibility Principle
Each class has exactly one reason to change:
- `RiderService` only changes if rider registration/lookup logic changes.
- `DriverService` only changes if driver registration/availability logic changes.
- `RideService` only changes if ride request/completion orchestration changes.
- Model classes (`Rider`, `Driver`, `Ride`, `FareReceipt`) hold zero business
  logic — they are pure data holders. Any temptation to add a method like
  `rider.requestRide()` was deliberately avoided, since that logic belongs
  in the service layer, not the entity.

## OCP — Open/Closed Principle
New ride-matching or pricing behavior can be added without touching existing
code. Example: adding `SurgePricingFareStrategy` means writing one new class
that implements `FareStrategy` — zero changes to `RideService`,
`DefaultFareStrategy`, or `PeakHourFareStrategy`. The system is open for
extension, closed for modification.

## LSP — Liskov Substitution Principle
Both `NearestDriverStrategy` and `LeastActiveDriverStrategy` can be passed
into `RideService`'s constructor interchangeably — `RideService` behaves
correctly regardless of which one is injected, because both fully honor the
`RideMatchingStrategy` contract (same input types, same return type, no
hidden assumptions). Same applies to `DefaultFareStrategy` and
`PeakHourFareStrategy` under `FareStrategy`.

## ISP — Interface Segregation Principle
`RideMatchingStrategy` and `FareStrategy` are each a single method — no
implementation is forced to support behavior it doesn't need. A class
implementing `FareStrategy` is never forced to also implement matching logic,
and vice versa.

## DIP — Dependency Inversion Principle
`RideService` depends on the `RideMatchingStrategy` and `FareStrategy`
interfaces, never on concrete classes. Both are passed in through the
constructor (constructor injection) — `RideService` has no `new
NearestDriverStrategy()` or `new DefaultFareStrategy()` anywhere inside it.
The decision of *which* concrete strategy to use is made once, at the top of
`Main.java`, not buried inside the service.

## DRY — Don't Repeat Yourself
Distance calculation exists in exactly one place: `Location.distanceTo()`.
Without this, `NearestDriverStrategy` (and any future distance-based
strategy) would each need their own copy of the same formula.

## KISS — Keep It Simple
Entities are plain data holders. No inheritance hierarchies, no unnecessary
abstraction layers. `Location` is the one "extra" class introduced beyond
the document's literal entity list, and it exists for a concrete reason
(supporting `NearestDriverStrategy` without duplicating math), not
speculative design.

## YAGNI — You Aren't Gonna Need It
Deliberately not implemented: ratings, payments, surge zones, persistence,
authentication, multiple vehicle-based pricing. `VehicleType` enum exists
because the document lists it, but is not wired into any class, since no
current requirement needs it.

## Law of Demeter
Services talk to immediate collaborators only:
- `RideService` calls `driverService.getAvailableDrivers()` and
  `driverService.updateAvailability(...)` directly — it never reaches into
  `DriverService`'s internal list or mutates a `Driver`'s availability flag
  itself.
- Strategies receive exactly the data they need as method/constructor
  parameters (`Rider`, `List<Driver>`, `List<Ride>`) rather than reaching
  through other objects to find it.
- The one borderline case: `rider.getLocation().distanceTo(...)` — this is
  treated as acceptable because `Location` is an immutable value object with
  no side effects, analogous to calling `.equals()` on a `String` returned by
  a getter, not a violation of the principle's intent (which targets chains
  through *services* with behavior and side effects).