package com.panel;

import com.other.Constant;
import com.panel.custom.EditText;
import com.panel.custom.GridLayout;
import com.panel.custom.TextView;
import com.panel.custom.WindowAlert;

import com.zhihu.ZhihuUtils;
import com.zhihu.ZhihuUtils.SaveCallback;
import com.zhihu.other.CommUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.yifan.hao.FileUtils;

//窗口          Stage
//  -场景       Scene
//    -布局     stackPane
//      -控件   Button
public class PanelDownload extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
//        HBox hbox = new HBox();
        String lastSavePath = CommUtils.getPrivateInfo("lastSavePath", Constant.DEF_SAVE_PATH);
        TextView seltPath = new TextView(lastSavePath);
        Button seltPathBtn = new Button("选择路径");
        seltPathBtn.setOnMouseClicked(event -> {
            //选择保存文件夹路径
            String path = new DirectoryChooser().showDialog(stage).getPath();
            seltPath.setText(path);
            CommUtils.setPrivateInfo("lastSavePath", path);
            FileUtils.writeFile(FileUtils.getDesktopPath() + "debug.log", "lastSavePath: " + path);
        });

        EditText articleEdt = new EditText("请输入文章链接");
        Button articleBtn = new Button("保存文章");
        articleBtn.setOnMouseClicked(event -> {
            //获取文章链接
            System.out.println("获取文章链接:" + articleEdt.getText());
            if (StringUtils.isBlank(articleEdt.getText())) {
                WindowAlert.display("警告", "链接不能为空");
                return;
            }
            ZhihuUtils.saveArticle(seltPath.getText(), articleEdt.getText(),
                (saveName, saveSuccess) -> {
                    WindowAlert.display("提示", saveName);
                });
        });

        EditText collectionEdt = new EditText("请输入收藏链接Id");
        Button collectionBtn = new Button("保存收藏");
        collectionBtn.setOnMouseClicked(event -> {
            //获取收藏id
            if (StringUtils.isBlank(collectionEdt.getText())) {
                WindowAlert.display("警告", "收藏id不能为空");
                return;
            }
            System.out.println("获取收藏id:" + collectionBtn.getText());
            ZhihuUtils.saveCollect(seltPath.getText(), collectionEdt.getText(), new SaveCallback() {
                @Override
                public void callback(String saveMsg, boolean saveSuccess) {
                    WindowAlert.display("警告", saveMsg);
                }
            });
        });
        GridLayout gridLayout = new GridLayout(3, 2);
        gridLayout
            .addView(seltPath, seltPathBtn, articleEdt, articleBtn, collectionEdt, collectionBtn);
        stage.setScene(new Scene(gridLayout)); // 把水平箱子放到边界窗格的中央
        stage.show();
    }

    public void handleClose() {
        // 接收窗体返回值
        boolean ret = WindowAlert.display("关闭窗口", "是否关闭窗口？");
        System.out.println(ret);
        if (ret) {
            stage.close();
        }

    }
}

