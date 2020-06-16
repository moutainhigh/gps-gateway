package com.zkkj.gps.gateway.jt808tcp.monitor.utils;

@SuppressWarnings("JavaDoc")
public class BCD8421Operator {

	/**
	 * BCD字节数组===>String
	 *
	 * @param bytes
	 * @return 十进制字符串
	 */
	public static String bcd2String(byte[] bytes) {
		StringBuilder temp = new StringBuilder(bytes.length * 2);
		for (byte aByte : bytes) {
			// 高四位
			temp.append((aByte & 0xf0) >>> 4);
			// 低四位
			temp.append(aByte & 0x0f);
		}
		return temp.toString();
	}

	/**
	 * @功能: BCD码转为10进制串(阿拉伯数据)新添加
	 * @参数: BCD码
	 * @结果: 10进制串
	 */
	public static String bcd2ToString(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
				.toString().substring(1) : temp.toString();
	}

	/**
	 * @功能: 10进制串转为BCD码（新增）
	 * @参数: 10进制串
	 * @结果: BCD码
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/**
	 * 字符串==>BCD字节数组
	 *
	 * @param str
	 * @return BCD字节数组
	 */
	public static byte[] string2Bcd(String str) {
		// 奇数,前补零
		if ((str.length() & 0x1) == 1) {
			str = "0" + str;
		}

		byte ret[] = new byte[str.length() / 2];
		byte bs[] = str.getBytes();
		for (int i = 0; i < ret.length; i++) {

			byte high = ascII2Bcd(bs[2 * i]);
			byte low = ascII2Bcd(bs[2 * i + 1]);

			ret[i] = (byte) ((high << 4) | low);
		}
		return ret;
	}

	private static byte ascII2Bcd(byte asc) {
		if ((asc >= '0') && (asc <= '9'))
			return (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			return (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			return (byte) (asc - 'a' + 10);
		else
			return (byte) (asc - 48);
	}

	public static String ascii2Bcd(byte asc) {
		if ((asc >= '0') && (asc <= '9'))
			return String.valueOf((byte) (asc - '0'));
		else if ((asc >= 'A') && (asc <= 'F'))
			return String.valueOf((byte) (asc - 'A' + 10));
		else if ((asc >= 'a') && (asc <= 'f'))
			return String.valueOf((byte) (asc - 'a' + 10));
		else
			return String.valueOf((byte) (asc - 48));
	}

	/**
	 * 将字节码数组转换为ascii字符串
	 *
	 * @param bytes 要转换的可变数组
	 * @return
	 * @Date: 2019年1月4日10:11:42
	 */
	public static String bytes2AsciiString(byte... bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		char c;
		for (byte b : bytes) {
			c = (char) b;
			stringBuilder.append(c);
		}
		return stringBuilder.toString();
	}
}
