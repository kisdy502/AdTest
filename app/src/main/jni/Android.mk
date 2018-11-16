LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := AdHelper
LOCAL_SRC_FILES := AdHelper.c
LOCAL_LDLIBS :=-llog

include $(BUILD_SHARED_LIBRARY)

