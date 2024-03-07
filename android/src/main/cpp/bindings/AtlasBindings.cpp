#ifndef SPINE_ANDROID_ATLAS_BINDINGS_CPP
#define SPINE_ANDROID_ATLAS_BINDINGS_CPP

#include <jni.h>
#include "spine/Atlas.h"
#include "../spine-android.h"

spine::AndroidTextureLoader textureLoader;

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_Atlas_create(JNIEnv *env, jobject jObj, jstring atlasData) {
    const char *atlasDataNative = env->GetStringUTFChars(atlasData, nullptr);
    auto length = (int32_t)strlen(atlasDataNative);
    auto atlas = new spine::Atlas(atlasDataNative, length, "", &textureLoader, true);
    return (jlong)atlas;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_Atlas_destroy(JNIEnv *env, jobject jObj, jlong pointer) {
    auto atlas = (spine::Atlas *)pointer;
    delete atlas;
}

#endif /* SPINE_ANDROID_ATLAS_BINDINGS_CPP */
