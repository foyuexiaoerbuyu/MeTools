package com.panel.tab;

import com.other.CommAdbs;
import com.other.Constant;
import com.other.QRCodeGenerator;
import com.panel.custom.EditDialog;
import com.panel.custom.ToolsTabUtils;
import com.translate.TranslateDemo;
import com.zhihu.other.CommUtils;
import org.yifan.hao.AdbUtils;
import org.yifan.hao.FileUtils;
import org.yifan.hao.StringUtil;
import org.yifan.hao.WinUtils;
import org.yifan.hao.swing.JswCustomWight;
import org.yifan.hao.swing.JswDialogUtils;
import org.yifan.hao.swing.JswOnLongClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具页面
 */
public class ToolsTab1 {
    public static int hSpace = 30;
    public static boolean postionTag = false;

    //    private static JLabel showContent;
    private static JTextArea jtCmdLins;
    private static Container container;
    private static JLabel preViewImgLabel;

    public static Component getToolsTab(JFrame frame) {

        int hang1Y = 10, hang2Y = 45, hang3Y = 80, hang4Y = 115, hang5Y = 150, hang6Y = 185, hang7Y = 215;
        int height = 30;
        JButton butterknifeBtn = JswCustomWight.getJButton(210, hang1Y, 100, height, "绑定控件", e -> {
            try {
                AdbUtils.fvbIdToClipboard();
            } catch (Exception ioException) {
                ioException.printStackTrace();
                showMessageDialog(ioException.getMessage());
            }
        });
        butterknifeBtn.setToolTipText("绑定butterknife控件和fvb");


        JButton qrCodeBtn = JswCustomWight.getJButton(315, hang1Y, 100, 30, "生成二维码", e -> {
            String text = WinUtils.getSysClipboardText();
            if (text.length() == 0) {
                JOptionPane.showMessageDialog(frame, "剪切板为空", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                QRCodeGenerator.showQrCode(text);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "生成二维码异常: " + ex.getMessage(), "异常", JOptionPane.ERROR_MESSAGE);
            }
        });

        container = new Container();
        container.add(JswCustomWight.getJButton(10, hang1Y, 125, height, "批量打开网页", e -> {
            try {
                batchOpenUrl();
            } catch (Exception ioException) {
                ioException.printStackTrace();
                showMessageDialog(ioException.getMessage());
            }
        }));
        container.add(JswCustomWight.getJButton(139, hang1Y, 65, height, "翻译", e -> {
            try {
                String str = TranslateDemo.startTranslators(WinUtils.getSysClipboardText());
                jtCmdLins.setText(str);
                JswDialogUtils.showAutoCloseDialog("已翻译", 500);
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
                JswDialogUtils.show("异常: " + noSuchAlgorithmException.getMessage());
            }
        }));
        container.add(qrCodeBtn);
        container.add(butterknifeBtn);
        container.add(JswCustomWight.getJLabel(15, hang2Y, 120, height, "执行间隔(毫秒):"));
        JTextField tfIntervalTime = JswCustomWight.getJTextField(140, hang2Y, 60, height, "500");
        container.add(tfIntervalTime);
        container.add(JswCustomWight.getJButton(210, hang2Y, 100, height, "触摸位置", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postionTag = !postionTag;
                AdbUtils.offTachPostion(postionTag);
            }
        }));
        container.add(JswCustomWight.getJButton(315, hang2Y, 100, height, "顶部Acty", e -> {
//            String content = AdbUtils.exeCmd("adb shell dumpsys window w |findstr \\/ |findstr name= ").split("name=")[1].replace(")", "");
            String content = AdbUtils.getTopAppActyName();
//            String content = AdbUtils.getTopAppPack2ActyName();
            WinUtils.setSysClipboardText(content);
            print(content);
        }));


        jtCmdLins = JswCustomWight.getJTextAreaMouse(15, hang3Y, 295, 68,
                "//返回 adb shell input keyevent 4\n" +
                        "//点击 adb shell input tap X Y\n" +
                        "//tab键 adb shell input keyevent KEYCODE_TAB\n" +
                        "//滑动 adb shell input swipe satrtX satrtY endX endY duration\n" +
                        "//输入 adb shell input text \"内容不能是中文\"", new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            String selectedText = jtCmdLins.getSelectedText();
                            if (selectedText != null && !selectedText.isEmpty()) {
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(selectedText), null);
                                System.out.println("已复制选中的文字: " + selectedText);
                            }
                        }
                    }
                });
        JScrollPane jsCmdLins = JswCustomWight.getJScrollPane(jtCmdLins);

        container.add(jsCmdLins);
