package com.zkkj.gps.gateway.watermeter.component;

/**
 * @author chailixing
 * 2019/4/18 9:29
 * 序列化接口
 */
public interface ProtocolSerializable {
	/**
	 * 序列化
	 */
	default byte[] encoder() throws Exception {
		throw new RuntimeException("无需序列化!");
	}

	/**
	 * 反序列化
	 */
	default void decoder(byte[] bytes) throws Exception {
		throw new RuntimeException("无需反序列化!");
	}
}
