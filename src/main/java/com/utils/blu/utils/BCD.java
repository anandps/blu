package com.utils.blu.utils;

public class BCD {

	public static byte[] encode(String s) {
		int i = 0, j = 0;
		int max = s.length() - (s.length() % 2);
		byte[] buf = new byte[(s.length() + (s.length() % 2)) / 2];
		while (i < max) {
			buf[j++] = (byte) ((((s.charAt(i++) - '0') << 4) | (s.charAt(i++) - '0')));
		}
		if ((s.length() % 2) == 1) {
			buf[j] = (byte) ((s.charAt(i++) - '0') << 4 | 0x0A);
		}
		return buf;
	}

	public static String decode(byte[] b) {
		StringBuffer buf = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; ++i) {
			buf.append((char) (((b[i] & 0xf0) >> 4) + '0'));
			if ((i != b.length) && ((b[i] & 0xf) != 0x0A))
				buf.append((char) ((b[i] & 0x0f) + '0'));
		}
		return buf.toString();
	}

	public static String bcdBytetoString(byte[] b) {

		StringBuffer string = new StringBuffer();
		String j = null;
		for (byte i : b) {
			j = String.format("%02X", i);
			string.append(Character.toString((char) Integer.parseInt(j, 16)));

		}
		String result = string.toString();
		return result;
	}

	public static byte[] getBCDBytes(String string) {
		byte[] stringBytes = string.getBytes();
		byte[] validBytes = new byte[stringBytes.length];
		int charInt = -1;

		int validByteCount = 0;
		for (int i = 0; i < stringBytes.length; i++) {
			charInt = stringBytes[i] & 0xFF;
			if (charInt > 153) {
				continue;
			}
			validBytes[validByteCount++] = stringBytes[i];
		}
		byte[] returnBytes = new byte[validByteCount];
		for (int i = 0; i < validByteCount; i++) {
			returnBytes[i] = validBytes[i];
		}
		return returnBytes;
	}

}
