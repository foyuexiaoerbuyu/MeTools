package com.panel.tab;


import com.other.Constant;
import com.zhihu.ZhihuUtils;
import com.zhihu.other.CommUtils;
import org.yifan.hao.StringUtil;
import org.yifan.hao.WinUtils;
import org.yifan.hao.swing.JswCustomWight;
import org.yifan.hao.swing.JswOnLongClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolsTab2 {

    private JComboBox<String> cbCategory;
    private JTextField tfSavePath = new JTextField();
    private JTextField tfArticleLink = new JTextField();
    private JTextField tfCollectionLink = new JTextField();
    private JButton button = new JButton("点击触发事件");
    private JFrame frame;
    private JLabel jLabelLog;
    private Timer timer;

    public Component getTab(JFrame frame) {
        this.frame = frame;
        //默认:  文档;社会;历史人物传记;笑一笑;赚钱
        cbCategory = new JComboBox<>(ZhihuUtils.getCategory());
        //左侧 x坐标 y坐标 宽度   右侧 x坐标 y坐标 宽度 高度 行间隔
        int labelX = 10, labelY = 10, labelW = 120, fieldX = 120, fieldY = 10, fieldW = 240, height = 30, hSpace = 40;

        JLabel label1 = new JLabel("默认保存路径:");
        label1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showOpenDialog(fileChooser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile()
                            .getAbsolutePath();//这个就是你选择的文件夹的路径
                    System.out.println("filePath = " + filePath);
                    tfSavePath.setText(filePath);
                    CommUtils.setPrivateInfo(Constant.CONFIG_KEY_SAVE_PATH, filePath);
                }
            }
        });
        label1.setBounds(labelX, labelY, labelW, height);

        JLabel label2 = new JLabel("选择保存分类:");
        label2.setBounds(labelX, labelY = labelY + hSpace, labelW, height);
        label2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String inputContent = JOptionPane.showInputDialog(
                        frame, "输入保存分类,用英文\";\"分割", ZhihuUtils.getCategoryStr());
                ZhihuUtils.setCategory(inputContent);
                String[] categorys = ZhihuUtils.getCategory();
                cbCategory.removeAllItems();
                Constant.TAG_ADD_CATEGORY = true;
                for (String category : categorys) {
                    cbCategory.addItem(category);
                }
            }
        });
        JLabel label3 = new JLabel("输入文章链接:");
        label3.setBounds(labelX, labelY = labelY + hSpace, labelW, height);
        JLabel label4 = new JLabel("输入收藏链接:");
        label4.setBounds(labelX, labelY + hSpace, labelW, height);
        tfSavePath.setBounds(fieldX, fieldY, fieldW, height);
        cbCategory.setBounds(fieldX, fieldY = fieldY + hSpace, fieldW, height);
        tfArticleLink.setBounds(fieldX, fieldY = fieldY + hSpace, fieldW, height);
        tfCollectionLink.setBounds(fieldX, fieldY = fieldY + hSpace, fieldW, height);
        int btnW = 140;
        button.setBounds(120, fieldY + hSpace + 10, btnW, height);
        cbCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Constant.TAG_ADD_CATEGORY) {
                    Constant.TAG_ADD_CATEGORY = false;
                    return;
                }
                if (tfCollectionLink.isEnabled() &&
                        !tfCollectionLink.getText().equals(Constant.ENABLE_TXT_TFCOLLECTIONLINK)) {
                    return;
                }
                cbCategory.setPopupVisible(false);
                if (cbCategory.getSelectedItem() == null) {
                    return;
                }
                saveArticleToLocal(cbCategory.getSelectedItem().toString());
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tfCollectionLink.isEnabled() && !StringUtil.isEmpty(tfCollectionLink.getText())
                        && !tfCollectionLink.getText().equals(Constant.ENABLE_TXT_TFCOLLECTIONLINK)) {
                    //下载收藏
                    saveCollectionLink();
                } else {
                    //下载文章
                    if (cbCategory.getSelectedItem() == null) {
                        return;
                    }
                    saveArticleToLocal(cbCategory.getSelectedItem().toString());
                }
            }
        });
        Container _ONE = new Container();
        String[] category = ZhihuUtils.getCategory();
        //动态生成按钮
//        if (category != null) {
//            for (int i = 0; i < category.length; i++) {
//                JButton btn = new JButton(category[i]);
//                btn.setBounds(10, 150, btn.getText().length() * 30, height);//参数:前两个是坐标,后两个是宽高
//
//                label2.setBounds(160, 250, labelW, height);
//                _ONE.add(btn);
//            }
//        }
        _ONE.add(label1);
        _ONE.add(label2);
        _ONE.add(label3);
        _ONE.add(label4);
        _ONE.add(tfSavePath);
        _ONE.add(cbCategory);
        _ONE.add(tfArticleLink);
        _ONE.add(tfCollectionLink);
        _ONE.add(button);
        // 创建流布局面板
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBounds(0, 220, 430, 300);
        String[] categorys = ZhihuUtils.getCategory();
        for (String btnCategory : categorys) {
            // 添加按钮到面板
            panel.add(JswCustomWight.getJButtonMargin(btnCategory, new JswOnLongClickListener() {
                @Override
                public void onClick() {
                    saveArticleToLocal(btnCategory);
                }
            }));
        }

        jLabelLog = JswCustomWight.getJLabel(500, 100, "", null);
        panel.add(jLabelLog);

        // 将面板添加到容器
        _ONE.add(panel);
        tfSavePath.setText(
                CommUtils.getPrivateInfo(Constant.CONFIG_KEY_SAVE_PATH, Constant.DEF_SAVE_PATH));
//        tfArticleLink.setText("请输入文章链接");
        tfCollectionLink.setText(Constant.ENABLE_TXT_TFCOLLECTIONLINK);
        tfCollectionLink.setEnabled(false);

        return _ONE;
    }

    private void saveCollectionLink() {
        ZhihuUtils.saveCollect(tfSavePath.getText(), tfCollectionLink.getText(),
                (saveMsg, saveSuccess) -> {
                    JOptionPane
                            .showMessageDialog(frame,
                                    tfCollectionLink.getText() + "\n" + saveMsg,
                                    "提示", JOptionPane.WARNING_MESSAGE);

                });
//                System.out.println(tfSavePath.getText() + tfArticleLink.getText() );
    }

    /**
     * 保存文章到本地文件
     */
    private void saveArticleToLocal(String saveCategory) {
        if (StringUtil.isBlank(tfSavePath.getText())) {
            JOptionPane.showMessageDialog(frame, "保存路径不能为空", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String savePath = tfSavePath.getText() + saveCategory;
        System.out.println(" 下拉框选择点击条目 " + saveCategory);
        //1.剪切板优先
        String articleUrl = WinUtils.getSysClipboardText();
        //2.文章链接
        if (StringUtil.isEmpty(articleUrl)) {
            articleUrl = tfArticleLink.getText();
        }
        //3.保存文章
        ZhihuUtils.saveArticle(savePath, articleUrl,
                (saveMsg, saveSuccess) -> {
                    if (saveSuccess) {
                        jLabelLog.setText(saveMsg);
                        // 停止之前的定时器（如果存在）
                        if (timer != null && timer.isRunning()) {
                            timer.stop();
                        }
                        timer = new Timer(500, e -> jLabelLog.setText(""));
                        timer.start();
                        return;
                    }
                    JOptionPane.showMessageDialog(frame, saveMsg, "消息提醒", JOptionPane.WARNING_MESSAGE);
                });
        //4.清空文章链接输入框
        tfArticleLink.setText("");
    }

}
