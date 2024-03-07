package com.spineplayer.spine.bindings;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.spineplayer.SpineEvent;
import com.spineplayer.spine.Event;
import com.spineplayer.spine.EventData;
import com.spineplayer.spine.EventType;

public final class AnimationStateBindings {
    public static void handleEvent(View view, long _statePointer, int type, long _entryPointer, long eventPointer) {
        final ReactContext reactContext = (ReactContext) view.getContext();
        final int viewTag = view.getId();
        final int surfaceId = UIManagerHelper.getSurfaceId(reactContext);

        EventType eventType = EventType.getValue(type);
        Event event = eventPointer == 0 ? null : new Event(eventPointer);
        EventDispatcher mEventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewTag);
        if (mEventDispatcher != null) {
            mEventDispatcher.dispatchEvent(new SpineEvent(surfaceId, viewTag, convertEventToMap(eventType, event)));
        }

        if (event != null) {
            event.close();
        }
    }

    private static WritableMap convertEventDataToMap(@NonNull EventData eventData) {
        WritableMap output = Arguments.createMap();
        output.putString("name", eventData.getName());
        output.putInt("intValue", eventData.getIntValue());
        output.putDouble("floatValue", eventData.getFloatValue());
        output.putString("stringValue", eventData.getStringValue());
        output.putString("audioPath", eventData.getAudioPath());
        output.putDouble("volume", eventData.getVolume());
        output.putDouble("balance", eventData.getBalance());
        return output;
    }

    private static WritableMap convertEventToMap(@NonNull EventType eventType, @Nullable Event event) {
        WritableMap output = Arguments.createMap();
        output.putInt("type", eventType.ordinal());

        if (event != null) {
            try (EventData eventData = event.getData()) {
                output.putMap("data", convertEventDataToMap(eventData));
            }

            output.putDouble("time", event.getTime());
            output.putInt("intValue", event.getIntValue());
            output.putDouble("floatValue", event.getFloatValue());
            output.putString("stringValue", event.getStringValue());
            output.putDouble("volume", event.getVolume());
            output.putDouble("balance", event.getBalance());
        }

        return output;
    }
}
