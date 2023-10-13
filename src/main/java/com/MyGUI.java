package com;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyGUI extends JFrame {

    public MyGUI() {
        setTitle("My GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        /* 第一行：输入框、分类下拉框和搜索按钮 */
        JPanel topPanel = new JPanel(new BorderLayout());
        JTextField textField = new JTextField();
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
        JButton searchButton = new JButton("Search");
        topPanel.add(textField, BorderLayout.CENTER);
        topPanel.add(comboBox, BorderLayout.WEST);
        topPanel.add(searchButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH); // 添加到顶部

        /* 第二行：复选框和动态添加的按钮 */
        JPanel middlePanel = new JPanel(new FlowLayout());
        JCheckBox checkBox = new JCheckBox("Checkbox");
        checkBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 移除边距
        middlePanel.add(checkBox);
        String jsonStr = "[\"Button A\",\"Button B\",\"Button C\",\"Button D\",\"Button E\"]";
        String[] buttons = jsonStr.replaceAll("\\[\"", "").replaceAll("\"\\]", "").split("\",\"");
        for (String button : buttons) {
            JButton dynamicButton = new JButton(button);
            middlePanel.add(dynamicButton);

            // 为每个动态按钮添加点击事件
            dynamicButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println(button + "被点击了！");
                }
            });
        }
        add(middlePanel, BorderLayout.CENTER); // 添加到中间

        /* 为下拉框选项添加点击事件 */
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String item = comboBox.getItemAt(i);
            comboBox.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JButton button = new JButton(value.toString());
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.out.println(item + "被点击了！");
                        }
                    });
                    return button;
                }
            });
        }

        /* 第三行：列表 */
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        for (int i = 1; i <= 20; i++) {
            listModel.addElement("Item " + i);
        }
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.SOUTH);

        /* 为复选框、搜索按钮和列表项添加点击事件 */
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("复选框被点击了！");
            }
        });
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("搜索按钮被点击了！");
            }
        });
        list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedItem = list.getSelectedValue();
                    System.out.println(selectedItem + "被点击了！");
                }
            }
        });

        /* 为输入框添加输入监听器 */
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            public void removeUpdate(DocumentEvent e) {
                update();
            }

            public void insertUpdate(DocumentEvent e) {
                update();
            }

            public void update() {
                System.out.println(textField.getText());
            }
        });

        /* 设置内边距和窗口大小并显示 */
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyGUI();
            }
        });
    }

}
