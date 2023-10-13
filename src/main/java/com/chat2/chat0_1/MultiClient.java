package com.chat2.chat0_1;


import org.yifan.hao.WinUtils;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MultiClient extends Thread {
    private Socket clientSocket;

    public MultiClient() {
    }

    public void startClient() {
        try {
            clientSocket = new Socket(WinUtils.getIpV4(), 3243);
            System.out.println("服务器连接成功");
            System.out.println("-----------聊天室-----------");
            Scanner sc = new Scanner(System.in);
            System.out.println(" 启动线程 ");
            /*while (true) {
                // 由用户输入选择执行不同的传输任务
                // 若用户输入传输文件，则传输指定文件，否则，则正常聊天任务
                System.out.println(" 开始循环");
                String str = sc.nextLine();
                System.out.println(" 输入信息 " + str);
                if (str.equals("传输文件")) {
                    String path = "E:/01/《激情焚烧》【NJ：小莫】.mp3";
                    clientSendFile(path);
                } else {
                    clientSendMsg(str);
                    if (str.equalsIgnoreCase("bye")) {
                        System.out.println("用户下线!");
                        break;
                    }
                }

            }*/
        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                stop();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        start();
    }

    public void clientSendMsg(String str) throws IOException {
        byte[] sb = str.getBytes(); // 转化为字节数组
        BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
        byte[] newByteArr = reviseArr(sb, sb.length);
        newByteArr[1] = 2;      // 表示第二个位置上的值为2时表示传输的是聊天内容
        bos.write(newByteArr);  // 把内容发给服务器
        bos.flush();
    }

    public void clientSendFile(String path) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        byte[] by = new byte[1024];
        int res = 0;
        int i = 0;
        while ((res = bis.read(by)) != -1) {
            i += 1;
            //System.out.println("i" + i + " res: " + res);
            byte[] newByteArr = reviseArr(by, res);
            newByteArr[1] = 1;  // 表示第二个位置上的值为1时表示传输的是文件
            bos.write(newByteArr, 0, res + 2);
        }
        bos.flush();
        bos.close();
        bis.close();
    }


    public byte[] reviseArr(byte[] by, int res) {
        byte[] newByteArr = new byte[by.length + 2];
        // 将by字节数组的内容都往后移动两位，即头部的两个位置空出来作为标志位
        for (int i = 0; i < by.length; i++) {
            newByteArr[i + 2] = by[i];
        }
        return newByteArr;
    }

    // 子线程执行读操作，读取服务端发回的数据
    @Override
    public void run() {
        BufferedInputStream bis = null;
        BufferedOutputStream bosFile = null;    // 与输出文件流相关联
        try {
            bis = new BufferedInputStream(clientSocket.getInputStream());
            //bosFile = new BufferedOutputStream(new FileOutputStream("./directoryTest/src/用户1 IO流的框架图.png"));
            // 等待接收服务器发送回来的消息
            while (true) {
                byte[] by = new byte[1024 + 2];
                int res = bis.read(by);
                int sendUser = by[0];
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                String format = sdf.format(date);
                if (by[1] == 1) // 说明传的是文件
                {
                    String path = "E:/01/《激情焚烧》【NJ：小莫】.mp3";
                    //String filePath = String.format("./directoryTest/src/用户%d传送来的IO流的框架图.png", sendUser);
                    bosFile = new BufferedOutputStream(new FileOutputStream(path, true));
                    bosFile.write(by, 2, res - 2);
                    bosFile.flush();
                    if (res < 1026)   // 说明是最后一次在传送文件，所以传送的字节数才会小于字节数组by的大小
                    {
                        System.out.println("用户" + sendUser + "\t" + format + ":");
                        System.out.printf("用户%d发送的文件传输完成\n", sendUser);
                    }
                } else    // 说明传输的是聊天内容，则按字符串的形式进行解析
                {
                    // 利用String构造方法的形式，将字节数组转化成字符串打印出来
                    String receive = new String(by, 2, res);
                    System.out.println("用户" + sendUser + "\t" + format + ":");
                    System.out.println(receive);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 主线程执行写操作，发送消息到服务器
        MultiClient mcc = new MultiClient();
        mcc.startClient();
        mcc.start();
    }
}