//        _ONE.add(cmdLins);//adb命令框
        container.add(JswCustomWight.getJButton(315, hang3Y, 100, height, "执行", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exeAdbs(tfIntervalTime);
            }
        }));
        container.add(JswCustomWight.getJButton(315, hang4Y, 100, height, "复制", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //选中复制或复制全部
                String selectedText = jtCmdLins.getSelectedText();
                if (selectedText == null || selectedText.isEmpty()) {
                    selectedText = jtCmdLins.getText();
                }
                StringSelection stringSelection = new StringSelection(selectedText);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        }));

        container.add(JswCustomWight.getJButton(15, hang5Y, 90, height, "去空格回车", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WinUtils.setSysClipboardText(WinUtils.getSysClipboardText().replaceAll("\\s", ""));
                // 只去除回车
//                WinUtils.setSysClipboardText(WinUtils.getSysClipboardText().replaceAll("\\n", ""));
            }
        }));
        container.add(JswCustomWight.getJButton(110, hang5Y, 90, height, "获取剪切板", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                AdbUtils.exeCmd("adb shell am start cn.mvp/.MainActivity");
                String sysClipboardText = AdbUtils.getSysClipboardText();
                System.out.println("获取到的数据: " + sysClipboardText);
                //Broadcasting: Intent { act=clipper.get mIsPicForceMode=0 flg=0x400000 } Broadcast completed: result=-1, data="测试"
                sysClipboardText = sysClipboardText.substring(sysClipboardText.lastIndexOf("=")).replace("\"", "");
                WinUtils.setSysClipboardText(sysClipboardText.replace("=", ""));
            }

            @Override
            public void onLongClick() {
                AdbUtils.exeCmd("adb shell am start cn.mvp/.MainActivity");
                AdbUtils.sendSysClipboardText(WinUtils.getSysClipboardText(), false);
            }
        }));
        container.add(JswCustomWight.getJButton(210, hang5Y, 90, height, "剪切板输入", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String sysClipboardText = WinUtils.getSysClipboardText();
                System.out.println("sysClipboardText = " + sysClipboardText);
                String regex = "^(http|https)://[a-zA-Z0-9\\-\\.]+(:\\d+)?(/\\S*)?$";
                Matcher matcher = Pattern.compile(regex).matcher(sysClipboardText);
                if (matcher.matches()) {
                    AdbUtils.clickBack();
                }
                AdbUtils.inutText(sysClipboardText, false);
            }

            @Override
            public void onLongClick() {
                AdbUtils.exeCmd("adb shell am start cn.mvp/.MainActivity");
                AdbUtils.sendSysClipboardText(WinUtils.getSysClipboardText(), true);
            }
        }));
        container.add(JswCustomWight.getJButton(315, hang5Y, 100, height, "截图", e -> {
            String fileName = System.currentTimeMillis() + ".png";
            String filePath = Constant.PATH_CACHE_DIR + fileName;
            WinUtils.exeCmd("adb shell screencap -p /sdcard/" + fileName,
                    "adb pull /sdcard/" + fileName + " " + filePath,
                    "adb shell rm /sdcard/" + fileName,
                    "mspaint " + filePath);
        }));//adb命令框

        container.add(JswCustomWight.getJLabel(15, hang6Y, 120, height, "其他命令:"));
        List<String> commAdbs = CommAdbs.getCommAdbsName();
        JComboBox<String> jComboBox = JswCustomWight.getJComboBox(110, hang6Y, 195, height, commAdbs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommAdbs.exeAdb(e, ToolsTab1::print);
            }
        });
        container.add(jComboBox);
        container.add(JswCustomWight.getJButton(315, hang6Y, 100, height, "生成文件", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String filePath = Constant.PATH_CACHE_DIR;
                WinUtils.sysClipboardTextToFile(filePath);
            }

            @Override
            public void onLongClick() {
//                ZhihuUtils.setAdbs(jtCmdLins.getText());
            }
        }));
