package com.adonogtx.poppocard.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
