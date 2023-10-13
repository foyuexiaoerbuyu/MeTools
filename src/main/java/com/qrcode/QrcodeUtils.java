package com.qrcode;//package com.qrcode;
//
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//import java.nio.file.Path;
//
//public class QrcodeUtils {
//    /**
//     * 生成二维码方法
//     *
//     * @param content 内容
//     * @param file    路径
//     */
//    public static void createQrcodeToFile(String content, Path file) throws Exception {
//        try {
//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
//            BitMatrix bm = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400);
//            MatrixToImageWriter.writeToPath(bm, "png", file);
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//    }
//}
