# PoppoCard (ポッポカード)

> A pure Java, in memory IC card sim inspired by the iconic Yakuza shop.

## About

Modeled after Suica, Japan's electronic ticketing system, PoppoCard can be recharged and spent across everyday places: convenience stores, subway stations, vending machines, drugstores, bars, restaurants, cafes.

The name comes from **Poppo** (ポッポ), the convenience store chain from *Ryu Ga Gotoku* (龍が如く), known worldwide as the *Yakuza* series.

## Challenge

Design and implement the domain model described below in pure Java.

- No framework.
- No persistence.
- No HTTP layer.

The goal is object oriented design: deciding which class owns which data, which behavior belongs where, and how to make invalid states hard to represent.

You are expected to define your own classes, fields, and types. This document describes what the domain must do, not how the code should be structured.

## Domain rules

### Balance

- A card starts with a balance of zero.
- A recharge increases the balance by the transaction amount.
- A charge decreases the balance by the transaction amount.
- A charge attempted against an insufficient balance must never go through, and the caller must be forced to deal with that outcome in some explicit way.
- No balance change and no transaction should result from a rejected operation.

### Amount validation

- An amount of zero or less must be rejected, for both recharges and charges.
- Amounts are expressed in US dollars. Consider what happens with rounding if you use a type that is not exact for decimal values.

### Transaction history

- The card keeps every transaction it has ever had, in the order they happened.
- Only successful operations generate a transaction. A rejected operation is not part of the history.
- Whoever holds a reference to the card should not be able to alter its history directly.

### Locations

Every location has a **type**, and every type has two independent capabilities: whether it accepts recharges, and whether it accepts charges.

| Type | Recharge | Charge | Example |
|---|:---:|:---:|---|
| `KONBINI` | Yes | Yes | Poppo |
| `SUBWAY` | Yes | Yes | Kamurocho Station |
| `VENDING_MACHINE` | Yes | No | Vending machine, Sotenbori |
| `DRUGSTORE` | No | Yes | Kotobuki Drugs |
| `BAR` | No | Yes | Serena |
| `RESTAURANT` | No | Yes | Yoronotaki |
| `CAFE` | No | Yes | Cafe Alps |

A location is defined by its **type** plus a **name**. There is no central registry: each location is created at the point of use. Two locations with the same type and the same name should be treated by your code as the same place, wherever that comparison matters.

An operation attempted where the location's type does not allow it (for example, a charge at a `VENDING_MACHINE`) must be rejected.

### Time

Every transaction happens at some point in time, represented with a timestamp such as `2026-09-17T08:02:10.213`.

### Querying the history

The card must be able to answer at least one question about its own history, derived from the history itself rather than tracked separately. Examples: total spent, transactions at a given location, how many operations happened per location type.

## What is expected

- A class or set of classes modeling the card, its balance, and its transaction history.
- A representation of a transaction: amount, type (recharge or charge), location, and timestamp.
- A representation of location that distinguishes what each type is allowed to do.
- A way for two locations with matching type and name to be recognized as the same place.
- A history that stops external code from being able to reach in and change it.
- A way of signaling insufficient balance that the caller cannot silently ignore, with a short written justification for the choice you made.
- At least one query answered directly from the history data, without keeping a second, separately maintained value for it.
- Validation that stops invalid combinations, such as a charge at a vending machine, from ever being recorded.

### Unit tests covering

- a successful recharge
- a successful charge
- a rejected charge due to insufficient balance
- a rejected charge at a location that does not accept charges
- a rejected operation due to a zero or negative amount
- the chronological order of the transaction history
- the query described above

## Example

| Operation | Location | Amount (USD) | Result |
|---|---|---:|---|
| Recharge | Poppo | 20.00 | Accepted |
| Charge | Kamurocho Station | 3.50 | Accepted |
| Charge | Vending machine, Sotenbori | 5.00 | Rejected, location does not accept charges |
| Charge | Serena | 50.00 | Rejected, insufficient balance |