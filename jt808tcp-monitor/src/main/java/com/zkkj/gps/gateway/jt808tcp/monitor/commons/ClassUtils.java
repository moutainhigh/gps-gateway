package com.zkkj.gps.gateway.jt808tcp.monitor.commons;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射工具类
 * @author suibozhuliu
 */
@Slf4j
public class ClassUtils {

    //默认使用的类加载器
    private static ClassLoader classLoader = ClassUtils.class.getClassLoader();

    public static List<Class<?>> getClassList(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> classList = getClassList(packageName);
        Iterator<Class<?>> iterator = classList.iterator();
        while (iterator.hasNext()) {
            Class<?> next = iterator.next();
            if (!next.isAnnotationPresent(annotationClass))
                iterator.remove();
        }
        return classList;
    }

    /**
     * 获取类列表
     * @param packageName
     * @return
     */
    private static List<Class<?>> getClassList(String packageName) {
        List<Class<?>> classList = new LinkedList();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    String packagePath = url.getPath().replaceAll("%20", " ");
                    switch (protocol){
                        case "file":
                            log.info("本地环境运行从本地获取文件");
                            findClassLocal(classList, packagePath, packageName);
                            break;
                        case "jar":
                            log.info("jar包运行从jar包中获取文件");
                            findClassJar(classList,packageName);
                            break;
                        default:
                            log.info("获取文件未知错误");
                            return classList;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classList;
    }

    /**
     * 从本地获取文件
     * @param classList
     * @param packagePath
     * @param packageName
     */
    private static void findClassLocal(List<Class<?>> classList, String packagePath, String packageName) {
        try {
            File[] files = new File(packagePath).listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
                }
            });
            if (files != null)
                for (File file : files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = fileName.substring(0, fileName.lastIndexOf("."));
                        if (StringUtils.isNotEmpty(packageName)) {
                            className = packageName + "." + className;
                        }
                        doAddClass(classList, className);
                    } else {
                        String subPackagePath = fileName;
                        if (StringUtils.isNotEmpty(packagePath)) {
                            subPackagePath = packagePath + "/" + subPackagePath;
                        }
                        String subPackageName = fileName;
                        if (StringUtils.isNotEmpty(packageName)) {
                            subPackageName = packageName + "." + subPackageName;
                        }
                        findClassLocal(classList, subPackagePath, subPackageName);
                    }
                }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * jar包查找
     * @param classList
     * @param packName
     */
    private static void findClassJar(List<Class<?>> classList,final String packName){
        String pathName = packName.replace(".", "/");
        JarFile jarFile ;
        try {
            URL url = classLoader.getResource(pathName);
            JarURLConnection jarURLConnection  = (JarURLConnection )url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (IOException e) {
            throw new RuntimeException("未找到策略资源");
        }
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();
            if(jarEntryName.contains(pathName) && !jarEntryName.equals(pathName+"/")){
                //递归遍历子目录
                if(jarEntry.isDirectory()){
                    String clazzName = jarEntry.getName().replace("/", ".");
                    int endIndex = clazzName.lastIndexOf(".");
                    String prefix = null;
                    if (endIndex > 0) {
                        prefix = clazzName.substring(0, endIndex);
                    }
                    findClassJar(classList,prefix);
                }
                if(jarEntry.getName().endsWith(".class")){
                    Class<?> clazz = null;
                    try {
                        //此时jarEntryName为jar包中类的相对路径
                        clazz = classLoader.loadClass(jarEntry.getName().replace("/", ".").replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    classList.add(clazz);
                }
            }
        }

    }

    private static void doAddClass(List<Class<?>> classList, String className) {
        Class<?> cls = loadClass(className, false);
        classList.add(cls);
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cls;
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}