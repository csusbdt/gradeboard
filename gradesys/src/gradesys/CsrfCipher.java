package gradesys;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class CsrfCipher {
	
	private static SecureRandom secureRandom = new SecureRandom();
	private static SecretKey secretKey = null;
	private static Cipher cipher = null;

	static {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// Logger.getLogger(CsrfCipher.class.getName()).severe("AES not supported.");
			throw new RuntimeException(e);
		}
		keyGenerator.init(128);
		secretKey = keyGenerator.generateKey();
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	// The follow 2 methods taken from
	// http://www.rgagnon.com/javadetails/java-0400.html
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	private static byte[] hexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	public static String encryptUserId(String userId) {
		try {
			String plaintext = userId + " " + secureRandom.nextLong();
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] bytes = plaintext.getBytes();
			byte[] encryptedBytes = cipher.doFinal(bytes);
			return byteArrayToHexString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decryptUserId(String ciphertext) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] encryptedBytes = hexStringToByteArray(ciphertext);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			String plaintext = new String(decryptedBytes);
			int endIndex = plaintext.indexOf(" ");
			if (endIndex <= 0) {
				return "";
			}
			String userId = plaintext.substring(0, endIndex);
			return userId;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
