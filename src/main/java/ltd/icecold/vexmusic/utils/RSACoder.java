package ltd.icecold.vexmusic.utils;


import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class RSACoder {
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * -----BEGIN PUBLIC KEY-----
     * MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwO3mvg4Mi+CKPkx15btK
     * lP/SVRgeDbFAqNalPGUWktH4FCbr9cqsssO33pTLp8IBw3YsvWpoSOVoLOCzsYAI
     * t+IkMKXAs9ucRUPk2nz7aRIp8JhWq+JNnpkCKoBAUoZ2weLX4Pc3Q4oL81+wH3p+
     * zp9/5I1w/WNRBmRAFyHAIi5Rr3BTMnfz2kg8S7YSlkGxAtrHjQzIEAzp1ajWGKYb
     * haBw6DLfiVDZOp9mdFT5h1LjyJkzkoUwZcVGR5yEkmh1M1JTv9DOiD5ttVIbLUue
     * 3c3c7N3qrYAEGhm5jlWJKGlpBAa6vBjM5qeod2bka6RGYtocMXo34W6oAB087y+A
     * vQIDAQAB
     * -----END PUBLIC KEY-----
     */

    private static final int KEY_SIZE = 512;
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwO3mvg4Mi+CKPkx15btKlP/SVRgeDbFAqNalPGUWktH4FCbr9cqsssO33pTLp8IBw3YsvWpoSOVoLOCzsYAIt+IkMKXAs9ucRUPk2nz7aRIp8JhWq+JNnpkCKoBAUoZ2weLX4Pc3Q4oL81+wH3p+zp9/5I1w/WNRBmRAFyHAIi5Rr3BTMnfz2kg8S7YSlkGxAtrHjQzIEAzp1ajWGKYbhaBw6DLfiVDZOp9mdFT5h1LjyJkzkoUwZcVGR5yEkmh1M1JTv9DOiD5ttVIbLUue3c3c7N3qrYAEGhm5jlWJKGlpBAa6vBjM5qeod2bka6RGYtocMXo34W6oAB087y+AvQIDAQAB";

    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        return keyMap;

    }

    public static PublicKey string2PublicKey(String pubStr) throws Exception{
        byte[] keyBytes = Base64.getDecoder().decode(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    public static byte[] encryptByPublicKey(byte[] data) throws Exception {
        byte[] key = Base64.getDecoder().decode(PUBLICKEY);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(byte[] data) throws Exception {
        byte[] key = Base64.getDecoder().decode(PUBLICKEY);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }



}