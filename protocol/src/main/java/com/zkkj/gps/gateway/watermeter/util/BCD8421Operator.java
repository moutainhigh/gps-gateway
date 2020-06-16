package com.zkkj.gps.gateway.watermeter.util;

public class BCD8421Operator {

	/**
	 * BCD字节数组===>String
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
	 * 字符串==>BCD字节数组
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

//	public static void main(String[] args) {
//		byte[] bytes = {25, 4, 40, 21, 85, 66};
//		String s1 = HexStringUtils.toHexString(bytes);
//		String s = bcd2String(bytes);
//		System.out.println(s);
//		System.out.println(s1);
//		byte[] bytes1 = string2Bcd("190428155542");
//		byte[] bytes1 = "190428155542".getBytes(Charset.forName("GBK"));
//		for (byte b : bytes1) {
//			System.out.println(b);
//		}

//		byte[] bytes = {0x19, 0x4, 0x28, 0x15, 0x55, 0x42};
//		String s = bcd2String(bytes);
//		LocalDateTime dateTime = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyMMddHHmmss"));
//		System.out.println(s);
//		System.out.println(dateTime);
//	}
}