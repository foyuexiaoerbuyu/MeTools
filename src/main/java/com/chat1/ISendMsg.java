package com.chat1;

import org.java_websocket.WebSocket;

import java.util.ArrayList;

public interface ISendMsg {
    public void sendMsgText(String msg);

    public void sendMsgText(String msg, ArrayList<WebSocket> tmpSockets);

    public void sendMsg(ChatMsg msg);

    public void sendMsg(ChatMsg msg, ArrayList<WebSocket> tmpSockets);
}
