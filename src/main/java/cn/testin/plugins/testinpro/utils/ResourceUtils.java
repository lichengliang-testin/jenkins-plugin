package cn.testin.plugins.testinpro.utils;

import java.io.InputStream;
import java.net.URL;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * <p>
 * date 2019/10/29
 */
public abstract class ResourceUtils {

    /**
     * 获取资源URL
     * @param path 文件路径
     * @return URL
     */
    public static URL getURL(String path) {
        if (isEmpty(path)) {
            return ClassLoader.getSystemResource("");
        }

        URL url ;
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        if (isEmpty(classLoader)) {
            url = ClassLoader.getSystemResource(path);
        } else {
            url = classLoader.getResource(path);
        }
        return url;
    }

    /**
     * 获取资源流
     * @param path 文件路径
     * @return InputStream
     */
    public static InputStream getInputStream(String path) {
        if (isEmpty(path)) {
            return ClassLoader.getSystemResourceAsStream("");
        }

        InputStream inputStream ;
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        if (isEmpty(classLoader)) {
            inputStream = ClassLoader.getSystemResourceAsStream(path);
        } else {
            inputStream = classLoader.getResourceAsStream(path);
        }
        return inputStream;
    }
}
