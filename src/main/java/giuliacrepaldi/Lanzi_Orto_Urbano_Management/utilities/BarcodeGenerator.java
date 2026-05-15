package giuliacrepaldi.Lanzi_Orto_Urbano_Management.utilities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class BarcodeGenerator {

    public BufferedImage generateBarcode128(String barCodeGs1) throws Exception {

        Code128Writer writer = new Code128Writer();

        BitMatrix bitMatrix = writer.encode(barCodeGs1, BarcodeFormat.CODE_128, 400, 150);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
