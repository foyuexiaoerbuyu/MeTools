package com.panel.custom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridLayout extends GridPane {

    private int line;
    private int column;

    private GridLayout() {
    }

    /**
     * @param line 行
     * @param column 列
     */
    public GridLayout(int line, int column) {
        this.line = line;
        this.column = column;
        //控件居中对齐
        setAlignment(Pos.CENTER);
        //组件与组件之间水平间距
        setHgap(15.5);
        //组件与组件之间纵向间距
        setVgap(15.5);
        //组件到面板之间的内边距
        setPadding(new Insets(10));
    }

//    public static void main(String[] args) {
//        int line = 3;//行
//        int column = 2;//列
//        String[] names = {"tv", "按钮", "编辑框文章", "按钮文章", "编辑框收藏", "按钮收藏"};
//        int tag = 0;
//        for (int i = 0; i < line; i++) {
//            for (int j = 0; j < column; j++) {
//                if (tag < names.length) {
//                    System.out.println(names[tag++] + "坐标:[" + i + "," + j + "]");
//                }
//            }
//        }
//    }

    public GridLayout addView(Node... node) {
        int tag = 0;
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < column; j++) {
                if (tag < node.length) {
                    add(node[tag++], j, i);
                }
            }
        }
        return this;
    }

}

