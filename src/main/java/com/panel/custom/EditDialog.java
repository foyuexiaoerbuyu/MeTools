package com.panel.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 两个编辑框
 */
public class EditDialog extends JDialog {
    private ICallBack iCallBack;

    public interface ICallBack {

        void str(String title, String content);
    }

    public EditDialog(JFrame parent, String text, ICallBack iCallBack) {
        this.iCallBack = iCallBack;
        new EditDialog(parent, text);
    }

    public EditDialog(JFrame parent, String text) {
        super(parent, "编辑对话框", true);
        setLocationRelativeTo(null); // 设置弹框居中显示

        // 创建一个文本编辑框
        JTextArea textAreaTitle = new JTextArea();
//        textAreaTitle.setText(text);
        JScrollPane scrollPane1 = new JScrollPane(textAreaTitle);
        getContentPane().add(scrollPane1, BorderLayout.NORTH);
        // 创建一个文本编辑框
        JTextArea textAreaContent = new JTextArea();
        textAreaContent.setText(text);
        JScrollPane scrollPane = new JScrollPane(textAreaContent);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // 添加三个按钮
        JButton button1 = new JButton("按钮1");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//                if (iCallBack != null) iCallBack.str(textAreaTitle.getText(), textAreaContent.getText());
//                String text = WinUtils.getSysClipboardText();
//                FileUtils.writeFile(Constant.path_tmp_clipboard_1, text);
//                System.out.println("文本内容为：\n" + textAreaContent.getText());
//                System.out.println("剪切板内容为：\n" + text);
//                JOptionPane.showMessageDialog(parent, "保存剪切板内容成功", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(button1);

        JButton button2 = new JButton("按钮2");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iCallBack != null) iCallBack.str(textAreaTitle.getText(), textAreaContent.getText());
                // 按钮2点击事件处理
//                String text = textAreaContent.getText();
//                System.out.println("文本内容为：\n" + text);
//                FileUtils.writeFile(Constant.path_tmp_clipboard_1, text);
//                JOptionPane.showMessageDialog(parent, "保存成功", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(button2);

//        JButton button3 = new JButton("按钮3");
//        button3.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // 按钮3点击事件处理
//                System.out.println("按钮3被点击，文本内容为：\n" + textArea.getText());
//            }
//        });
//        buttonPanel.add(button3);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // 添加关闭对话框监听器
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (iCallBack != null) iCallBack.str(textAreaTitle.getText(), textAreaContent.getText());
                System.out.println("对话框关闭");
                dispose();
            }
        });

        // 设置对话框大小
        setSize(400, 300);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("测试");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null); // 设置主窗口居中显示

        JButton button = new JButton("打开编辑对话框");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditDialog dialog = new EditDialog(null, "");
                dialog.setVisible(true);
            }
        });

        frame.getContentPane().add(button, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}