#include <jni.h>
#ifndef _Included_com_newland_opengl_render_BackgroundNativeRenderer
#define _Included_com_newland_opengl_render_BackgroundNativeRenderer
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL surfaceCreated(JNIEnv *, jobject, jint);
JNIEXPORT void JNICALL surfaceChanged(JNIEnv *, jobject, jint, jint);
JNIEXPORT void JNICALL drawFrame(JNIEnv *, jobject);
#ifdef __cplusplus
}
#endif
#endif