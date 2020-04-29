//
// Created by xwli on 20-4-29.
//

#include "ffmpeg.h"
#include "opensl.h"

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"HusterYP",FORMAT,##__VA_ARGS__);  // 输出到AS的log中
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"HusterYP",FORMAT,##__VA_ARGS__);
using namespace std;

int playthread_start = 0;
SLAndroidSimpleBufferQueueItf pcmBufferQueue;

/** 准备FFmpeg **/
void *preparFFmpeg(void *urltmp) {

    char *url = static_cast<char *>(urltmp);

    int retcode;
    //初始化FormatContext
    AVFormatContext *formatContext = NULL;
    //打开输入流
    retcode = avformat_open_input(&formatContext, url, NULL, 0);
    LOGI("avformat_open_input:%d", retcode);

    if (retcode != 0) {
        return nullptr;
    }
    LOGI("preparFFmpeg:%s", "avformat_open_input");

    //读取媒体文件信息
    retcode = avformat_find_stream_info(formatContext, NULL);
    if (retcode != 0) {
        return nullptr;
    }
    //分配codecContext
    AVCodecContext *codecContext = avcodec_alloc_context3(NULL);
    if (!codecContext) {
        return nullptr;
    }
    //寻找到音频流的下标
    int audioIndex = av_find_best_stream(formatContext, AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);

    //将视频流的的编解码信息拷贝到codecContext中
    retcode = avcodec_parameters_to_context(codecContext,
                                            formatContext->streams[audioIndex]->codecpar);
    if (retcode != 0) {
        return nullptr;
    }
    //查找解码器
    AVCodec *codec = avcodec_find_decoder(codecContext->codec_id);
    if (codec == NULL) {
        return nullptr;
    }
    LOGI("avcodec_find_decoder:%s", "avcodec_find_decoder success");

    //打开解码器
    retcode = avcodec_open2(codecContext, codec, NULL);
    if (retcode != 0) {
        return nullptr;
    }
    LOGI("avcodec_open2:%d", retcode);

    AVPacket *packet = av_packet_alloc();
    AVFilterGraph *graph;
    AVFilterContext *in_ctx;
    AVFilterContext *out_ctx;

    while (av_read_frame(formatContext, packet) == 0) {
        LOGI("av_read_frame(formatContext, packet) == 0");
        if (packet->stream_index == audioIndex) {
            if (avcodec_send_packet(codecContext, packet) == 0) {
                int ret = 0;
                while (ret == 0) {
                    AVFrame *avFrame = av_frame_alloc();
                    ret = avcodec_receive_frame(codecContext, avFrame);
                    if (ret == 0) {

                        init_volume_filter(&graph, &in_ctx, &out_ctx, const_cast<char *>("2.0"), avFrame);

                        if (av_buffersrc_add_frame(in_ctx, avFrame) < 0) {//将frame放入输入filter上下文
                            LOGI("av_buffersrc_add_frame error");

                            break;
                        }

                        while (av_buffersink_get_frame(out_ctx, avFrame) >= 0) {
                            queuePacket.push(avFrame);
                            LOGI(" queuePacketOne.push");
                            if (playthread_start== 0) {
                                playthread_start = 1;
                                pthread_t threadId_play;
                                // 开启播放线程
                                struct OpenStruct openStruct = {
                                        avFrame,
                                        pcmBufferQueue
                                };

                                pthread_create(&threadId_play, nullptr,
                                               (void *(*)(void *)) (initOpenSLES),
                                               (void *) &openStruct);

                            }



                            pthread_cond_wait(&cond, &mutexSync);

                        }
                        avfilter_graph_free(&graph);
                    }
                }

            }
        }

    }

    av_packet_free(&packet);
    avformat_close_input(&formatContext);
    LOGI("%s", "FFmpeg Prepare Success..");

    return nullptr;

}

