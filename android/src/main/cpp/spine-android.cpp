#include "spine-android.h"

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVM = vm;
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    delete javaVM;
}

namespace spine {
    SkeletonRenderer::SkeletonRenderer() {
        texturePaint.setAntiAlias(false);
        Bone::setYDown(true);
        worldVertices.ensureCapacity(SPINE_MESH_VERTEX_COUNT_MAX);
        quadIndices.add(0);
        quadIndices.add(1);
        quadIndices.add(2);
        quadIndices.add(2);
        quadIndices.add(3);
        quadIndices.add(0);
    }

    void SkeletonRenderer::drawFromVertexArray(SkCanvas *canvas, sk_sp<SkImage> image) {
        size_t vertexCount = vertexArray.size();
        SkVertices::Builder builder(SkVertices::kTriangles_VertexMode,
                                    vertexCount,
                                    0,
                                    SkVertices::kHasTexCoords_BuilderFlag
                                    | SkVertices::kHasColors_BuilderFlag);

        SkPoint* positions = builder.positions();
        SkColor* colors = builder.colors();
        SkPoint* texCoords = builder.texCoords();
        for (size_t i = 0; i < vertexCount; i++) {
            Vertex vertex = vertexArray[i];
            positions[i].set(vertex.position.x(), vertex.position.y());
            colors[i] = vertex.color;
            texCoords[i].set(vertex.texCoords.x(), vertex.texCoords.y());
        }

        if (texturePaint.getShader() == nullptr) {
            SkSamplingOptions sampling = SkSamplingOptions(SkFilterMode::kLinear, SkMipmapMode::kLinear);
            texturePaint.setShader(
                    image->makeShader(SkTileMode::kRepeat,
                                      SkTileMode::kRepeat,
                                      sampling,
                                      nullptr));
        }

        canvas->drawVertices(builder.detach(), SkBlendMode::kModulate, texturePaint);
    }

    void SkeletonRenderer::draw(JNIEnv *env, jobject bitmap, spine::Skeleton *skeleton) {
        vertexArray.clear();

        // Early out if skeleton is invisible
        if (skeleton->getColor().a == 0) return;

        AndroidBitmapInfo info;
        void *pixels;
        AndroidBitmap_getInfo(env, bitmap, &info);
        AndroidBitmap_lockPixels(env, bitmap, &pixels);
        SkImageInfo imageInfo = info.flags & ANDROID_BITMAP_FLAGS_ALPHA_PREMUL
                                ? SkImageInfo::MakeN32Premul(info.width, info.height)
                                : SkImageInfo::MakeN32(info.width, info.height,
                                                       SkAlphaType::kUnpremul_SkAlphaType);
        auto pixmap = SkPixmap(imageInfo, pixels, info.stride);
        pixmap.erase(SK_ColorTRANSPARENT);
        auto surface = SkSurfaces::WrapPixels(pixmap);
        auto canvas = surface->getCanvas();

        Vertex vertex{};
        Texture *texture = nullptr;
        for (unsigned i = 0; i < skeleton->getSlots().size(); ++i) {
            spine::Slot &slot = *skeleton->getDrawOrder()[i];
            spine::Attachment *attachment = slot.getAttachment();
            if (!attachment) continue;

            // Early out if the slot color is 0 or the bone is not active
            if (slot.getColor().a == 0 || !slot.getBone().isActive()) {
                clipper.clipEnd(slot);
                continue;
            }

            spine::Vector<float> *vertices = &worldVertices;
            spine::Vector<float> *uvs;
            spine::Vector<unsigned short> *indices;
            int indicesCount;
            spine::Color *attachmentColor;

            if (attachment->getRTTI().isExactly(spine::RegionAttachment::rtti)) {
                spine::RegionAttachment *regionAttachment = (spine::RegionAttachment *) attachment;
                attachmentColor = &regionAttachment->getColor();

                // Early out if the slot color is 0
                if (attachmentColor->a == 0) {
                    clipper.clipEnd(slot);
                    continue;
                }

                worldVertices.setSize(8, 0);
                regionAttachment->computeWorldVertices(slot, worldVertices, 0, 2);
                uvs = &regionAttachment->getUVs();
                indices = &quadIndices;
                indicesCount = 6;
                texture = ((Texture *) ((spine::AtlasRegion *) regionAttachment->getRegion())->page->texture);

            } else if (attachment->getRTTI().isExactly(spine::MeshAttachment::rtti)) {
                spine::MeshAttachment *mesh = (spine::MeshAttachment *) attachment;
                attachmentColor = &mesh->getColor();

                // Early out if the slot color is 0
                if (attachmentColor->a == 0) {
                    clipper.clipEnd(slot);
                    continue;
                }

                worldVertices.setSize(mesh->getWorldVerticesLength(), 0);
                mesh->computeWorldVertices(slot, 0, mesh->getWorldVerticesLength(),
                                           worldVertices.buffer(), 0, 2);
                uvs = &mesh->getUVs();
                indices = &mesh->getTriangles();
                indicesCount = mesh->getTriangles().size();
                texture = (Texture *) ((spine::AtlasRegion *) mesh->getRegion())->page->texture;

            } else if (attachment->getRTTI().isExactly(spine::ClippingAttachment::rtti)) {
                spine::ClippingAttachment *clip = (spine::ClippingAttachment *) slot.getAttachment();
                clipper.clipStart(slot, clip);
                continue;
            } else
                continue;

            uint8_t r = static_cast<uint8_t>(skeleton->getColor().r * slot.getColor().r *
                                             attachmentColor->r * 255);
            uint8_t g = static_cast<uint8_t>(skeleton->getColor().g * slot.getColor().g *
                                             attachmentColor->g * 255);
            uint8_t b = static_cast<uint8_t>(skeleton->getColor().b * slot.getColor().b *
                                             attachmentColor->b * 255);
            uint8_t a = static_cast<uint8_t>(skeleton->getColor().a * slot.getColor().a *
                                             attachmentColor->a * 255);
            vertex.color = SkColorSetARGB(a, r, g, b);

            spine::Color light;
            light.r = r / 255.0f;
            light.g = g / 255.0f;
            light.b = b / 255.0f;
            light.a = a / 255.0f;

            switch (slot.getData().getBlendMode()) {
                case spine::BlendMode_Normal:
                    texturePaint.setBlendMode(SkBlendMode::kSrcOver);
                    break;
                case spine::BlendMode_Additive:
                    texturePaint.setBlendMode(SkBlendMode::kPlus);
                    break;
                case spine::BlendMode_Multiply:
                    texturePaint.setBlendMode(SkBlendMode::kMultiply);
                    break;
                case spine::BlendMode_Screen:
                    texturePaint.setBlendMode(SkBlendMode::kScreen);
                    break;
                default:
                    texturePaint.setBlendMode(SkBlendMode::kSrcOver);
            }

            if (clipper.isClipping()) {
                clipper.clipTriangles(worldVertices, *indices, *uvs, 2);
                vertices = &clipper.getClippedVertices();
                uvs = &clipper.getClippedUVs();
                indices = &clipper.getClippedTriangles();
                indicesCount = indices->size();
            }

            for (int ii = 0; ii < indicesCount; ++ii) {
                int index = (*indices)[ii] << 1;
                vertex.position = SkVector::Make((*vertices)[index], (*vertices)[index + 1]);
                vertex.texCoords = SkVector::Make((*uvs)[index] * texture->image->width(),
                                                  (*uvs)[index + 1] * texture->image->height());
                vertexArray.add(vertex);
            }
            clipper.clipEnd(slot);
        }

        drawFromVertexArray(canvas, texture->image);
        clipper.clipEnd();
        AndroidBitmap_unlockPixels(env, bitmap);
    }

