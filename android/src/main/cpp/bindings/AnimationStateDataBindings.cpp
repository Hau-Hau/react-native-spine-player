#ifndef SPINE_ANDROID_ANIMATION_STATE_DATA_BINDINGS_CPP
#define SPINE_ANDROID_ANIMATION_STATE_DATA_BINDINGS_CPP

#include <jni.h>
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

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_AnimationStateData_create(JNIEnv *env, jobject jObj, jlong skeletonDataPointer) {
  auto skeletonData = (spine::SkeletonData *)skeletonDataPointer;
  auto animationStateData = new spine::AnimationStateData(skeletonData);
  return (jlong)animationStateData;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationStateData_destroy(JNIEnv *env, jobject jObj, jlong pointer) {
  auto animationStateData = (spine::AnimationStateData *)pointer;
  delete animationStateData;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationStateData_clear(JNIEnv *env, jobject jObj, jlong pointer) {
  auto animationStateData = (spine::AnimationStateData *)pointer;
  animationStateData->clear();
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationStateData_setDefaultMix(JNIEnv *env, jobject jObj, jlong pointer, jfloat inValue) {
  auto animationStateData = (spine::AnimationStateData *)pointer;
  animationStateData->setDefaultMix((float)inValue);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AnimationStateData_setMix(JNIEnv *env, jobject jObj, jlong pointer, jstring fromAnimationName, jstring toAnimationName, jfloat inValue) {
  auto animationStateData = (spine::AnimationStateData *)pointer;
    auto from = env->GetStringUTFChars(fromAnimationName, nullptr);
    auto to = env->GetStringUTFChars(toAnimationName, nullptr);
    animationStateData->setMix(from, to, inValue);
}

#endif /* SPINE_ANDROID_ANIMATION_STATE_DATA_BINDINGS_CPP */
