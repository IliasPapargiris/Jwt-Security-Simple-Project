package com.logicea.demo.util;

import lombok.Getter;

@Getter
public enum CardStatus {
    TODO("To do"),
    IN_PROGRESS("In progress"),

    DONE("Done");

    private String status;
    CardStatus(String status) {}
}
