#ifndef SPINE_ANDROID_SKELETON_RENDERER_BINDINGS_CPP
#define SPINE_ANDROID_SKELETON_RENDERER_BINDINGS_CPP

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
#include "../spine-android.h"

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_SkeletonRenderer_create(JNIEnv *env, jobject jObj) {
  auto skeletonRenderer = new spine::SkeletonRenderer();
  return (jlong)skeletonRenderer;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_SkeletonRenderer_destroy(JNIEnv *env, jobject jObj, jlong pointer) {
  auto skeletonRenderer = (spine::SkeletonRenderer *)pointer;
  delete skeletonRenderer;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_SkeletonRenderer_draw(JNIEnv *env, jobject jObj, jlong skeletonRendererPointer, jobject bitmap, jlong skeletonPointer) {
  auto skeletonRenderer = (spine::SkeletonRenderer *)skeletonRendererPointer;
  auto skeleton = (spine::Skeleton *)skeletonPointer;
  auto bitmapWeakRef = env->NewWeakGlobalRef(bitmap);
  skeletonRenderer->draw(env, bitmapWeakRef, skeleton);
  env->DeleteWeakGlobalRef(bitmapWeakRef);
}

#endif /* SPINE_ANDROID_SKELETON_RENDERER_BINDINGS_CPP */