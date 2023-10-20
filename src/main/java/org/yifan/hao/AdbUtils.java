package org.yifan.hao;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://blog.csdn.net/xiaoerbuyu1233/article/details/122124201
 * https://blog.csdn.net/aoaoxiexie/article/details/53464716
 * https://www.huaweicloud.com/articles/39c8580fd6d8eacb6b0b89082f9d15b4.html
 * https://juejin.cn/post/6844903645289398280#heading-43
 */
public class AdbUtils {

    private static Runtime runtime;

    static {
        runtime = Runtime.getRuntime();
    }

    /**
     * @param sysClipboardText 发送剪切板内容
     */
    public static void sendSysClipboardText(String sysClipboardText, boolean isBack) {
        String commandStr = "adb shell am broadcast -a clipper.set -f 0x01000000 --es \"text\" \"" + sysClipboardText + "\"";
        System.out.println("发送剪切板内容: " + commandStr);
        String result = exeCmd(commandStr);
        System.out.println("result = " + result);
        if (isBack && result.contains("文本被复制到剪贴板")) {
            AdbUtils.clickBack();
        }
    }

    /**
     * 获取剪切板内容
     */
    public static String getSysClipboardText() {
        return exeCmd("adb shell am broadcast -a clipper.get");
    }

    /**
     * 点击返回键
     */
    public static void clickBack() {
        exeCmd("adb shell input keyevent 4");
    }

