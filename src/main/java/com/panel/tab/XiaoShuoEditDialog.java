package com.panel.tab;


import org.yifan.hao.FileUtils;
import org.yifan.hao.WinUtils;
import org.yifan.hao.swing.JswCustomWight;
import org.yifan.hao.swing.JswDialogUtils;
import org.yifan.hao.swing.JswOnLongClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * 两个编辑框
 */
public class XiaoShuoEditDialog extends JDialog {

    public XiaoShuoEditDialog(JFrame parent, String text) {
        super(parent, "编辑对话框", true);
        setLocationRelativeTo(null); // 设置弹框居中显示

        // 创建一个文本编辑框
        JTextArea textAreaTitle = new JTextArea();
        JScrollPane scrollPane1 = new JScrollPane(textAreaTitle);
        getContentPane().add(scrollPane1, BorderLayout.NORTH);
        // 创建一个文本编辑框
        JTextArea textAreaContent = new JTextArea();
        textAreaContent.setText(text);
        JScrollPane scrollPane = new JScrollPane(textAreaContent);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // 添加三个按钮
        JButton button1 = new JButton("生成文件标题");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaTitle.setText("D:/" + FileUtils.clearFileName(WinUtils.getSysClipboardText()) + ".txt");
//                if (iCallBack != null) iCallBack.str(textAreaTitle.getText(), textAreaContent.getText());
//                String text = WinUtils.getSysClipboardText();
//                FileUtils.writeFile(Constant.path_tmp_clipboard_1, text);
//                System.out.println("文本内容为：\n" + textAreaContent.getText());
//                System.out.println("剪切板内容为：\n" + text);
//                JOptionPane.showMessageDialog(parent, "保存剪切板内容成功", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(button1);

        JButton button2 = new JButton("写小说");
        button2.setToolTipText("长按弹框设置截取索引");
        button2.addMouseListener(new JswOnLongClickListener() {
            private String indexStr;

            @Override
            public void onClick() {
                String sysClipboardText = WinUtils.getSysClipboardText();
                if (indexStr != null && indexStr.contains("@")) {
                    String[] split = indexStr.split("@");
                    sysClipboardText = sysClipboardText.substring(sysClipboardText.indexOf(split[0]), sysClipboardText.lastIndexOf(split[1]));
                }
                textAreaContent.append(sysClipboardText);
                FileUtils.writeFile(textAreaTitle.getText(), textAreaContent.getText() + "\n", false);
            }

            @Override
            public void onLongClick() {
                JswDialogUtils.showEditDialogSimple("截取索引", "开头结尾用逗号(@)分割", "@", content -> {
                    indexStr = content;
                });
            }
        });
        buttonPanel.add(button2);

        buttonPanel.add(JswCustomWight.getJButtonMargin("打开文件", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                WinUtils.opeDir(textAreaTitle.getText());
            }
        }));
        buttonPanel.add(JswCustomWight.getJButtonMargin("自动化", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                Robot r = null;
                try {
                    r = new Robot();
                    WinUtils.keyPressWithAlt(r, KeyEvent.VK_TAB);
                    r.delay(1000);
                    WinUtils.keyPressWithCtrl(r, KeyEvent.VK_A);
                    r.delay(1000);
                    WinUtils.keyPressWithCtrl(r, KeyEvent.VK_C);
                    r.delay(1000);
                    WinUtils.keyPressWithAlt(r, KeyEvent.VK_TAB);
//                    WinUtils.keyPressEnter(r);
//                    r.delay(1000);
//                    WinUtils.keyPressWithAlt(r, KeyEvent.VK_TAB);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        }));
//        buttonPanel.setSize(900,1200);
//        buttonPanel.setPreferredSize(new Dimension(900, 1200));
//        pack();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // 设置对话框大小
        setSize(400, 300);
    }

    public static void main(String[] args) {
        XiaoShuoEditDialog dialog = new XiaoShuoEditDialog(null, "");
        dialog.setLocationRelativeTo(null); // 设置主窗口居中显示
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
    }

    public interface ICallBack {

        void str(String title, String content);
    }
}