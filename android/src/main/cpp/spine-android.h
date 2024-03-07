#ifndef SPINE_ANDROID_H_
#define SPINE_ANDROID_H_

#include "jni.h"
#include "android/log.h"
#include "android/bitmap.h"
#include "cstring"
#include "list"
#include "typeinfo"
#include "string"
#include "SkImage.h"
#include "SkCanvas.h"
#include "SkTileMode.h"
#include "SkPath.h"
#include "SkMatrix.h"
#include "SkShader.h"
#include "SkVertices.h"
#include "SkRect.h"
#include "SkSurface.h"
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
#include "spine/Atlas.h"
#include "spine/AtlasAttachmentLoader.h"
#include "spine/SkeletonJson.h"
#include "spine/SkeletonClipping.h"
#include "spine/ClippingAttachment.h"
#include "spine/Extension.h"
#include "spine/TextureLoader.h"

#ifndef SPINE_MESH_VERTEX_COUNT_MAX
#define SPINE_MESH_VERTEX_COUNT_MAX 1000
#endif

static JavaVM *javaVM = nullptr;

namespace spine {
    class Texture {
    public:
        const String path;
        sk_sp<SkImage> image;
    };

    class Vertex {
    public:
        SkVector position;
        SkVector texCoords;
        SkColor color;
    };

    class SkeletonRenderer {
    private:
        Vector<float> worldVertices;
        Vector<unsigned short> quadIndices;
        SkeletonClipping clipper = SkeletonClipping();
        Vector<Vertex> vertexArray = Vector<Vertex>();
        SkPaint texturePaint;

        void drawFromVertexArray(SkCanvas *canvas, sk_sp<SkImage> image);

    public:
        SkeletonRenderer();

        virtual void draw(JNIEnv *env, jobject bitmap, Skeleton *skeleton);
    };

    class AndroidTextureLoader : public TextureLoader {
    public:
        virtual void load(AtlasPage &page, const String &path);

        virtual void unload(void *texture);

        String toString() const;
    };

    class AndroidAnimationStateListenerObject : public AnimationStateListenerObject {
    private:
        jobject view;
    public:
        AndroidAnimationStateListenerObject(jobject view);

        virtual ~AndroidAnimationStateListenerObject();

        virtual void callback(AnimationState *state, EventType type, TrackEntry *entry, Event *event);
    };

} /* namespace spine */

#endif /* SPINE_ANDROID_H_ */
