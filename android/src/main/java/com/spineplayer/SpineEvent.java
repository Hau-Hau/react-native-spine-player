package com.spineplayer;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.spineplayer.spine.EventData;
import com.spineplayer.spine.EventType;

public class SpineEvent extends Event<SpineEvent> {
    public static final String EVENT_NAME = "SpineEvent";
    private final WritableMap event;

    public SpineEvent(int surfaceId, int viewTag, WritableMap event) {
        super(surfaceId, viewTag);

        this.event = event;
    }

    @NonNull
    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    protected WritableMap getEventData() {
        return event;
    }
}