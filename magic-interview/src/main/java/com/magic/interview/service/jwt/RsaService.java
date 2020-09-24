package com.magic.interview.service.jwt;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;

/**
 * @author Cheng Yufei
 * @create 2020-09-24 15:29
 **/
public class RsaService {

	/**
	 * 2048 位 pkcs1 密钥
	 */
	private static final String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiZrlbaYsCf14cId0s+PR\n" +
			"rCeR08a/xyFhMqv0rpzLkn8vgP405M2b7qFRN2h0UqWLRlWJ+XCQnPpm1KeF10NX\n" +
			"j15BU7HpfI/jXxHvXl1pNOrmXKtyko8HaeXS6/oTaoNtOWBOjXxEWwtM38EdzAqg\n" +
			"rJmZJ0o3SrmdInzNIoUDmkG3LmtC8fyn4j/uq0shSs8FroRUBJSRvXKukrsSReSY\n" +
			"OL7vXiAEwVSXQlPAyjtHP6SFE+67Asr9HUjMmjP4L66nMhtFmIGubKNRFoMbkHrP\n" +
			"rmf7lPNM+kkPwhFky6TyuaqTrzOpyB3QeM7QT5DlJhrWKJbVcHK0d/XMRQ5BBFVT\n" +
			"qwIDAQAB\n" +
			"-----END PUBLIC KEY-----";

	private static final String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
			"MIIEogIBAAKCAQEAiZrlbaYsCf14cId0s+PRrCeR08a/xyFhMqv0rpzLkn8vgP40\n" +
			"5M2b7qFRN2h0UqWLRlWJ+XCQnPpm1KeF10NXj15BU7HpfI/jXxHvXl1pNOrmXKty\n" +
			"ko8HaeXS6/oTaoNtOWBOjXxEWwtM38EdzAqgrJmZJ0o3SrmdInzNIoUDmkG3LmtC\n" +
			"8fyn4j/uq0shSs8FroRUBJSRvXKukrsSReSYOL7vXiAEwVSXQlPAyjtHP6SFE+67\n" +
			"Asr9HUjMmjP4L66nMhtFmIGubKNRFoMbkHrPrmf7lPNM+kkPwhFky6TyuaqTrzOp\n" +
			"yB3QeM7QT5DlJhrWKJbVcHK0d/XMRQ5BBFVTqwIDAQABAoIBABo9SDyVcFZjWCEV\n" +
			"I2LeMXBDh2I7xvwg2FkCQ0E8svD3gcZ2Mv3iWzaw2jzXlT7hRGKgExRWq6zTbuJk\n" +
			"I3h95ed30Ls19NEE2xWY5O04oMQvesf3BXz++yntYkAPSr6H2z8Sp0gBh0NZL0qH\n" +
			"l7f92s+u5m6Aj3SXWmhmJfPMK3ixgaGgwNiOsp1RYIoHiNZ1UohKEqM8ayEvmCkr\n" +
			"Pltfs9BiMwpIMxe/RXrSAYaRwLTtuAQp/VmqWHV+Jg8EtoSRbh5u6MVRXBVq5Pe+\n" +
			"Xb8Mq3jyEMn8adp521plA5mu6yV3ZaLLRHBxrhnFt2bB17kvxMMoHx4V9ZlqN0zY\n" +
			"Xh+m4HECgYEAxXSFgApEwpz/TbM/8BrdHMbWVeSbE+UmWq8a0XrbzUTpXUx3Fo+U\n" +
			"oBnfpjgVHguLnvoLSGpoVWsHWyF1vENEODCvxIIYHTL1fa/wLaQGfJXseVEDrGM+\n" +
			"WyKGuzdhvkJ1pFOICvT07txIOAxGaxmAQ9EzWitz0hewhHeUsVBroLkCgYEAsmeP\n" +
			"jRW8n8PO9f5UwZXqZFrlGT2YOMHkr/lFL9bk1oET1gcv6xoqDVyeYKiTCForhU/y\n" +
			"ssgUmuBF1hHU4SDrbvzkZux04yDpxvlHsuCqw0dnAkZJpC1YeET9pPSspPDAzczF\n" +
			"BFVhPp/JXz5emoRo9U42t2N0g98Ytg0bmQCmPYMCgYAChx5aaEc/EpF2JjBQW5ev\n" +
			"EaCW0ullVM6r5If8XI1J0HMIXb08jbQCZLJnR1qF2vH7pAnW8H3LciZS9VezhEzw\n" +
			"RzdI1b2HSiq4ZDM38lye5bB0USQx5cdblVKSPQBEVkd5RhR8x2wHTsyh4w5XuqjY\n" +
			"tWnp4pqF+wWofHtr1bK7CQKBgE6i1SRz21N0hInrU8KMaOdZJThN7QW/eSSTtApV\n" +
			"J0LhXDPvsRBo1PZUx76FL2H0FNDLH4fsJyDpD/8+lt2wm/Ws5KP1P8RJYqIAiLjw\n" +
			"zHQMyfu3rYf/MMq6Zi7KZjrBn8pWotS5KYbn+WPQ4vQqvvS5R+bnoJjrwkGkX3C+\n" +
			"V4gvAoGAbG+uzK2f2dMPKlGLZtmDMrvmpySnbDW7mioeS9eHim4OUNvKRxhXsZM4\n" +
			"t9VryqT2Ne6zB4O4RQPSDZXenUyU4gAg5+8EovjtBeNaCwRNHpz3ffct/Q/jrqZn\n" +
			"6P3yATDTx6HRodTfoGVPM29xqdxXDjgMvFc8da+TXqjvlZT1Xzg=\n" +
			"-----END RSA PRIVATE KEY-----";

	private static Cipher decryptCipher;
	private static Cipher encryptCipher;

	public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
		init();

		String str = "RSA加密字符串";

		String encryptRSA = encryptRSA(str);
		System.out.println(">>加密后：" + encryptRSA);

		String decrypt = decrypt(encryptRSA);
		System.out.println(">>解密后：" + decrypt);

	}


	private static String encryptRSA(String srcString) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		byte[] encrypt = encryptCipher.doFinal(srcString.getBytes());
		return Base64.getEncoder().encodeToString(encrypt);
	}

	private static String decrypt(String data) {
		byte[] res;
		String decrypt = "";
		try {
			res = decryptCipher.doFinal(Base64.getDecoder().decode(data));
			decrypt = new String(res, "utf-8");
		} catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decrypt;
	}

	private static void init() {

		Security.addProvider(new BouncyCastleProvider());
		try (PEMParser reader = new PEMParser(new StringReader(publicKey))) {
			encryptCipher = Cipher.getInstance("RSA");
			PublicKey publicKey = new JcaPEMKeyConverter().setProvider("BC")
					.getPublicKey((SubjectPublicKeyInfo) reader.readObject());
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);


			decryptCipher = Cipher.getInstance("RSA");
			decryptCipher.init(Cipher.DECRYPT_MODE, getPriKey(privateKey));
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
			e.printStackTrace();
		}
	}

	private static PrivateKey getPriKey(String priKey) {
		Security.addProvider(new BouncyCastleProvider());
		//FileReader fileReader = new FileReader(new File("/Users/chengyufei/Downloads/rsa_private_key.pem"));
		StringReader stringReader = new StringReader(priKey);
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
		try (PEMParser pemParser = new PEMParser(stringReader)) {
			Object object = pemParser.readObject();
			KeyPair kp = converter.getKeyPair((PEMKeyPair) object);
			PrivateKey pk = kp.getPrivate();
			return pk;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
