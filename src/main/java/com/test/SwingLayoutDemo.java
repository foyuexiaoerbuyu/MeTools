package com.test;



import org.yifan.hao.swing.JswCustomWight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SwingLayoutDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swing Layout Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setSize(300, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JTextField textField = new JTextField();
            textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
            textField.addActionListener(e -> System.out.println("Enter pressed"));
            panel.add(textField);

            JPanel checkBoxPanel = new JPanel();
            JCheckBox saveCheckBox = JswCustomWight.getJCheckBox("保存", e -> {
                JCheckBox source = (JCheckBox) e.getSource();
                System.out.println("勾选状态: " + source.getText() + "  =  " + source.isSelected());
            });
            JCheckBox tagCheckBox = JswCustomWight.getJCheckBox("按标签搜索", e -> {
                JCheckBox source = (JCheckBox) e.getSource();
                System.out.println("勾选状态: " + source.getText() + "  =  " + source.isSelected());
            });
            JCheckBox contentCheckBox = JswCustomWight.getJCheckBox("按内容搜索", e -> {
                JCheckBox source = (JCheckBox) e.getSource();
                System.out.println("勾选状态: " + source.getText() + "  =  " + source.isSelected());
            });
            JCheckBox titleCheckBox = JswCustomWight.getJCheckBox("按标题(文件名)搜索", e -> {
                JCheckBox source = (JCheckBox) e.getSource();
                System.out.println("勾选状态: " + source.getText() + "  =  " + source.isSelected());
            });
            checkBoxPanel.add(saveCheckBox);
            checkBoxPanel.add(tagCheckBox);
            checkBoxPanel.add(contentCheckBox);
            checkBoxPanel.add(titleCheckBox);
            panel.add(checkBoxPanel);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            String[] buttonLabels = {"按钮1", "按钮2", "按钮3", "按钮3", "按钮3", "按钮3", "按钮3", "按钮3", "按钮3", "按钮3", "按钮3", "按钮3"};
            for (int i = 0; i < buttonLabels.length; i++) {
                JButton button = JswCustomWight.getJButton((buttonLabels[i]), e -> {

                });
                int index = i;
                button.addActionListener(e -> System.out.println("点击了按钮: " + (index + 1)));
                buttonPanel.add(button);
            }
            panel.add(buttonPanel);

            JList<String> list = new JList<>(new String[]{"项目1", "项目2", "项目3"});
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        System.out.println("单击了项目: " + list.getSelectedValue());
                    }else if (e.getClickCount() == 2) {
                        System.out.println("双击了项目: " + list.getSelectedValue());
                    }
                }
            });
            JScrollPane scrollPane = new JScrollPane(list);
            frame.add(panel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}