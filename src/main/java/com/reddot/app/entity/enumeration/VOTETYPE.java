package com.reddot.app.entity.enumeration;

import lombok.Getter;

@Getter
public enum VOTETYPE {
    UPVOTE(1),
    DOWNVOTE(2);

    private final int direction;

    VOTETYPE(int direction) {
        this.direction = direction;
    }

    public static VOTETYPE fromDirection(int direction) {
        for (VOTETYPE type : VOTETYPE.values()) {
            if (type.getDirection() == direction) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid direction: " + direction);
    }

}
