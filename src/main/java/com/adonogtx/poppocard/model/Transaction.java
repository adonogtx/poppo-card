package com.adonogtx.poppocard.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    BigDecimal amount;
    TransactionType type;
    Location location;
    LocalDateTime timestamp;

    public Transaction(BigDecimal amount, TransactionType type, Location location, LocalDateTime timestamp) {
        this.amount = amount;
        this.type = type;
        this.location = location;
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(amount, that.amount) && type == that.type && Objects.equals(location, that.location) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, type, location, timestamp);
    }
}
