package com.panel.tab;

import com.chat1.ChatMsg;
import com.chat1.ChatWebSocketService;
import com.chat1.IChatServiceCallBack;
import com.other.Constant;
import com.other.QRCodeGenerator;
import org.yifan.hao.DateUtil;
import org.yifan.hao.FileUtils;
import org.yifan.hao.GsonUtils;
import org.yifan.hao.WinUtils;
import org.yifan.hao.swing.JswCustomWight;
import org.yifan.hao.swing.JswDialogUtils;
import org.yifan.hao.swing.JswOnLongClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ToolsTab3 implements IChatServiceCallBack {

    int hang1Y = 10, hang2Y = 45, hang3Y = 80, hang4Y = 115, hang5Y = 150, hang6Y = 185, hang7Y = 215, hang8Y = 245, hang9Y = 275;
    int lie1X = 10;
    private ChatWebSocketService chatWebSocket;
    private JLabel jLabel;
    /* 行高 */
    private int btnHeight = 30;
    /*行间距*/
    private int hSpace = 40;
    private JTextArea jtaAddress;

    /**
     * @param mainFrame 350*270
     */
    public Component getTab(JFrame mainFrame) {
        Container _ONE = new Container();
        String ipV4 = WinUtils.getIpV4() + ":8887";
        jtaAddress = JswCustomWight.getJTextArea(10, hang1Y, 285, btnHeight, ipV4);
        _ONE.add(jtaAddress);
//        List<String> ips = Arrays.asList(ipV4, "127.0.0.1");
//        JComboBox jtaAddress = CustomWight.getJComboBoxEdit(10, y01, 130, 20, ips,null);//适用于多个ip
        //String selectedItem = (String) jtaAddress.getSelectedItem();

        JButton btnGenQeCode = JswCustomWight.getJButton(300, hang1Y, 110, btnHeight, "生成二维码", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                WinUtils.setSysClipboardText(ipV4);
                System.out.println("btnGenQeCode = " + jtaAddress.getText());
                try {
                    QRCodeGenerator.showQrCode(jtaAddress.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        _ONE.add(btnGenQeCode);

        JTextArea jtaTextArea = JswCustomWight.getJTextArea(10, hang2Y, 285, 65);
        _ONE.add(JswCustomWight.getJScrollPane(jtaTextArea));
        jtaTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.isControlDown()) {
                        // Ctrl+Enter 按下，插入换行符
                        jtaTextArea.insert("\n", jtaTextArea.getCaretPosition());
                        System.out.println("Ctrl+Enter pressed");
                    } else {
                        // Enter 按下，不插入换行符
                        e.consume(); // 阻止默认的 Enter 行为（插入换行符）
                        if (chatWebSocket != null) {
                            chatWebSocket.sendMsgText(jtaTextArea.getText());
                            jLabel.setText(jtaTextArea.getText());
                            jtaTextArea.setText("");
                        }
                        System.out.println("Enter pressed");
                    }
                }
            }
        });
        JButton jBtnSendMsg = JswCustomWight.getJButton(300, hang2Y, 110, btnHeight, "剪切板发送", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                chatWebSocket.sendMsgText(WinUtils.getSysClipboardText());
            }
        });
        _ONE.add(jBtnSendMsg);
        JButton jBtnCopyNewMsg = JswCustomWight.getJButton(300, hang3Y, 110, btnHeight, "复制新消息", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                WinUtils.setSysClipboardText(jtaTextArea.getText());
            }
        });
        _ONE.add(jBtnCopyNewMsg);
        _ONE.add(JswCustomWight.getJButton(300, hang4Y, 110, btnHeight, "启动服务", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                startService(true);
            }
        }));
        _ONE.add(JswCustomWight.getJButton(300, hang5Y, 110, btnHeight, "停止服务", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                stopService(true);
            }
        }));
