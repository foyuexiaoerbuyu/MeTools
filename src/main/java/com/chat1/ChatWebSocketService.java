package com.chat1;


import org.yifan.hao.GsonUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.yifan.hao.FileUtils;
import org.yifan.hao.WinUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://blog.huangyuanlove.com/2017/12/25/Android%E4%B8%AD%E4%BD%BF%E7%94%A8WebSocket/
 * implementation 'org.java-websocket:Java-WebSocket:1.5.3'
 * <dependency>
 * <groupId>org.java-websocket</groupId>
 * <artifactId>Java-WebSocket</artifactId>
 * <version>1.5.3</version>
 * </dependency>
 */
public class ChatWebSocketService extends WebSocketServer implements ISendMsg {

    /**
     * 端口
     */
    private int port;
    private IChatServiceCallBack iServiceCallBack;
    /**
     * 客户端连接
     */
    private List<WebSocket> webSockets = new ArrayList<>();

    public ChatWebSocketService(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public ChatWebSocketService(int port, IChatServiceCallBack iChatServiceCallBackClass) throws UnknownHostException {
        super(new InetSocketAddress(port));
        this.port = port;
        iServiceCallBack = iChatServiceCallBackClass;
    }

    public ChatWebSocketService(InetSocketAddress address) {
        super(address);
    }


    public static ChatWebSocketService startService(int port) throws UnknownHostException {
        System.out.println("port = " + port);
//        String ipV4 = NetUtils.getIpV4();
//        System.out.println("ipV4 = " + ipV4);
//        int port = 8887; // 843 flash policy port
        ChatWebSocketService s = new ChatWebSocketService(port);
        s.start();
        System.out.println("ChatServer started on port: " + s.getPort());
//        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            String in = sysin.readLine();
//            if (in.equals("send")) {
//                String path = "D:/01系统相关/下载/skyeye-company_server.zip";
//                s.sendFile(s, path);
//                continue;
//            }
//            s.sendMsg(new ChatMsg(in));
//            if (in.equals("exit")) {
//                s.stop(1000);
//                break;
//            }
//        }
        return s;
    }


    public void stopService() throws InterruptedException {
        this.stop(1000, "pc主动停止服务");
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        sendMsg(new ChatMsg("与服务器建立连接"));
        String clientName = clientHandshake.getFieldValue("client_name");
        webSocket.setAttachment(new SocketInfo(clientName));
        webSockets.add(webSocket);
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " " + clientName + " 进入房间!");
//        String path = "D:/01系统相关/下载/codecrafts-release-latest-version.apk";
//        sendFile(webSocket, path);
    }

    @Override
    public void sendMsgText(String msg) {
        broadcast(GsonUtils.toJson(new ChatMsg(msg)));
    }

    @Override
    public void sendMsgText(String msg, ArrayList<WebSocket> tmpSockets) {
        broadcast(GsonUtils.toJson(new ChatMsg(msg)), tmpSockets);
    }

    @Override
    public void sendMsg(ChatMsg msg) {
        broadcast(GsonUtils.toJson(msg));
    }

    @Override
    public void sendMsg(ChatMsg msg, ArrayList<WebSocket> tmpSockets) {
        broadcast(GsonUtils.toJson(msg), tmpSockets);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        sendMsg(new ChatMsg(webSocket + " onClose"));
        webSockets.remove(webSocket);
        System.out.println(webSocket + " onClose");
    }

    @Override
    public void onMessage(WebSocket webSocket, String msgStr) {
//        Platform.runLater(() -> {//主线程运行
//
//        });
        if (msgStr.startsWith("cmd_clipboard_get")) {//获取手机剪贴板内容
            String appClipboardStr = msgStr.replace("cmd_clipboard_get", "");
            WinUtils.setSysClipboardText(appClipboardStr);
            iServiceCallBack.log("已复制: " + appClipboardStr);
            return;
        }
        ArrayList<WebSocket> tmpSockets = new ArrayList<>();
        for (WebSocket socket : webSockets) {
            if (socket != webSocket) {
                tmpSockets.add(socket);
            }
        }
        sendMsgText(msgStr, tmpSockets);

        SocketInfo attachment = webSocket.getAttachment();
        if (iServiceCallBack != null) {
//            iServiceCallBack.receiveMsg(attachment.getName() + ": " + DateUtil.formatCurrentDate(DateUtil.REGEX_DATE_TIME) + "\n   " + msg + "\n");
            iServiceCallBack.receiveMsg(msgStr);
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
        if (webSocket != null) {
            webSockets.remove(webSocket);
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        iServiceCallBack.log("服务已启动: " + WinUtils.getIpV4() + ":" + port);
//        Platform.runLater(() -> DialogUtils.showAutoCloseDialog("服务已启动", 1000));
    }


    private static final int BUFFER_SIZE = 1024 * 1024; // 1 MB

    public void sendFileToClient(String filePath) {
        File file = new File(filePath);
        String md5 = FileUtils.getFileMD5(file);
        sendMsgText("开始接收文件: " + file.getName());
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] dataToSend = Arrays.copyOf(buffer, bytesRead);
                ChatMsg fileMsg = new ChatMsg(file.getName(), md5, file.length(), dataToSend);
                sendMsg(fileMsg);
            }
            sendMsgText("接收完毕: " + file.getName());

            System.out.println("File send: " + filePath);
        } catch (IOException e) {
            sendMsgText("接收文件异常: " + e.getMessage());
            System.err.println("Error sending file: " + e.getMessage());
        }
    }

    /**
     * 待验证
     */
    @Deprecated
    public void sendFileToClient(File file) {
        WebSocket webSocket = webSockets.get(0);

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            int progress = 0;
            String fileMD5 = FileUtils.getFileMD5(file);
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] dataToSend = Arrays.copyOf(buffer, bytesRead);
                progress += bytesRead;
                ChatMsg chatMsg = new ChatMsg(file.getName(), fileMD5, file.length(), dataToSend);
                chatMsg.setProgress(progress);
                webSocket.send(chatMsg.toJson());
            }

            System.out.println("File sent: " + file.getPath());
        } catch (IOException e) {
            if (iServiceCallBack != null) {
                iServiceCallBack.errMsg("发送文件失败: " + e.getMessage());
            }
            System.err.println("Error sending file: " + e.getMessage());
        }
    }
}