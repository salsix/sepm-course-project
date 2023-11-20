package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.exception.QrException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;

public class QrcodeUtil {

    public static BufferedImage generateQrCodeImage(String barcodeText) {
        try {
            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 250, 250);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException writerException) {
            throw new QrException("Cannot create QR-Code");
        }


    }

}
