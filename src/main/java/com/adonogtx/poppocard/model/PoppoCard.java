package com.adonogtx.poppocard.model;

import java.math.BigDecimal;
import java.util.List;

public class PoppoCard {

    BigDecimal balance;
    List<Transaction> transactionsHistory;

    public PoppoCard(List<Transaction> transactions) {
        this.transactionsHistory = transactions;
        this.balance = BigDecimal.valueOf(0.00);
    }

    public boolean rechargeCard(Transaction transaction) {

        if (transaction.amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if(transaction.location.getType().toString().equals(Chargeability.CHARGEABLE.toString())
        || transaction.location.getType().toString().equals(Chargeability.CHARGEABLE_AND_RECHARGEABLE.toString())){
            transactionsHistory.add(transaction);
        }


        return false;
    }
}
