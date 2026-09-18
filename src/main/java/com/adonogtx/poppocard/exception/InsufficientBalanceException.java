package com.adonogtx.poppocard.exception;

public class InsufficientBalanceException extends PoppoCardTransactionException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
