package my.com.sains.teams.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by User on 15/12/2017.
 */

public class CipherAES {

    private static final String KEY = "55f83721e99d95874e9ca06ac307cea4";

    public static String aesEncode(String plainText) {
        try {
            byte[] raw = hexDecode("55f83721e99d95874e9ca06ac307cea4");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, skeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
//            String hash = Base64.encodeBase64String(encrypted);
            String hash = Base64.encodeToString(encrypted, Base64.DEFAULT);
            return hash;
        } catch (Exception var6) {
            return null;
        }
    }

    public static String aesDecode(String cipherText) {
        try {
            byte[] raw = hexDecode("55f83721e99d95874e9ca06ac307cea4");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(2, skeySpec);
//
            byte[] encrypted = Base64.decode(cipherText, Base64.DEFAULT);
            byte[] plain = cipher.doFinal(encrypted);
            return new String(plain);
        } catch (Exception var6) {
            return null;
        }
    }

    public static byte[] hexDecode(String hexInput) {
        byte[] bts = new byte[hexInput.length() / 2];

        for(int i = 0; i < bts.length; ++i) {
            bts[i] = (byte)Integer.parseInt(hexInput.substring(2 * i, 2 * i + 2), 16);
        }

        return bts;
    }
}
