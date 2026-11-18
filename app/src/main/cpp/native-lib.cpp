#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_cart3r_mobile_NativeBridge_nativeVersion(JNIEnv* env, jobject /* thiz */) {
    return env->NewStringUTF("C++ JNI: CART3R demo module loaded");
}
