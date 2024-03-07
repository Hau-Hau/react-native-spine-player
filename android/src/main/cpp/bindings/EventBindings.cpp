#ifndef SPINE_ANDROID_EVENT_BINDINGS_CPP
#define SPINE_ANDROID_EVENT_BINDINGS_CPP

#include <jni.h>
#include "spine/EventData.h"
#include "spine/Event.h"

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_Event_getData(JNIEnv *env, jobject jObj, jlong pointer) {
    auto event = (spine::Event *)pointer;
    return (jlong) std::addressof(event->getData());
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_Event_getTime(JNIEnv *env, jobject jObj, jlong pointer) {
    auto event = (spine::Event *)pointer;
    return (jfloat)event->getTime();
}

extern "C" JNIEXPORT jint JNICALL Java_com_spineplayer_spine_Event_getIntValue(JNIEnv *env, jobject jObj, jlong pointer) {
    auto event = (spine::Event *)pointer;
    return (jint)event->getIntValue();
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_Event_getFloatValue(JNIEnv *env, jobject jObj, jlong pointer) {
    auto event = (spine::Event *)pointer;
    return (jfloat)event->getFloatValue();
}

extern "C" JNIEXPORT jstring JNICALL Java_com_spineplayer_spine_Event_getStringValue(JNIEnv *env, jobject jObj, jlong pointer) {
    auto event = (spine::Event *)pointer;
    return (jstring)env->NewStringUTF(event->getStringValue().buffer());
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_Event_getVolume(JNIEnv *env, jobject jObj, jlong pointer) {
    auto event = (spine::Event *)pointer;
    return (jfloat)event->getVolume();
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_Event_getBalance(JNIEnv *env, jobject jObj, jlong pointer) {
    auto event = (spine::Event *)pointer;
    return (jfloat)event->getBalance();
}

#endif /* SPINE_ANDROID_EVENT_BINDINGS_CPP */
