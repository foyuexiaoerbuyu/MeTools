package com.chat2;


import org.yifan.hao.DateUtil;
import org.yifan.hao.WinUtils;
import org.yifan.hao.swing.JswCustomWight;

import javax.swing.*;
import java.awt.*;

public class SwingSocketExample {

    public SwingSocketExample() {
    }

    public static void main(String[] args) {
        SwingSocketExample example = new SwingSocketExample();
        example.start();
//        example.startService();
    }

    private void start() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Swing Example");

        // 设置窗口大小
        int width = 800;
        int height = 400;
        jFrame.setSize(width, height);

        // 获取屏幕的尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // 计算窗口的位置使其居中显示
        int x = (screenWidth - width) / 2;
        int y = (screenHeight - height) / 2;
        jFrame.setLocation(x, y);

        // 使用默认的FlowLayout布局管理器
        jFrame.setLayout(new FlowLayout());

        // 添加组件到窗口
        JTextField edt = new JTextField(120);
        jFrame.add(edt);

        JLabel tv = new JLabel("这是一个文本显示控件");

        SocketUtils socketUtils = new SocketUtils(new SocketUtils.IServiceNotifyMsg() {
            @Override
            public void errMsg(Exception e, String errMsg) {
                e.printStackTrace();
                System.out.println("errMsg = " + errMsg);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(JswCustomWight.getJButton("启动服务端", e -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    socketUtils.startService(9090, new SocketUtils.IReceiverMsg() {
                        @Override
                        public void receiverMsg(String receiveMsg) {
                            System.out.println("接收到客户端信息: " + receiveMsg);
                        }
                    });
                }
            }).start();
        }));
        buttonPanel.add(JswCustomWight.getJButton("客户端连接", e -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    socketUtils.connService(WinUtils.getIpV4(), 9090, new SocketUtils.IReceiverMsg() {
                        @Override
                        public void receiverMsg(String receiveMsg) {
                            System.out.println("接收到服务端信息: " + receiveMsg);
                        }
                    });
                }
            }).start();
        }));
        buttonPanel.add(JswCustomWight.getJButton("服务端发送", e -> {
            String str = "服务端发送:" + DateUtil.formatCurrentDate(DateUtil.REGEX_DATE_TIME);
            String path = "E:/01/《激情焚烧》【NJ：小莫】.mp3";
            socketUtils.sendFileToClient(path);
//            socketUtils.sendMsgToClient(str);
        }));
        buttonPanel.add(JswCustomWight.getJButton("客户端发送", e -> {
            String str = "客户端发送:" + DateUtil.formatCurrentDate(DateUtil.REGEX_DATE_TIME);
            String path = "E:/01/《激情焚烧》【NJ：小莫】.mp3";
            socketUtils.sendFileToService(path);
//            socketUtils.sendMsgToClient(str);
        }));
        jFrame.add(buttonPanel);
        jFrame.add(tv);
        jFrame.setVisible(true);
    }

}
