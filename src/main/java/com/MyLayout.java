package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class MyLayout extends JFrame {
    public MyLayout() {
        // 设置窗口标题
        super("搜索窗口");

        // 创建输入框、下拉框和搜索按钮
        JTextField textField = new JTextField();
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"选项1", "选项2", "选项3"});
        JButton searchButton = new JButton("搜索");

        // 创建列表模型并添加带有链接的列表项
        DefaultListModel<Object> listModel = new DefaultListModel<>();
        listModel.addElement("列表项 1");
        listModel.addElement("<html><a href='https://www.baidu.com'>百度一下</a></html>");
        listModel.addElement("列表项 3");
        JList<Object> list = new JList<>(listModel);

        // 将组件添加到顶部面板中
        Box topBox = Box.createHorizontalBox();
        topBox.add(Box.createHorizontalStrut(10));
        topBox.add(textField);
        topBox.add(Box.createHorizontalStrut(10));
        topBox.add(comboBox);
        topBox.add(Box.createHorizontalStrut(10));
        topBox.add(searchButton);
        topBox.add(Box.createHorizontalGlue());

        // 创建列表容器并添加到窗口中
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(topBox, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 为列表添加鼠标事件监听器
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // 双击事件
                    int index = list.getSelectedIndex();
                    Object selected = list.getModel().getElementAt(index);
                    if (selected.toString().contains("<a href=")) {
                        String url = selected.toString().split("'")[1];
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // 设置窗口大小并显示窗口
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

    }

    public static void main(String[] args) {
        new MyLayout();
    }
}

