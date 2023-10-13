package com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GridLayoutExample extends JFrame {

    public GridLayoutExample() {
        // 设置窗口标题
        setTitle("GridLayout Example");

        // 创建组件
        JPanel mainPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        mainPanel.add(new JButton("Button 1"));
        mainPanel.add(new JButton("Button 2"));
        mainPanel.add(new JButton("Button 3"));
        mainPanel.add(new JButton("Button 4"));

        // 添加组件到窗口
        add(mainPanel, BorderLayout.CENTER);

        // 设置窗口大小和位置
        setSize(400, 400);
        setLocationRelativeTo(null);

        // 设置窗口可见
        setVisible(true);
    }

    public static void main(String[] args) {
//        new GridLayoutExample();
        JPanel gui = new JPanel(new BorderLayout());
        gui.setBorder(new EmptyBorder(2, 3, 2, 3));

        JPanel textPanel = new JPanel(new BorderLayout(5, 5));//构造具有组件之间指定间隙的边框布局。
        textPanel.add(new JScrollPane(new JTextArea("Top Text", 1, 20)), BorderLayout.PAGE_START);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        for (int i = 1; i <= 15; i++) {
            listModel.addElement("Item " + i);
        }
        JScrollPane scrollPane = new JScrollPane(list);
        textPanel.add(scrollPane, BorderLayout.CENTER);
//        textPanel.add(new JScrollPane(new JTextArea("Main Text", 10, 10)));
        gui.add(textPanel, BorderLayout.CENTER);

        JPanel buttonCenter = new JPanel(new GridBagLayout());
        buttonCenter.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 2, 5));
        buttonPanel.add(new JCheckBox("保存"));
        for (int ii = 1; ii < 6; ii++) {
            buttonPanel.add(new JButton("Button " + ii));
        }

        buttonCenter.add(buttonPanel);
        gui.add(buttonCenter, BorderLayout.LINE_END);

        JFrame f = new JFrame("Demo");
        f.add(gui);

        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        f.setLocationByPlatform(true);

        f.pack();

        f.setVisible(true);
    }
}