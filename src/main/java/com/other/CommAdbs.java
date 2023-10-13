package com.other;

import org.yifan.hao.AdbUtils;
import org.yifan.hao.FileUtils;
import org.yifan.hao.WinUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

/**
 * 编码解码问题
 * byte ptext[] = androidFilePath.getBytes("UTF-8");
 * androidFilePath = new String(ptext, "CP1252");
 * https://stackoverflow.com/questions/24219090/adb-pull-file-with-special-character-in-its-path
 * https://juejin.cn/post/6844904051386089479
 */
public class CommAdbs {

    public static List<String> getCommAdbsName() {
        return Arrays.asList(
                "前台Activity名称",
                "前台App包名称",
                "Activity页面详情",
                "剪切板输入",
                "卸载前台app",
                "设置手机剪切板内容",
                "获取手机剪切板内容",
                "打开文件闪传",
                "开启边界布局",
                "关闭布局边界",
                "保持唤醒状态",
                "开发者界面",
                "获取屏幕信息",
                "获取ip",
                "60秒息屏",
                "保持屏幕常亮",
                "手机基本信息",
                "顶部fragment信息",
                "获取javaBen信息",
                "重启手机",
                "关机"

        );
    }

    public static void exeAdb(ActionEvent e, AdbUtils.IAdbExeCallBack iAdbExeCallBack) {
        int index = ((JComboBox) e.getSource()).getSelectedIndex();//获取到选中项的索引
        System.out.println(e.getActionCommand());
        System.out.println(e.paramString());
        System.out.println(((JComboBox) e.getSource()).getActionCommand());
        Object selectedItem = ((JComboBox) e.getSource()).getSelectedItem();
        System.out.println(selectedItem);
        if (selectedItem.equals("前台Activity名称")) {
            getTopActyName(iAdbExeCallBack);
        } else if (selectedItem.equals("前台App包名称")) {
            getTopAppPakName(iAdbExeCallBack);
        } else if (selectedItem.equals("停止前台app")) {
            AdbUtils.stopCurrApp();
        } else if (selectedItem.equals("卸载前台app")) {
            AdbUtils.unInstallFrontApp();
        } else if (selectedItem.equals("开启边界布局")) {
            AdbUtils.offLayoutView(true);
        } else if (selectedItem.equals("关闭布局边界")) {
            AdbUtils.offLayoutView(false);
        } else if (selectedItem.equals("保持唤醒状态")) {
            AdbUtils.keepSober();
        } else if (selectedItem.equals("开发者界面")) {
            AdbUtils.openDeveloperSetting();
        } else if (selectedItem.equals("设置手机剪切板内容")) {
            AdbUtils.exeCmd("adb shell am start cn.mvp/.MainActivity");
            AdbUtils.sendSysClipboardText(WinUtils.getSysClipboardText(), false);
        } else if (selectedItem.equals("获取手机剪切板内容")) {
            AdbUtils.exeCmd("adb shell am start cn.mvp/.MainActivity");
            String sysClipboardText = AdbUtils.getSysClipboardText();
            System.out.println("sysClipboardText = " + sysClipboardText);
            //Broadcasting: Intent { act=clipper.get mIsPicForceMode=0 flg=0x400000 } Broadcast completed: result=-1, data="测试"
            sysClipboardText = sysClipboardText.substring(sysClipboardText.lastIndexOf("=")).replace("\"", "");
            WinUtils.setSysClipboardText(sysClipboardText);
        } else if (selectedItem.equals("剪切板输入")) {
            AdbUtils.clickBack();
            String sysClipboardText = WinUtils.getSysClipboardText();
            System.out.println("sysClipboardText = " + sysClipboardText);
            AdbUtils.inutText(sysClipboardText, false);
        } else if (selectedItem.equals("获取屏幕信息")) {
            WinUtils.setSysClipboardText(AdbUtils.getDisplaysDetails());
        } else if (selectedItem.equals("获取ip")) {
            WinUtils.setSysClipboardText(AdbUtils.getIP());
        } else if (selectedItem.equals("获取javaBen信息")) {
            String path = FileUtils.replaceSeparator(WinUtils.getSysClipboardText());
            WinUtils.setSysClipboardText(WinUtils.javaBen2Json(path) + "\n\n" + WinUtils.javaBenGetSet(path));
        } else if (selectedItem.equals("顶部fragment信息")) {
            String exeCmdStr = AdbUtils.exeCmd("adb shell dumpsys activity top");
            WinUtils.setSysClipboardText(exeCmdStr);
            iAdbExeCallBack.exeCallBack("fragment详细信息已复制到剪切板,粘贴查看.");
        } else if (selectedItem.equals("60秒息屏")) {
            AdbUtils.exeCmd("adb shell settings put system screen_off_timeout 600000");
        } else if (selectedItem.equals("打开文件闪传")) {
            AdbUtils.openFilFastTransmissionApp();
        } else if (selectedItem.equals("手机基本信息")) {
            AdbUtils.getPhoneBaseInfo();
            AdbUtils.startSettingPage();
        } else if (selectedItem.equals("Activity页面详情")) {
            WinUtils.setSysClipboardText(AdbUtils.getActivityDetails());
        } else if (selectedItem.equals("重启手机")) {
            AdbUtils.exeCmd("adb reboot");
        } else if (selectedItem.equals("关机")) {
            AdbUtils.exeCmd("adb shell reboot -p");
        } else {//保持屏幕常亮
            AdbUtils.openScreenSteadyLight();
        }
    }

    private static void getTopAppPakName(AdbUtils.IAdbExeCallBack iAdbExeCallBack) {
        String content = AdbUtils.getTopAppPackageName();
        WinUtils.setSysClipboardText(content);
        if (iAdbExeCallBack != null) {
            iAdbExeCallBack.exeCallBack(content);
        }
    }

    private static void getTopActyName(AdbUtils.IAdbExeCallBack iAdbExeCallBack) {
        String content = AdbUtils.getTopAppActyName();
        WinUtils.setSysClipboardText(content);
        if (iAdbExeCallBack != null) {
            iAdbExeCallBack.exeCallBack(content);
        }
    }
}
