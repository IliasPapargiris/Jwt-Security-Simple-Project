package com.logicea.demo.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Getter
public enum CardStatus {



    TODO("To do"),
    IN_PROGRESS("In progress"),

    DONE("Done");

    private final String status;

    CardStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    private static final List<String> ALL_STATUSES_LOWER_CASE = new ArrayList<>();

    static {
        for (CardStatus status : CardStatus.values()) {
            ALL_STATUSES_LOWER_CASE.add(status.getStatus().toLowerCase());
        }
    }

    public static List<String> getAllStatusesLowerCase() {
        return ALL_STATUSES_LOWER_CASE;
    }


    public static CardStatus fromString(String status) {
        Optional<CardStatus> optionalCardStatus =
                EnumSet.allOf(CardStatus.class)
                        .stream()
                        .filter(s -> s.getStatus().equalsIgnoreCase(status))
                        .findFirst();
        if (optionalCardStatus.isPresent()) {
            return optionalCardStatus.get();
        } else {
            throw new IllegalArgumentException("Invalid card status: " + status);
        }
    }

}
