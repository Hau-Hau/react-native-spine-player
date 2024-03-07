#ifndef SPINE_ANDROID_EVENT_DATA_BINDINGS_CPP
#define SPINE_ANDROID_EVENT_DATA_BINDINGS_CPP

#include <jni.h>
#include "spine/EventData.h"

extern "C" JNIEXPORT jstring JNICALL Java_com_spineplayer_spine_EventData_getName(JNIEnv *env, jobject jObj, jlong pointer) {
    auto eventData = (spine::EventData *)pointer;
    return (jstring)env->NewStringUTF(eventData->getName().buffer());
}

extern "C" JNIEXPORT jint JNICALL Java_com_spineplayer_spine_EventData_getIntValue(JNIEnv *env, jobject jObj, jlong pointer) {
    auto eventData = (spine::EventData *)pointer;
    return (jint)eventData->getIntValue();
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_EventData_getFloatValue(JNIEnv *env, jobject jObj, jlong pointer) {
    auto eventData = (spine::EventData *)pointer;
    return (jfloat)eventData->getFloatValue();
}

extern "C" JNIEXPORT jstring JNICALL Java_com_spineplayer_spine_EventData_getStringValue(JNIEnv *env, jobject jObj, jlong pointer) {
    auto eventData = (spine::EventData *)pointer;
    return (jstring)env->NewStringUTF(eventData->getStringValue().buffer());
}

extern "C" JNIEXPORT jstring JNICALL Java_com_spineplayer_spine_EventData_getAudioPath(JNIEnv *env, jobject jObj, jlong pointer) {
    auto eventData = (spine::EventData *)pointer;
    return (jstring)env->NewStringUTF(eventData->getAudioPath().buffer());
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_EventData_getVolume(JNIEnv *env, jobject jObj, jlong pointer) {
    auto eventData = (spine::EventData *)pointer;
    return (jfloat)eventData->getVolume();
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_EventData_getBalance(JNIEnv *env, jobject jObj, jlong pointer) {
    auto eventData = (spine::EventData *)pointer;
    return (jfloat)eventData->getBalance();
}

#endif /* SPINE_ANDROID_EVENT_DATA_BINDINGS_CPP */
