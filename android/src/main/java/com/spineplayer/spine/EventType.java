package com.spineplayer.spine;

public enum EventType {
    EventType_Start,
    EventType_Interrupt,
    EventType_End,
    EventType_Complete,
    EventType_Dispose,
    EventType_Event;

    private static final EventType[] VALUES = values();

    public static EventType getValue(int i) {
        if (i < 0 || i >= VALUES.length) {
            return null;
        }

        return VALUES[i];
    }
}
