cmake_minimum_required(VERSION 3.10.2)
project("myapplication")

##官方标准配置
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall")

##ANDROID_PLATFORM_LEVEL=18
add_definitions("-DDYNAMIC_ES3")
set(OPENGL_LIB GLESv3)

add_library(
        native-lib
        SHARED
        native-lib.cpp)

target_link_libraries(native-lib
        ${OPENGL_LIB}
        android
        EGL
        log
        m)