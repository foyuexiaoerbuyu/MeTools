package com.panel.tab;

import com.other.Constant;
import com.other.DialogUtils;






import com.zhihu.other.CommUtils;
import org.yifan.hao.FileUtils;
import org.yifan.hao.StringUtil;
import org.yifan.hao.WinUtils;
import org.yifan.hao.swing.JswCustomWight;
import org.yifan.hao.swing.JswOnLongClickListener;
import org.yifan.hao.swing.JswPopupMenus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ToolsTabTest {
    public Component getTab(JFrame frame) {
//        ConfigBen config = ConfigUtils.getConfig();

        Container container = new Container();
        JTextArea jTextAreaDrag = JswCustomWight.getJTextAreaDrag(5, 5, 400, 60, new JswCustomWight.IDragCallBack() {
            @Override
            public void dragCallBack(String path) {
                System.out.println("path = " + path);
            }
        });
        jTextAreaDrag.setComponentPopupMenu(
                new JswPopupMenus()
                        .addMenu("复制", menuName -> WinUtils.setSysClipboardText(jTextAreaDrag.getText()))
                        .addMenu("粘贴", menuName -> jTextAreaDrag.setText(WinUtils.getSysClipboardText()))
        );
        container.add(jTextAreaDrag);
        // 设置流布局
//        container.setLayout(new FlowLayout(FlowLayout.LEFT));
//        // 将按钮添加到容器中
//        container.add(JswCustomWight.getJButton("测试", new OnLongClickListener() { @Override public void onClick() {} }));
        JPanel flowLayoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 创建一个JPanel，并为其设置流布局
        flowLayoutPanel.setBounds(5, 70, 400, 300);
        container.add(flowLayoutPanel);
        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("净化文件名", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String clipboardString = WinUtils.getSysClipboardText();
                if (clipboardString != null)
                    WinUtils.setSysClipboardText(FileUtils.clearFileName(clipboardString, true));
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("文件开头补0", new JswOnLongClickListener() {
            @Override
            public void onClick() {
//                String[] files = jTextAreaDrag.getText().split("\n");
//                for (String filePath : files) {
//                    FileUtils.reNameSelf(filePath, "0" + FileUtils.getFileName(filePath));
//                    System.out.println("filePath = " + filePath);
//                }
//                jTextAreaDrag.setText("");
                WinUtils.getClipboardFilePath(new WinUtils.IReadLin() {
                    @Override
                    public void readLin(String filePath) {
                        System.out.println("lin = " + filePath);
                        if (FileUtils.isFileExist(filePath)) {
                            FileUtils.reNameSelf(filePath, "0" + FileUtils.getFileName(filePath));
                        }
                    }

                    @Override
                    public void onException(Exception exception) {
                        exception.printStackTrace();
                        DialogUtils.showAutoCloseDialog("重命名异常: " + exception.getMessage());
                    }

                    @Override
                    public void end() {
                        DialogUtils.showAutoCloseDialog("重命名成功");
                    }
                });
            }
        }));
//        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("去回车", event -> {
//            String clipboardString = WinUtils.getSysClipboardText();
//            if (clipboardString != null) WinUtils.setSysClipboardText(clipboardString.replace("\n", ""));
//        }));
        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("剪切板写文件", "长按打开剪切板文件", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String content = WinUtils.getSysClipboardText() + "\n";
                StringBuilder sb = new StringBuilder();
                StringUtil.readStrByLins(content, lin -> {
                    if (lin.trim().length() > 0) {
                        sb.append(lin.trim()).append("\n");
                    }
                });
                FileUtils.writeFile(Constant.CUT_TMP_PATH, sb.toString(), true);
            }

            @Override
            public void onLongClick() {
                WinUtils.opeDir(Constant.CUT_TMP_PATH);
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("读取剪切板文件前五行", new JswOnLongClickListener() {

            private int readLineNum = 5;

            @Override
            public void onClick() {
                CommUtils.readFileTop5LineAndSave(Constant.CUT_TMP_PATH, readLineNum);
                if (WinUtils.getSysClipboardText().trim().length() < 10) {
                    jTextAreaDrag.setText("读取剪切板文件前五行_临时剪切板内容为空");
                    return;
                }
                jTextAreaDrag.setText("");
                Robot r = null;
                try {
                    r = new Robot();
                    WinUtils.keyPressWithAlt(r, KeyEvent.VK_TAB);
                    r.delay(1000);
                    WinUtils.keyPressWithCtrl(r, KeyEvent.VK_V);
                    r.delay(1000);
                    WinUtils.keyPressEnter(r);
                    r.delay(1000);
                    WinUtils.keyPressWithAlt(r, KeyEvent.VK_TAB);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLongClick() {
                readLineNum = Integer.parseInt(DialogUtils.showEditDialogSimple("", "", "5"));
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("排除剪贴板特定开头行", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String s = DialogUtils.showEditDialogSimple("", "", "");
                StringBuilder sb = new StringBuilder();
                StringUtil.readStrByLins(WinUtils.getSysClipboardText(), lin -> {
                    if (lin.trim().length() > 0 && !lin.startsWith(s)) {
                        sb.append(lin.trim()).append("\n");
                    }
                });
                WinUtils.setSysClipboardText(sb.toString());
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("排除剪贴板小于特定长度行", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String s = DialogUtils.showEditDialogSimple("", "", "50");
                StringBuilder sb = new StringBuilder();
                StringUtil.readStrByLins(WinUtils.getSysClipboardText(), lin -> {
                    if (lin.length() > Integer.parseInt(s)) {
                        sb.append(lin.trim()).append("\n");
                    }
                });
                WinUtils.setSysClipboardText(sb.toString());
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("重置大小", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                frame.setSize(frame.getWidth() == 420 ? 900 : 420, 570);
            }
        }));
        JButton jButton = JswCustomWight.getJButtonMargin("测试", new JswOnLongClickListener() {
            @Override
            public void onClick() {
            }
        });
        // Swing JLabel组件的设置工具提示
        jButton.setToolTipText("这是测试按钮这是测试按钮这是测试按钮这是测试按钮这是测试按钮这是测试按钮这是测试按钮这是测试按钮");
        flowLayoutPanel.add(jButton);
//        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("获取鼠标位置", e -> {
//            WinUtils.getCurPostion();
//        }));
//        flowLayoutPanel.add(JswCustomWight.getJButtonMargin("自动化", e -> {
//            int[] mouseLocationXY = RobotUtils.getMouseLocationXY();
//            String fileName = "D:/新文件 3.txt";
//            CommUtils.readFileTop5LineAndSave(fileName);
//            RobotUtils.click(1000, 1034);
//            RobotUtils.paste();
//            RobotUtils.pressEnter();
//            RobotUtils.moveMouse(1000, 350);
//        }));
        return container;
    }
}
