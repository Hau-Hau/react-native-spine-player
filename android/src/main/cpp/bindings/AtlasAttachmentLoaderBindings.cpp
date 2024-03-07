#ifndef ATLAS_ATTACHMENT_LOADE_RBINDINGS_CPP
#define ATLAS_ATTACHMENT_LOADE_RBINDINGS_CPP

#include <jni.h>
#include "spine/Atlas.h"
#include "spine/AtlasAttachmentLoader.h"

extern "C" JNIEXPORT jlong JNICALL Java_com_spineplayer_spine_AtlasAttachmentLoader_create(JNIEnv *env, jobject jObj, jlong atlasPointer) {
    auto atlas = (spine::Atlas *)atlasPointer;
    auto atlasAttachmentLoader = new spine::AtlasAttachmentLoader(atlas);
    return (jlong)atlasAttachmentLoader;
}

extern "C" JNIEXPORT void JNICALL Java_com_spineplayer_spine_AtlasAttachmentLoader_destroy(JNIEnv *env, jobject jObj, jlong pointer) {
    auto atlasAttachmentLoader = (spine::AtlasAttachmentLoader *)pointer;
    delete atlasAttachmentLoader;
}

#endif /* ATLAS_ATTACHMENT_LOADE_RBINDINGS_CPP */
