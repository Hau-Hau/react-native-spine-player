#ifndef SPINE_ANDROID_SKELETON_DATAB_INDINGS_CPP
#define SPINE_ANDROID_SKELETON_DATAB_INDINGS_CPP

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
#include "spine/Animation.h"
#include "spine/AnimationState.h"
#include "spine/AnimationStateData.h"
#include "spine/SkeletonData.h"
#include "spine/SpineString.h"

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_SkeletonData_create(JNIEnv *env, jobject jObj) {
  auto skeletonData = new spine::SkeletonData();
  return (jlong)skeletonData;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_SkeletonData_destroy(JNIEnv *env, jobject jObj, jlong pointer) {
  auto skeletonData = (spine::SkeletonData *)pointer;
  delete skeletonData;
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_SkeletonData_getFps(JNIEnv *env, jobject jObj, jlong pointer) {
  auto skeletonData = (spine::SkeletonData *)pointer;
  return skeletonData->getFps();
}

extern "C" JNIEXPORT float JNICALL Java_com_spineplayer_spine_SkeletonData_getWidth(JNIEnv *env, jobject jObj, jlong pointer) {
  auto skeletonData = (spine::SkeletonData *)pointer;
  return skeletonData->getWidth();
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_SkeletonData_setWidth(JNIEnv *env, jobject jObj, jlong pointer, jfloat value) {
  auto skeletonData = (spine::SkeletonData *)pointer;
  skeletonData->setWidth(value);
}

extern "C" JNIEXPORT float JNICALL Java_com_spineplayer_spine_SkeletonData_getHeight(JNIEnv *env, jobject jObj, jlong pointer) {
    auto skeletonData = (spine::SkeletonData *)pointer;
    return skeletonData->getHeight();
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_SkeletonData_setHeight(JNIEnv *env, jobject jObj, jlong pointer, jfloat value) {
    auto skeletonData = (spine::SkeletonData *)pointer;
    skeletonData->setHeight(value);
}

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_SkeletonData_findAnimation(JNIEnv *env, jobject jObj, jlong pointer, jstring animationName) {
  auto skeletonData = (spine::SkeletonData *)pointer;
  auto nativeAnimationName = env->GetStringUTFChars(animationName, nullptr);
  auto animation = skeletonData->findAnimation(nativeAnimationName);
  return (jlong)animation;
}

#endif /* SPINE_ANDROID_SKELETON_DATAB_INDINGS_CPP */
