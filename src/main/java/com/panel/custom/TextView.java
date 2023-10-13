package com.panel.custom;

import javafx.scene.text.Text;

import java.awt.*;

public class TextView extends Text {

    public TextView() throws HeadlessException {
    }

    public TextView(String text) throws HeadlessException {
        super(text);
        setText(text);
    }
}

