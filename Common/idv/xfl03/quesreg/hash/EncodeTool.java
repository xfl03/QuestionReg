package idv.xfl03.quesreg.hash;

import java.security.MessageDigest;

/**
 * Use Java System Function To Encode.
 * Support MD5, SHA1, etc.
 *
 * @author Michael (http://www.micmiu.com/lang/java/java-md5-sha1/)[2012.6.4] , xfl03 [2015.2 2015.5]
 *
 */
public class EncodeTool {
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	
	/**
	 * encode string by algorithm
	 *
	 * @param algorithm
	 * @param str
	 * @return Encoded String
	 */
	public static String encode(String algorithm, String str) throws Exception {
		if (str == null||str.equalsIgnoreCase("")) {
			throw new IllegalArgumentException("String which will be encoded Cannot Be Empty");
		}
		if(algorithm==null||algorithm.equalsIgnoreCase("")){
			throw new IllegalArgumentException("Algorithm Cannot Be Empty");
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	
	/**
	 * encode By MD5
	 *
	 * @param str
	 * @return Encoded String
	 * @throws Exception 
	 */
	public static String encodeByMD5(String str) throws Exception {
		return encode("MD5",str);
	}
	
	
	/**
	 * encode By SH1
	 *
	 * @param str
	 * @return Encoded String
	 * @throws Exception 
	 */
	public static String encodeBySHA1(String str) throws Exception {
		return encode("SHA1",str);
	}

	
	/**
	 * Takes the raw bytes from the digest and formats them correct.
	 *
	 * @param bytes
	 *            the raw bytes from the digest.
	 * @return the formatted bytes.
	 */
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
	
	/**
	 * MD5(SHA1(str)+MD5(str))
	 *
	 * @param str
	 * @return Encoded String
	 * @throws Exception 
	 */
	public static String basicEncode(String str) throws Exception{
		return encodeByMD5(encodeBySHA1(str)+encodeByMD5(str));
	}
	
}