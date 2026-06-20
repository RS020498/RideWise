# RideWise — Requirements

## A. Functional Requirements
1. Register Riders
2. Register Drivers
3. Show available drivers
4. Request a ride
5. Match ride to driver using a strategy
6. Calculate fare using a pricing strategy
7. Track ride status: REQUESTED, ASSIGNED, COMPLETED, CANCELLED

## B. Non-Functional Requirements
- Easily extendable pricing algorithm
- Easily change driver matching logic
- Low coupling between services
- Maintainable and readable code

## Scope Decisions
- Built as a plain Java console application — no web framework, no database,
  no persistence beyond in-memory lists. This matches the assignment's stated
  goal of demonstrating LLD and SOLID principles, not building a production app.
- `VehicleType` enum exists as required by the brief, but is not yet wired into
  any entity, since no functional requirement currently depends on it. It is
  ready to be attached to `Driver` if vehicle-based pricing is added later.
- Out of scope (YAGNI): payments, ratings, surge zones, persistence, authentication.