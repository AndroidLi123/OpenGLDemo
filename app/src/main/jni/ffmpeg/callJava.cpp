//
// Created by mobvoi on 23/09/2019.
//
#include "callJava.h"

callJava::callJava( JavaVM *javaVM,JNIEnv *env, jobject *obj) {

    this->javaVM = javaVM;
    this->jniEnv = env;
    this->jobj = obj;
}

void callJava::timeCallback(double currentTime,int totalTime) {


    JNIEnv *jniEnv;

    javaVM->AttachCurrentThread(&jniEnv, nullptr);

    jclass clazz = jniEnv->GetObjectClass(*jobj);

    jmethodID method1 = jniEnv->GetMethodID(clazz, "timeCallback", "(DI)V");

    jniEnv->CallVoidMethod(*jobj, method1, currentTime, totalTime);

    javaVM->DetachCurrentThread();



}