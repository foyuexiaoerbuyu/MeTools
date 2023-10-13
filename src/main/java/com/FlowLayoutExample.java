package com;


import org.yifan.hao.swing.JswCustomWight;
import org.yifan.hao.swing.JswOnLongClickListener;

import javax.swing.*;
import java.awt.*;

public class FlowLayoutExample extends JFrame {
    public FlowLayoutExample() {
        // 创建一个 JPanel，并设置布局为流布局
        JPanel panel = new JPanel(new FlowLayout());

        // 创建三个按钮
        JButton button2 = new JButton("按钮2");
        JButton button3 = new JButton("按钮3");

        // 将按钮添加到面板中
        panel.add(JswCustomWight.getJButton("测试-1", new JswOnLongClickListener() {
            @Override
            public void onClick() {

            }
        }));
        panel.add(button2);
        panel.add(button3);

        // 将面板添加到窗口中
        add(panel);

        // 设置窗口属性
        setTitle("Swing 流布局示例");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setVisible(true);
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 100; i++) {
//            System.out.println("https://www.huapp.one/index.php/vod/search/page/" + i+"/wd/%E4%B9%B1%E4%BC%A6.html");
//        }
        SwingUtilities.invokeLater(() -> {
            new FlowLayoutExample();
        });
    }
}
