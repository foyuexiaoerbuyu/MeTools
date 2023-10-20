package hao.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * https://bbs.huaweicloud.com/blogs/286058
 * 自定义控件
 */
public class JfxCustomWight {
    /**
     * @param iTextFieldCallBack 拖拽及回车
     */
    public static TextField getTextField(int w, int h, ITextFieldCallBack iTextFieldCallBack) {
        return getTextField(w, h, false, iTextFieldCallBack);
    }

    /**
     * @param isDrag 启用拖拽
     */
    public static TextField getTextField(int w, int h, boolean isDrag, ITextFieldCallBack iTextFieldCallBack) {
        TextField textField = new TextField();
        textField.setPrefSize(w, h);
        if (isDrag) {
            //<editor-fold desc="拖拽功能">
            textField.setOnDragOver(event -> {
                if (event.getGestureSource() != textField && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            });

            textField.setOnDragDropped(event -> {
                Dragboard dragboard = event.getDragboard();
                boolean success = false;
                if (dragboard.hasFiles()) {
                    List<File> files = dragboard.getFiles();
                    // 处理拖放的文件
                    StringBuilder filePaths = new StringBuilder();
                    for (File file : files) {
                        String filePath = file.getAbsolutePath();
                        filePaths.append(filePath).append("\n");
                        // 在这里使用文件路径进行操作
                        System.out.println(filePath);
                    }
                    success = true;
                    textField.setText(filePaths.toString());
                    iTextFieldCallBack.onDragFiles(files);
                }
                event.setDropCompleted(success);
                event.consume();
            });
        }
        //</editor-fold>
        if (iTextFieldCallBack != null) {
            // 添加回车按键监听
            textField.setOnAction(actionEvent -> {

                String trim = textField.getText().trim();
                iTextFieldCallBack.onClickEnter(textField, textField.getText(), startsWithWindowsDrive(trim));
            });

            // 添加ctrl+回车按键监听
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.ENTER && keyEvent.isControlDown()) {
                        // 插入换行符
                        textField.insertText(textField.getCaretPosition(), System.getProperty("line.separator"));
                        String trim = textField.getText().trim();
                        iTextFieldCallBack.onClickCtrlEnter(textField, textField.getText(), startsWithWindowsDrive(trim));
                    }
                }
            });
        }
        return textField;
    }

    public static Button getButton(String text, IClickCallBack iClickCallBack) {
        AtomicBoolean isLong = new AtomicBoolean(false);
        Button btn = new Button(text);
        if (iClickCallBack != null) {
            btn.setOnAction(event -> {//点击事件
                if (isLong.get()) {
                    isLong.set(false);
                    return;
                }
                iClickCallBack.onClick(btn);
            });

            final Timeline longPress = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                //长按事件
                iClickCallBack.onLongClick();
                isLong.set(true);
            }));
            longPress.setCycleCount(1);

            btn.setOnMousePressed(e -> longPress.playFromStart());
            btn.setOnMouseReleased(e -> longPress.stop());
        }

        return btn;
    }

    public static Label getLabel(String txt, IClick iClick) {
        Label lab = new Label(txt);
        // 为Label对象添加鼠标点击事件处理程序
        lab.setOnMouseClicked((MouseEvent event) -> {
            if (iClick != null) {
                iClick.click(lab, lab.getText());
                System.out.println("Label被点击了");
            }
        });

        return lab;
    }

    public static TextArea getTextArea(ITextAreaCallBack iTextAreaCallBack) {
        return getTextArea(iTextAreaCallBack, null);
    }

    /**
     * @param iTextAreaCallBack 回车监听
     * @param changeListener    文本选择监听
     * @return
     */
    public static TextArea getTextArea(ITextAreaCallBack iTextAreaCallBack, ChangeListener<String> changeListener) {
        TextArea textArea = new TextArea();
        // 监听选择文本的更改
        if (changeListener != null) {
            textArea.selectedTextProperty().addListener(changeListener);
        }

        textArea.setOnKeyPressed(event -> {
            if (iTextAreaCallBack == null) return;
            if (event.getCode() == KeyCode.ENTER && event.isControlDown()) {
                // 插入换行符
                textArea.insertText(textArea.getCaretPosition(), System.getProperty("line.separator"));
                iTextAreaCallBack.ctrlEnter(textArea);
                System.out.println("Ctrl+Enter key pressed");
            } else if (event.getCode() == KeyCode.ENTER) {
                iTextAreaCallBack.enter(textArea);
                System.out.println("Enter key pressed");
            }
        });
        return textArea;
    }

    /**
     * 选择文件
     */
    public static void selFiles(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("所有文件", "*.*"));

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                System.out.println(file.getAbsolutePath());
            }
        }
    }

    public static MenuItem getMenuItem(String btnName, EventHandler<ActionEvent> eventHandler) {
        // 创建菜单项
        MenuItem clearItem1 = new MenuItem(btnName);
        clearItem1.setOnAction(eventHandler);
        return clearItem1;
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
     * 自定义控件
     */
    public interface IClickCallBack {

        void onClick(Button btn);

        default void onLongClick() {

        }
    }

    /**
     * 自定义控件
     */
    public interface ITextFieldCallBack {

        void onClickEnter(TextField textField, String text, boolean isFile);

        /**
         * @param isFile 是否为拖拽的文件/也有可能是文件夹
         */
        default void onClickCtrlEnter(TextField textField, String text, boolean isFile) {

        }

        /**
         * @param files 拖拽文件
         */
        default void onDragFiles(List<File> files) {

        }
    }


    public interface IClick {
        /**
         * @param obj  对象
         * @param text 文字
         */
        void click(Object obj, String text);

        default void longClick(Object obj) {

        }
    }

    public interface ITextAreaCallBack {

        default void ctrlEnter(TextArea textArea) {

        }

        default void enter(TextArea textArea) {

        }
    }

}
