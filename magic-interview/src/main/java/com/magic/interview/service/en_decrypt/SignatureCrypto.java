package com.magic.interview.service.en_decrypt;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.util.Base64;

/**
 * 加签、验签
 * @author Cheng Yufei
 * @create 2021-02-04 3:58 下午
 **/
public class SignatureCrypto {

    /**
     * 用私钥加密内容，形成数字签名。
     * 接受人公钥验证【数字证书】，确保内容是真的由某个人发布的，而不是其他人
     *
     *
     */
    public static void main(String[] args) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, IOException {

        String data = "加签、验签,SHA1=RSA";
        String sign = signWithRSA(data);
        System.out.println(sign);
        System.out.println(checkSignWithRSA(data, Base64.getDecoder().decode(sign)));

    }

    public static String signWithRSA(String data) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPair keyPair = getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        //SHA1 摘要算法进行加签，用 RSA 算法进行加密
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public static boolean checkSignWithRSA(String data, byte[] sign) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPair keyPair = getKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        return signature.verify(sign);

    }

    public static KeyPair getKeyPair() throws IOException {

        InputStream inputStream = new PathMatchingResourcePatternResolver().getResource("classpath:key.pem").getInputStream();

        PEMParser pemParser = new PEMParser(new InputStreamReader(inputStream));
        PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
        KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
        return keyPair;
    }

    /**
     * 银行 A 发布了一个银行客户端的补丁供所有用户更新，那为了确保人家下载的是正确完整的客户端，银行A会为这个程序打上一个数字签名（就是用银行A的私钥对这个程序加密然后发布）
     * ，你需要在你的电脑里装上银行 A 的数字证书（就是银行对外发布的公钥），然后下载好这个程序，数字证书会去解密这个程序的数字签名，解密成功，补丁得以使用。
     * 同时你能知道这个补丁确实是来自这个银行 A，是由他发布的，而不是其他人发布的。
     */
}
