package com.reddot.app.entity.enumeration;

public enum GENDER {
    FEMALE(0),
    MALE(1),
    OTHER(3);

    private final int code;

    GENDER(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
