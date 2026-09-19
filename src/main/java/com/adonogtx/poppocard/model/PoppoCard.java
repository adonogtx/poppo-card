package com.adonogtx.poppocard.model;

import com.adonogtx.poppocard.exception.InsufficientBalanceException;
import com.adonogtx.poppocard.exception.InvalidValueException;
import com.adonogtx.poppocard.exception.OperationNotAllowedException;
import com.adonogtx.poppocard.exception.PoppoCardTransactionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PoppoCard {

    BigDecimal balance;
    List<Transaction> transactionsHistory = new ArrayList<>();

    public PoppoCard() {
        this.balance = BigDecimal.valueOf(0.00);
    }

    public void rechargeCard(Transaction transaction) throws PoppoCardTransactionException {

        if (transaction.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(" ");
        }

        if (transaction.getLocation().getType().isAcceptsRecharge()) {
            this.balance = this.balance.add(transaction.getAmount());
            this.transactionsHistory.add(transaction);
        } else {
            throw new OperationNotAllowedException(" ");
        }


    }

    public void chargeCard(Transaction transaction) throws PoppoCardTransactionException {

        if (transaction.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(" ");
        }

        if (transaction.getAmount().compareTo(this.balance) > 0) {
            throw new InsufficientBalanceException(" ");
        }

        if (transaction.getLocation().getType().isAcceptsCharge()) {
            this.balance = this.balance.subtract(transaction.getAmount());
            this.transactionsHistory.add(transaction);
        } else {
            throw new OperationNotAllowedException(" ");
        }

    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionsHistory() {
        return transactionsHistory;
    }
}
