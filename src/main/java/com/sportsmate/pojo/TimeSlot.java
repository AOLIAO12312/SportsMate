package com.sportsmate.pojo;

public enum TimeSlot {
    _8_10("8:00-10:00"),
    _10_12("10:00-12:00"),
    _14_16("14:00-16:00"),
    _16_18("16:00-18:00"),
    _19_21("19:00-21:00");

    private final String label;

    TimeSlot(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
