package com.zkkj.gps.gateway.protocol.util;

import com.zkkj.gps.gateway.protocol.component.common.BaseCompose;
import com.zkkj.gps.gateway.protocol.source.P_0310;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chailixing
 * 2019/1/17 11:47
 * 客户端工具
 */
public class ClientUtils {
	/**
	 * 加载文件
	 *
	 * @return 字节数组
	 */
	public static byte[] getBytes(InputStream inputStream) {
		try (InputStream fileInputStream = inputStream) {
			//声明一个int存储每次读取到的数据
			int i = 0;
			//定义一个记录索引的变量
			int index = 0;
			byte[] buff = new byte[fileInputStream.available()];
			while ((i = fileInputStream.read()) != -1) {//把读取的数据放到i中
				buff[index] = (byte) i;
				byte[] bf = new byte[]{(byte) i};
				index++;
			}
			return buff;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 序列化到本地
	 *
	 * @param baseCompose 协议对象
	 */
	public static void out(BaseCompose baseCompose) {
		String path = P_0310.class.getResource("").getPath().substring(1);
		String messageId = HexStringUtils.toHexString(BitOperator.integerTo2Bytes(baseCompose.getMessageHeader().getMessageId()));
		String filePath = path + messageId + ".out";
		try (OutputStream outputStream = new FileOutputStream(filePath, true)) {
			outputStream.write(baseCompose.encoder());
			System.out.println("序列化成功!" + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从文件截取有效字节数组
	 *
	 * @param source   源文件
	 * @param highByte 消息id高字节
	 * @param lowByte  消息id低字节
	 */
	public static byte[] getProtocol(byte[] source, int highByte, int lowByte) {
		// 先将消息id转为字节表示
		highByte = BitOperator.integerTo1Byte(highByte);
		lowByte = BitOperator.integerTo1Byte(lowByte);

		List<Byte> list = new ArrayList<>();
		for (int i = 0; i < source.length; ++i) {
			// 判断帧头 例:7E 02 00
			if ((source[i] == 0x7e) && (source[i + 1] == highByte) && (source[i + 2] == lowByte)) {
				while (true) {
					list.add(source[i]);
					// 判断帧尾
					if (source[i + 1] == 0x7e) {
						list.add(source[i + 1]);
						break;
					}
					i++;
				}
				break;
			}
		}
		return BitOperator.toArray(list);
	}

	public static byte[] getProtocol(byte[] source, byte[] msgId) {

		if (msgId == null || msgId.length != 2) {
			return null;
		}
		List<Byte> list = new ArrayList<>();
		for (int i = 0; i < source.length; ++i) {
			// 判断帧头 例:7E 02 00
			if ((source[i] == 0x7e) && (source[i + 1] == msgId[0]) && (source[i + 2] == msgId[1])) {
				while (true) {
					list.add(source[i]);
					// 判断帧尾
					if (source[i + 1] == 0x7e) {
						list.add(source[i + 1]);
						break;
					}
					i++;
				}
				break;
			}
		}
		return BitOperator.toArray(list);
	}

}
