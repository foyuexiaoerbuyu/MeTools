package com.panel;

import com.chat1.ChatWebSocketService;
import com.chat1.IChatServiceCallBack;
import com.other.QRCodeGenerator;
import org.yifan.hao.WinUtils;
import org.yifan.hao.swing.JswCustomWight;
import org.yifan.hao.swing.JswDialogUtils;
import org.yifan.hao.swing.JswOnLongClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class TabTest implements IChatServiceCallBack {

    int hang1Y = 10, hang2Y = 45, hang3Y = 80, hang4Y = 115, hang5Y = 150, hang6Y = 185, hang7Y = 215;
    int lie1X = 10;
    private ChatWebSocketService chatWebSocket;
    private JLabel jLabel;
    /* 行高 */
    private int btnHeight = 30;
    /*行间距*/
    private int hSpace = 40;

    /**
     * @param mainFrame 350*270
     */
    public Component getTab(JFrame mainFrame) {
        Container _ONE = new Container();
        String ipV4 = WinUtils.getIpV4() + ":8887";
        JTextArea jtaAddress = JswCustomWight.getJTextArea(10, hang1Y, 285, btnHeight, ipV4);
        _ONE.add(jtaAddress);
//        List<String> ips = Arrays.asList(ipV4, "127.0.0.1");
//        JComboBox jtaAddress = CustomWight.getJComboBoxEdit(10, y01, 130, 20, ips,null);//适用于多个ip
        //String selectedItem = (String) jtaAddress.getSelectedItem();

        JButton btnGenQeCode = JswCustomWight.getJButton(300, hang1Y, 110, btnHeight, "生成二维码", new JswOnLongClickListener() {
            @Override
            public void onClick() {
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
                        chatWebSocket.sendMsgText(jtaTextArea.getText());
                        jLabel.setText(jtaTextArea.getText());
                        jtaTextArea.setText("");
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
        JButton btnStartService = JswCustomWight.getJButton(300, hang4Y, 110, btnHeight, "启动服务", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                try {
                    if (chatWebSocket != null) {
                        chatWebSocket.stopService();
                    }
                    int port = Integer.parseInt(jtaAddress.getText().split(":")[1]);
                    chatWebSocket = new ChatWebSocketService(port, TabTest.this);
                    chatWebSocket.start();
//                    chatWebSocket = ChatWebSocket.startService(port);
                } catch (Exception e) {
                    if (e.getMessage().contains("Address already in use: bind")) {
                        System.out.println("端口被占用");
                    }
                    e.printStackTrace();
                }
            }
        });
        _ONE.add(btnStartService);
        JButton jBtnStopService = JswCustomWight.getJButton(300, hang5Y, 110, btnHeight, "停止服务", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                try {
                    if (chatWebSocket != null) {
                        chatWebSocket.stopService();
                    }
                    JswDialogUtils.showAutoCloseDialog("服务已停止", 1000);
                } catch (InterruptedException e) {
                    JswDialogUtils.showAutoCloseDialog("服务停止失败:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        _ONE.add(jBtnStopService);

        jLabel = JswCustomWight.getFileDropLabel(10, hang4Y, 285, 110, "拖拽传文件",
                files -> {
                    StringBuilder sb = new StringBuilder("<html><body><p align=\"center\">");
                    for (File file : files) {
                        if (chatWebSocket != null) chatWebSocket.sendFileToClient(files.get(0));
                        sb.append(file.getName()).append("<br/>");
                    }
                    jLabel.setText(sb.append("</p></body></html>").toString());

                });
        _ONE.add(jLabel);
        return _ONE;
    }

    @Override
    public void errMsg(String errMsg) {
        System.out.println("errMsg = " + errMsg);
        JswDialogUtils.showAutoCloseDialog(errMsg);
    }

    @Override
    public void receiveMsg(String serviceMsg) {
        System.out.println("serviceMsg = " + serviceMsg);
        jLabel.setText(serviceMsg);
    }
}
