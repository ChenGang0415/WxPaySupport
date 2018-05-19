package com.lalameow.wxpaysupport.network.uitls;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 创建人: LaLaMeow-Chen
 * 日期: 2018/5/19
 * 时间: 14:56
 * 功能：请进行修改
 */
public class QRCodeUtils {
    public static String decode(byte[] imgage) {
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(
                            ImageIO.read(new ByteArrayInputStream(imgage))
                    )));
            Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
            return qrCodeResult.getText();
        } catch (NotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateQR(String text, int width, int height)  {
        try {
            Map<EncodeHintType, Object> params = new HashMap<>();
            params.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            params.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, params);
            return toAscii(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toAscii(BitMatrix bitMatrix) {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < bitMatrix.getHeight(); r++) {
            for (int c = 0; c < bitMatrix.getWidth(); c++) {
                if (!bitMatrix.get(r, c)) {
                    builder.append("\033[47m   \033[0m");
                } else {
                    builder.append("\033[40m   \033[0m");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
