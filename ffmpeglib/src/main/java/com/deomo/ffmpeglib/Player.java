package com.deomo.ffmpeglib;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/30
 * @content: 简易的万能音乐播放器,支持任意格式
 * @version: 1.0.0
 */
public class Player {
    static {

        System.loadLibrary("avcodec-58");
        System.loadLibrary("avdevice-58");
        System.loadLibrary("avfilter-7");
        System.loadLibrary("avformat-58");
        System.loadLibrary("avutil-56");
        System.loadLibrary("swresample-3");
        System.loadLibrary("swscale-5");
        System.loadLibrary("wlffmpeg");
    }

    public void play(String path) {
        playWithFFmpeg(path);
    }

    /**
     * 播放音乐
     * @param path 文件路径
     */
    private native void playWithFFmpeg(String path);

}
