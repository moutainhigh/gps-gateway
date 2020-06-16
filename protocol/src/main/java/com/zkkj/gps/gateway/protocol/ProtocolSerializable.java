package com.zkkj.gps.gateway.protocol;

/**
 * @author chailixing
 * 2018/12/27 13:34
 * 序列器
 */
public interface ProtocolSerializable {
	/**
	 * 序列化
	 */
	default byte[] encoder(int messageBodyLength) throws Exception {
		throw new RuntimeException("无需序列化!");
	}

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
