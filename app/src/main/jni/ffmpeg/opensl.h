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

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"HusterYP",FORMAT,##__VA_ARGS__);  // 输出到AS的log中
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"HusterYP",FORMAT,##__VA_ARGS__);
#define MAX_AUDIO_FRAME_SIZE 192000 // 1 second of 48khz 32bit audio   48000 * (32/8)


void *initOpenSLES(void *openresult);
void ffmpegBufferCallBack(SLAndroidSimpleBufferQueueItf bf, void *context);
#endif //OPENGLDEMO_OPENSL_H


