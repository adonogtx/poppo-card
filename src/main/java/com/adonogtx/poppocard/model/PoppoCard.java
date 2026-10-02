package com.adonogtx.poppocard.model;

import com.adonogtx.poppocard.exception.InsufficientBalanceException;
import com.adonogtx.poppocard.exception.InvalidValueException;
import com.adonogtx.poppocard.exception.OperationNotAllowedException;
import com.adonogtx.poppocard.exception.PoppoCardTransactionException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PoppoCard {

    BigDecimal balance;
    List<Transaction> transactionsHistory = new ArrayList<>();

    public PoppoCard() {
        this.balance = new BigDecimal("0.00");
    }

    public Transaction rechargeCard(BigDecimal amount, Location location)
            throws PoppoCardTransactionException {

        Objects.requireNonNull(amount, "amount cannot be null");
        Objects.requireNonNull(location, "location cannot be null");

        if (!location.getType().isAcceptsRecharge()) {
            throw new OperationNotAllowedException(String.format(
                    "RECHARGE not possible on %s", location.getName()));
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(String.format(
                    "The value must be greater than zero: %s", amount));
        }

        Transaction transaction = new Transaction(
                amount,
                TransactionType.RECHARGE,
                location,
                LocalDateTime.now()
        );

        this.balance = this.balance.add(transaction.getAmount());
        this.transactionsHistory.add(transaction);

        return transaction;
    }

    public Transaction chargeCard(BigDecimal amount, Location location)
            throws PoppoCardTransactionException {

        Objects.requireNonNull(amount, "amount cannot be null");
        Objects.requireNonNull(location, "location cannot be null");

        if (!location.getType().isAcceptsCharge()) {
            throw new OperationNotAllowedException(String.format(
                    "CHARGE not possible on %s",
                    location.getName()));
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(String.format(
                    "The value must be greater than zero: %s",
                    amount));
        }

        if (amount.compareTo(this.balance) > 0) {
            throw new InsufficientBalanceException(String.format(
                    "Available balance is %s, but the charge requires %s",
                    this.balance, amount));
        }

        Transaction transaction = new Transaction(
                amount,
                TransactionType.CHARGE,
                location,
                LocalDateTime.now()
        );

        this.balance = this.balance.subtract(transaction.getAmount());
        this.transactionsHistory.add(transaction);

        return transaction;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionsHistory() {
        return Collections.unmodifiableList(transactionsHistory);
    }

    public List<Transaction> getTransactionsAtLocation(Location location) {
        return this.transactionsHistory.stream().filter(h -> h.getLocation().equals(location)).toList();
    }
}
