package com.test;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;

/**
 * 监听键盘按键
 * 需要这两个文件
 * <p>
 * jnativehook-2.2.2.jar
 * JNativeHook-2.2.2.x86_64.dll
 */
public class KeyboardListenerExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Keyboard Listener Example");
        JTextArea textArea = new JTextArea(10, 30);

        NativeKeyListener nativeKeyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int keyCode = e.getKeyCode();
                    String keyText = NativeKeyEvent.getKeyText(keyCode);
                    textArea.append(keyText + " pressed\n");
                });
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                // 不处理释放事件
            }

            @Override
            public void nativeKeyTyped(NativeKeyEvent e) {
                // 不处理键入事件
            }
        };

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(nativeKeyListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.getContentPane().add(new JScrollPane(textArea));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
