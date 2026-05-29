package giuliacrepaldi.Lanzi_Orto_Urbano_Management.utilities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Component
public class BarcodeGenerator {

    public BufferedImage generateBarcode128(String barCodeGs1) throws Exception {

        Code128Writer writer = new Code128Writer();

        //CONFORMITA GS1
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.GS1_FORMAT, Boolean.TRUE);

        //CARATTERE FNC1
        String gs1String = barCodeGs1;
        if (!gs1String.startsWith("\u00f1")) {
            gs1String = "\u00f1" + gs1String;
        }

        //GENERAZIONE DELLA MATRICE 450PX x 150PX
        BitMatrix bitMatrix = writer.encode(barCodeGs1, BarcodeFormat.CODE_128, 400, 150, hints);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
