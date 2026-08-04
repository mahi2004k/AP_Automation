package com.ap_automation.exception;

public class PurchaseOrderAlreadyExistsException extends RuntimeException {

    public PurchaseOrderAlreadyExistsException(String message) {
        super(message);
    }

}