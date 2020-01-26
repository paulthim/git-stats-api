package org.gitstats.api.models;

public enum UserRole {
    STUDENT(0), PROFESSOR(1), ADMIN(2);

    private int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
