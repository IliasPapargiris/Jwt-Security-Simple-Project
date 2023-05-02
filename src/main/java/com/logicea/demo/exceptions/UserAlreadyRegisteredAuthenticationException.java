package com.logicea.demo.exceptions;

import javax.naming.AuthenticationException;

public class UserAlreadyRegisteredAuthenticationException  extends AuthenticationException {

    private static final String MESSAGE = "User is already registered";
    public UserAlreadyRegisteredAuthenticationException() {
        super(MESSAGE);
    }
}
