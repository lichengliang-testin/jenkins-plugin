package cn.testin.plugins.testinpro.utils;

import cn.testin.plugins.testinpro.annotation.Nullable;

/**
 * @author lichengliang
 *
 * date 2019/10/29
 */
public abstract class ClassUtils {

    /**
     * 获取classloader
     * @return ClassLoader
     */
    @Nullable
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = ClassUtils.class.getClassLoader();
        }
        catch (Throwable ex) {
            // ignore
        }
        if (cl == null) {
            try {
                cl = Thread.currentThread().getContextClassLoader();
            }
            catch (Throwable ex) {
                // ignore
            }
            if (cl == null) {
                // 使用系统的classloder
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // ignore
                }
            }
        }
        return cl;
    }
}
