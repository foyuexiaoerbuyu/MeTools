package com.other;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class DialogUtils {

    public interface IDialogClick {
        void okBtnClick();

        default void cancelBtnClick() {
        }

        default void canceListen() {
        }
    }

    public interface IImageDialog {
        default void print() {
        }

        default void close() {
        }

        void clickImg();
    }

    public interface IConfirmDialogCallBack {
        void callBack(boolean isOk);
    }

    public interface IClick {
        void callBack(int index, String btnName);
    }


    public static String showEditDialogSimple(String title, String message, String defaultValue) {
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, null, null, defaultValue).toString();
//        return JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showEditDialogSimple(String defaultValue, ISimpleCallBack iCallBack) {
        String inputValue = JOptionPane.showInputDialog(null, "请输入一个值：", defaultValue);
        if (inputValue != null) {
            iCallBack.str(inputValue);
            // 用户点击了确定按钮，执行相应操作
            System.out.println("用户输入的值是：" + inputValue);
        } else {
            // 用户点击了取消按钮，执行相应操作
            System.out.println("用户取消了输入");
        }
    }

    public static void showEditDialog(String textContent, ICallBack iCallBack) {
        JDialog jDialog = new JDialog();
        jDialog.setTitle("编辑对话框");
        jDialog.setSize(350, 300);

//        jDialog.setLocationRelativeTo(null); // 设置弹框居中显示
        // 设置弹框居中显示
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jDialog.setLocation(new Point((int) ((screenSize.getWidth() - jDialog.getWidth()) / 2),
                (int) ((screenSize.getHeight() - jDialog.getHeight()) / 2)));// 设定窗口出现位置

        // 创建一个文本编辑框
        JTextArea textAreaTitle = new JTextArea();
//        textAreaTitle.setText(text);
        JScrollPane scrollPane1 = new JScrollPane(textAreaTitle);
        jDialog.add(scrollPane1, BorderLayout.NORTH);
        // 创建一个文本编辑框
        JTextArea textAreaContent = new JTextArea();
        textAreaContent.setText(textContent);
        JScrollPane scrollPane = new JScrollPane(textAreaContent);
        jDialog.add(scrollPane, BorderLayout.CENTER);

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // 添加三个按钮
        JButton button1 = new JButton("关闭");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jDialog.dispose();
//                if (iCallBack != null) iCallBack.str(textAreaTitle.getText(), textAreaContent.getText());
//                String text = WinUtils.getSysClipboardText();
//                FileUtils.writeFile(Constant.path_tmp_clipboard_1, text);
//                System.out.println("文本内容为：\n" + textAreaContent.getText());
//                System.out.println("剪切板内容为：\n" + text);
//                JOptionPane.showMessageDialog(parent, "保存剪切板内容成功", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(button1);

        JButton button2 = new JButton("确定");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iCallBack != null) iCallBack.str(textAreaTitle.getText(), textAreaContent.getText());
                jDialog.dispose();
                // 按钮2点击事件处理
//                String text = textAreaContent.getText();
//                System.out.println("文本内容为：\n" + text);
//                FileUtils.writeFile(Constant.path_tmp_clipboard_1, text);
//                JOptionPane.showMessageDialog(parent, "保存成功", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(button2);

