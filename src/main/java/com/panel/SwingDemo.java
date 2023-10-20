package com.panel;


import com.other.Constant;
import com.panel.tab.ToolsTab1;
import com.panel.tab.ToolsTab2;
import com.panel.tab.ToolsTab3;
import com.panel.tab.ToolsTabTest;
import org.yifan.hao.WinUtils;
import org.yifan.hao.swing.JswDialogUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * https://www.jianshu.com/p/340cba3acbc8
 */
public class SwingDemo implements ActionListener {

    private static final String LOCK_FILE_PATH = "app.lock";
    private FileLock lock;

    private JFrame frame = new JFrame("MyTools");

    private SwingDemo() {
//        try {
//            GlobalScreen.unregisterNativeHook();
//        } catch (NativeHookException e) {
//            e.printStackTrace();
//        }
        //try {
        //    GlobalScreen.registerNativeHook();
        //    GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        //} catch (NativeHookException e) {
        //    e.printStackTrace();
        //}

        if (!tryAcquireLock()) {
            JOptionPane.showMessageDialog(null, "应用程序已经在运行中！", "警告", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.isAltDown() && e.getID() == KeyEvent.KEY_RELEASED) {//按下alt键并且释放其他按键
                    if (e.getKeyCode() == KeyEvent.VK_1) {// 最小化窗口
                        frame.setState(JFrame.ICONIFIED);
                        System.out.println("KeyEvent dispatched: Alt+1");
                        return true;  // 返回true，标记事件为已处理
                    } else if (e.getKeyCode() == KeyEvent.VK_2) {
                        WinUtils.sysClipboardTextToFile(Constant.PATH_CACHE_DIR);
                        JswDialogUtils.showAutoCloseDialog("已生成文件", 600);
                        frame.setState(JFrame.ICONIFIED);
                        System.out.println("KeyEvent dispatched: Alt+2");
                        return true;  // 返回true，标记事件为已处理
                    }
                }
                return false;  // 其他按键，不做任何处理
            }
        });

        JMenuBar menuBar = new JMenuBar();

        // 创建菜单并添加到菜单栏
        JMenu optionsMenu = new JMenu("选项");
        menuBar.add(optionsMenu);

        // 添加置顶菜单项
        JMenuItem topItem = new JMenuItem("置顶");
        topItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setAlwaysOnTop(true);
            }
        });
        optionsMenu.add(topItem);

        // 添加取消置顶菜单项
        JMenuItem untopItem = new JMenuItem("取消置顶");
        untopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setAlwaysOnTop(false);
            }
        });
        optionsMenu.add(untopItem);

        frame.setJMenuBar(menuBar);
        // 设置全局字体大小
        setUIFont(new javax.swing.plaf.FontUIResource("宋体", Font.PLAIN, 15));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double a = screenSize.getWidth();
        double b = screenSize.getHeight();
        int frameW = 900, frameH = 570;
        frame.setLocation(new Point((int) ((a - frameW) / 2), (int) ((b - frameH) / 2)));// 设定窗口出现位置
        frame.setSize(frameW, frameH);// 设定窗口大小
        JTabbedPane tabPane = new JTabbedPane();
        frame.setContentPane(tabPane);// 设置布局
        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
        tabPane.add("会话1", ToolsTab1.getToolsTab(frame));
        tabPane.add("会话2", new ToolsTab2().getTab(frame));
        ToolsTab3 tab3 = new ToolsTab3();
        tabPane.add("会话3", tab3.getTab(frame));
        tabPane.add("测试", new ToolsTabTest().getTab(frame));
        tabPane.addChangeListener(e -> {
            int selectedIndex = tabPane.getSelectedIndex();
            if (selectedIndex == 2) {
                tab3.startService(true);
            } else {
                tab3.stopService(false);
            }
        });
    }

    /**
     * 设置全局字体大小
     *
     * @param f 字体
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public static void main(String[] args) {
        new SwingDemo();
    }

    private boolean tryAcquireLock() {
        try {
            File lockFile = new File(LOCK_FILE_PATH);
            FileOutputStream outputStream = new FileOutputStream(lockFile);
            FileChannel channel = outputStream.getChannel();
            lock = channel.tryLock();
            if (lock == null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void releaseLock() {
        try {
            if (lock != null) {
                lock.release();
            }
            File lockFile = new File(LOCK_FILE_PATH);
            lockFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource().equals(button)) {frame.dispose();//关闭弹框}
    }
}
