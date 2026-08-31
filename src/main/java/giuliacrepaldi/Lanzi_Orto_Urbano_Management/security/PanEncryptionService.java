package giuliacrepaldi.Lanzi_Orto_Urbano_Management.security;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PanEncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;


    //1. GENERAZIONE CHIAVE DI SICUREZZA 256 BIT (da salvare in un KMS protetto)
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }


    //2. CIFRATURA DEL PAN
    public static String encryptPan(String rawPan, SecretKey key) throws Exception {

        //Genera un IV casuale unico per questa operazione
        byte[] iv = new byte[IV_LENGTH_BYTE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte[] cipherText = cipher.doFinal(rawPan.getBytes());

        //Unisce IV e Testo Cifrato in un unico array per facilitare il salvataggio nel DB
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        byte[] encryptedPayload = byteBuffer.array();

        //Ritorna una string in Base64 pronte per il DB
        return Base64.getEncoder().encodeToString(encryptedPayload);
    }


    //3. DECIFRATURA DEL PAN
    public static String decryptPan(String encryptedPanBase64, SecretKey key) throws Exception {
        byte[] encryptedPayload = Base64.getDecoder().decode(encryptedPanBase64);

        //Estrae l'IV (i primi 12 byte)
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedPayload);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byteBuffer.get(iv);

        //Estrae il testo cifrato rimanente
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        byte[] decryptedText = cipher.doFinal(cipherText);
        return new String(decryptedText);
    }


}
