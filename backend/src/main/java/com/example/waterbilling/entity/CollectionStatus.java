package com.example.waterbilling.entity;

public enum CollectionStatus {
    PENDING,
    READING,
    COMPLETED;

    public static CollectionStatus fromFlutterIndex(Integer value) {
        if (value == null || value < 0 || value >= values().length) {
            return PENDING;
        }
        return values()[value];
    }

    public int toFlutterIndex() {
        return ordinal();
    }
}
