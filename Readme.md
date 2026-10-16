# Restaurant Kitchen — Facade Pattern

## Description

This project demonstrates the Facade design pattern using a restaurant system.

The workflow contains three independent subsystems:

- Subsystem A — Order and Inventory
- Subsystem B — Kitchen
- Subsystem C — Billing and Delivery

`RestaurantFacade` coordinates them in the sequence:

A → B → C

`AdminFacade` is used to inspect saved data.

## Persistence

The application uses text files:

- `orders.txt`
- `kitchen.txt`
- `delivery.txt`
- `chain.txt`

`ChainState` stores the progress of the whole operation:

- `PENDING_B`
- `PENDING_C`
- `COMPLETED`
- `COMPENSATED`

The data remains available after restarting the application.

## Failure and Compensation

If B fails after A succeeds:

A → B fails → rollback A.

If C fails after A and B succeed:

A → B → C fails → rollback B → rollback A.

An unavailable dish is an additional failure case. It happens before anything is committed, so compensation is not required.

## Principle of Least Knowledge

Without Facade, the client would need to work directly with many subsystem classes.

With Facade, the client only calls:

```java
restaurant.placeOrder(1, "Pizza", false, false);
```

The Facade hides the internal A → B → C coordination.

## Facade vs Adapter

Facade simplifies access to several classes through one interface.

Adapter converts one interface into another.

This project uses Facade because it coordinates several independent restaurant subsystems.

## Tests

JUnit 5 tests cover:

- normal order
- kitchen failure
- payment failure
- persistence/reload
- unavailable dish
- repositories and subsystem services

Line coverage: **81%**.

## Run

Run `Main.java`.

Menu:

1. Normal order
2. Kitchen failure
3. Payment failure
4. Show saved data
5. Exit
6. Resume order

## Reset

For a clean demo, delete:

`orders.txt`, `kitchen.txt`, `delivery.txt`, `chain.txt`.

They will be created again automatically.

## UML

The UML diagram represents the three subsystems, both Facades, repositories and `ChainState`.