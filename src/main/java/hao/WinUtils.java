package hao;


import com.google.gson.JsonObject;
import org.yifan.hao.swing.JswCustomWight;
import org.yifan.hao.swing.JswDialogUtils;
import org.yifan.hao.swing.JswOnLongClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class WinUtils {

    private static Point mousePosition;

    /**
     * 1. 从剪切板获得文字。
     */
    public static String getSysClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * 2.将字符串复制到剪切板。
     */
    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    /**
     * 3. 从剪切板获得图片。
     */
    public static Image getImageFromClipboard() throws Exception {
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = sysc.getContents(null);
        if (cc == null)
            return null;
        else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
            return (Image) cc.getTransferData(DataFlavor.imageFlavor);
        return null;

    }

    /**
     * 4.复制图片到剪切板。
     */
    public static void setClipboardImage(final Image image) throws Exception {
        Transferable trans = new Transferable() {
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor))
                    return image;
                throw new UnsupportedFlavorException(flavor);
            }

        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans,
                null);
    }

    /**
     * 依次执行批量命令行命令
     */
    public static String exeBatchCmd(String command) {
        ProcessBuilder pb;
        Process process = null;
        BufferedReader br = null;
        StringBuilder resMsg = null;
        OutputStream os = null;
        String cmd1 = "cmd";
        try {
            pb = new ProcessBuilder(cmd1);
            pb.redirectErrorStream(true);
            process = pb.start();
            os = process.getOutputStream();
            os.write(command.getBytes());
            os.flush();
            os.close();

            resMsg = new StringBuilder();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = br.readLine()) != null) {
                resMsg.append(s).append("\n");
            }
            resMsg.deleteCharAt(resMsg.length() - 1);
            int result = process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return resMsg.toString();
    }

    /**
     * 依次执行批量命令行命令
     * Arrays.asList("","","");
     */
    public static void exeCmd(String... args) {
        try {
            for (String arg : args) {
                ProcessBuilder processBuilder1 = new ProcessBuilder("cmd.exe", "/c", arg);
                Process process1 = processBuilder1.start();
                process1.waitFor();//阻塞当前限制等待执行完毕
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 依次执行批量命令行命令
     * Arrays.asList("","","");
     */
    public static void exeCmd(List<String> args) {
        try {
            for (String arg : args) {
                ProcessBuilder processBuilder1 = new ProcessBuilder("cmd.exe", "/c", arg);
                Process process1 = processBuilder1.start();
                process1.waitFor();//阻塞当前限制等待执行完毕
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName 这个方法将文件内容复制到剪切板。
     */
    @Deprecated
    public static void copyFileToClipboard(String fileName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "type " + fileName + " | clip");
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InetAddress getLocalHostExactAddress() {
        try {
            InetAddress candidateAddress = null;

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                // 该网卡接口下的ip会有多个，也需要一个个的遍历，找到自己所需要的
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    // 排除loopback回环类型地址（不管是IPv4还是IPv6 只要是回环地址都会返回true）
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了 就是我们要找的
                            // ~~~~~~~~~~~~~绝大部分情况下都会在此处返回你的ip地址值~~~~~~~~~~~~~
                            return inetAddr;
                        }

                        // 若不是site-local地址 那就记录下该地址当作候选
                        if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }

                    }
                }
            }

            // 如果出去loopback回环地之外无其它地址了，那就回退到原始方案吧
            return candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIpV4() {
        InetAddress localHost = getLocalHostExactAddress();
        return localHost.getHostAddress();
    }

    /**
     * 查看本机某端口是否被占用
     *
     * @param port 端口号
     * @return 如果被占用则返回true，否则返回false
     */
    public static boolean isLoclePortUsing(int port) {
        boolean flag = true;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据IP和端口号，查询其是否被占用
     *
     * @param host IP
     * @param port 端口号
     * @return 如果被占用，返回true；否则返回false
     * @throws UnknownHostException IP地址不通或错误，则会抛出此异常
     */
    public static boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = false;
        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(theAddress, port);
            flag = true;
        } catch (IOException e) {
            //如果所测试端口号没有被占用，那么会抛出异常，这里利用这个机制来判断
            //所以，这里在捕获异常后，什么也不用做
        }
        return flag;
    }

    /**
     * 根据剪切板生成文件
     *
     * @param fileDir 生成文件所在文件夹
     */
    public static void sysClipboardTextToFile(String fileDir) {
        String sysClipboardText = WinUtils.getSysClipboardText();
        if (sysClipboardText.length() == 0) {
            System.out.println("剪切板内容为空或不是文本内容");
            return;
        }
        String fileNmae = System.currentTimeMillis() + ".txt";
        if (sysClipboardText.contains("class ") && sysClipboardText.contains("{")) {
            String className = sysClipboardText.substring(sysClipboardText.indexOf("class ") + 6, sysClipboardText.indexOf("{")).trim();
            if (className.contains(" extends")) {
                className = className.substring(0, className.indexOf(" extends"));
            }
            if (className.contains(" implements ")) {
                className = className.substring(0, className.indexOf(" implements"));
            }
            fileNmae = className + ".java";
        } else if (sysClipboardText.contains("public interface ") && sysClipboardText.contains("{")) {
            fileNmae = sysClipboardText.substring(sysClipboardText.indexOf("public interface ") + 16, sysClipboardText.lastIndexOf("{")).trim() + ".java";
        }
        String filePath = fileDir + fileNmae;
        FileUtils.writeFile(filePath, sysClipboardText);
        WinUtils.copyFileToClipboard(new File(filePath));
    }

    /**
     * @param path 调用系统打开文件或文件夹
     */
    public static void opeDir(String path) {
        try {
            File folder = new File(path);
            if (Desktop.isDesktopSupported() && folder.exists()) {
                // 执行打开文件夹的操作
                Desktop.getDesktop().open(folder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        System.out.println("屏幕尺寸： " + screenWidth + " x " + screenHeight);
        return new int[]{screenWidth, screenHeight};
    }

    /**
     * mouseLocation.x   mouseLocation.y
     *
     * @return 获取鼠标当前位置(信息)
     */
    public static Point getMouseCurPos() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    /**
     * @param commandStr 执行命令并返回内容
     * @return 执行命令并返回内容
     */
    public static String exeCmd(String commandStr) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec("cmd.exe " + commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String content = sb.toString();
//        System.out.println("执行命令返回内容: " + content);
        return content;
    }

    /**
     * 打开浏览器
     */
    public static void open(String url) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri = new URI(url);
            desktop.browse(uri);
        }
    }


    /**
     * java 实体转json
     */
    public static String javaBen2Json(String path) {
        JsonObject jo = new JsonObject();
        if (path.endsWith("}")) {
            try {
                StringUtil.readStrByLins(path, linStr -> {
                    ben2Json(jo, linStr);
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("转换ben错误: " + e.getStackTrace());
            }
        } else {
            FileUtils.readTextFileLines(path, linStr -> {
                ben2Json(jo, linStr);
            });
        }
        System.out.println("jo = " + jo.toString());
        return jo.toString();
    }

    private static void ben2Json(JsonObject jo, String linStr) {
        if ((
                linStr.contains(" int ")
                        || linStr.contains(" Integer ")
                        || linStr.contains(" String ")
                        || linStr.contains(" boolean ")
                        || linStr.contains(" Boolean ")
                        || linStr.contains(" Date ")
                        || linStr.contains(" long ")
                        || linStr.contains(" Long ")
                        || linStr.contains(" float ")
                        || linStr.contains(" Float ")
        ) && linStr.endsWith(";")) {
            linStr = linStr
                    .replace(" int ", "")
                    .replace(" Integer ", "")
                    .replace(" String ", "")
                    .replace(" boolean ", "")
                    .replace(" Boolean ", "")
                    .replace(" Date ", "")
                    .replace(" long ", "")
                    .replace(" Long ", "")
                    .replace(" float ", "")
                    .replace(" Float ", "")
                    .replace("private ", "")
                    .replace(";", "")
                    .replace(" ", "");
            jo.addProperty(linStr, "");
        }
    }

    /**
     * java 实体获取getset方法
     */
    public static String javaBenGetSet(String path) {
        StringBuffer all = new StringBuffer();
        StringBuffer className = new StringBuffer();
        StringBuffer example = new StringBuffer();
        StringBuffer getSb = new StringBuffer();
        StringBuffer setSb = new StringBuffer();
        if (path.endsWith("}")) {
            try {
                StringUtil.readStrByLins(path, linStr -> {
                    benGetSet(linStr, className, example, getSb, setSb);
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("转换ben错误: " + e.getStackTrace());
            }
        } else {
            FileUtils.readTextFileLines(path, new FileUtils.ReadLines() {
                @Override
                public void onReadLine(String linStr) {
                    benGetSet(linStr, className, example, getSb, setSb);
                }
            });
        }
        StringBuffer allSb = all.append(example).append(getSb).append(setSb);
        System.out.println(allSb);
        return allSb.toString();
    }

    private static void benGetSet(String linStr, StringBuffer className, StringBuffer example, StringBuffer getSb, StringBuffer setSb) {
        String tmp = "";
        if (linStr.contains("class")) {
            className.append(linStr.substring(linStr.indexOf("class")).replace(" ", "").replace("class", "").replace("{", ""));
            tmp = className + " " + StringUtil.toLowerCaseFirstOne(className.toString()) + " = new " + className + "();\n";
            example.append(tmp);
        }
        if (linStr.startsWith("    public ") && linStr.contains(") {") && (linStr.contains(" get") || linStr.contains(" set"))) {
            int startIndex = 0;
            if (linStr.contains("get")) {
                startIndex = linStr.indexOf("get");
            } else if (linStr.contains("set")) {
                startIndex = linStr.indexOf("set");
            }
            tmp = tmp + StringUtil.toLowerCaseFirstOne(className.toString()) + "." + linStr.substring(startIndex, linStr.indexOf("(")) + "();\n";
            if (linStr.contains("get")) {
                getSb.append(tmp);
            } else if (linStr.contains("set")) {
                setSb.append(tmp);
            }
        }
    }


    /**
     * 复制文件到剪切板
     */
    public static void copyFileToClipboard(File file) {
        List<File> files = new ArrayList<File>();
        files.add(file);

        FileTransferable fileTransferable = new FileTransferable(files);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(fileTransferable, null);
    }

    /**
     * 把文本设置到剪贴板（复制）
     */
    public static void getClipboardFilePath(IReadLin iReadLin) {
        try {
            // 获取系统剪贴板
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            // 获取剪贴板中的内容
            Transferable contents = clipboard.getContents(null);

            if (contents != null && contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                // 获取复制的文件列表
                List<File> files = (List<File>) contents.getTransferData(DataFlavor.javaFileListFlavor);

                // 遍历文件列表并打印每个文件的路径
                for (File file : files) {
                    iReadLin.readLin(file.getAbsolutePath());
//                    System.out.println("File path: " + file.getAbsolutePath());
                }
            } else {
                System.out.println("No files found in clipboard.");
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
            iReadLin.onException(e);
            return;
        }
        iReadLin.end();
    }

    /**
     * @param num 模拟鼠标滚动3次向上
     */
    public static void mouseWheelUp(int num) {
        try {
            // 创建Robot对象
            Robot robot = new Robot();
            // 模拟鼠标滚动3次向上
            for (int i = 0; i < num; i++) {
                robot.mouseWheel(-1);
                // 等待一段时间，让滚轮滚动生效
                robot.delay(500);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param num 模拟鼠标滚动2次向下
     */
    public static void mouseWheelDown(int num) {
        try {
            // 创建Robot对象
            Robot robot = new Robot();
            // 模拟鼠标滚动2次向下
            for (int i = 0; i < num; i++) {
                robot.mouseWheel(1);
                // 等待一段时间，让滚轮滚动生效
                robot.delay(500);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 获取屏幕中心位置
     */
    public static int[] getScreenCenterPos() {
        // 获取当前屏幕设备
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        // 获取屏幕设备的配置信息
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // 获取屏幕的矩形边界
        Rectangle screenBounds = gc.getBounds();

        // 计算屏幕中心的坐标
        int centerX = screenBounds.x + screenBounds.width / 2;
        int centerY = screenBounds.y + screenBounds.height / 2;

        System.out.println("屏幕中心位置：X=" + centerX + ", Y=" + centerY);
        return new int[]{centerX, centerY};
    }

    // shift+ 按键
    public static void keyPressWithShift(Robot r, int key) {
        r.keyPress(KeyEvent.VK_SHIFT);
        r.keyPress(key);
        r.keyRelease(key);
        r.keyRelease(KeyEvent.VK_SHIFT);
        r.delay(100);
    }

    // ctrl+ 按键
    public static void keyPressWithCtrl(Robot r, int key) {
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyPress(key);
        r.keyRelease(key);
        r.keyRelease(KeyEvent.VK_CONTROL);
        r.delay(100);
    }

    // alt+ 按键
    public static void keyPressWithAlt(Robot r, int key) {
        r.keyPress(KeyEvent.VK_ALT);
        r.keyPress(key);
        r.keyRelease(key);
        r.keyRelease(KeyEvent.VK_ALT);
        r.delay(100);
    }

    //打印出字符串
    public static void keyPressString(Robot r, String str) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取剪切板
        Transferable tText = new StringSelection(str);
        clip.setContents(tText, null); //设置剪切板内容
        keyPressWithCtrl(r, KeyEvent.VK_V);//粘贴
        r.delay(100);
    }

    //单个 按键
    public static void keyPress(Robot r, int key) {
        r.keyPress(key);
        r.keyRelease(key);
        r.delay(100);
    }

    // 按下 enter 换行
    public static void keyPressEnter(Robot r) {
        keyPress(r, KeyEvent.VK_ENTER); // 按下 enter 换行
    }

    public static void keyPressPageDown(Robot r) {
        keyPress(r, KeyEvent.VK_PAGE_DOWN);
    }

    public static void keyPressPageUp(Robot r) {
        keyPress(r, KeyEvent.VK_PAGE_UP);
    }

    public static void keyPressDown(Robot r) {
        keyPress(r, KeyEvent.VK_DOWN);
    }

    public static void keyPressUp(Robot r) {
        keyPress(r, KeyEvent.VK_UP);
    }

    public static void keyPressBackSpace(Robot r) {
        keyPress(r, KeyEvent.VK_BACK_SPACE);
    }

    public static void mouseMove(Robot r, int x, int y) {
        // 获取当前鼠标位置
        Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();
        // 移动鼠标到指定位置
        r.mouseMove(x, y);
    }

    public static void mouseMoveToScreenCenterPos(Robot r) {
        int[] screenCenterPos = getScreenCenterPos();
        // 移动鼠标到指定位置
        r.mouseMove(screenCenterPos[0], screenCenterPos[1]);
    }

    /**
     * @throws AWTException 显示鼠标位置
     */
    public static void showMousePos() throws AWTException {
        JFrame frame = new JFrame("显示鼠标位置");
        //https://blog.csdn.net/jxiao_linbei/article/details/117918341
//        frame.setUndecorated(true); // 去掉窗口的装饰
//        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);//采用指定的窗口装饰风格
        frame.setLocation(1740, 136);
        frame.setAlwaysOnTop(true);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("鼠标位置: ");
        frame.add(label);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 获取鼠标位置
                Point mouseLocation = WinUtils.getMouseCurPos();
                // 更新标签文本
                label.setText("鼠标位置: " + mouseLocation.x + ", " + mouseLocation.y);
            }
        }, 0, 10);

        Robot r = new Robot();

        JButton testBtn = JswCustomWight.getJButtonMargin("开始", new JswOnLongClickListener() {
            @Override
            public void onClick() {
            }
        });
        frame.add(testBtn);

        // 添加键盘监听器
        testBtn.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("e = " + e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    timer.cancel();
                    System.out.println("空格");
                    // 空格键被按下
                    label.setText("click 空格");
                }
            }
        });

        frame.add(JswCustomWight.getJButtonMargin("上一页", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                r.delay(2000);
//                Point mouseCurPos = WinUtils.getMouseCurPos();
//                int[] screenCenterPos = WinUtils.getScreenCenterPos();
//                XLog.showLogArgs(screenCenterPos[0], screenCenterPos[1]);
//                WinUtils.mouseMove(r, screenCenterPos[0], screenCenterPos[1]);
//                r.delay(1500);
                WinUtils.keyPressPageUp(r);
                System.out.println("------");
//                r.delay(1500);
//                WinUtils.mouseMove(r, 859, 222);
            }
        }));
        frame.add(JswCustomWight.getJButtonMargin("下一页", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                WinUtils.keyPressPageUp(r);
            }
        }));
        frame.add(JswCustomWight.getJButtonMargin("复制到文件", new JswOnLongClickListener() {
            @Override
            public void onClick() {
                System.out.println("sy");
            }
        }));
        moveFrameForCtrl(frame);
        frame.setSize(50, 200);
        frame.setVisible(true);
    }

    private static void moveFrameForCtrl(JFrame frame) {
        // 添加鼠标监听器
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 捕获鼠标位置
                mousePosition = e.getPoint();
            }
        });

        // 添加鼠标移动监听器
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 如果同时按下了Ctrl键，则移动窗口
                if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
                    Point newMousePosition = e.getLocationOnScreen();
                    frame.setLocation(newMousePosition.x - mousePosition.x, newMousePosition.y - mousePosition.y);
                }
            }
        });

    }

    /**
     * 是否为盘符开头
     */
    public static boolean startsWithWindowsDrive(String str) {
        if (str == null) return false;
        // 使用正则表达式判断字符串是否以 Windows 盘符开头
        return str.trim().matches("^[A-Za-z]:\\\\.*");
    }

    /**
     * 5.通过流获取，可读取图文混合
     */
    public void getImageAndTextFromClipboard() throws Exception {
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipTf = sysClip.getContents(null);
        DataFlavor[] dataList = clipTf.getTransferDataFlavors();
        int wholeLength = 0;
        for (int i = 0; i < dataList.length; i++) {
            DataFlavor data = dataList[i];
            if (data.getSubType().equals("rtf")) {
                Reader reader = data.getReaderForText(clipTf);
                OutputStreamWriter osw = new OutputStreamWriter(
                        new FileOutputStream("d:\\test.rtf"));
                char[] c = new char[1024];
                int leng = -1;
                while ((leng = reader.read(c)) != -1) {
                    osw.write(c, wholeLength, leng);
                }
                osw.flush();
                osw.close();
            }
        }
    }

    public interface IReadLin {
        void readLin(String filePath);

        default void onException(Exception exception) {

        }

        default void end() {

        }
    }

    static class FileTransferable implements Transferable {
        private List files;

        public FileTransferable(List files) {
            this.files = files;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.javaFileListFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.javaFileListFlavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return files;
        }
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

    /**
     * 生成批量网址
     */
    public static void batchCreateUrl() {
        JswDialogUtils.showEditDialogSimple("生成批量网址", "使用@符号分割", WinUtils.getSysClipboardText(), content -> {
            if (content != null) {
                String[] split = content.split("@");
                StringBuilder sb = new StringBuilder();
                int startNum = Integer.parseInt(split[1]);
                for (int i = 0; i < 100; i++) {
                    sb.append(split[0]).append(startNum + i).append(split[2]).append("\n");
                }
                WinUtils.setSysClipboardText(sb.toString());
                JswDialogUtils.show("生成完毕");
            }
        });
    }

}