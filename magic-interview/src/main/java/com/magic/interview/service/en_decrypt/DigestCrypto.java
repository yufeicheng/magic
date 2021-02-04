package com.magic.interview.service.en_decrypt;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法
 * @author Cheng Yufei
 * @create 2021-02-04 3:22 下午
 **/
public class DigestCrypto {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String data = "摘要算法-MD5";
        System.out.println(md5(data));
        System.out.println(md52(data));
    }

    /**
     * MD5: 产生16字节的校验值，通常用32位16进制表示。
     * @param data
     * @return
     */
    public static String md5(String data) {
        return DigestUtils.md5Hex(data);
    }

    public static String md52(String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(data.getBytes());
        byte[] digest = messageDigest.digest();
        //16字节
        System.out.println(digest.length);
        //return new BigInteger(1, digest).toString(16);
        return Hex.encodeHexString(digest);
    }

    /**
     * 摘要：
     * 1.通过对所有数据提取指纹信息以实现数据签名、数据完整性校验等功能，由于其不可逆性，有时候会被用做敏感信息的加密。数据摘要算法也被称为哈希（Hash）算法或散列算法。
     *
     * 2.消息摘要算法的主要特征是加密过程不需要密钥，并且经过加密的数据无法被解密，只有输入相同的明文数据经过相同的消息摘要算法才能得到相同的密文
     *
     * 3.无论输入的消息有多长，计算出来的消息摘要的长度总是固定的。一般地，只要输入的消息不同，对其进行摘要以后产生的摘要消息也必不相同；
     *     但相同的输入必会产生相同的输出。只能进行正向的信息摘要，而无法从摘要中恢复出任何的消息，甚至根本就找不到任何与原信息相关的信息（不可逆性）。
     *
     * 4. 一个信息的摘要为该信息的指纹或数字签名。
     *     a。 数字签名保证消息的完整性、不可否认性。
     *            完整性：信息源发送后，中间无任何更改。
     *            不可否认性：信源不能否认曾经发送过的信息。
     *            不可逆性：无法根据签名还原被签名的消息的任何信息
     *
     * 5. 摘要算法：
     *    CRC32、MD5、SHA1、SHA256【32 字节（256位）】、SHA384、SHA512【64 字节（512位）】
     *
     *    SHA产生的数据摘要的长度更长，因此更难以发生碰撞，因此也更为安全,比MD5慢。
     */
}
