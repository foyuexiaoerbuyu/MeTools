package com.chat2.chat0_1;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiServer {


    Socket serviceSocket = null;

    public MultiServer() {
    }

    public static void main(String[] args) {
        MultiServer multiServer = new MultiServer();
        multiServer.start();
    }

    public void start() {
        // 定义一个List列表来保存每个客户端，每新建一个客户端连接，就添加到List列表里。
        List<Socket> listSocket = new ArrayList<>();
        ServerSocket serverSocket = null;
        try {
            // 1. 创建ServerSocket类型的对象并提供端口号
            serverSocket = new ServerSocket(3243);
            // 2. 等待客户端的连接请求，调用accept方法
            // 采用多线程的方式，允许多个用户请求连接。
            int i = 0;
            while (true) {
                System.out.println("等待客户端的连接请求...");
                serviceSocket = serverSocket.accept();
                listSocket.add(serviceSocket);
                //sArr[i] = s;
                i++;
                System.out.printf("欢迎用户%d加入群聊!\n", i);
                System.out.printf("目前群聊中共有%d人\n", listSocket.size());
                InetAddress inetAddress = serviceSocket.getInetAddress();
                System.out.println("客户端" + inetAddress + "连接成功!");
                // 调用多线程方法，每一个连上的客户端，服务器都有一个线程为之服务
                new MultiServerThread(serviceSocket, inetAddress, listSocket).start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                serviceSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serviceSendMsg(String str) throws IOException {
        byte[] sb = str.getBytes(); // 转化为字节数组
        BufferedOutputStream bos = new BufferedOutputStream(serviceSocket.getOutputStream());
        byte[] newByteArr = reviseArr(sb, sb.length);
        newByteArr[1] = 2;      // 表示第二个位置上的值为2时表示传输的是聊天内容
        bos.write(newByteArr);  // 把内容发给服务器
        bos.flush();
    }

    public void serviceSendFile(String path) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(serviceSocket.getOutputStream());
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        int res = 0;
        byte[] by = new byte[1024];
        while ((res = bis.read(by)) != -1) {
            //System.out.println("i" + i + " res: " + res);
            byte[] newByteArr = reviseArr(by, res);
            newByteArr[1] = 1;  // 表示第二个位置上的值为1时表示传输的是文件
            bos.write(newByteArr, 0, res + 2);
        }
        bos.flush();
        bis.close();
        bos.close();
    }

    public byte[] reviseArr(byte[] by, int res) {
        byte[] newByteArr = new byte[by.length + 2];
        // 将by字节数组的内容都往后移动两位，即头部的两个位置空出来作为标志位
        for (int i = 0; i < by.length; i++) {
            newByteArr[i + 2] = by[i];
        }
        return newByteArr;
    }
}