    void AndroidTextureLoader::load(AtlasPage &page, const String &path) {
        JNIEnv *env;
        if (javaVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
            return;
        }

        auto jClass = env->FindClass("com/spineplayer/spine/bindings/TextureCacheBindings");
        auto jMethodId = env->GetStaticMethodID(jClass, "get",
                                                "(Ljava/lang/String;)Landroid/graphics/Bitmap;");
        auto bitmap = env->CallStaticObjectMethod(jClass,
                jMethodId,
                env->NewStringUTF(path.buffer()));

        AndroidBitmapInfo info;
        void *pixels;
        AndroidBitmap_getInfo(env, bitmap, &info);
        AndroidBitmap_lockPixels(env, bitmap, &pixels);
        SkImageInfo imageInfo = info.flags & ANDROID_BITMAP_FLAGS_ALPHA_PREMUL
                                ? SkImageInfo::MakeN32Premul(info.width, info.height)
                                : SkImageInfo::MakeN32(info.width, info.height,
                                                       SkAlphaType::kUnpremul_SkAlphaType);
        auto pixmap = SkPixmap(imageInfo, pixels, info.stride);
        sk_sp<SkImage> image = SkImages::RasterFromPixmapCopy(pixmap);
        AndroidBitmap_unlockPixels(env, bitmap);

        auto texture = new Texture { .path =  String(path.buffer()), .image =  image };
        page.texture = texture;
        page.width = texture->image->width();
        page.height = texture->image->height();
    }

    void AndroidTextureLoader::unload(void *texture) {
        JNIEnv *env;
        if (javaVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
            return;
        }

        auto tex = ((Texture *) texture);
        delete tex->image.release();
        delete tex;
    }

    SpineExtension *getDefaultExtension() {
        return new DefaultSpineExtension();
    }

    AndroidAnimationStateListenerObject::AndroidAnimationStateListenerObject(jobject view) {
        this->view = view;
    }

    AndroidAnimationStateListenerObject::~AndroidAnimationStateListenerObject() {
        if (this->view == nullptr) {
            return;
        }

        JNIEnv *env;
        if (javaVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
            return;
        }

        env->DeleteGlobalRef(this->view);
    }

    void AndroidAnimationStateListenerObject::callback(AnimationState *state, EventType type, TrackEntry *entry, Event *event) {
        JNIEnv *env;
        if (javaVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
            return;
        }

        jclass jClass = env->FindClass("com/spineplayer/spine/bindings/AnimationStateBindings");
        jmethodID jMethodId = env->GetStaticMethodID(jClass, "handleEvent",
                                                     "(Landroid/view/View;JIJJ)V");

        env->CallStaticVoidMethod(jClass, jMethodId, this->view, (jlong) state, (jint)type, (jlong)entry, (jlong)event);
    }

} /* namespace spine */