int init_volume_filter(AVFilterGraph **pGraph, AVFilterContext **src, AVFilterContext **out,
                       char *value, AVFrame *avFrame) {

    std::string str = "sample_rates=48000:sample_fmts=s16p:channel_layouts=stereo";
    //初始化AVFilterGraph
    AVFilterGraph *graph = avfilter_graph_alloc();
    //获取abuffer用于接收输入端
    const AVFilter *abuffer = avfilter_get_by_name("abuffer");
    AVFilterContext *abuffer_ctx = avfilter_graph_alloc_filter(graph, abuffer, "src");

    char ch_layout[64];
    av_get_channel_layout_string(ch_layout, sizeof(ch_layout), 0, avFrame->channel_layout);
    av_opt_set(abuffer_ctx, "channel_layout", ch_layout, AV_OPT_SEARCH_CHILDREN);
    av_opt_set(abuffer_ctx, "sample_fmt", av_get_sample_fmt_name(
            static_cast<AVSampleFormat>(avFrame->format)),
               AV_OPT_SEARCH_CHILDREN);
    av_opt_set_int(abuffer_ctx, "sample_rate", avFrame->sample_rate,
                   AV_OPT_SEARCH_CHILDREN);
    //设置参数，这里需要匹配原始音频采样率、数据格式（位数）
    int code = avfilter_init_str(abuffer_ctx, nullptr);
    if (code < 0) {
        LOGE("error init abuffer filter  %d", code);
        return -1;
    }
    //初始化volume filter
    const AVFilter *volume = avfilter_get_by_name("volume");
    AVFilterContext *volume_ctx = avfilter_graph_alloc_filter(graph, volume, "volume");
    //这里采用av_dict_set设置参数
    AVDictionary *args = NULL;
    av_dict_set(&args, "volume", value, 0);//这里传入外部参数，可以动态修改
    if (avfilter_init_dict(volume_ctx, &args) < 0) {
        LOGE("error init volume filter");
        return -1;
    }

    const AVFilter *aformat = avfilter_get_by_name("aformat");
    AVFilterContext *aformat_ctx = avfilter_graph_alloc_filter(graph, aformat, "aformat");

    char ch_layout1[64];
    av_get_channel_layout_string(ch_layout1, sizeof(ch_layout1), 0, avFrame->channel_layout);
    av_opt_set(aformat_ctx, "channel_layout", ch_layout1, AV_OPT_SEARCH_CHILDREN);
    av_opt_set(aformat_ctx, "sample_fmt", av_get_sample_fmt_name(
            static_cast<AVSampleFormat>(avFrame->format)),
               AV_OPT_SEARCH_CHILDREN);
    av_opt_set_int(aformat_ctx, "sample_rate", avFrame->sample_rate, AV_OPT_SEARCH_CHILDREN);


    if (avfilter_init_str(aformat_ctx, nullptr) < 0) {
        LOGE("error init aformat filter");
        return -1;
    }
    //初始化sink用于输出
    const AVFilter *sink = avfilter_get_by_name("abuffersink");
    AVFilterContext *sink_ctx = avfilter_graph_alloc_filter(graph, sink, "sink");
    if (avfilter_init_str(sink_ctx, NULL) < 0) {//无需参数
        LOGE("error init sink filter");
        return -1;
    }
    //链接各个filter上下文
    if (avfilter_link(abuffer_ctx, 0, volume_ctx, 0) != 0) {
        LOGE("error link to volume filter");
        return -1;
    }
    if (avfilter_link(volume_ctx, 0, aformat_ctx, 0) != 0) {
        LOGE("error link to aformat filter");
        return -1;
    }
    if (avfilter_link(aformat_ctx, 0, sink_ctx, 0) != 0) {
        LOGE("error link to sink filter");
        return -1;
    }
    if (avfilter_graph_config(graph, NULL) < 0) {
        LOGI("error config filter graph");
        return -1;
    }
    *pGraph = graph;
    *src = abuffer_ctx;
    *out = sink_ctx;
    LOGI("init filter success...");
    return 0;
}
