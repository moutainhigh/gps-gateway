package com.zkkj.gps.gateway.watermeter.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BitOperator {

	/**
	 * 把一个整形该为byte
	 */
	public static byte integerTo1Byte(int value) {
		return (byte) (value & 0xFF);
	}

	/**
	 * 把一个整形该为1位的byte数组
	 */
	public static byte[] integerTo1Bytes(int value) {
		byte[] result = new byte[1];
		result[0] = (byte) (value & 0xFF);
		return result;
	}

	/**
	 * 把一个整形改为2位的byte数组 高字节在前
	 */
	public static byte[] integerTo2Bytes(int value) {
		byte[] result = new byte[2];
		result[0] = (byte) ((value >>> 8) & 0xFF);
		result[1] = (byte) (value & 0xFF);
		return result;
	}

	/**
	 * 把一个整形改为2位的byte数组 低字节在前
	 */
	public static byte[] integerTo2LowBytes(int value) {
		byte[] result = new byte[2];
		result[0] = (byte) (value & 0xFF);
		result[1] = (byte) ((value >>> 8) & 0xFF);
		return result;
	}

	/**
	 * 把一个2位的数组转化位整形 低字节在前
	 */
	public static int twoLowBytesToInteger(byte[] value) {
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		return ((temp1 << 8) + temp0);
	}

	/**
	 * 把一个整形改为4位的byte数组
	 */
	public static byte[] integerTo4Bytes(int value) {
		byte[] result = new byte[4];
		result[0] = (byte) ((value >>> 24) & 0xFF);
		result[1] = (byte) ((value >>> 16) & 0xFF);
		result[2] = (byte) ((value >>> 8) & 0xFF);
		result[3] = (byte) (value & 0xFF);
		return result;
	}

	/**
	 * 把byte[]转化位整形,通常为指令用
	 */
	public static int byteToInteger(byte[] value) {
		int result;
		if (value.length == 1) {
			result = oneByteToInteger(value[0]);
		} else if (value.length == 2) {
			result = twoBytesToInteger(value);
		} else if (value.length == 3) {
			result = threeBytesToInteger(value);
		} else if (value.length == 4) {
			result = fourBytesToInteger(value);
		} else {
			result = fourBytesToInteger(value);
		}
		return result;
	}

	/**
	 * 把一个byte转化位整形,通常为指令用
	 */
	public static int oneByteToInteger(byte value) {
		return (int) value & 0xFF;
	}

	/**
	 * 把一个2位的数组转化位整形
	 */
	public static int twoBytesToInteger(byte[] value) {
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		return ((temp0 << 8) + temp1);
	}

	/**
	 * 把一个3位的数组转化位整形
	 */
	public static int threeBytesToInteger(byte[] value) {
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		return ((temp0 << 16) + (temp1 << 8) + temp2);
	}

	/**
	 * 把一个4位的数组转化位整形,通常为指令用
	 */
	public static int fourBytesToInteger(byte[] value) {
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		int temp3 = value[3] & 0xFF;
		return ((temp0 << 24) + (temp1 << 16) + (temp2 << 8) + temp3);
	}

	/**
	 * ip端口地址 byte[] ----> String "192.168.3.100:8888"
	 */
	public static String decoderIpAddr(byte[] ipAddr) {
		StringBuilder stringBuilder = new StringBuilder();
		byte[] ipBytes = Arrays.copyOfRange(ipAddr, 0, 4);
		byte[] portBytes = Arrays.copyOfRange(ipAddr, 4, ipAddr.length);
		for (byte b : ipBytes) {
			int i = BitOperator.oneByteToInteger(b);
			stringBuilder.append(i).append(".");
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.append(":");
		for (byte b : portBytes) {
			int i = BitOperator.oneByteToInteger(b);
			if (i < 10) {
				String s = "0" + i;
				stringBuilder.append(s);
			} else {
				stringBuilder.append(i);
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * ip端口地址 String ----> byte[]
	 * 192.168.3.100:8888
	 */
	public static byte[] encoderIpAddr(String ipAddr) {
		String ip = ipAddr.substring(0, ipAddr.indexOf(":"));
		String port = ipAddr.substring(ipAddr.indexOf(":") + 1);
		byte[] bytes = processIp(ip);
		byte[] portBytes;
		if (port.length() > 4) {
			String oneByte = port.substring(0, 3);
			String twoByte = port.substring(3);
			portBytes = BitOperator.concatAll(
					BitOperator.integerTo1Bytes(Integer.parseInt(oneByte)),
					BitOperator.integerTo1Bytes(Integer.parseInt(twoByte))
			);
		} else {
			String oneByte = port.substring(0, 2);
			String twoByte = port.substring(2);
			portBytes = BitOperator.concatAll(
					BitOperator.integerTo1Bytes(Integer.parseInt(oneByte)),
					BitOperator.integerTo1Bytes(Integer.parseInt(twoByte))
			);
		}
		return BitOperator.concatAll(
				bytes,
				portBytes
		);
	}

	/**
	 * 将ip字符串转换为字节数组
	 */
	public static byte[] processIp(String ip) {
		String[] ipArr = ip.split("[.]");
		byte[] bytes = new byte[4];
		for (int i = 0; i < ipArr.length; ++i) {
			bytes[i] = BitOperator.integerTo1Byte(Integer.parseInt(ipArr[i]));
		}
		return bytes;
	}

	/**
	 * 合并字节数组
	 */
	public static byte[] concatAll(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			if (array != null) {
				totalLength += array.length;
			}
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			if (array != null) {
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
		}
		return result;
	}

	/**
	 * 合并字节数组
	 */
	public static byte[] concatAll(List<byte[]> rest) {
		int totalLength = 0;
		for (byte[] array : rest) {
			if (array != null) {
				totalLength += array.length;
			}
		}
		byte[] result = new byte[totalLength];
		int offset = 0;
		for (byte[] array : rest) {
			if (array != null) {
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
		}
		return result;
	}

	/**
	 * 累加和校验码计算
	 */
	public static byte getCrc(byte[] bs) {
		if (bs.length == 0) {
			throw new RuntimeException("字节数组为空,计算检验码失败!");
		}
		int result = 0;
		for (byte b : bs) {
			result += b;
		}
		return BitOperator.integerTo1Byte(result);
	}

	/**
	 * @param bytes 有效数组(消息头消息体)
	 * @param msgId 提示信息
	 */
	public static boolean checkCrc(byte[] bytes, String msgId) {
		int crc = BitOperator.getCrc(Arrays.copyOfRange(bytes, 0, bytes.length - 1));
		if (crc != bytes[bytes.length - 1]) {
			throw new RuntimeException("校验码不一致,协议无效!" + msgId);
		} else {
			return true;
		}
	}

	public static byte[] toArray(Collection<? extends Number> collection) {

		Object[] boxedArray = collection.toArray();
		int len = boxedArray.length;
		byte[] array = new byte[len];

		for (int i = 0; i < len; ++i) {
			array[i] = ((Number) checkNotNull(boxedArray[i])).byteValue();
		}

		return array;

	}

	private static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		} else {
			return reference;
		}
	}

	public static void main(String[] args) {
		// 测试ip地址转换
//		byte[] encoder = encoderIpAddr("192.168.1.2:2003");
//		System.out.println(HexStringUtils.toHexString(encoder));

		byte[] bytes = {
				(byte) 0xC0, (byte) 0xA8, 0x01, 0x02, 0x14, 0x03
		};

		System.out.println(decoderIpAddr(bytes));

		// 测试大小端序
//		int i = 0b00000000_00000000_00111001_00011110;
//		byte[] bytes = integerTo2LowBytes(i);
//		byte[] bytes = {0X1e, 0X39};
//		int i = twoLowBytesToInteger(bytes);
//		int j = 0b00011110_00111001;
//		int k = 0b00111001_00011110;
//		System.out.println(i);
//		System.out.println(HexStringUtils.toHexString(bytes));

//		// 测试校验码计算
//		byte[] bytes = {0b1110100, 0b1111111, 0b1000000, 0b110011};
//		byte result = 0b110011;
//		byte crc = getCrc(bytes);
//		System.out.println(crc);
//		System.out.println(checkCrc(bytes, "11"));
	}
}