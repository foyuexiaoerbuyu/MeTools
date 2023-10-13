package com.panel.custom;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class SceneChange extends Application {
    Scene scene1,  scene2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 场景1
        Button button1 = new Button("场景1 的button");

        // 事件监听 点击后切换到场景2
        button1.setOnMouseClicked(e -> {
            primaryStage.setScene(scene2);
        });

        VBox vBox = new VBox();
        vBox.getChildren().add(button1);
        scene1 = new Scene(vBox, 400, 400);

        // 场景2
        Button button2 = new Button("场景2 的button");

        // 事件监听 点击后切换到场景1
        button2.setOnMouseClicked(e -> {
            primaryStage.setScene(scene1);
        });

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(button2);
        scene2 = new Scene(stackPane, 400, 400);

        primaryStage.setScene(scene1);
        primaryStage.show();
    }
}


