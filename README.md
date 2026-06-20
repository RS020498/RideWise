# RideWise

A console-based ride-sharing simulator (Uber/Ola-style) built in plain Java to
demonstrate Low-Level Design (LLD), SOLID principles, and the Strategy Pattern.

This project is intentionally **not** a production app — it's a focused
exercise in object-oriented design: modeling entities, separating concerns
into layers, and designing extension points that don't require modifying
existing code when new behavior is added.

## What it does

- Register riders and drivers
- View currently available drivers
- Request a ride, automatically matched to a driver using a pluggable
  matching strategy
- Complete a ride, automatically priced using a pluggable fare strategy
- Track every ride's lifecycle: `REQUESTED → ASSIGNED → COMPLETED` (or `CANCELLED`)
- View full ride history

All of this runs through a simple numbered console menu — no database, no
web server, no external dependencies beyond the JDK and JUnit for tests.

## Why it's built this way

The core design goal: **`RideService` never knows which driver-matching
algorithm or pricing algorithm is in use.** Both are injected through its
constructor as interfaces (`RideMatchingStrategy`, `FareStrategy`). Adding a
new strategy — say, a surge-pricing fare model — means writing one new class
that implements the interface. Zero changes to `RideService` or any other
existing class. That's the Strategy Pattern enabling Open/Closed Principle
in practice, not just in theory.

Full reasoning for every SOLID/design-principle decision made is documented
in [`docs/SOLID_Reflection.md`](docs/SOLID_Reflection.md).

## Project structure

\```
src/
└── com/airtribe/ridewise/
    ├── Main.java              — console menu, wires services + strategies together
    ├── model/                 — Rider, Driver, Ride, FareReceipt, Location, enums
    ├── strategy/               — RideMatchingStrategy, FareStrategy + implementations
    ├── service/                — RiderService, DriverService, RideService
    ├── exception/              — NoDriverAvailableException
    └── util/                   — IdGenerator
test/
└── com/airtribe/ridewise/      — JUnit 5 tests mirroring the src/ structure
docs/
├── Requirements.md             — functional/non-functional scope and exclusions
├── Class_Model.md              — one-page map of every class and its purpose
├── Object_Relationships.md     — association vs. composition reasoning
└── SOLID_Reflection.md         — how each SOLID/design principle was applied
\```

## How to run it

This is a plain Java project (no Maven/Gradle) built and run through IntelliJ IDEA:

1. Open the project in IntelliJ
2. Open `Main.java`
3. Click the green ▶ run icon next to the `main` method
4. Follow the on-screen numbered menu

## How to run the tests

JUnit 5 is attached as a project library (not a build-tool dependency, since
this project has no Maven/Gradle). To run all tests:

1. Right-click the `test` folder in the project tree
2. Select **Run 'All Tests'**

Test coverage focuses on classes with real logic — strategies, `Location`'s
distance math, and `RideService`'s orchestration — and intentionally skips
pure data classes (`Rider`, `Driver`, etc.) and trivial pass-through services,
since testing a getter that returns what was just set adds no value.

## Switching strategies

To change which matching or pricing strategy is active, edit two lines at
the top of `Main.java`:

\```java
private static final RideMatchingStrategy matchingStrategy =
        new NearestDriverStrategy(); // swap for new LeastActiveDriverStrategy(rideService.getAllRides())

private static final FareStrategy fareStrategy =
        new DefaultFareStrategy(); // swap for new PeakHourFareStrategy()
\```

No other file needs to change — this is the practical proof that the design
satisfies the Open/Closed Principle.

## Out of scope (by design)

Payments, ratings, surge zones, persistence, and authentication were
deliberately excluded. The goal of this project is demonstrating correct
LLD and SOLID application, not building a feature-complete app. Full
reasoning is in [`docs/Requirements.md`](docs/Requirements.md).