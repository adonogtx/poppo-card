package com.adonogtx.poppocard.model;

import com.adonogtx.poppocard.exception.InsufficientBalanceException;
import com.adonogtx.poppocard.exception.InvalidValueException;
import com.adonogtx.poppocard.exception.OperationNotAllowedException;
import com.adonogtx.poppocard.exception.PoppoCardTransactionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PoppoCard {

    BigDecimal balance;
    List<Transaction> transactionsHistory = new ArrayList<>();

    public PoppoCard() {
        this.balance = BigDecimal.valueOf(0.00);
    }

    public void rechargeCard(Transaction transaction) throws PoppoCardTransactionException {

        if (transaction.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(String.format("The value must be greater than zero: %s", transaction.getAmount()));
        }

        if (transaction.getLocation().getType().isAcceptsRecharge()) {
            this.balance = this.balance.add(transaction.getAmount());
            this.transactionsHistory.add(transaction);
        } else {
            throw new OperationNotAllowedException(String.format("%s not possible on %s",
                    transaction.getType(),transaction.getLocation().getName()));
        }


    }

    public void chargeCard(Transaction transaction) throws PoppoCardTransactionException {

        if (transaction.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(String.format("The value must be greater than zero: %s", transaction.getAmount()));
        }

        if (transaction.getAmount().compareTo(this.balance) > 0) {
            throw new InsufficientBalanceException(String.format("Available balance is %s, but the charge requires %s", this.balance, transaction.getAmount()));
        }

        if (transaction.getLocation().getType().isAcceptsCharge()) {
            this.balance = this.balance.subtract(transaction.getAmount());
            this.transactionsHistory.add(transaction);
        } else {
            throw new OperationNotAllowedException(String.format("%s not possible on %s",
                    transaction.getType(),transaction.getLocation().getName()));
        }

    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionsHistory() {
        return  Collections.unmodifiableList(transactionsHistory);
    }

    public List<Transaction> getTransactionsAtLocation(Location location){
        return this.transactionsHistory.stream().filter(h->h.getLocation().equals(location)).toList();
    }
}
