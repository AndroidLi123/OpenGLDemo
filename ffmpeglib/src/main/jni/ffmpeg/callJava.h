//
// Created by mobvoi on 23/09/2019.
//

#ifndef MYAPPLICATION3_CALLJAVA_H
#define MYAPPLICATION3_CALLJAVA_H
#include "jni.h"
#include <linux/stddef.h>

class callJava {

public:
    JNIEnv *jniEnv = NULL;
    jobject *jobj;
    JavaVM *javaVM = NULL;
public:
    callJava( JavaVM *javaVM,JNIEnv *env, jobject *obj);

    ~callJava();

    void timeCallback(double currentTime, int totalTime);
};


#endif //MYAPPLICATION3_CALLJAVA_H

