package com.spineplayer;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.List;

@SuppressWarnings("unused")
public class SpinePlayerPackage implements ReactPackage {
    static {
        System.loadLibrary("react-native-spine-player");
    }

    /** @noinspection rawtypes */
    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        List<ViewManager> viewManagers = new java.util.ArrayList<>();
        viewManagers.add(new SpinePlayerManager());
        return viewManagers;
    }

    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> nativeModules = new java.util.ArrayList<>();
        return nativeModules;
    }
}