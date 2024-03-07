#include <jni.h>
#include "spine/Atlas.h"
#include "spine/SkeletonJson.h"
#include "spine/AtlasAttachmentLoader.h"

#ifndef SPINE_ANDROID_SKELETON_JSON_BINDINGS_CPP
#define SPINE_ANDROID_SKELETON_JSON_BINDINGS_CPP

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_SkeletonJson_create(JNIEnv *env, jobject jObj, jlong atlasAttachmentLoaderPointer) {
    auto atlasAttachmentLoader = (spine::AtlasAttachmentLoader *)atlasAttachmentLoaderPointer;
    auto skeletonJson = new spine::SkeletonJson(atlasAttachmentLoader);
    return (jlong)skeletonJson;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_SkeletonJson_destroy(JNIEnv *env, jobject jObj, jlong pointer) {
    auto skeletonJson = (spine::SkeletonJson *)pointer;
    delete skeletonJson;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_SkeletonJson_readSkeletonData(JNIEnv *env, jobject jObj, jlong pointer, jstring json) {
    auto jsonNative = env->GetStringUTFChars(json, 0);
    auto skeletonJson = (spine::SkeletonJson *)pointer;
    return (jlong)skeletonJson->readSkeletonData(jsonNative);
}

#endif /* SPINE_ANDROID_SKELETON_JSON_BINDINGS_CPP */