//        _ONE.add(JswCustomWight.getJButton(300, hang6Y, 110, btnHeight, "发送文件", new OnLongClickListener() {
//            @Override
//            public void onClick() {
//                WinUtils.getClipboardFilePath(filePath -> {
//                    if (chatWebSocket != null) chatWebSocket.sendFileToClient(filePath);
//                });
//            }
//        }));
        _ONE.add(JswCustomWight.getJButton(300, hang6Y, 110, btnHeight, "拉取文件", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                if (chatWebSocket != null) chatWebSocket.sendMsg(new ChatMsg("cm_pull_files", ChatMsg.MSG_TYPE_CMD));
            }
        }));

        jLabel = JswCustomWight.getFileDropLabel(10, hang4Y, 285, 110, "拖拽传文件",
                files -> {
                    StringBuilder sb = new StringBuilder("<html><body><p align=\"center\">");
                    for (File file : files) {
                        if (chatWebSocket != null) chatWebSocket.sendFileToClient(file);
                        sb.append(file.getName()).append("<br/>");
                    }
                    jLabel.setText(sb.append("</p></body></html>").toString());

                });
        _ONE.add(jLabel);
        try {
            BufferedImage image = QRCodeGenerator.generateQRCodeImage(ipV4);
            JLabel label = new JLabel(new ImageIcon(image));
            label.setBounds(0, hang7Y - 15, 300, 300);
            label.setText(ipV4);
            // 设置图片和文字的位置关系
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setHorizontalAlignment(JLabel.CENTER);
            // 添加鼠标事件监听器
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 在此处执行点击事件操作
                    WinUtils.setSysClipboardText(ipV4);
                    JOptionPane.showMessageDialog(mainFrame, "已复制二维码信息!");
                }
            });
            _ONE.add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _ONE;
    }

    public void stopService(boolean isShowDialog) {
        try {
            if (chatWebSocket != null) {
                chatWebSocket.stopService();
            }
            if (isShowDialog) {
                JswDialogUtils.showAutoCloseDialog("服务已停止", 1000);
            }
        } catch (InterruptedException e) {
            if (isShowDialog) {
                JswDialogUtils.showAutoCloseDialog("服务停止失败:" + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    public void startService(boolean isShowDialog) {
        try {
            if (chatWebSocket != null) {
                chatWebSocket.stopService();
            }
            int port = Integer.parseInt(jtaAddress.getText().split(":")[1]);
            chatWebSocket = new ChatWebSocketService(port, ToolsTab3.this);
            chatWebSocket.start();
//                    chatWebSocket = ChatWebSocket.startService(port);
            if (isShowDialog) {
                jLabel.setText("服务启动成功");
//                DialogUtils.showAutoCloseDialog("服务启动成功", 500);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Address already in use: bind")) {
                System.out.println("端口被占用.");
            }
            JswDialogUtils.showAutoCloseDialog("服务启动异常,端口被占用!");
            e.printStackTrace();
        }
    }

    @Override
    public void errMsg(String errMsg) {
        System.out.println("errMsg = " + errMsg);
        JswDialogUtils.showAutoCloseDialog(errMsg);
    }

    @Override
    public void receiveMsg(String serviceMsg) {
        System.out.println("serviceMsg = " + serviceMsg);
        ChatMsg chatMsg = GsonUtils.fromJson(serviceMsg, ChatMsg.class);
        if (chatMsg.getMsgType() == ChatMsg.MSG_TYPE_FILE) {
            FileUtils.writeFile(Constant.PATH_FILE_DIR + chatMsg.getFileName(), chatMsg.getFileData(), true);
            WinUtils.opeDir(Constant.PATH_FILE_DIR);
            JswDialogUtils.showAutoCloseDialog("接收完毕");
        } else {
            System.out.println("serviceMsg = " + chatMsg.getMsgContent());
            jLabel.setText(DateUtil.formatCurrentDate(DateUtil.REGEX_DATE_TIME) + "\n   " + chatMsg.getMsgContent() + "\n");
        }
    }
}
