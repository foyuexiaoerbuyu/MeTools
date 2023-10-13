package com.panel.custom;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public class EditText extends TextField implements ChangeListener {

    public EditText() {
        setEditable(true); // 设置单行输入框能否编辑
        setAlignment(Pos.CENTER_LEFT); // 设置单行输入框的对齐方式
        setPrefColumnCount(11); // 设置单行输入框的推荐列数
//        this.addFocusListener()
//        textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue,
//                String newValue) {
//
//            }
//        });
    }

    public EditText(String text) {
//        super(text);
        setPromptText(text);
        setEditable(true); // 设置单行输入框能否编辑
//        setPromptText(text); // 设置单行输入框的提示语
        setAlignment(Pos.CENTER_LEFT); // 设置单行输入框的对齐方式
//        setPrefColumnCount(11); // 设置单行输入框的推荐列数
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        System.out.println("observable = " + observable + oldValue + newValue);
    }
}

