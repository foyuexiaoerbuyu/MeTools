package com.qrcode;//package com.qrcode;
//
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.nio.file.Path;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * 使用
// * QRCodeFrame qrCodeFrame = new QRCodeFrame();
// * qrCodeFrame.showDialog("我和我的祖国");
// */
//public class QRCodeFrame extends JFrame {
//
//    private Timer updateTitleTimer = new Timer();
//    private Integer time = 10;
//    private String imagePath;
//
//    //外部调用这个函数加载窗口，显示二维码
//    public void launchFrame(String imgPath) {
//        this.imagePath = imgPath;
//        updateFrameTitle();
//        this.setVisible(true);
//        this.setSize(420, 440);//大小
//        this.setLocation(100, 100);//左上角位置
//        //指定要从本地加载的二维码的路径
//
////        JPanel jPanel = new JPanel();
////        jPanel.getGraphics().drawImage(new ImageIcon(imagePath).getImage(), 0, 0, 400, 400, jPanel);
//        JPanel jPanel = new QRCodeFrameImage(this.imagePath);
//        this.getContentPane().add(jPanel);
//        //设置始终在窗体层最上面
//        this.setAlwaysOnTop(true);
//    }
//
//    public void closeWindows() {
//        //setVisible方法仅仅隐藏窗体，而dispose方法是关闭窗体，并释放一部分资源。
//        this.dispose();
//        if (null != updateTitleTimer) {
//            updateTitleTimer.cancel();
//            updateTitleTimer = null;
//        }
//    }
//
//    private void updateFrameTitle() {
//        if (null == updateTitleTimer) {
//            return;
//        }
//        //定时器中发布任务，每1000ms执行一次，执行后递归调用自己
//        updateTitleTimer.schedule(new TimerTask() {
//            public void run() {
//                time--;
//                if (time < 1) {
//                    //关闭窗口
//                    closeWindows();
//                    //将创建的二维码删除
//                    deleteFile(imagePath);
//                }
//                setTitle(String.format("请使用微信扫描二维码,%s秒后自动关闭", time));
//                updateFrameTitle();
//            }
//        }, 500);
//    }
//
//    public void deleteFile(String filePath) {
//        File file = new File(filePath);
//        if (file.exists()) {
//            file.delete();
//        }
//    }
//
//    public int getTime() {
//        return time;
//    }
//
//    /**
//     * 生成二维码方法
//     *
//     * @param content 内容
//     * @param file    路径
//     */
//    public void createQrcode(String content, Path file) throws Exception {
//        try {
//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
//            BitMatrix bm = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400);
//            MatrixToImageWriter.writeToPath(bm, "png", file);
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//    }
//
//    /**
//     * @param content 二维码内容
//     */
//    public void showDialog(String content) throws Exception {
//        String sysPath = FileUtils.getSysPath("user.home") + "MyTools" + File.separator;
//        FileUtils.createDir(sysPath);
//        Path of = Path.of(sysPath + "tmp.png");
//        createQrcode(content, of);
//        launchFrame(of.toFile().getAbsolutePath());
//    }
//
//
//    private static class QRCodeFrameImage extends JPanel {
//        private String imagePath;
//
//        public QRCodeFrameImage(String imagePath) {
//            this.imagePath = imagePath;
//        }
//
//        public void paint(Graphics g) {
//            super.paint(g);
//            ImageIcon icon = new ImageIcon(imagePath);
//            g.drawImage(icon.getImage(), 0, 0, 400, 400, this);
//        }
//
//    }
//
//}
