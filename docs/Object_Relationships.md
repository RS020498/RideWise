# RideWise — Object Relationships

| From          | To           | Relationship | Why                                                          |
|---------------|--------------|--------------|----------------------------------------------------------------|
| Rider         | Ride         | Association  | A ride references a rider, but the rider exists independently of any ride — before, during, and after. |
| Driver        | Ride         | Association  | Same reasoning as above — a driver exists independently of any specific ride. |
| Ride          | FareReceipt  | Composition  | A receipt is generated for a specific ride and has no independent meaning outside it. It is created from `ride.getId()` and `ride.getDistance()` (via the fare strategy) and never exists without a parent ride. |
| RideService   | RideMatchingStrategy | Composition | `RideService` holds a strategy instance as a field, set once at construction, used internally for the lifetime of the service. |
| RideService   | FareStrategy | Composition  | Same as above — held as a constructor-injected field. |
| RideService   | DriverService | Association | `RideService` depends on `DriverService` to check availability and update it, but `DriverService` exists independently and could be used by other services too. |

## Why Composition vs Association Matters Here
Association objects (`Rider`, `Driver`) are passed *by reference* into `Ride` —
`Ride` does not create them, does not own their lifecycle, and they survive
the ride's deletion (conceptually). Composition objects (`FareReceipt`, the
strategies inside `RideService`) are tightly bound to their owner's life cycle
and have no independent purpose outside that context.