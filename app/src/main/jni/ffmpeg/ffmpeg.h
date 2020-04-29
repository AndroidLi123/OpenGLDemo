//
// Created by xwli on 20-4-29.
//

#ifndef OPENGLDEMO_FFMPEG_H
#define OPENGLDEMO_FFMPEG_H


#include "common.h"
#include "pthread.h"
#include <queue>
#include <android/log.h>
#include "unistd.h"
#include <string>
#include <cstdint>


void *preparFFmpeg(void *urltmp);
int init_volume_filter(AVFilterGraph **pGraph, AVFilterContext **src, AVFilterContext **out,
                       char *value, AVFrame *avFrame);



#endif //OPENGLDEMO_FFMPEG_H
