package com.other;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.yifan.hao.WinUtils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码工具类
 */
public class QRCodeGenerator {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("QR Code Generator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setLayout(new FlowLayout());

            JTextField textField = new JTextField(30);
            JButton button = new JButton("Generate QR Code");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = textField.getText();
                    if (!text.isEmpty()) {
                        try {
                            BufferedImage qrCodeImage = generateQRCodeImage(text);
                            displayQRCode(qrCodeImage, text);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "Error generating QR Code: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            frame.setLocationRelativeTo(null);
            frame.add(textField);
            frame.add(button);
            frame.setVisible(true);
        });
    }

    public static BufferedImage generateQRCodeImage(String text) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map hints = new HashMap<>();
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        return image;
    }

    public static void displayQRCode(BufferedImage image, String text) {
        JFrame qrCodeFrame = new JFrame("QR Code");
        qrCodeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        qrCodeFrame.setSize(250, 350);
        qrCodeFrame.setLayout(new BorderLayout());
        qrCodeFrame.setLocationRelativeTo(null);
        JLabel label = new JLabel(new ImageIcon(image));
        label.setText(text);
        // 设置图片和文字的位置关系
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalAlignment(JLabel.CENTER);
        // 添加鼠标事件监听器
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 在此处执行点击事件操作
                WinUtils.setSysClipboardText(text);
                JOptionPane.showMessageDialog(qrCodeFrame, "已复制二维码信息!");
            }
        });
        qrCodeFrame.add(label, BorderLayout.CENTER);

        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qrCodeFrame.dispose();
            }
        });

        timer.setRepeats(false);
        timer.start();

        qrCodeFrame.setVisible(true);
    }


    private static BufferedImage generateQRCodeImage(String data, int size) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size, size, hints);

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int value = bitMatrix.get(x, y) ? 0 : 0xFFFFFF;
                image.setRGB(x, y, value);
            }
        }

        return image;
    }

    public static void showQrCode(String text) throws Exception {
        BufferedImage qrCodeImage = generateQRCodeImage(text, 250);
        displayQRCode(qrCodeImage, text);
    }
}