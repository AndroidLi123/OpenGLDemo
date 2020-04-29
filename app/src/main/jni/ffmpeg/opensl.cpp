//
// Created by xwli on 20-4-29.
//
#include "opensl.h"
#include "common.h"
int out_buffer_size = -1;

void *initOpenSLES(void *openresult) {
    OpenStruct *openStruct = static_cast<OpenStruct *>(openresult);
    AVFrame *avFrame = openStruct->avFrame;
    SLAndroidSimpleBufferQueueItf pcmBufferQueue = openStruct->pcmBufferQueue;
    SLresult result;
    // 引擎接口
    SLObjectItf engineObject = NULL;
    SLEngineItf engineEngine = NULL;

    //混音器
    SLObjectItf outputMixObject = NULL;
    SLEnvironmentalReverbItf outputMixEnvironmentalReverb = NULL;
    SLEnvironmentalReverbSettings reverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;
    SLObjectItf pcmPlayerObject = NULL;
    SLPlayItf pcmPlayerPlay = NULL;
    SLVolumeItf pcmPlayerVolume = NULL;

    result = slCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);
    result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
    result = (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineEngine);
    LOGI("%s", " createEngine Success..");

    //第二步，创建混音器
    const SLInterfaceID mids[1] = {SL_IID_ENVIRONMENTALREVERB};
    const SLboolean mreq[1] = {SL_BOOLEAN_FALSE};
    result = (*engineEngine)->CreateOutputMix(engineEngine, &outputMixObject, 1, mids, mreq);
    (void) result;
    result = (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
    (void) result;
    result = (*outputMixObject)->GetInterface(outputMixObject, SL_IID_ENVIRONMENTALREVERB,
                                              &outputMixEnvironmentalReverb);
    if (SL_RESULT_SUCCESS == result) {
        result = (*outputMixEnvironmentalReverb)->SetEnvironmentalReverbProperties(
                outputMixEnvironmentalReverb, &reverbSettings);
        (void) result;
    }
    SLDataLocator_OutputMix outputMix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};

    SLDataSink audioSnk = {&outputMix, NULL};

    LOGI("%s", "创建混音器 Success..");

    // 第三步，配置PCM格式信息
    SLDataLocator_AndroidSimpleBufferQueue android_queue = {
            SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,
            2};

    //LOGI("重采样 %d %d", resample_channel, resample_rate * 1000);

    SLDataFormat_PCM pcm = {
            SL_DATAFORMAT_PCM,//播放pcm格式的数据
            (SLuint32) avFrame->channels,//2个声道（立体声）
            (SLuint32) (avFrame->sample_rate * 1000),//44100hz的频率
            SL_PCMSAMPLEFORMAT_FIXED_16,//位数 16位
            SL_PCMSAMPLEFORMAT_FIXED_16,//和位数一致就行
            SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,//立体声（前左前右）
            SL_BYTEORDER_LITTLEENDIAN//结束标志
    };

    SLDataSource slDataSource = {&android_queue, &pcm};
    const SLInterfaceID ids[3] = {SL_IID_BUFFERQUEUE, SL_IID_EFFECTSEND, SL_IID_VOLUME};
    const SLboolean req[3] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};
    LOGI("%s", "创建PCM Success..");
    result = (*engineEngine)->CreateAudioPlayer(engineEngine, &pcmPlayerObject, &slDataSource,
                                                &audioSnk, 3, ids, req);
    //初始化播放器
    (*pcmPlayerObject)->Realize(pcmPlayerObject, SL_BOOLEAN_FALSE);

//    得到接口后调用  获取Player接口
    (*pcmPlayerObject)->GetInterface(pcmPlayerObject, SL_IID_PLAY, &pcmPlayerPlay);

//    注册回调缓冲区 获取缓冲队列接口
    (*pcmPlayerObject)->GetInterface(pcmPlayerObject, SL_IID_BUFFERQUEUE, &pcmBufferQueue);
    //缓冲接口回调
    (*pcmBufferQueue)->RegisterCallback(pcmBufferQueue, ffmpegBufferCallBack, NULL);
//    获取音量接口
    (*pcmPlayerObject)->GetInterface(pcmPlayerObject, SL_IID_VOLUME, &pcmPlayerVolume);

//    获取播放状态接口
    (*pcmPlayerPlay)->SetPlayState(pcmPlayerPlay, SL_PLAYSTATE_PLAYING);
    LOGI("%s", "OpenSL ES Prepare Success..");

//    主动调用回调函数开始工作
    ffmpegBufferCallBack(pcmBufferQueue, NULL);

    LOGI("%s", "主动调用回调函数开始工作 Success..");

    return nullptr;
}


void getDeoceAudioData(pthread_cond_t *cond, pthread_mutex_t *mutex,
                       SLAndroidSimpleBufferQueueItf *pcmBufferQueue,
                       std::queue<AVFrame *> *queuePacket, unsigned char *outBuff, int mode) {

    pthread_mutex_lock(&*mutex);
    if (!queuePacket->empty()) {

        AVFrame *frame = queuePacket->front();

        queuePacket->pop();
        pthread_mutex_unlock(&*mutex);

        //初始化重采样器
        SwrContext *auConvertContext = swr_alloc_set_opts(nullptr, AV_CH_LAYOUT_STEREO,
                                                          AV_SAMPLE_FMT_S16,
                                                          frame->sample_rate,
                                                          frame->channel_layout,
                                                          static_cast<AVSampleFormat>(frame->format),
                                                          frame->sample_rate, 0,
                                                          nullptr);
        //初始化SwResample的Context
        swr_init(auConvertContext);

        int nb = swr_convert(auConvertContext, &outBuff, frame->nb_samples,
                             (const uint8_t **) frame->data,
                             frame->nb_samples);

        int out_channels = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);

        out_buffer_size = nb * out_channels * av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);

        (**pcmBufferQueue)->Enqueue(*pcmBufferQueue,
                                    outBuff, (SLuint32) out_buffer_size);

    } else {
        pthread_mutex_unlock(&*mutex);
        pthread_cond_signal(&*cond);
        (**pcmBufferQueue)->Enqueue(*pcmBufferQueue,
                                    "", 1);
    }


}

void ffmpegBufferCallBack(SLAndroidSimpleBufferQueueItf bf, void *context) {

    getDeoceAudioData(&cond, &mutexSync, &bf, &queuePacket, outBuff, 1);


}
