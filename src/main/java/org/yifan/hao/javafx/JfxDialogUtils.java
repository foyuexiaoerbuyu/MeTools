package org.yifan.hao.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Timer;
import java.util.TimerTask;

public class JfxDialogUtils {

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.show();
    }

    public static void showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.show();
    }

    public static void showMsgDialog(String title, String message, int delayCloseTime) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (alert.isShowing()) {
                        alert.setResult(ButtonType.CLOSE); // 模拟点击关闭按钮
                    }
                });
            }
        }, delayCloseTime);
    }

    public static void showMsgDialog() {
        showMsgDialog("提示", "操作成功！", 3000);
    }

    public static void showMsgDialog(String message) {
        showMsgDialog("提示", message, 3000);
    }

    public static void showMsgDialog(String message, int delayCloseTime) {
        showMsgDialog("提示", message, delayCloseTime);
    }

//    public static void showInputDialog(String title, String message, String hintText, ICloseCallBack iCloseCallBack) {
//        TextInputDialog dialog = new TextInputDialog();
//        dialog.setTitle(title);
//        dialog.setHeaderText("提示");
//        dialog.setContentText(message);
//        // 获取编辑器
//        TextInputControl editor = dialog.getEditor();
//// 设置默认文本
//        editor.setText(hintText);
//        dialog.initStyle(StageStyle.UNDECORATED);
////        dialog.setOnCloseRequest(event -> iCloseCallBack.inputText(dialog.getEditor().getText()));
//// 获取对话框面板
//        DialogPane dialogPane = dialog.getDialogPane();
//// 获取确定按钮
//        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
//// 添加按钮点击监听器
//        okButton.addEventHandler(ActionEvent.ACTION, event -> {
//            // 处理按钮点击事件
//            String inputText = dialog.getEditor().getText();
////            System.out.println("输入的名字是：" + inputText);
//            if (iCloseCallBack != null) {
//                iCloseCallBack.inputText(inputText);
//            }
//        });
//        dialog.show();
//    }


    public static void showInputDialog(InputDialogCallBack inputDialogCallBack) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setWidth(200);
        dialog.setHeight(200);

        TextArea content = new TextArea();
        content.setWrapText(true);
        content.setPrefHeight(100);

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.getChildren().addAll(JfxCustomWight.getButton("保存", btn -> {
            inputDialogCallBack.onSave(content.getText());
            dialog.close();
        }), JfxCustomWight.getButton("关闭", btn -> {
            inputDialogCallBack.onClose(content.getText());
            dialog.close();
        }));

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.getChildren().addAll(content, flowPane);

        Scene dialogScene = new Scene(vBox, 200, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }


    /**
     * 包含标题输入框和内容输入框的弹框
     */
    public static void showTitleContentInputDialog(InputTitleContentDialogCallBack inputDialogCallBack) {
//(String title, String message, String hintText,
        Stage dialog = new Stage();
        dialog.setTitle("提示");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setWidth(200);
        dialog.setHeight(200);

        TextField title = new TextField();
        title.setPrefHeight(20);
        TextArea content = new TextArea();
        content.setPrefHeight(80);
        content.setWrapText(true);

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.getChildren().addAll(JfxCustomWight.getButton("保存", btn -> {
            inputDialogCallBack.onClose(title.getText(), content.getText());
            dialog.close();
        }), JfxCustomWight.getButton("关闭", btn -> {
//            inputDialogCallBack.onClose(title.getText(), content.getText());
            dialog.close();
        }), JfxCustomWight.getButton("取消", btn -> {
//            inputDialogCallBack.onClose(title.getText(), content.getText());
            dialog.close();
        }));

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.getChildren().addAll(title, content, flowPane);

        Scene dialogScene = new Scene(vBox, 200, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    // 生成二维码图片
    public static void showQrImgDialog(String msg) {
        DialogPane grid = new DialogPane();
        grid.setPadding(new Insets(5));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Image qrCodeImage = generateQRCodeImage(msg);
        ImageView customImage = new ImageView(qrCodeImage);

        Label label = new Label(msg);

        vBox.getChildren().addAll(customImage, label);
        grid.setContent(vBox);


        Dialog<String> dlg = new Dialog<>();
        Window window = dlg.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event1 -> window.hide());
        dlg.setDialogPane(grid);
        dlg.show();
    }

    private static Image generateQRCodeImage(String text) {
        // TODO: 2023/10/13
        //使用qrgen库生成二维码
//        byte[] qrCodeBytes = QRCode.from(text).to(ImageType.PNG).stream().toByteArray();
//        return new Image(new ByteArrayInputStream(qrCodeBytes));
        return null;
    }


    /**
     * 自定义控件
     */
    public interface InputDialogCallBack {

        void onSave(String content);

        default void onClose(String content) {

        }
    }

    /**
     * 自定义控件
     */
    public interface InputTitleContentDialogCallBack {

        void onClose(String title, String content);
    }

}