    /**
     * 打开文件闪传
     */
    public static void openFilFastTransmissionApp() {
        AdbUtils.exeCmd("adb shell am start app.eleven.com.fastfiletransfer/app.eleven.com.fastfiletransfer.ContainerActivity");
        String commandStr = "http://" + AdbUtils.getIP() + ":2333";
        System.out.println("打开文件闪传: " + commandStr);
        try {
            WinUtils.open(commandStr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * https://github.com/xbdcc/CCommand
     *
     * @return 手机基本信息
     */
    public static String getPhoneBaseInfo() {
        StringBuilder allSb = new StringBuilder();
        String wm_size = AdbUtils.exeCmd("adb shell wm size");
        String density = AdbUtils.exeCmd("adb shell wm density");
        String model = AdbUtils.exeCmd("adb shell getprop ro.product.model");
        String android_id = AdbUtils.exeCmd("adb shell settings get secure android_id");
        String version = AdbUtils.exeCmd("adb shell getprop ro.build.version.release");
        String fingerprint = AdbUtils.exeCmd("adb shell getprop ro.build.fingerprint");
        String cpus = AdbUtils.exeCmd("adb shell getprop ro.product.cpu.abilist");
        String sdk = AdbUtils.exeCmd("adb shell getprop ro.build.version.sdk");
        //adb shell dumpsys battery //电池信息
        String screen_off_timeout = AdbUtils.exeCmd("adb shell settings get system screen_off_timeout");
        String ip = AdbUtils.getIP();
        allSb.append("屏幕分辨率: ").append(wm_size.replace("Physical size: ", ""));
        allSb.append("密度:      ").append(density.replace("Physical density: ", ""));
        allSb.append("型号:      ").append(model);
        allSb.append("品牌:      ").append(fingerprint);
        allSb.append("系统版本:   ").append(version);
        allSb.append("sdk版本:   ").append(sdk);
        allSb.append("cpu架构:   ").append(cpus);
        allSb.append("设备id:    ").append(android_id);
        allSb.append("联网ip:    ").append(ip);
        allSb.append("休眠时间:   ").append(screen_off_timeout);
        String baseInfo = allSb.toString();
        WinUtils.setSysClipboardText(baseInfo);
        System.out.println("手机基本信息:\n" + baseInfo);
        return baseInfo;
    }

    /**
     * 打开设置页面
     */
    public static void startSettingPage() {
        exeCmd("adb shell am start com.android.settings/com.android.settings.Settings");
    }

    /**
     * @param enable 打开wifi
     */
    public static void openWifi(boolean enable) {
        exeCmd("adb shell svc wifi " + (enable ? "enable" : "disable"));
    }

    /**
     * 打开屏幕常量
     */
    public static void openScreenSteadyLight() {
//        AdbUtils.exeCmd("adb shell settings put system screen_off_timeout 360000000");//始终亮屏 360手机可用(显示和主题>自动灭屏)
        AdbUtils.exeCmd("adb shell settings put system screen_off_timeout 2147483647");//不知道为什么这个也可以
        //adb shell svc power stayon [true/false/usb/ac] 设置屏幕的常亮，true保持常亮，false不保持，usb当插入usb时常亮，ac当插入电源时常亮
        AdbUtils.exeCmd("adb shell svc power stayon true");//屏幕常量 true usb 360手机测试都可以
        AdbUtils.exeCmd("adb shell settings put system screen_brightness 255");//设置最大亮度
    }

    /**
     * 获取Activity 详情
     */
    public static String getActivityDetails() {
//        String frontAppPackageName = AdbUtils.getFrontAppPackageName();
//        return exeCmd("adb shell dumpsys activity " + frontAppPackageName);
        //上面的也行
        return exeCmd("adb shell dumpsys activity top");
    }

    /**
     * https://www.cnblogs.com/qican/p/12589912.html
     */
    public static void inputKeyTab() {
        exeCmd("adb shell input keyevent KEYCODE_TAB");
    }

    /**
     * https://www.cnblogs.com/qican/p/12589912.html
     */
    public static void inputKeyBack() {
        exeCmd("adb shell input keyevent KEYCODE_BACK");
    }

    /**
     * fvbId
     */
    public static void fvbIdToClipboard() throws IOException {

        String s = WinUtils.getSysClipboardText();
        HashMap<String, String> keys = new HashMap<>();
        String element = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.equals("")) {
                if (line.startsWith("<")) {
                    if (line.contains(" xmlns:android=\"")) {
                        line = line.split(" xmlns:android=\"")[0];
                    }
                    if (line.contains(".")) {
                        String[] split = line.split("\\.");
                        line = split[split.length - 1];
                    }
                    element = line.replace("<", "");
                } else if (line.contains("android:id=\"@+id/")) {
                    keys.put(line.split("/")[1].replace("\"", ""), element);
                }
            }
        }
        /*butterknife字段*/
        StringBuilder globalFieldSb = new StringBuilder();
        /*butterknife点击事件*/
        StringBuilder clickMethod = new StringBuilder("@OnClick({");
        /*butterknife点击主体*/
        StringBuilder clickBody = new StringBuilder("public void click(View view) {\n        switch (view.getId()) {\n");

        /*fvb*/
        StringBuilder fvbFieldSb = new StringBuilder("\n\n");
        StringBuilder fvbBodySb = new StringBuilder("\n");

        keys.keySet().forEach(key -> {
            globalFieldSb.append("@BindView(R.id.").append(key).append(")\n")
                    .append(keys.get(key)).append(" ").append(key).append(";\n\n");
            clickMethod.append("R.id.").append(key).append(",");
            clickBody.append("case R.id.").append(key).append(" :\nbreak;\n");

            fvbFieldSb.append(keys.get(key)).append(" ").append(key).append(";\n");
            fvbBodySb.append(key).append(" = findViewById(R.id.").append(key).append(");\n");
        });
        String content = globalFieldSb.append(clickMethod.append("})\n")).append(clickBody.append("default:\n}}"))
                .append(fvbFieldSb).append(fvbBodySb).toString().replace(",})", "})");
//        System.out.println(content);
        WinUtils.setSysClipboardText(content);
    }

    /**
     * 两台手机点击一下
     */
    public static void testTapForTwoAndroid() {
        try {
            runtime.exec("adb -s 13b6e4c4 shell input tap 400 400 ");
            runtime.exec("adb -s 296ec3e2 shell input tap 400 400 ");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 控制手机点击一下
     */
    public static void click(int x, int y) {
        try {
            runtime.exec("adb shell input tap " + x + " " + y);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * //adb shell input swipe 100 100 100 100 1000 在 100 100 位置长按 1000毫秒 控制手机点击一下
     */
    public static void longClick(String x, String y) {
        try {
            runtime.exec("adb shell input swipe " + x + " " + y + " " + x + " " + y);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 控制手机滑动
     */
    public static void swipe(String satrtX, String satrtY, String endX, String endY) {
        try {
            runtime
                    .exec("adb shell input swipe " + satrtX + " " + satrtY + " " + endX + " " + endY);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 控制手机滑动
     *
     * @param duration 持续时间
     */
    public static void swipe(int satrtX, int satrtY, int endX, int endY, int duration) {
        try {
            runtime
                    .exec("adb shell input swipe " + satrtX + " " + satrtY + " " + endX + " " + endY + " " + duration);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 输入文字 不能输入汉字 特殊符号需要隐藏键盘或设置键盘为英文才能输入
     *
     * @param x           点击坐标x
     * @param y           点击坐标y
     * @param inputStr    输入信息
     * @param isClickBack 是否点击返回
     */
    public static void inutText(int x, int y, String inputStr, boolean isClickBack) {
        inputStr = StringUtil.replaceAllChinese(inputStr);
        try {
            runtime.exec("adb shell input tap " + x + " " + y);
            runtime.exec("adb shell input text \"" + inputStr + "\"");
            if (isClickBack) {
                runtime.exec("adb shell input keyevent 4");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输入文字 不能输入汉字 特殊符号需要隐藏键盘或设置键盘为英文才能输入
     *
     * @param inputStr    输入信息
     * @param isClickBack 是否点击返回
     */
    public static void inutText(String inputStr, boolean isClickBack) {
        inputStr = StringUtil.replaceAllChinese(inputStr);
        try {
            runtime.exec("adb shell input text \"" + inputStr + "\"");
            if (isClickBack) {
                runtime.exec("adb shell input keyevent 4");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param millis 间隔时间
     */
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param commandStr 执行命令并返回内容
     * @return
     */
    public static String exeCmd(String commandStr) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec("cmd.exe /c " + commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));//编码: Charset.forName("GBK")
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

    public static void execCommandAndGetOutput() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("cmd.exe /c ipconfig");
            // 输出结果，必须写在 waitFor 之前
            String outStr = getStreamStr(process.getInputStream());
            // 错误结果，必须写在 waitFor 之前
            String errStr = getStreamStr(process.getErrorStream());
            int exitValue = process.waitFor(); // 退出值 0 为正常，其他为异常
            System.out.println("exitValue: " + exitValue);
            System.out.println("outStr: " + outStr);
            System.out.println("errStr: " + errStr);
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取流内容
     */
    public static String getStreamStr(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        return sb.toString();
    }

    /**
     * 开启触摸位置显示
     */
    public static void offTachPostion(boolean postionTag) {
        exeCmd("adb shell settings put system pointer_location " + (postionTag ? "1" : "0"));
    }

    /**
     * adb shell dumpsys window | findstr mCurrentFocus
     * 前台包名和顶部Activity
     */
    public static String getTopAppPack2ActyName() {
        String content = AdbUtils.exeCmd("adb shell dumpsys window w |findstr \\/ |findstr name= ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("line = " + line);
                if (line.length() > 0 && line.contains(".") && line.contains("mSurface=Surface(name=") && line.contains("Activity") || line.contains("Acty)")) {
                    int count = StringUtil.countMatches(line, "/");
                    if (count == 1) {
                        return line.replace("mSurface=Surface(name=", "").replace(")", "").trim();
                    }
                    return line.substring(0, line.lastIndexOf(")/")).replace("mSurface=Surface(name=", "").trim();
                }
//                line = line.substring(0, line.lastIndexOf("/@"));
//                if (!line.contains(".") && line.startsWith("name=com.android.")) {
//                    String replace = line.replace("mSurface=Surface(name=", "").replace(")", "");
//                    System.out.println(replace);
//                    return replace.substring(0, replace.lastIndexOf("/"));
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 顶部Activity名称
     */
    public static String getTopAppActyName() {
        return getTopAppPack2ActyName().split("/")[1];
    }

    /**
     * 顶部包名
     */
    public static String getTopAppPackageName() {
        return getTopAppPack2ActyName().split("/")[0];
    }

    /**
     * 前台包名
     */
    public static String getFrontAppPackageName() {
        //com.hebccc.crm/com.hebccc.crm.bussiness.main.MainActivity
//        String packName = AdbUtils.exeCmd("adb shell dumpsys window w |findstr \\/ |findstr name= ");
        String packName = getTopAppPack2ActyName();
        if (packName.contains("/")) {
            packName = packName.split("/")[0];
        }
        return packName;
    }

    /**
     * 前台Activity 名称
     */
    public static String getFrontActyName() {
        //com.hebccc.crm/com.hebccc.crm.bussiness.main.MainActivity
//        String packName = AdbUtils.exeCmd("adb shell dumpsys window w |findstr \\/ |findstr name= ");
        String packName = getTopAppPack2ActyName();
        if (packName.contains("/")) {
            packName = packName.split("/")[0];
        }
        return packName;
    }

    /**
     * 卸载前台app
     */
    public static void unInstallFrontApp() {
        exeCmd("adb uninstall " + getFrontAppPackageName());
    }

    /**
     * 打开布局边界(需要重启app)
     *
     * @param off true:开启布局边界 false:关闭布局边界
     */
    public static void offLayoutView(boolean off) {
        exeCmd("adb shell setprop debug.layout " + off);
    }

    /**
     * 保持唤醒状态
     * <p>
     * 暂时不清楚和 keepScreenSteadyLight() 方法区别
     */
    public static void keepSober() {
        exeCmd("adb shell am start -n com.android.settings/.DisplaySettings");
    }

    /**
     * 保存屏幕常亮
     * <p>
     * 暂时不清楚和 keepSober() 方法区别
     * adb shell svc power stayon [true/false/usb/ac] 设置屏幕的常亮，true保持常亮，false不保持，usb当插入usb时常亮，ac当插入电源时常亮
     * <p>
     *
     * @param mode true/false/usb/ac 设置屏幕的常亮，true保持常亮，false不保持，usb当插入usb时常亮，ac当插入电源时常亮
     */
    public static void keepScreenSteadyLight(String mode) {
        exeCmd("adb shell svc power stayon " + mode);
    }

    /**
     * 开发者界面
     */
    public static void openDeveloperSetting() {
        //360n7pro
        exeCmd("adb shell am start com.android.settings/.DevelopmentSettings");
        //一加9rt
        exeCmd("adb shell am start -n com.android.settings/com.android.settings.Settings\\$DevelopmentSettingsDashboardActivity");
    }

    /**
     * 推pc文件到手机
     */
    public static void pushFile(String filePath) {
        filePath = filePath.replace(" ", "");
        String fileName = FileUtils.getFileName(filePath);
        String commandStr = "adb push \"" + filePath + "\" /sdcard/Download/\"" + fileName + "\"";
        String s = exeCmd(commandStr);
        System.out.println("adb命令: " + commandStr + " \nadb命令执行结果: " + s + " 文件名:" + fileName);
    }

    /**
     * /sdcard/Download/
     * 提取手机文件到pc桌面
     * 需要先创建pc本地文件夹才行,不存在的情况下可能会执行失败
     * adb pull "sdcard/_电脑传输/" D:\tmp\_电脑传输\
     * adb pull /sdcard/Android/data/cn.mvp/files <本地目标路径>
     *
     * <p>授权给ADB访问文件的权限
     * adb shell
     * run-as cn.mvp
     * chmod -R 777 files
     * exit
     * adb pull /sdcard/Android/data/cn.mvp/files <本地目标路径>
     * </p>
     */
    public static void pullFile(String fromPath, String toPath) {
        if (toPath == null) {
            toPath = "D:/tmp";
        }
        if (!fromPath.contains("/sdcard/")) {
            fromPath = ("/sdcard/" + fromPath);
        }
        fromPath = fromPath.replace("//", "");
        //byte ptext[] = androidFilePath.getBytes("UTF-8");
        //androidFilePath = new String(ptext, "CP1252");

        String fileName = FileUtils.getFileName(fromPath);
        String commandStr = "adb pull \"" + fromPath + "\" \"" + toPath + "\"";
        String s = exeCmd(commandStr);
        System.out.println("adb命令: " + commandStr + " \nadb命令执行结果: " + s + " 文件名:" + fileName);
    }

    /**
     * @return 设备宽高分辨率(单位 : PX)
     */
    public static String getScreenWH() {
        return exeCmd("adb shell wm size");
    }

    /**
     * @return 获取android设备屏幕密度：
     */
    public static String getScreenDensity() {
        return exeCmd("adb shell wm density");
    }

    /**
     * @return 修改android设备屏幕密度：
     */
    public static String modifyScreenDensity(int x, int y) {
        return exeCmd(" adb shell wm size " + x + "X" + y);
    }

    /**
     * @return 重置屏幕尺寸
     */
    public static String resetScreenSize(int x, int y) {
        return exeCmd("adb shell wm size reset");
    }

    /**
     * @return 设置屏幕 dpi
     */
    public static String setDpi(int size) {
        return exeCmd("adb shell wm density " + size);
    }

    /**
     * @return 重置 dpi
     */
    public static String retDpi(int size) {
        return exeCmd("adb shell wm density reset");
    }

    /**
     * 在输出中，查找 "init=" 字符串后面的数字，这就是屏幕分辨率 ，例如： init=1080x1920 420dpi
     *
     * @return 设备屏幕详细信息
     */
    public static String getDisplaysDetails() {
        return exeCmd("adb shell dumpsys window displays");
    }

    /**
     * https://blog.csdn.net/yaoyaozaiye/article/details/122826340
     *
     * @param keyCode 键码
     */
    public static void keyCode(int keyCode) {
        exeCmd("adb shell input keyevent " + keyCode);
    }

    /**
     * 截图(暂时不可用)
     * cd /
     * D:
     * md pic
     * SET str="%date:~0,4%-%date:~5,2%-%date:~8,2%"
     * adb shell screencap -p /sdcard/%str%.png
     * adb pull /sdcard/%str%.png D:\pic
     * adb shell rm /sdcard/*.png
     * exit
     */
    public static void screenshot() {

        exeCmd("cd /\n" +
                "D:\n" +
                "md pic\n" +
                "SET str=\"%date:~0,4%-%date:~5,2%-%date:~8,2%\"\n" +
                "adb shell screencap -p /sdcard/%str%.png\n" +
                "adb pull /sdcard/%str%.png D:\\pic\n" +
                "adb shell rm /sdcard/*.png\n" +
                "exit");
    }

    /**
     * 截图
     */
    public static void screenshot1() {
        String absolutePath = new File("").getAbsolutePath() + "/src/main/resources/adb截图.bat";
        String commandStr = "cmd /c start " + absolutePath;
        System.out.println("截图: " + commandStr);
        AdbUtils.exeCmd(commandStr);
        return;
    }

    /**
     * 停止当前app
     */
    public static void stopCurrApp() {
        String topAppPackageName = getTopAppPackageName();
        System.out.println("topAppPackageName = " + topAppPackageName);
        String commandStr = "adb shell am force-stop " + topAppPackageName;
        System.out.println("停止当前app:" + commandStr);
        exeCmd(commandStr);
    }

    /**
     * 开发者模式
     */
    public static void startDevelopmentSettings() {
        exeCmd("adb shell am start -n com.android.settings/com.android.settings.DevelopmentSettings");
    }

    /**
     * 筛选ip
     * https://blog.csdn.net/c1024197824/article/details/120595003
     */
    public static String getIP() {
        String content = exeCmd("adb shell ip addr show wlan0");
        String reg = "inet\\s(\\d+?\\.\\d+?\\.\\d+?\\.\\d+?)/\\d+";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    /**
     * @param enable 数据开关
     */
    public static void openPhoneFlow(boolean enable) {
        exeCmd("adb shell svc data " + (enable ? "enable" : "disadle"));
    }

    public static void main(String[] args) {
//adb pull  目标系统中的文件路径(a.txt)  本机系统要存放取出来的路径(a.txt)
//adb pull  /sdcard/_电脑传输/"MikelProject Demo-dev.zip"  "D:\tmp\MikelProject Demo-dev.zip"
        String activityDetails = getActivityDetails();
        System.out.println("activityDetails = " + activityDetails);
    }


    /**
     * adb执行回调
     */
    public interface IAdbExeCallBack {
        void exeCallBack(String content);
    }
}