package com.zkkj.gps.gateway.terminal.monitor.utils;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author chailixing
 * 2019/4/9 15:17
 * 区域单元测试基类
 */
class BaseTest {

	private static final Class<BaseTest> clazz = BaseTest.class;
	private static final String SEPARATOR = ";,";
	static List<Point2D.Double> pointList = new ArrayList<>();

	static {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void init() throws IOException {
		Stream<String> file = getFile(getFileName());
		pointList = getPointList(file);
		file.close();
	}

	private static String getFileName() throws IOException {
		Properties properties = new Properties();
		InputStream resourceAsStream = clazz.getResourceAsStream("/filename.properties");
		properties.load(resourceAsStream);
		return properties.getProperty("file.name");
	}

	private static Stream<String> getFile(String fileName) throws IOException {
//		String path = clazz.getResource("/" + fileName).getPath().substring(1);
		InputStream stream = clazz.getResourceAsStream("/" + fileName);
//		BufferedReader bufferedReader1 = Files.newBufferedReader(Paths.get(path));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
		return Optional.of(bufferedReader).get().lines();
	}

	private static List<Point2D.Double> getPointList(Stream<String> lines) {
		List<Point2D.Double> list = new ArrayList<>(1024);
		lines.forEach(x -> {
			String[] pointArrays = x.split(String.valueOf(SEPARATOR.charAt(0)));
			Stream.of(pointArrays)
					.forEach(point -> {
						String[] xy = point.split(String.valueOf(SEPARATOR.charAt(1)));
						list.add(new Point2D.Double(Double.parseDouble(xy[0]),
								Double.parseDouble(xy[1])));
					});
		});
		return list;
	}
}