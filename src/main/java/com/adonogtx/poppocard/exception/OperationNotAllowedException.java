package com.adonogtx.poppocard.exception;

public class OperationNotAllowedException extends PoppoCardTransactionException {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
