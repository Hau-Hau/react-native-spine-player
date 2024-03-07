#ifndef SPINE_ANDROID_ANIMATION_STATE_BINDINGS_CPP
#define SPINE_ANDROID_ANIMATION_STATE_BINDINGS_CPP

#include "map"
#include "jni.h"
#include "spine/Skeleton.h"
#include "spine/Attachment.h"
#include "spine/RegionAttachment.h"
#include "spine/Skeleton.h"
#include "spine/MeshAttachment.h"
#include "spine/Slot.h"
#include "spine/SlotData.h"
#include "spine/BlendMode.h"
#include "spine/AttachmentLoader.h"
#include "spine/Bone.h"
#include "spine/AnimationState.h"
#include "spine/AnimationStateData.h"
#include "spine/SkeletonData.h"
#include "spine/SpineString.h"
#include "spine/Event.h"
#include "../spine-android.h"

static std::map<long, spine::AndroidAnimationStateListenerObject*> listeners;

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_AnimationState_create(JNIEnv *env, jobject jObj, jlong animationStateDataPointer) {
  auto animationStateData = (spine::AnimationStateData *)animationStateDataPointer;
  auto animationState = new spine::AnimationState(animationStateData);
  return (jlong)animationState;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_destroy(JNIEnv *env, jobject jObj, jlong pointer) {
  auto animationState = (spine::AnimationState *)pointer;
  delete animationState;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_AnimationState_getData(JNIEnv *env, jobject jObj, jlong pointer) {
  auto animationState = (spine::AnimationState *)pointer;
  return (jlong)animationState->getData();
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_setAnimation(JNIEnv *env, jobject jObj, jlong pointer, jint trackIndex, jstring animationName, jboolean loop) {
  auto animationState = (spine::AnimationState *)pointer;
  auto nativeAnimationName = env->GetStringUTFChars(animationName, nullptr);
  animationState->setAnimation(trackIndex, spine::String(nativeAnimationName), (bool)loop);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_setEmptyAnimation(JNIEnv *env, jobject jObj, jlong pointer, jint trackIndex, jfloat mixDuration) {
  auto animationState = (spine::AnimationState *)pointer;
  animationState->setEmptyAnimation(trackIndex, mixDuration);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_setTimeScale(JNIEnv *env, jobject jObj, jlong pointer, float inValue) {
  auto animationState = (spine::AnimationState *)pointer;
  animationState->setTimeScale(inValue);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_update(JNIEnv *env, jobject jObj, jlong pointer, jfloat delta) {
  auto animationState = (spine::AnimationState *)pointer;
  animationState->update(delta);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_apply(JNIEnv *env, jobject jObj, jlong pointer, jlong skeletonPointer) {
  auto skeleton = (spine::Skeleton *)skeletonPointer;
  auto animationState = (spine::AnimationState *)pointer;
  animationState->apply(*skeleton);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_clearTracks(JNIEnv *env, jobject jObj, jlong pointer) {
  auto animationState = (spine::AnimationState *)pointer;
  animationState->clearTracks();
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_setListener(JNIEnv *env, jobject jObj, jlong pointer, jobject view) {
    auto animationState = (spine::AnimationState *)pointer;
    if (listeners.count(pointer)) {
        auto listenerObject = listeners[pointer];
        listeners.erase(pointer);
        delete listenerObject;
    }

    auto listenerObject = new spine::AndroidAnimationStateListenerObject(env->NewGlobalRef(view));
    listeners[pointer] = listenerObject;
    animationState->setListener(listenerObject);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationState_destroyListener(JNIEnv *env, jobject jObj, jlong pointer) {
    auto animationState = (spine::AnimationState *)pointer;
    animationState->setListener((spine::AnimationStateListenerObject *) NULL);

    if (!listeners.count(pointer)) {
       return;
    }

    auto listenerObject = listeners[pointer];
    listeners.erase(pointer);
    delete listenerObject;
}

#endif /* SPINE_ANDROID_ANIMATION_STATE_BINDINGS_CPP */
