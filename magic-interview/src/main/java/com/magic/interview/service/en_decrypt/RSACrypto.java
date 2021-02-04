package com.magic.interview.service.en_decrypt;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.util.Base64;

/**
 *
 * @author Cheng Yufei
 * @create 2021-02-02 5:16 下午
 **/
public class RSACrypto {

    /**
     * 非对称加密: RSA.
     * 1. 公钥加密，私钥解密
     */
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        String data = "RSA 加解密";
        String encrypt = encrypt(data.getBytes());
        System.out.println(encrypt);
        System.out.println(">>>>>>");
        System.out.println(decrypt(Base64.getDecoder().decode(encrypt)));
    }

    public static String encrypt(byte[] data) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        InputStream inputStream = new PathMatchingResourcePatternResolver().getResource("classpath:key.pem").getInputStream();

        PEMParser pemParser = new PEMParser(new InputStreamReader(inputStream));
        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
        KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(((PEMKeyPair) pemParser.readObject()));
        PublicKey publicKey = keyPair.getPublic();

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println(publicKeyString);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(data);

        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(byte[] data) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        InputStream inputStream = new PathMatchingResourcePatternResolver().getResource("classpath:key.pem").getInputStream();

        PEMParser pemParser = new PEMParser(new InputStreamReader(inputStream));
        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
        KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(((PEMKeyPair) pemParser.readObject()));
        PrivateKey privateKey = keyPair.getPrivate();


        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println(privateKeyString);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(data);
        return new String(bytes);
    }
}
