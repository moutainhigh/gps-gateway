package com.zkkj.gps.gateway.jt808tcp.monitor.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexStringUtils {

	private static final char[] DIGITS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 * 将字符串转换成字节数据
	 * @param paramValue
	 * @param length
	 * @param encodeType
	 * @return
	 */
	public static byte[] stringToBytes(String paramValue,int length,String encodeType){
		try {
			byte[] xx= paramValue.getBytes(encodeType);
			byte[] bytes = new byte[length];
			for (int i = 0; i < length; i++) {
				if (i < xx.length){
					bytes[i] = xx[i];
				} else {
					bytes[i] = (byte)0x00;
				}
			}
			return bytes;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("参数字符集异常！");
		}
	}

	private static char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_HEX[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_HEX[0x0F & data[i]];
		}
		return out;
	}

	private static byte[] decodeHex(char[] data) {
		int len = data.length;
		if ((len & 0x01) != 0) {
			throw new RuntimeException("字符个数应该为偶数");
		}
		byte[] out = new byte[len >> 1];
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f |= toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}

	private static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

	public static String toHexString(byte[] bs) {
		return new String(encodeHex(bs));
	}

	private static String hexString2Bytes(String hex) {
		return new String(decodeHex(hex.toCharArray()));
	}

	public static byte[] chars2Bytes(char[] bs) {
		return decodeHex(bs);
	}


	/**
	 * 处理字节对齐,位数不够补 0X00
	 *
	 * @param b      需要处理的字节码
	 * @param length 解析后的长度
	 * @return 解析后的字节码
	 */
	public static byte[] parseByte(byte[] b, int length) {
		byte[] bytes = new byte[length];
		/*if (b.length < length) {
			for (int i = 0; i < b.length; ++i) {
				bytes[(bytes.length - 1) - i] = b[b.length - 1 - i];
			}
		} else if (b.length == length) {
			return b;
		} else {
			throw new RuntimeException("定位: 电子运单参数长度异常!");
		}*/

		/******************************************/
		if (b.length < length){
			for (int i = 0; i < length; i++) {
				if (i >= b.length){
					bytes[i] = (byte)0x00;
				} else {
					bytes[i] = b[i];
				}
			}
		} else if (b.length == length) {
			return b;
		} else {
			throw new RuntimeException("定位: 电子运单参数长度异常!");
		}
		/******************************************/

		return bytes;
	}

	/**
	 * 将字节码转换为字符串
	 *
	 * @param b       需要解析的字节码
	 * @param charset 设定字符集
	 * @return 解析后的字符串
	 * @throws UnsupportedEncodingException 忽略
	 */
	public static String parseString(byte[] b, String charset) throws UnsupportedEncodingException {
		String result = "";
		for (int i = 0; i < b.length; ++i) {
			if (b[i] != 0) {
				byte[] bytes = Arrays.copyOfRange(b, i, b.length);
				String strTemp = new String(bytes,0,bytes.length,charset);
				Pattern pattern = Pattern.compile("([^\u0000]*)");
				Matcher matcher = pattern.matcher(strTemp);
				if(matcher.find(0)){
					bytes = matcher.group(1).getBytes(charset);
				}
				result = new String(bytes, charset);
				break;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		/*byte[] b = {
				(byte) 0xCE, (byte) 0xF7,
				(byte) 0xB4, (byte) 0xA8, (byte) 0xBF, (byte) 0xF3,0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00
		};*/
		/*byte[] b = {
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};*/
		byte[] b = {
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xcd, (byte)0xa8, (byte) 0xd1, (byte) 0xb6, (byte) 0xd6, (byte) 0xd0, (byte) 0xd0,
				(byte) 0xc4, (byte) 0xb2, (byte) 0xe2, (byte) 0xca, (byte) 0xd4, 0x32, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};
		/*byte[] b = {
				0x31, 0x35, 0x30, 0x31, 0x32, 0x31, 0x39, 0x30, 0x37, 0x32, 0x31, 0x30, 0x30, 0x30, 0x31, 0x00, 0x00, 0x00, 0x00, 0x00
		};*/
		String s = null;
		try {
			s = parseString(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(s);
	}
}