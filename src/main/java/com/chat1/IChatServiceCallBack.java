package com.chat1;

public interface IChatServiceCallBack {
    void errMsg(String errMsg);

    void receiveMsg(String serviceMsg);

    default void log(String log) {

    }
}
