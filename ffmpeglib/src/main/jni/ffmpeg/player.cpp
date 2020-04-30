//
// Created by xwli on 20-4-29.
//


#include <android/log.h>
#include <cstdint>
#include <jni.h>
#include "callJava.h"
#include "ffmpeg.h"


JavaVM *javaVM = nullptr;
jobject myJobject;
callJava *myCallJava = nullptr;

extern "C"
JNIEXPORT void JNICALL
Java_com_deomo_ffmpeglib_Player_playWithFFmpeg(JNIEnv *env, jobject instance,
                                                            jstring pamPath_) {
    //release();
    const char *pamPath = env->GetStringUTFChars(pamPath_, nullptr);
    myJobject = env->NewGlobalRef(instance);
    env->GetJavaVM(&javaVM);
    if (myCallJava == nullptr) {
        myCallJava = new callJava(javaVM, env, &myJobject);
    }

    outBuff = (unsigned char *) av_malloc(MAX_AUDIO_FRAME_SIZE * 2); //双声道
    pthread_t threadId_adecode;
    pthread_create(&threadId_adecode, nullptr, preparFFmpeg, (void *) pamPath);
    env->ReleaseStringUTFChars(pamPath_, pamPath);

}


