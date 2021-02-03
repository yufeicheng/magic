package com.magic.interview.service.en_decrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

/**
 *
 * @author Cheng Yufei
 * @create 2021-02-03 3:47 下午
 **/
public class AESCrypt {


    /**
     * AES：支持三种密钥长度：16、24、32 字节，对应128、192、256位
     *
     * @param args
     */
    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {

        //字符数1
        System.out.println("加".length());
        //一个汉字3个字节
        System.out.println("加".getBytes(StandardCharsets.UTF_8).length);
        //一个汉字1个字节
        System.out.println("加".getBytes(StandardCharsets.ISO_8859_1).length);
        //一个汉字2个字节
        System.out.println("加".getBytes("GBK").length);
        //一个汉字2个字节
        System.out.println("加".getBytes("gb2312").length);


        String key = "qsx456wedc1234567890123456789加";
        String data = "AES 加解密？";
        String encrypt = encrypt(key.getBytes(), data.getBytes());
        System.out.println(encrypt);
        System.out.println(decrypt(key.getBytes(),Base64.getDecoder().decode(encrypt)));
    }

    public static String encrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] bytes = cipher.doFinal(data);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] bytes = cipher.doFinal(data);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
