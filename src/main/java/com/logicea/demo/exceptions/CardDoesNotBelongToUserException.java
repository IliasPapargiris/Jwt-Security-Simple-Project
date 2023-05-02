package com.logicea.demo.exceptions;

public class CardDoesNotBelongToUserException  extends RuntimeException {
    public CardDoesNotBelongToUserException(String message) {
        super(message);
    }
}
