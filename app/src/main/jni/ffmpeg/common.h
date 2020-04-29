//
// Created by xwli on 20-4-29.
//

#ifndef OPENGLDEMO_COMMON_H
#define OPENGLDEMO_COMMON_H
extern "C" {
#include "libavcodec/avcodec.h"
#include "libavcodec/mediacodec.h"
#include "libavformat/avformat.h"
#include "libavutil/frame.h"
#include "libavutil/mem.h"
#include "libswresample/swresample.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "SLES/OpenSLES.h"
#include "SLES/OpenSLES_Android.h"
#include <libavutil/time.h>
#include <libavfilter/buffersrc.h>
#include <libavfilter/buffersink.h>
#include <libavfilter/avfilter.h>
#include <libavutil/opt.h>
}
#include <mutex>
#include <queue>

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"HusterYP",FORMAT,##__VA_ARGS__);  // 输出到AS的log中
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"HusterYP",FORMAT,##__VA_ARGS__);
#define MAX_AUDIO_FRAME_SIZE 192000

extern struct OpenStruct {
    AVFrame *avFrame;
    SLAndroidSimpleBufferQueueItf pcmBufferQueue;

} openStruct;


extern pthread_mutex_t mutexSync ;//init mutex

extern pthread_cond_t cond ;//init cond

extern std::queue<AVFrame *> queuePacket;

extern unsigned char *outBuff;//输出的Buffer数据


#endif //OPENGLDEMO_COMMON_H
