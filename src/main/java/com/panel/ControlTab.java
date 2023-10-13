package com.panel;

import javax.swing.*;
import java.awt.*;

public class ControlTab {
    public Component getTab(JFrame frame) {

        // 创建一个流布局面板
        JPanel flowLayoutPanel = new JPanel(new FlowLayout());

//        flowLayoutPanel.add(CustomWight.getJButton("背景选择器", new OnLongClickListener() {
//
//            @Override
//            public void onClick() {
//                //这个例子中，我们为LinearLayout定义了两种状态：按下状态（state_pressed="true"）和普通状态。按下状态时，背景颜色为灰色（#D3D3D3），普通状态时，背景颜色为白色（#FFFFFF）。同时，我们为背景添加了圆角（radius="5dp"）。
//                String tmp = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                        "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
//                        "    <item android:state_pressed=\"true\">\n" +
//                        "        <shape>\n" +
//                        "            <solid android:color=\"#D3D3D3\"/>\n" +
//                        "            <corners android:radius=\"5dp\"/>\n" +
//                        "        </shape>\n" +
//                        "    </item>\n" +
//                        "    <item>\n" +
//                        "        <shape>\n" +
//                        "            <solid android:color=\"#FFFFFF\"/>\n" +
//                        "            <corners android:radius=\"5dp\"/>\n" +
//                        "        </shape>\n" +
//                        "    </item>\n" +
//                        "</selector>";
//                WinUtils.setSysClipboardText(tmp);
//            }
//        }));
//        flowLayoutPanel.add(CustomWight.getJButton("圆角白底灰边", new OnLongClickListener() {
//
//            @Override
//            public void onClick() {
//                String tmp = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                        "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
//                        "    <solid android:color=\"#FFFFFF\"/>\n" +
//                        "    <corners android:radius=\"4dp\"/>\n" +
//                        "    <stroke android:color=\"#CCCCCC\" android:width=\"0.5dp\"/>\n" +
//                        "</shape>";
//                WinUtils.setSysClipboardText(tmp);
//            }
//        }));
//        flowLayoutPanel.add(CustomWight.getJButton("文字颜色选择", new OnLongClickListener() {
//
//            @Override
//            public void onClick() {
//                String tmp = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                        "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
//                        "    <item android:color=\"@color/color_pressed\" android:state_pressed=\"true\" />\n" +
//                        "    <item android:color=\"@color/color_normal\" />\n" +
//                        "</selector>";
//                WinUtils.setSysClipboardText(tmp);
//            }
//        }));
//        flowLayoutPanel.add(CustomWight.getJButton("临时剪切1", new OnLongClickListener() {
//
//            @Override
//            public void onLongClick() {
//                String text = FileUtils.readFile(Constant.path_tmp_clipboard_1);
//                EditDialog dialog = new EditDialog(frame,text);
//                dialog.show();
////                dialog.setVisible(true);
//            }
//
//            @Override
//            public void onClick() {
//                String text = FileUtils.readFile(Constant.path_tmp_clipboard_1);
//                WinUtils.setSysClipboardText(text);
//                DialogUtils.show("复制成功");
//            }
//        }));
        return flowLayoutPanel;
    }
}
