//
// Created by xwli on 20-4-29.
//

#include "common.h"

pthread_mutex_t mutexSync = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
std::queue<AVFrame *> queuePacket;
unsigned char *outBuff= nullptr;//输出的Buffer数据