//        JButton button3 = new JButton("按钮3");
//        button3.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // 按钮3点击事件处理
//                System.out.println("按钮3被点击，文本内容为：\n" + textArea.getText());
//            }
//        });
//        buttonPanel.add(button3);

        jDialog.add(buttonPanel, BorderLayout.SOUTH);

        // 添加关闭对话框监听器
        jDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (iCallBack != null) iCallBack.str(textAreaTitle.getText(), textAreaContent.getText());
                System.out.println("对话框关闭");
                jDialog.dispose();
            }
        });
        jDialog.setVisible(true);
    }

    public static void show(String title, String msg, WindowAdapter windowClosed) {
        JOptionPane op = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        copy(msg, op);

        final JDialog dialog = op.createDialog(title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);
        dialog.setVisible(true);
        if (windowClosed != null) {
            dialog.addWindowListener(windowClosed);
        }
        dialog.show();
    }

    private static void copy(String msg, JOptionPane op) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("复制");
        copyItem.addActionListener(e -> {
            StringSelection selection = new StringSelection(msg);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        });
        popupMenu.add(copyItem);

        op.setComponentPopupMenu(popupMenu);
    }

    public static void show(String title, String msg) {
        show(title, msg, null);
    }

    public static void show(String msg) {
        show("提示", msg);
    }

    public static void showAutoCloseDialog(String title, String msg, int delay) {
        JOptionPane op = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        final JDialog dialog = op.createDialog(title);
        copy(msg, op);
        // 创建一个新计时器
        Timer timer = new Timer();
        // 30秒 后执行该任务
        TimerTask task = new TimerTask() {
            public void run() {
                dialog.setVisible(false);
                dialog.dispose();
            }
        };
        timer.schedule(task, delay);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                // 在对话框关闭时取消计时器任务
                task.cancel();
                timer.cancel();
            }
        });

    }

    public static void showAutoCloseDialog(String msg, int delay) {
        showAutoCloseDialog("提示", msg, delay);
    }

    public static void showAutoCloseDialog(String msg) {
        showAutoCloseDialog("提示", msg, 3000);
    }

    public static void showLong(String msg) {
        showAutoCloseDialog("提示", msg, 333000);
    }

    public static void showAutoCloseDialog(JFrame parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "提示", JOptionPane.WARNING_MESSAGE);
    }


    // 显示图片弹框
    public static void showImageDialog(JFrame frame, String path, IImageDialog iImageDialog) {
        // 创建一个面板用于承载图片和按钮
        JPanel panel = new JPanel(new BorderLayout());

        // 创建JLabel并设置图片
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(path); // 替换为你的图片路径
        imageLabel.setIcon(imageIcon);
        // 创建滚动窗格，并将图片添加到其中
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 创建两个按钮
        JButton printButton = new JButton("打印");
        JButton closeButton = new JButton("关闭");


        // 将按钮添加到面板的第二行
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建自定义对话框
        JDialog dialog = new JDialog(frame, "图片弹框", true);
        dialog.setContentPane(panel);
        int iconHeight = imageIcon.getIconHeight();
        // 获取默认工具包
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        // 获取屏幕尺寸
        Dimension screenSize = toolkit.getScreenSize();
        // 获取屏幕高度
        int screenHeight = screenSize.height;
        if (iconHeight > screenHeight) {
            iconHeight = screenHeight - 150;
        }
        dialog.setSize(imageIcon.getIconWidth(), iconHeight); // 设置对话框的大小
        dialog.setLocationRelativeTo(frame); // 居中显示对话框

        // 为按钮添加点击事件监听器
        printButton.addActionListener(e -> {
            System.out.println("点击了打印按钮");
            // 在这里添加你想要执行的打印逻辑
            dialog.dispose();
            if (iImageDialog != null) {
                iImageDialog.print();
            }
        });

        closeButton.addActionListener(e -> {
            System.out.println("点击了关闭按钮");
            dialog.dispose();
//            closeImageDialog(frame);
            if (iImageDialog != null) {
                iImageDialog.close();
            }
        });

        // 添加鼠标点击事件监听器
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("点击了图片");
                dialog.dispose();
                // 在这里添加你想要执行的点击事件逻辑
//                closeImageDialog(frame);
                if (iImageDialog != null) {
                    iImageDialog.clickImg();
                }
            }
        });
        // 显示对话框
        dialog.setVisible(true);
    }

    // 关闭图片弹框
    private static void closeImageDialog(JFrame frame) {
        Container parent = frame.getParent();
        while (parent != null) {
            if (parent instanceof JDialog) {
                ((JDialog) parent).dispose();
                break;
            }
            parent = parent.getParent();
        }
    }

    public static void showConfirmDialog(String title, String message, IDialogClick confirmAction) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel(message);
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(e -> {
            confirmAction.okBtnClick();
            dialog.dispose();
        });
        buttonPanel.add(confirmButton);

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> {
            confirmAction.cancelBtnClick();
            dialog.dispose();
        });
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }


    public interface ICallBack {
        void str(String title, String content);
    }

    public interface ISimpleCallBack {
        void str(String content);
    }

    public static void showCheckboxDialog(Component parent, String title, String message, String[] checkboxLabels) {
        JCheckBox[] checkboxes = new JCheckBox[checkboxLabels.length];

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(checkboxLabels.length, 1));

        for (int i = 0; i < checkboxLabels.length; i++) {
            checkboxes[i] = new JCheckBox(checkboxLabels[i]);
            panel.add(checkboxes[i]);
        }

        int result = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            StringBuilder selectedItems = new StringBuilder();
            for (JCheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    selectedItems.append(checkbox.getText()).append(", ");
                }
            }
            if (selectedItems.length() > 0) {
                selectedItems.delete(selectedItems.length() - 2, selectedItems.length());
            }
            JOptionPane.showMessageDialog(parent, "Selected items: " + selectedItems.toString());
        }
    }

    public static void showRadioButtonDialog(Component parent, String title, String message, String[] radioButtonLabels) {
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton[] radioButtons = new JRadioButton[radioButtonLabels.length];
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(radioButtonLabels.length, 1));

        for (int i = 0; i < radioButtonLabels.length; i++) {
            radioButtons[i] = new JRadioButton(radioButtonLabels[i]);
            panel.add(radioButtons[i]);
            buttonGroup.add(radioButtons[i]);
        }

        int result = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            for (JRadioButton radioButton : radioButtons) {
                if (radioButton.isSelected()) {
                    JOptionPane.showMessageDialog(parent, "Selected item: " + radioButton.getText());
                    break;
                }
            }
        }
    }

    /**
     * * DialogUtils.showBtnsDialog(new DialogUtils.IClick() {
     * *             @Override
     * *             public void callBack(int index, String btnName) {
     * *                 System.out.println("index = " + index + " btnName = " + btnName);
     * *             }
     * *         }, "按钮0", "按钮1", "按钮2");
     */
    public static void showBtnsDialog(IClick iClick, String... radioButtonLabels) {
        JButton[] jButtons = new JButton[radioButtonLabels.length];
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(radioButtonLabels.length, 1));
        JDialog dialog = new JDialog();
        dialog.setTitle("操作");

        for (int i = 0; i < radioButtonLabels.length; i++) {
            int index = i;
            String btnName = radioButtonLabels[index];
            jButtons[index] = new JButton(btnName);
            jButtons[index].addActionListener(e -> {
                iClick.callBack(index, btnName);
                dialog.dispose();
            });
            panel.add(jButtons[i]);
        }

        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.SOUTH);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static void showConfirmDialog(String message, IConfirmDialogCallBack iConfirmDialogCallBack) {
        if (message == null) {
            message = "确定要执行操作吗？";
        }
        int choice = JOptionPane.showOptionDialog(null, message, "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (choice == JOptionPane.YES_OPTION) {
            // 用户点击了确定按钮
            System.out.println("用户选择了确定");
            // 执行相关操作
        } else if (choice == JOptionPane.NO_OPTION) {
            // 用户点击了取消按钮
            System.out.println("用户选择了取消");
            // 执行其他操作或关闭应用程序
        }
        iConfirmDialogCallBack.callBack(choice == JOptionPane.YES_OPTION);
    }
}
