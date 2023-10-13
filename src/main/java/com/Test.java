package com;


import org.yifan.hao.WinUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        InetAddress localhost = InetAddress.getLocalHost();
        String localIP = localhost.getHostAddress();
        //空格按键监听
        //m2();
    }

    /**
     * 空格按键监听
     */
    private static void m2() {
        JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setTitle("Swing窗口");
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);

        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("空格键被按下");
                    // 在此处添加你想要执行的代码
                    Point mouseCurPos = WinUtils.getMouseCurPos();
                    int[] pos = WinUtils.getScreenCenterPos();
                    System.out.println("mouseCurPos = " + mouseCurPos.x + "  " + mouseCurPos.y + "  " + pos[0] + "  " + pos[1]);
                }
            }
        };

        frame.addKeyListener(keyListener);
        frame.setVisible(true);
    }

    public static List<String> discoverDevices(String subnet) {
        List<String> devices = new ArrayList<>();

        for (int i = 1; i <= 255; i++) {
            String host = subnet + "." + i;

            try {
                InetAddress address = InetAddress.getByName(host);

                if (address.isReachable(1000)) {
                    devices.add(host);
                }
            } catch (IOException e) {
                // 异常处理，可能由于超时或其他原因导致设备不可达
            }
        }

        return devices;
    }

    /**
     * 打开文件闪传
     */
    private static void m1() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String localIP = localhost.getHostAddress();
            String str1 = localIP.substring(0, localIP.lastIndexOf(".") + 1);
            System.out.println("str1 = " + str1);
            int port = 2333; // 目标主机端口号
            System.out.println("localIP = " + localIP);
            for (int i = 0; i < 255; i++) {
                int finalI = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String host = str1 + finalI;
                        try {
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress(host, port), 500);
                            System.out.println("可以正常浏览 " + host + ":" + port);
                            socket.close();
                            WinUtils.open("http://" + host + ":" + port);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}