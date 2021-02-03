package com.magic.interview.service.en_decrypt;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 *
 * @author Cheng Yufei
 * @create 2021-02-02 5:16 下午
 **/
public class DESCrypt {

    /**
     * 对称加密: DES
     * 密钥长度必须大于等于8，大于8的话截取前8位。
     */
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String key = "Candice@";
        String data = "DES 加解密";
        String encrypt = encrypt(key.getBytes(), data.getBytes(StandardCharsets.UTF_8));
        System.out.println(encrypt);
        System.out.println(decrypt(key.getBytes(), Base64.getDecoder().decode(encrypt)));
    }

    public static String encrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException {

        //用原始密钥 创建DESKeySpec，此方式获取的Key初始化Cipher 设置的密钥长度可大于8
       /* DESKeySpec desKeySpec = new DESKeySpec(key);

        //密钥工厂将 KeySpec 转为 SecretKey
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);*/

        //此方式获取的Key初始化Cipher 设置的密钥长度只能等于8
        SecretKeySpec keySpec = new SecretKeySpec(key, "DES");

        Cipher instance = Cipher.getInstance("DES");
        instance.init(Cipher.ENCRYPT_MODE, keySpec, new SecureRandom());
        byte[] result = instance.doFinal(data);
        return Base64.getEncoder().encodeToString(result);
    }

    public static String decrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException {
        DESKeySpec desKeySpec = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        //SecretKeySpec keySpec = new SecretKeySpec(key, "DES");

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new SecureRandom());

        byte[] result = cipher.doFinal(data);
        return new String(result, StandardCharsets.UTF_8);
    }
}
