package com.test;

import org.yifan.hao.AdbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 右键菜单  监听按键
 */
public class RightClickMenuExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Right Click Menu Example");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        panel.add(JswCustomWight.getJButtonMargin("test1", new JswOnLongClickListener() {
//            @Override
//            public void onClick() {
//
//            }
//        }));
//        panel.add(JswCustomWight.getJButtonMargin("test2", new JswOnLongClickListener() {
//            @Override
//            public void onClick() {
//
//            }
//        }));
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // 键盘按下并释放了某个键，但不包括特殊功能键（如Shift、Ctrl等）
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // 键盘按下某个键
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    System.out.println("e = " + e.getKeyCode());
                    AdbUtils.click(500, 500);
                    AdbUtils.click(778, 2270);
                    AdbUtils.click(500, 2000);
                    AdbUtils.click(500, 500);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    AdbUtils.exeCmd("adb shell input swipe 500 500 100 500 20");
                    System.out.println("e1 = " + e.getKeyCode());
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    AdbUtils.exeCmd("adb shell input swipe 100 500 500 500 20");
                    //adb shell input swipe 500 500 100 500 20
                    System.out.println("e1 = " + e.getKeyCode());
                } else {
                    System.out.println("e2 = " + e.getKeyCode());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // 键盘释放某个键

            }
        });
        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("菜单项1");
        menuItem1.addActionListener(e -> {
            System.out.println("e = " + e);
        });
        JMenuItem menuItem2 = new JMenuItem("菜单项2");
        JMenuItem menuItem3 = new JMenuItem("菜单项3");
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);

        // 添加鼠标右键监听器
//        panel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                if (e.isPopupTrigger()) {
//                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
//                }
//            }
//        });
        panel.setComponentPopupMenu(popupMenu);
        /*
        panel.setComponentPopupMenu(new JPopupMenus().addMenu("菜单项2", new JPopupMenus.JPopupMenusClick() {
            @Override
            public void click(String str) {
                System.out.println("str = " + str);
            }
        }).addMenu("菜单项22", new JPopupMenus.JPopupMenusClick() {
            @Override
            public void click(String str) {
                System.out.println("str = " + str);
            }
        }));*/

        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
