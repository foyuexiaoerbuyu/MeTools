package hao.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.File;
import java.util.List;

/**
 * 自定义控件
 */
public class JswCustomWight {


    /**
     * 编辑框
     */
    public static JTextField getJTextField(int x, int y, int w, int h) {
        return getJTextField(x, y, w, h, null, null);
    }

    /**
     * 编辑框
     */
    public static JTextField getJTextField(int x, int y, int w, int h, String text) {
        return getJTextField(x, y, w, h, text, null);
    }

    /**
     * 编辑框
     */
    public static JTextField getJTextField(int x, int y, int w, int h, String text, IJTextFieldListener ijTextFieldLinser) {
        JTextField jTextField = new JTextField(text);
        jTextField.setBounds(x, y, w, h);
        if (ijTextFieldLinser == null) {
            return jTextField;
        }
        jTextField.addActionListener(e -> ijTextFieldLinser.actionPerformed(e, jTextField));
        jTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ijTextFieldLinser.focusGained(jTextField);
            }

            @Override
            public void focusLost(FocusEvent e) {
                ijTextFieldLinser.focusLost(jTextField);
            }
        });
        return jTextField;
    }

    /**
     * 自动滚动的多行编辑框
     */
    public static JScrollPane getJScrollPane(JTextArea jTextArea) {
        JScrollPane scroll = new JScrollPane(jTextArea);
        scroll.setBounds(jTextArea.getX(), jTextArea.getY(), jTextArea.getWidth(), jTextArea.getHeight());
//分别设置水平和垂直滚动条自动出现
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
////分别设置水平和垂直滚动条总是出现
//        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scroll;
    }

    /**
     * 自动滚动的多行编辑框
     */
    public static JScrollPane getJScrollPane(int x, int y, int w, int h) {
        JScrollPane scroll = new JScrollPane(getJTextArea(x, y, w, h, null, null));
        scroll.setBounds(15, 63, 230, 40);
//分别设置水平和垂直滚动条自动出现
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
////分别设置水平和垂直滚动条总是出现
//        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scroll;
    }

    /**
     * 多行编辑框
     */
    public static JTextArea getJTextArea(int x, int y, int w, int h) {
        return getJTextArea(x, y, w, h, null);
    }

    /**
     * 多行编辑框
     */
    public static JTextArea getJTextArea(int x, int y, int w, int h, String text) {
        return getJTextArea(x, y, w, h, text, null);
    }

    /**
     * 多行编辑框
     */
    public static JTextArea getJTextArea(int x, int y, int w, int h, String text, IDragCallBack iDragCallBack) {
        JTextArea jTextArea = new JTextArea(text);
        jTextArea.setBounds(x, y, w, h);
        jTextArea.setLineWrap(true);
//        jTextArea.setTransferHandler(new TransferHandler() {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public boolean importData(JComponent comp, Transferable t) {
//                try {
//                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
//
//                    String filepath = o.toString();
//                    if (filepath.startsWith("[")) {
//                        filepath = filepath.substring(1);
//                    }
//                    if (filepath.endsWith("]")) {
//                        filepath = filepath.substring(0, filepath.length() - 1);
//                    }
//                    System.out.println("拖拽文件路径: " + filepath);
//                    if (iDragCallBack != null) {
//                        iDragCallBack.dragCallBack(filepath);
//                    }
//                    jTextArea.setText(filepath);
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
//                for (int i = 0; i < flavors.length; i++) {
//                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
        return jTextArea;
    }

    /**
     * 多行编辑框,增加监听
     * mouseClicked(MouseEvent e)：当鼠标在组件上单击时调用。该方法没有任何操作，需要根据需求覆盖该方法并添加自定义的处理逻辑。
     * mousePressed(MouseEvent e)：当鼠标按下时调用。同样地，该方法需要根据需求进行覆盖和实现。
     * mouseReleased(MouseEvent e)：当鼠标释放时调用，同样需要根据需求进行覆盖和实现。
     * mouseEntered(MouseEvent e)：当鼠标进入组件区域时调用，同样需要根据需求进行覆盖和实现。
     * mouseExited(MouseEvent e)：当鼠标离开组件区域时调用，同样需要根据需求进行覆盖和实现。
     * mouseWheelMoved(MouseWheelEvent e)：当鼠标滚轮滚动时调用，此方法从Java 1.6开始提供。同样需要根据需求进行覆盖和实现。
     * mouseDragged(MouseEvent e)：当鼠标拖动时调用，此方法从Java 1.6开始提供。同样需要根据需求进行覆盖和实现。
     * mouseMoved(MouseEvent e)：当鼠标移动时调用，此方法从Java 1.6开始提供。同样需要根据需求进行覆盖和实现。
     */
    public static JTextArea getJTextAreaMouse(int x, int y, int w, int h, String text, MouseAdapter mouseAdapter) {
        JTextArea jTextArea = new JTextArea(text);
        jTextArea.setBounds(x, y, w, h);
        jTextArea.setLineWrap(true);
        jTextArea.addMouseListener(mouseAdapter);
        return jTextArea;
    }

    /**
     * @param iDragCallBack 拖拽文件回调
     */
    public static JTextArea getJTextAreaDrag(int x, int y, int w, int h, IDragCallBack iDragCallBack) {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setBounds(x, y, w, h);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setEditable(false);
        if (iDragCallBack == null) return jTextArea;
        // 添加拖拽监听器
        jTextArea.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    if (jTextArea.getText().contains(File.separator)) {
                        return true;
                    }
                    // 获取拖拽的文件列表
                    List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    // 遍历文件列表并输出到文本区域
                    for (File file : files) {
                        jTextArea.append(file.getAbsolutePath() + "\n");
                    }
                    iDragCallBack.dragCallBack(jTextArea.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        return jTextArea;
    }

    /**
     * 编辑框添加拖拽回调
     */
    public static JTextField getJTextField2DragCallBack(int x, int y, int w, int h, String text) {
        return getJTextField2DragCallBack(x, y, w, h, text, null);
    }

    /**
     * 编辑框添加拖拽回调
     */
    public static JTextField getJTextField2DragCallBack(int x, int y, int w, int h) {
        return getJTextField2DragCallBack(x, y, w, h, null, null);
    }

    /**
     * 编辑框添加拖拽回调
     */
    public static JTextField getJTextField2DragCallBack(int x, int y, int w, int h, IDragCallBack iDragCallBack) {
        return getJTextField2DragCallBack(x, y, w, h, null, iDragCallBack);
    }

    /**
     * 编辑框添加拖拽回调
     */
    public static JTextField getJTextField2DragCallBack(int x, int y, int w, int h, String text, IDragCallBack iDragCallBack) {
        JTextField jTextField = new JTextField(text);
        jTextField.setBounds(x, y, w, h);
        jTextField.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    System.out.println("拖拽文件路径: " + filepath);
                    if (iDragCallBack != null) {
                        iDragCallBack.dragCallBack(filepath);
                    }
                    jTextField.setText(filepath);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });
        return jTextField;
    }

    /**
     * 文本
     */
    public static JLabel getFileDropLabel(int x, int y, int w, int h,
                                          String text, JswFileDropLabel.IFileDropLabelCallBack iFileDropLabelCallBack) {
        JswFileDropLabel fileDropLabel = new JswFileDropLabel(iFileDropLabelCallBack);
        fileDropLabel.setBounds(x, y, w, h);
        fileDropLabel.setText(text);
        fileDropLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fileDropLabel.setOpaque(true);
        fileDropLabel.setBackground(Color.white);
        return fileDropLabel;
    }

    /**
     * 文本
     */
    public static JLabel getJLabel(int x, int y, int w, int h, String text) {
        return getJLabel(x, y, w, h, text, null);
    }

    /**
     * 文本
     */
    public static JLabel getJLabel(int x, int y, int w, int h, String text, JswOnLongClickListener onLongClickListener) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);
        label.addMouseListener(onLongClickListener);
        return label;
    }

    /**
     * 文本
     */
    public static JLabel getJLabel(int w, int h, String text, JswOnLongClickListener onLongClickListener) {
        JLabel label = new JLabel(text);
        label.setBounds(new Rectangle(w, h));
        label.addMouseListener(onLongClickListener);
        return label;
    }

    /**
     * 按钮
     */
    @Deprecated
    public static JButton getJButton(String text, ActionListener actionListener) {
        JButton label = new JButton(text);
        label.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮的内边距为0
        label.addActionListener(actionListener);
        return label;
    }

    /**
     * 按钮
     */
    @Deprecated
    public static JButton getJButtonMargin(String text, ActionListener actionListener) {
        JButton label = new JButton(text);
//        label.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮的内边距为0
        label.addActionListener(actionListener);
        return label;
    }

    public static JButton getJButtonMargin(String text, JswOnLongClickListener actionListener) {
        JButton jButton = new JButton(text);
//        label.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮的内边距为0
        jButton.addMouseListener(actionListener);
        actionListener.setBtnText(jButton.getText());
        return jButton;
    }

    public static JButton getJButtonMargin(String text, String tipText, JswOnLongClickListener actionListener) {
        JButton jButton = new JButton(text);
//        label.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮的内边距为0
        jButton.addMouseListener(actionListener);
        actionListener.setBtnText(jButton.getText());
        jButton.setToolTipText(tipText);
        return jButton;
    }

    /**
     * 长按按钮
     */
    public static JButton getJButton(String text, JswOnLongClickListener actionListener) {
        JButton label = new JButton(text);
        label.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮的内边距为0
        actionListener.setBtnText(text);
        label.addMouseListener(actionListener);
        return label;
    }

    /**
     * 按钮
     */
    @Deprecated
    public static JButton getJButton(int x, int y, int w, int h, String text, ActionListener actionListener) {
        JButton label = new JButton(text);
        label.setBounds(x, y, w, h);
        label.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮的内边距为0
        label.addActionListener(actionListener);
        return label;
    }

    /**
     * 按钮 长按监听
     */
    public static JButton getJButton(int x, int y, int w, int h, String text, JswOnLongClickListener actionListener) {
        JButton label = new JButton(text);
        label.setBounds(x, y, w, h);
        label.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮的内边距为0
        label.addMouseListener(actionListener);
        return label;
    }

    /**
     * 下拉选项
     */
    public static JComboBox<String> getJComboBox(int x, int y, int w, int h) {
        return getJComboBox(x, y, w, h, null, null);
    }

    /**
     * 下拉选项
     *
     * @param items key:命令, val:名称
     */
    public static JComboBox<String> getJComboBox(int x, int y, int w, int h, List<String> items) {
        return getJComboBox(x, y, w, h, items, null);
    }

    /**
     * 下拉选项
     *
     * @param items key:命令, val:名称
     */
    public static JComboBox<String> getJComboBox(int x, int y, int w, int h, List<String> items, ActionListener itemListener) {
        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setBounds(x, y, w, h);
        if (items != null) {
            items.forEach(jComboBox::addItem);
        }
//        jComboBox.setSelectedIndex(1);
//        jComboBox.setPopupVisible(false);
//        jComboBox.setVisible(true);
        jComboBox.addActionListener(itemListener);
        return jComboBox;
    }

    /**
     * 下拉选项
     *
     * @param items key:命令, val:名称
     */
    public static JComboBox<String> getJComboBoxEdit(int x, int y, int w, int h, List<String> items, IJComboBoxListener ijComboBoxListener) {
        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setBounds(x, y, w, h);
        if (items != null) {
            items.forEach(jComboBox::addItem);
        }
        jComboBox.setEditable(true);
        jComboBox.addActionListener(e -> {
            if (ijComboBoxListener != null)
                ijComboBoxListener.itemClick(e, jComboBox, jComboBox.getSelectedIndex(), String.valueOf(jComboBox.getSelectedItem()));
        });
        // 获取编辑框组件
        Component editorComponent = jComboBox.getEditor().getEditorComponent();
        // 添加鼠标点击监听器
        editorComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                jComboBox.setPopupVisible(true);// 设置下拉列表框可见
                System.out.println("编辑框被点击");
                if (ijComboBoxListener != null) ijComboBoxListener.mouseClicked(mouseEvent, jComboBox);
            }
        });
        // 添加焦点监听器
        editorComponent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("编辑框获得焦点");
                if (ijComboBoxListener != null) ijComboBoxListener.focusGained(e, jComboBox);
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("编辑框失去焦点");
                if (ijComboBoxListener != null) ijComboBoxListener.focusLost(e, jComboBox);
            }
        });
        // 添加键盘事件监听器
        jComboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (ijComboBoxListener != null) ijComboBoxListener.enter(e, jComboBox, jComboBox.getSelectedItem());
                }
            }
        });
        return jComboBox;
    }

    /**
     * JLabel自动换行
     *
     * @param content 内容
     */
    public static void JlabelSetText(JLabel jLabel, String content) {
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = content.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int start = 0;
        int len = 0;
        while (start + len < content.length()) {
            while (true) {
                len++;
                if (start + len > content.length()) break;
                if (fontMetrics.charsWidth(chars, start, len)
                        > jLabel.getWidth()) {
                    break;
                }
            }
            builder.append(chars, start, len - 1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, content.length() - start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
    }

    public static JCheckBox getJCheckBox(int x, int y, int w, int h, String btnName, ActionListener actionListener) {
        JCheckBox checkBox = new JCheckBox(btnName);
        checkBox.setBounds(x, y, w, h);
        checkBox.addActionListener(actionListener);
//        checkBox.addActionListener(e -> {
//            JCheckBox source = (JCheckBox) e.getSource();
//            System.out.println("勾选状态: " + source.isSelected());
//        });
        return checkBox;
    }

    public static JCheckBox getJCheckBox(String btnName, ActionListener actionListener) {
        JCheckBox checkBox = new JCheckBox(btnName);
        checkBox.addActionListener(actionListener);
//        checkBox.addActionListener(e -> {
//            JCheckBox source = (JCheckBox) e.getSource();
//            System.out.println("勾选状态: " + source.isSelected());
//        });
        return checkBox;
    }

    /**
     * 拖拽回调
     */
    public interface IDragCallBack {
        void dragCallBack(String path);
    }

    public interface IJTextFieldListener {
        /*获取焦点*/
        void focusGained(JTextField jTextField);

        /*失去焦点*/
        void focusLost(JTextField jTextField);

        void actionPerformed(ActionEvent e, JTextField jTextField);
    }

    public interface IJComboBoxListener {

        default void enter(KeyEvent keyEvent, JComboBox<String> jComboBox, Object selectedItem) {

        }

        //编辑框失去焦点
        default void focusLost(FocusEvent focusEvent, JComboBox<String> jComboBox) {
        }

        /*编辑框获得焦点*/
        default void focusGained(FocusEvent focusEvent, JComboBox<String> jComboBox) {
        }

        //编辑框被点击
        default void mouseClicked(MouseEvent mouseEvent, JComboBox<String> jComboBox) {
        }

        /**
         * 点击item
         *
         * @param actionEvent
         * @param jComboBox
         * @param index        获取到选中项的索引
         * @param selectedItem
         */
        default void itemClick(ActionEvent actionEvent, JComboBox<String> jComboBox, int index, String selectedItem) {
        }
    }
}
