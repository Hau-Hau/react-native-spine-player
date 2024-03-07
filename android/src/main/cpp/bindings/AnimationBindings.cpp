#ifndef SPINE_ANDROID_ANIMATION_BINDINGS_CPP
#define SPINE_ANDROID_ANIMATION_BINDINGS_CPP

#include <jni.h>
#include "spine/Animation.h"

extern "C" JNIEXPORT jfloat JNICALL Java_com_spineplayer_spine_Animation_getDuration(JNIEnv *env, jobject jObj, jlong pointer) {
  auto animation = (spine::Animation *)pointer;
  return (jfloat)animation->getDuration();
}

#endif /* SPINE_ANDROID_ANIMATION_BINDINGS_CPP */
