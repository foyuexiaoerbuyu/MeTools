package com.panel.custom;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class WindowAlert {

    public static boolean answer;

    /**
     * @param title 标题
     * @param msg 消息
     */
    public static boolean display(String title, String msg) {
        // 创建舞台
        Stage stage = new Stage();

        // 设置显示模式
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        // 创建控件
        Button buttonYes = new Button("确定");
        buttonYes.setOnMouseClicked(event -> {
            answer = true;
            stage.close();
        });

        Label label = new Label(msg);

        // 创建布局
        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, buttonYes);
        vBox.setAlignment(Pos.CENTER); // 布局居中显示
        // 创建场景
        Scene scene = new Scene(vBox, 100, 100);
        // 显示舞台
        stage.setScene(scene);
//        stage.show();
        stage.showAndWait();  // 等待窗体关闭才继续
        // 窗体返回值
        return answer;
    }
}

