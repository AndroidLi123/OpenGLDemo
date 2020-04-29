cmake_minimum_required(VERSION 3.4.1) # cmake最低版本
include_directories(src/main/jni/ffmpeg/include)
include_directories(src/main/jni/ffmpeg/soundtouch/include)
include_directories(src/main/jni/ffmpeg/soundtouch/SoundTouch)
add_library( # Sets the name of the library.
             wlffmpeg

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
        src/main/jni/ffmpeg/callJava.cpp
        src/main/jni/ffmpeg/ffmpeg.cpp
        src/main/jni/ffmpeg/opensl.cpp
        src/main/jni/ffmpeg/common.cpp
        src/main/jni/ffmpeg/player.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/AAFilter.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/FIFOSampleBuffer.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/FIRFilter.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/cpu_detect_x86.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/sse_optimized.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/RateTransposer.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/SoundTouch.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/InterpolateCubic.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/InterpolateLinear.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/InterpolateShannon.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/TDStretch.cpp
        src/main/jni/ffmpeg/soundtouch/SoundTouch/PeakFinder.cpp
        )


add_library( avcodec-58  # 库名字
             SHARED
             IMPORTED)
set_target_properties( avcodec-58
                       PROPERTIES IMPORTED_LOCATION
                       ${CMAKE_SOURCE_DIR}/src/main/jni/ffmpeg/armeabi-v7a/libavcodec-58.so)

add_library( avdevice-58
             SHARED
             IMPORTED)
set_target_properties( avdevice-58
                       PROPERTIES IMPORTED_LOCATION
                       ${CMAKE_SOURCE_DIR}/src/main/jni/ffmpeg/armeabi-v7a/libavdevice-58.so)

add_library( avfilter-7
             SHARED
             IMPORTED)
set_target_properties( avfilter-7
                       PROPERTIES IMPORTED_LOCATION
                       ${CMAKE_SOURCE_DIR}/src/main/jni/ffmpeg/armeabi-v7a/libavfilter-7.so)

add_library( avformat-58
             SHARED
             IMPORTED)
set_target_properties( avformat-58
                       PROPERTIES IMPORTED_LOCATION
                       ${CMAKE_SOURCE_DIR}/src/main/jni/ffmpeg/armeabi-v7a/libavformat-58.so)

add_library( avutil-56
             SHARED
             IMPORTED)
set_target_properties( avutil-56
                       PROPERTIES IMPORTED_LOCATION
                       ${CMAKE_SOURCE_DIR}/src/main/jni/ffmpeg/armeabi-v7a/libavutil-56.so)

add_library( swresample-3
             SHARED
             IMPORTED)
set_target_properties( swresample-3
                       PROPERTIES IMPORTED_LOCATION
                       ${CMAKE_SOURCE_DIR}/src/main/jni/ffmpeg/armeabi-v7a/libswresample-3.so)

add_library( swscale-5
             SHARED
             IMPORTED)
set_target_properties( swscale-5
                       PROPERTIES IMPORTED_LOCATION
                       ${CMAKE_SOURCE_DIR}/src/main/jni/ffmpeg/armeabi-v7a/libswscale-5.so)


find_library( # Sets the name of the path variable.
              log-lib  # Android内置的log模块, 用于将JNI层的log打到AS控制台

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )


target_link_libraries( # Specifies the target library.  # 链接

                       wlffmpeg
                       avcodec-58
                       avdevice-58
                       avfilter-7
                       avformat-58
                       avutil-56
                       swresample-3
                       swscale-5
                        OpenSLES
                        android
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )