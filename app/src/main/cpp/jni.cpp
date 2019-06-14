#include <jni.h>

extern "C"
JNIEXPORT jint JNICALL
Java_net_yui_app_JNI_add(JNIEnv *env, jobject instance, jint a, jint b) {
    return a + b;
}