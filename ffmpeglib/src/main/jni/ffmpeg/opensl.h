//
// Created by xwli on 20-4-29.
//

#ifndef OPENGLDEMO_OPENSL_H
#define OPENGLDEMO_OPENSL_H
#include <android/log.h>
#include <cstdint>
#include <jni.h>
#include <string>
#include <SLES/OpenSLES_Android.h>
#include "pthread.h"
#include "unistd.h"
#include "callJava.h"

void *initOpenSLES(void *openresult);
void ffmpegBufferCallBack(SLAndroidSimpleBufferQueueItf bf, void *context);
#endif //OPENGLDEMO_OPENSL_H


