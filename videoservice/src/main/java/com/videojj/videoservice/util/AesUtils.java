package com.videojj.videoservice.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES加密解密算法
 * @author WangKun
 * 2017年3月4日
 */
@Slf4j
public class AesUtils {

	/**
	 * AES加密解密算法
	 */
//	private static final String Algorithm = "AES";

	private static final String Algorithm = "AES/CBC/PKCS5Padding";

	private static final BouncyCastleProvider provider = new BouncyCastleProvider();

	/**
	 *
	 * @description: AES加密实现
	 * @param strkey
	 *            加密密钥
	 * @param src
	 *            加密前数据字节
	 * @return
	 */
	public static byte[] encryptMode(String strkey, byte[] src) {
		try {
			Security.addProvider(provider);

//			byte[] keybyte = string2Hex(strkey);
//			// 生成密钥
//			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//			secureRandom.setSeed(keybyte);
//			KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
//			keygen.init(128, secureRandom);
//			SecretKey sk = keygen.generateKey();
			SecretKeySpec sk = new SecretKeySpec(strkey.getBytes(),"AES");
			Cipher cip = Cipher.getInstance(Algorithm,provider);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec("inekcndsaqwertyi".getBytes());
			cip.init(Cipher.ENCRYPT_MODE, sk,paramSpec);
			return cip.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			log.error(e1.getMessage(), e1);
		} catch (javax.crypto.NoSuchPaddingException e2) {
			log.error(e2.getMessage(), e2);
		} catch (Exception e3) {
			log.error(e3.getMessage(), e3);
		}
		return null;
	}

	public static byte[] string2Hex(String src) {
		int length = (src.length() + 1 ) / 2 ;
		byte [] dst = new byte [length];
		int i;
		byte cTemp;

		for ( i = 0; i < src.length(); i++ ) {
			if( src.charAt(i) < 'A' )
				cTemp = (byte) (src.charAt(i) - '0');
			else if( src.charAt(i) < 'a' )
				cTemp = (byte) (src.charAt(i) - 'A' + 10);
			else
				cTemp = (byte) (src.charAt(i) - 'a' + 10);

			if( i % 2 == 1 )
				dst[i/2] |= cTemp;
			else
				dst[i/2] = (byte) (cTemp << 4);
		}
		return dst;
	}

	/**
	 *
	 * @description: AES加密算法入口
	 * @param sKey
	 *            加密密钥
	 * @param content
	 *            加密前数据
	 * @return
	 */
	public static String encrypt4Aes(String sKey, String content) {
		if (StringUtils.isBlank(content)) {
			return content;
		}
		try {
			byte[] src = content.getBytes("UTF-8");
//			if (sKey.length() == 48) {
			// 加密
			byte[] bytOut = encryptMode(sKey, src);

			String originalString = base64encode(bytOut);

			originalString = originalString.replaceAll(System.getProperty("line.separator"), "");

			return originalString;
//			}
		} catch (Exception e3) {
			log.error( "AES加密出错:sKey:" + sKey + ",content:" + content + "。详细信息" + e3.getMessage(), e3);
		}
		return null;
	}

	/**
	 * 解密模式
	 *
	 * @param strkey
	 * @param src
	 * @return
	 */
	public static byte[] decryptMode(String strkey, byte[] src) {
		try {

			Security.addProvider(provider);
			/***/
//			byte[] keybyte = string2Hex(strkey);
//			// 生成密钥
//			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//			secureRandom.setSeed(keybyte);
//			KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
//			keygen.init(128, secureRandom);
//			SecretKey sk = keygen.generateKey();
//			SecretKeySpec sk = new SecretKeySpec(strkey.getBytes(),"AES");
//			Cipher cip = Cipher.getInstance(Algorithm,"BC");
			Cipher cip = Cipher.getInstance(Algorithm,provider);

//			provider
//			cip.init(Cipher.DECRYPT_MODE, sk);

			SecretKeySpec keySpec = new SecretKeySpec(strkey.getBytes(), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec("inekcndsaqwertyi".getBytes());
			cip.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);

			return cip.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			log.error(e1.getMessage(), e1);
		} catch (javax.crypto.NoSuchPaddingException e2) {
			log.error(e2.getMessage(), e2);
		} catch (Exception e3) {
			log.error(e3.getMessage(), e3);
		}
		return null;
	}

	/**
	 * 解密
	 *
	 * @param sKey
	 * @param contentbase64
	 * @return
	 */
	public static byte[] decrypt4Aes(String sKey, String contentbase64) {
		try {
			byte[] src = base64decode(contentbase64);
//			if (sKey.length() == 48) {
			// 解密
			return decryptMode(sKey, src);
//			}
		} catch (Exception e3) {
			log.error( e3.getMessage(), e3);
		}
		return null;
	}

	/**
	 * AES算法解密入口
	 *
	 * @param sKey
	 * @param contentbase64
	 * @return
	 */
	public static String decrypt4Aes2Str(String sKey, String contentbase64) {
		String result = null;
		if (StringUtils.isBlank(contentbase64))
			return contentbase64;
		try {
			byte[] dst = decrypt4Aes(sKey, contentbase64);
			if (null != dst) {
				result = new String(dst, "UTF-8");

				result = result.replaceAll(System.getProperty("line.separator"), "");
			}
		} catch (Exception e3) {
			log.error(
					"AES算法解密出错:sKey:" + sKey + ",contentbase64:" + contentbase64 + "。详细信息" + e3.getMessage(), e3);
		}
		return result;
	}

	/**
	 * byte数组转十六进制
	 *
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		StringBuffer stmp = new StringBuffer();

		for (int n = 0; n < b.length; n++) {
			stmp.append(Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
			if (n < b.length - 1)
				hs = hs.append(",");
		}
		return hs.toString().toUpperCase();
	}


	/**
	 * encode十六进制数据
	 *
	 * @param bytes
	 * @return
	 */
	public static String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;
		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * 将 s 进行 BASE64 编码
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String strBase64encode(String str) throws Exception {
		byte[] src = str.getBytes("UTF-8");
		return base64encode(src);
	}

	/**
	 * 将 s 进行 BASE64 编码
	 *
	 * @param src
	 * @return
	 */
	public static String base64encode(byte[] src) {
		if (src == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(src);
	}

	/**
	 * 将 BASE64 编码的字符串 s 进行解码
	 *
	 * @param s
	 * @return
	 */
	public static byte[] base64decode(String s) {
		if (s == null)
			return null;
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return b;
		} catch (Exception e) {
			log.error("解码BASE64 编码字符串出错.", e);
			return null;
		}
	}



}