//        showContent = CustomWight.getJLabel(15, hang7Y, 200, height, "返回数据:");
        JPanel flowLayoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 创建一个JPanel，并为其设置流布局
        // 添加几个按钮到流布局JPanel中
//        flowLayoutPanel.add(CustomWight.getJButton("新增", new OnLongClickListener() {
//            @Override
//            public void onClick() {
////                ToolsTabUtils.m1();
//                ConfigBen config = ConfigUtils.getConfig();
//                for (ConfigBen.CusBtnsDTO cusBtn : config.getCusBtns()) {
//                    cusBtn.setId(System.currentTimeMillis());
////                    cusBtn.setName();
//                }
//            }
//
//            @Override
//            public void onLongClick() {
//                super.onLongClick();
//            }
//        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("打印多个参数", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                ToolsTabUtils.m1();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("ImageView", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                ToolsTabUtils.mW0();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("TextView", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                ToolsTabUtils.mW1();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("AppCompatTextView", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.mW2();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("LinearLayout", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.mW3();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("RecyclerView", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.mW4();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("列表弹框", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.mW5();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("单选列表弹框", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.mW6();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("多选列表弹框", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.mW7();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("约束布局属性", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                ToolsTabUtils.m7();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("ChildLog", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.m5();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("圆形背景", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                ToolsTabUtils.m6();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("背景(颜色)选择器", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                ToolsTabUtils.m2();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("复选框样式", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                ToolsTabUtils.m9();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("圆角白底灰边", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.m3();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("文字颜色选择", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.m4();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("按下抬起背景", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.m11();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("谷歌流布局", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.m12();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("剪切板小写空格替换下划线", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.m13();
                WinUtils.setSysClipboardText(CommUtils.convert(WinUtils.getSysClipboardText()));
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("去除换行", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                String clipboardString = WinUtils.getSysClipboardText();
                if (clipboardString != null) WinUtils.setSysClipboardText(clipboardString.replace("\n", ""));
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("常用依赖", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                ToolsTabUtils.m13();
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("去除剪切板换行", new JswOnLongClickListener() {

            @Override
            public void onClick() {
                WinUtils.setSysClipboardText(WinUtils.getSysClipboardText().replace("\n", ""));
            }
        }));
        flowLayoutPanel.add(JswCustomWight.getJButton("临时剪切1", new JswOnLongClickListener() {

            @Override
            public void onLongClick() {
                String text = FileUtils.readFile(Constant.path_tmp_clipboard_1);
                EditDialog dialog = new EditDialog(frame, text);
                dialog.show();
//                dialog.setVisible(true);
            }

            @Override
            public void onClick() {
                String text = FileUtils.readFile(Constant.path_tmp_clipboard_1);
                WinUtils.setSysClipboardText(text);
                JswDialogUtils.showAutoCloseDialog("复制成功");
            }
        }));
        flowLayoutPanel.setBounds(0, hang7Y, 415, 300);
        container.add(flowLayoutPanel);

        container.add(JswCustomWight.getJTextField(425, 10, 430, 20, Constant.str_pull_file, new JswCustomWight.IJTextFieldListener() {
            @Override
            public void focusGained(JTextField jTextField) {
                if (jTextField.getText().equals(Constant.str_pull_file)) {
                    jTextField.setText("");
                }
            }

            @Override
            public void focusLost(JTextField jTextField) {
                if (jTextField.getText().trim().equals("")) {
                    jTextField.setText(Constant.str_pull_file);
                }
            }

            @Override
            public void actionPerformed(ActionEvent e, JTextField jTextField) {

            }
        }));

        JComboBox<String> pathJcb = JswCustomWight.getJComboBoxEdit(425, 30, 170, 20, Arrays.asList("公共路径", "内部路径", "私有路径"), null);
        container.add(pathJcb);
        JComboBox<String> jComboBoxEdit = JswCustomWight.getJComboBoxEdit(600, 30, 150, 20, Arrays.asList("Download", "files", "cache"), null);
        container.add(jComboBoxEdit);
        container.add(JswCustomWight.getJButton(755, 30, 100, 20, "生成命令", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                //adb pull /sdcard/Android/data/cn.mvp/files
                System.out.println("点击索引: " + pathJcb.getSelectedIndex());
                String adb = "";
                if (pathJcb.getSelectedIndex() == 1) {
                    // TODO: 2023/9/6 最私有目录有可能不行
//                    adb += "adb pull /sdcard/" + jComboBoxEdit.getSelectedItem();
                } else if (pathJcb.getSelectedIndex() == 2) {
                    adb += "adb pull /sdcard/Android/data/" + AdbUtils.getTopAppPackageName() + "/" + jComboBoxEdit.getSelectedItem();
                } else {
                    adb += "adb pull /sdcard/" + jComboBoxEdit.getSelectedItem();
                }
                String filePath = Constant.PATH_CACHE_DIR + "pull files.bat";
                System.out.println("filePath = " + filePath);
                FileUtils.writeFile(filePath, adb + " D:/tmp/\npause");
                WinUtils.copyFileToClipboard(new File(filePath));
            }

            @Override
            public void onLongClick() {
                AdbUtils.pullFile(jComboBoxEdit.getSelectedItem() + "", null);
                WinUtils.opeDir("D:/tmp");
                JswDialogUtils.show("拉取完成,正在打开");
            }
        }));

        JPanel flowLayoutPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 创建一个JPanel，并为其设置流布局
        flowLayoutPanel1.setBounds(425, 55, 430, 300);
        flowLayoutPanel1.add(JswCustomWight.getJButton("长按重启手机", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                JswDialogUtils.showConfirmDialog("关机", "确定关机吗?", () -> {
                    // 用户点击了确定按钮
                    AdbUtils.exeCmd("adb shell reboot -p");
                    JswDialogUtils.showAutoCloseDialog("正在关机");
                    System.out.println("执行操作");
                });
            }

            @Override
            public void onLongClick() {
                AdbUtils.exeCmd("adb reboot");
                JswDialogUtils.showAutoCloseDialog("正在重启");
            }
        }));
        flowLayoutPanel1.add(JswCustomWight.getJButton("去空格回车", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                //去空格回车
                WinUtils.setSysClipboardText(WinUtils.getSysClipboardText().replaceAll("\\s", ""));
            }

            @Override
            public void onLongClick() {
                // 只去除回车
                WinUtils.setSysClipboardText(WinUtils.getSysClipboardText().replaceAll("\\n", ""));
            }
        }));
        flowLayoutPanel1.add(JswCustomWight.getJButton("手机基本信息", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String phoneBaseInfo = AdbUtils.getPhoneBaseInfo();
                System.out.println("phoneBaseInfo = " + phoneBaseInfo);
                JswDialogUtils.show(phoneBaseInfo);
            }
        }));
        flowLayoutPanel1.add(JswCustomWight.getJButton("修改分辨率", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String currPx = AdbUtils.exeCmd("adb shell wm size");
                String cmd = JswDialogUtils.showEditDialogSimple("请输入分辨率(中间空格)", "当前分辨率: " + currPx, "1080x1920")
                        .trim().replace(" ", "x").replace("*", "x");
                System.out.println("cmd = " + cmd);
                AdbUtils.exeCmd("adb shell wm size " + cmd);
            }
        }));
        flowLayoutPanel1.add(JswCustomWight.getJButton("重置分辨率", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                AdbUtils.exeCmd("adb shell wm size reset");
            }
        }));

        flowLayoutPanel1.add(JswCustomWight.getJButton("获取(屏幕)dpi密度", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String density = AdbUtils.exeCmd("adb shell wm density");
                WinUtils.setSysClipboardText(density);
                JswDialogUtils.show(density);
            }
        }));
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(160);
        flowLayoutPanel1.add(JswCustomWight.getJButton("设置(屏幕)dpi密度", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                String cmd = JswDialogUtils.showEditDialogSimple("请输入密度", "常用的dpi有160mdpi, 240hdpi, 320xhdpi, 480xxhdpi", atomicInteger.getAndIncrement() + "");
                System.out.println("cmd = " + cmd);
                atomicInteger.set(Integer.parseInt(cmd));
                AdbUtils.exeCmd("adb shell wm density " + cmd);
            }
        }));

        flowLayoutPanel1.add(JswCustomWight.getJButton("重置(屏幕)dpi密度", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                AdbUtils.exeCmd("adb shell wm density reset");
            }
        }));
        flowLayoutPanel1.add(JswCustomWight.getJButton("测试", new JswOnLongClickListener() {
            @Override
            public void onClick() {
//                AdbUtils.exeCmd("adb reboot");
            }
        }));
        container.add(flowLayoutPanel1);
        return container;
    }

    /**
     * 预览图片
     *
     * <pre><code>
     *     if (TestUtils.getBoolean()) {
     *          String path = CommUtils.getImgFile("bj.jpg");
     *          prView(path);
     *      } else {
     *          preViewImgLabel.setVisible(false);
     *          container.revalidate();
     *          container.repaint();
     *      }
     * </code></pre>
     *
     * @param path 图片路径
     */
    private static void prView(String path) {
        //path为null时隐藏
        if (path == null && preViewImgLabel != null && preViewImgLabel.isShowing()) {
            preViewImgLabel.setVisible(false);
            container.revalidate();
            container.repaint();
            return;
        }
        // 图片文件路径
//        String path = "D:/tmp/0001/1.jpg";
        System.out.println("path = " + path);
        ImageIcon icon = new ImageIcon(path);
        // 获取原始图片的宽度和高度
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();
//        originalWidth/originalHeight=0.5
        // 创建一个JLabel组件
        preViewImgLabel = new JLabel();

        // 设置label的位置和大小
        preViewImgLabel.setBounds(450, 10, originalWidth / 3, originalHeight / 3);

        // 缩放图片
        Image image = icon.getImage().getScaledInstance(originalWidth / 3, originalHeight / 3, Image.SCALE_SMOOTH);

        // 创建一个新的ImageIcon，将缩放后的Image设置为图标
        ImageIcon scaledIcon = new ImageIcon(image);

        // 将ImageIcon设置为JLabel的图标
        preViewImgLabel.setIcon(scaledIcon);
        preViewImgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("e.getClickCount() = " + e.getClickCount());
            }
        });

        container.add(preViewImgLabel);
        container.revalidate();
        container.repaint();
    }

    private static void exeAdbs(JTextField tfIntervalTime) {
        try {
            String text = jtCmdLins.getText().trim();
            if (jtCmdLins.getText().length() == 0) {
                String sysClipboardText = WinUtils.getSysClipboardText();
                if (sysClipboardText.trim().startsWith("adb ")) {
                    StringUtil.readStrByLins(sysClipboardText, lin -> {
                        if (!lin.startsWith("//")) {
                            AdbUtils.exeCmd(lin);
                            try {
                                Thread.sleep(StringUtil.parseInt(tfIntervalTime.getText(), 500));
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    });
                } else {
                    AdbUtils.inutText(text, false);
                }
                showMessageDialog("输入命令不能为空");
                return;
            } else if (!text.startsWith("adb ")) {
                String regex = "^(http|https)://[a-zA-Z0-9\\-\\.]+(:\\d+)?(/\\S*)?$";
                Matcher matcher = Pattern.compile(regex).matcher(text);
                if (matcher.matches()) {
                    AdbUtils.clickBack();
                }
                AdbUtils.inutText(text, false);
                return;
            }
            StringUtil.readStrByLins(jtCmdLins.getText(), lin -> {
                if (!lin.startsWith("//")) {
                    AdbUtils.exeCmd(lin);
                    try {
                        Thread.sleep(StringUtil.parseInt(tfIntervalTime.getText(), 500));
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
            showMessageDialog(e2.getMessage());
        }
    }

    /**
     * @param content 输出内容
     */
    public static void print(String content) {
//        int index = showContent.getWidth() - 15;
//        if (content.length()> index) {
//            showContent.setText("返回数据:" + content);
//        }
//        showContent.setText("返回数据:" + content);
        jtCmdLins.setText("返回数据:" + content);
//        CustomWight.JlabelSetText(showContent, content);
    }

    /**
     * https://cloud.tencent.com/developer/article/1702763
     *
     * @param content 显示消息弹框
     */
    public static void showMessageDialog(String content) {
        JOptionPane.showMessageDialog(null, content, "消息提示", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 批量打开网页
     */
    public static void batchOpenUrl() throws Exception {

        String s = WinUtils.getSysClipboardText();
        System.out.println("s = " + s);
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.equals("") && line.trim().length() > 0 && line.startsWith("http")) {
                String[] urls = line.split("http");
                if (urls.length > 1) {
                    for (String url : urls) {
                        if (url.trim().length() == 0) {
                            continue;
                        }
                        WinUtils.open("http" + url);
                    }
                } else {
                    WinUtils.open(line);
                }
            }
        }
    }
}
