cmake_minimum_required(VERSION 3.4.1)

add_library(AdHelper
            SHARED
            src/main/jni/AdHelper.c)

find_library(log-lib
             log)

target_link_libraries(AdHelper
                      ${log-lib} )