package com.chat2;

import com.chat1.ChatMsg;
import com.other.Constant;
import org.yifan.hao.FileUtils;
import org.yifan.hao.GsonUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * https://www.runoob.com/java/net-serversocket-socket.html
 */

public class SocketUtils {

    private static final int BUFFER_SIZE = 1024 * 1024; // 1 MB
    private final IServiceNotifyMsg iServiceNotifyMsg;
    private ServerSocket serverSocket;
    private Socket mClientSocket;
    private Socket mServiceSocket;

    public SocketUtils(IServiceNotifyMsg iServiceNotifyMsg) {
        this.iServiceNotifyMsg = iServiceNotifyMsg;
    }

    /**
     * 启动服务端
     *
     * @param port        服务器端口
     * @param serviceCall 接收客户端消息回调
     */
    public void startService(int port, IReceiverMsg serviceCall) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.printf("启动服务 %d\n", serverSocket.getLocalPort());

                while (true) {
                    mServiceSocket = serverSocket.accept();
                    BufferedReader serviceReader = new BufferedReader(new InputStreamReader(mServiceSocket.getInputStream()));

                    receiverMsg(serviceCall, serviceReader);

                    mServiceSocket.close();
                }
            } catch (IOException e) {
                iServiceNotifyMsg.errMsg(e, "启动服务器失败");
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 客户端连接服务器
     *
     * @param host       服务器地址
     * @param port       端口
     * @param clientCall 接收服务端消息
     */
    public void connService(String host, int port, IReceiverMsg clientCall) {
        try {
            mClientSocket = new Socket(host, port);

            BufferedReader clientReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
//            BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream()));
//            clientWriter.write("连接服务器成功");
//            clientWriter.newLine();
//            clientWriter.flush();
            sendMsgToClient("连接服务器成功");
            receiverMsg(clientCall, clientReader);
        } catch (IOException e) {
            iServiceNotifyMsg.errMsg(e, "连接服务器异常");
            e.printStackTrace();
        }
    }

    /**
     * 发送原始字符串数据给服务端(标记过期只是标识一下)
     *
     * @param str 客户端发消息给服务端(原始数据)
     */
    @Deprecated
    private void sendMsgToServicePrimitive(String str) {
        try {
            BufferedWriter serviceWriter = new BufferedWriter(new OutputStreamWriter(mServiceSocket.getOutputStream()));
            serviceWriter.write(str);
            serviceWriter.newLine();
            serviceWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            iServiceNotifyMsg.errMsg(e, "服务端发送消息异常");
        }
    }

    /**
     * 发送原始字符串数据给客户端(标记过期只是标识一下)
     *
     * @param str 服务端发消息给客户端
     */
    @Deprecated
    private void sendMsgToClientPrimitive(String str) {
        try {
            BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream()));
            clientWriter.write(str);
            clientWriter.newLine();
            clientWriter.flush();
        } catch (IOException e) {
            iServiceNotifyMsg.errMsg(e, "客户端发送消息异常");
            e.printStackTrace();
        }
    }

    /**
     * @param clientCall 消息回调
     * @param reader     输入流
     */
    private void receiverMsg(IReceiverMsg clientCall, BufferedReader reader) {
        String msg;
        try {
            while ((msg = reader.readLine()) != null) {
                System.out.println("msg = " + msg);
                ChatMsg chatMsg = GsonUtils.fromJson(msg, ChatMsg.class);
                if (chatMsg.getMsgType() == ChatMsg.MSG_TYPE_FILE) {
                    FileUtils.writeFile(Constant.PATH_FILE_DIR + chatMsg.getFileName(), chatMsg.getFileData(), true);
                } else if (chatMsg.getMsgType() == ChatMsg.MSG_TYPE_MSG) {
                    clientCall.receiverMsg(chatMsg.getMsgContent());
                }
                if ("Bye".equals(chatMsg.getMsgContent())) {
                    clientCall.receiverMsg("已下线");
                    break;
                }
            }
        } catch (IOException e) {
            iServiceNotifyMsg.errMsg(e, "接收消息异常");
            e.printStackTrace();
        }
    }

    /**
     * 停止服务器
     */
    public void stopService() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            iServiceNotifyMsg.errMsg(e, "停止失败");
            e.printStackTrace();
        }
    }

    /**
     * 停止服务器
     */
    public void stopClient() {
        try {
            mClientSocket.close();
        } catch (IOException e) {
            iServiceNotifyMsg.errMsg(e, "停止客户端失败");
            e.printStackTrace();
        }
    }

    public void sendMsgToClient(String msg) {
        sendMsgToClientPrimitive(new ChatMsg(msg).toJson());
    }
//-----------------------------------------------------------------------------------------

    public void sendMsgToService(String msg) {
        sendMsgToServicePrimitive(new ChatMsg(msg).toJson());
    }

    public void sendFileToService(String filePath) {
        File file = new File(filePath);
        String md5 = FileUtils.getFileMD5(file);
        System.out.println("start send file----" + file.getName());
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                System.out.println("bytesRead = " + bytesRead);
                byte[] dataToSend = Arrays.copyOf(buffer, bytesRead);
                ChatMsg fileMsg = new ChatMsg(file.getName(), md5, file.length(), dataToSend);
                sendMsgToServicePrimitive(fileMsg.toJson());
            }

            System.out.println("File send: " + filePath);
        } catch (IOException e) {
            sendMsgToClient("接收文件异常: " + e.getMessage());
            System.err.println("Error sending file: " + e.getMessage());
        }
    }

    public void sendFileToClient(String filePath) {
        File file = new File(filePath);
        String md5 = FileUtils.getFileMD5(file);
        System.out.println("start send file" + file.getName());
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] dataToSend = Arrays.copyOf(buffer, bytesRead);
                ChatMsg fileMsg = new ChatMsg(file.getName(), md5, file.length(), dataToSend);
                sendMsgToClientPrimitive(fileMsg.toJson());
            }

            System.out.println("File send: " + filePath);
        } catch (IOException e) {
            sendMsgToService("接收文件异常: " + e.getMessage());
            System.err.println("Error sending file: " + e.getMessage());
        }
    }

    public interface IReceiverMsg {
        void receiverMsg(String receiveMsg);

        default void progress(int progress) {

        }
    }

    @FunctionalInterface
    public interface IServiceNotifyMsg {
        void errMsg(Exception e, String errMsg);
    }
}