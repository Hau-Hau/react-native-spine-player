#ifndef SPINE_ANDROID_SKELETON_BINDINGS_CPP
#define SPINE_ANDROID_SKELETON_BINDINGS_CPP

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
#include "spine/BoundingBoxAttachment.h"
#include "spine/Physics.h"

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_Skeleton_create(JNIEnv *env, jobject jObj, jlong skeletonDataPointer) {
  auto skeletonData = (spine::SkeletonData *)skeletonDataPointer;
  auto skeleton = new spine::Skeleton(skeletonData);
  return (jlong)skeleton;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_Skeleton_destroy(JNIEnv *env, jobject jObj, jlong pointer) {
  auto skeleton = (spine::Skeleton *)pointer;
  delete skeleton;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_Skeleton_setX(JNIEnv *env, jobject jObj, jlong pointer, jfloat x) {
  auto skeleton = (spine::Skeleton *)pointer;
  skeleton->setX((float)x);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_Skeleton_setY(JNIEnv *env, jobject jObj, jlong pointer, jfloat y) {
  auto skeleton = (spine::Skeleton *)pointer;
  skeleton->setY((float)y);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_Skeleton_setScaleX(JNIEnv *env, jobject jObj, jlong pointer, jfloat x) {
  auto skeleton = (spine::Skeleton *)pointer;
  skeleton->setScaleX((float)x);
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_Skeleton_setScaleY(JNIEnv *env, jobject jObj, jlong pointer, jfloat y) {
  auto skeleton = (spine::Skeleton *)pointer;
  skeleton->setScaleY((float)y);
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_com_spineplayer_spine_Skeleton_calculateAllAnimationsBounds(JNIEnv *env, jobject jObj, jlong pointer, jlong animationStatePointer, jint physics) {
    auto skeleton = (spine::Skeleton *) pointer;
    auto animationState = (spine::AnimationState *) animationStatePointer;
    auto currentScaleX = skeleton->getScaleX();
    auto currentScaleY = skeleton->getScaleY();
    auto tracks = animationState->getTracks();
    spine::Vector<spine::String> currentAnimationNames;
    spine::Vector<bool> currentAnimationLoopStatus;
    for (auto i = 0; i < tracks.getCapacity(); i++) {
        auto track = animationState->getCurrent(i);
        auto animation = track->getAnimation();
        currentAnimationNames.add(animation->getName());
        currentAnimationLoopStatus.add(track->getLoop());
    }

    skeleton->setScaleX(1);
    skeleton->setScaleY(1);
    float x =  0;
    float width = 0;
    float y = 0;
    float height = 0;
    auto animations = skeleton->getData()->getAnimations();
    for (auto i = 0; i < animations.getCapacity(); i++) {
        auto animation = animations[i];
        auto skeletonData = skeleton->getData();
        animationState->clearTracks();
        skeleton->setToSetupPose();
        animationState->setAnimation(0, animation, true);
        int steps = round(skeletonData->getFps() * animation->getDuration());
        for (auto j = 0; j < steps; j++) {
            animationState->update(skeletonData->getFps());
            animationState->apply(*skeleton);
            skeleton->updateWorldTransform(static_cast<spine::Physics>(physics));
            float outX;
            float outY;
            float outWidth;
            float outHeight;
            spine::Vector<float> outVertexBuffer;
            skeleton->getBounds(outX, outY, outWidth, outHeight, outVertexBuffer);
            x = fmin(outX, x);
            width = fmax(outWidth, width);
            y = fmin(outY, y);
            height = fmax(outHeight, height);
        }
    }

    skeleton->setScaleX(currentScaleX);
    skeleton->setScaleY(currentScaleY);
    animationState->clearTracks();
    skeleton->setToSetupPose();
    for (auto i = 0; i < currentAnimationNames.getCapacity(); i++) {
        animationState->setAnimation(i, currentAnimationNames[i], currentAnimationLoopStatus[i]);
    }

    auto result = env->NewFloatArray(2);
    if (result == NULL) {
        return NULL;
    }

    jfloat resultContent[2];
    resultContent[0] = abs(x)+abs(width);
    resultContent[1] = abs(y)+abs(height);
    env->SetFloatArrayRegion(result, 0, 2, resultContent);
    return result;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_Skeleton_setToSetupPose(JNIEnv *env, jobject jObj, jlong pointer) {
  auto skeleton = (spine::Skeleton *)pointer;
  skeleton->setToSetupPose();
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_Skeleton_updateWorldTransform(JNIEnv *env, jobject jObj, jlong pointer, jint physics) {
  auto skeleton = (spine::Skeleton *)pointer;
  skeleton->updateWorldTransform(static_cast<spine::Physics>(physics));
}

#endif /* SPINE_ANDROID_SKELETON_BINDINGS_CPP */
