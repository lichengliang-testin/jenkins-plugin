package cn.testin.plugins.testinpro.utils;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 *
 * date 2019/10/29
 */
public abstract class ArrayUtils {

    /**
     * 查找当前字节是否存在
     * @param bytes 比对字节
     * @param b 比较字节
     * @return 是否存在
     */
    public static boolean exists(byte[] bytes, byte b) {
        if (!isEmpty(bytes)) {
            for (byte aByte : bytes) {
                if (aByte == b) {
                    return true;
                }
            }
        }
        return false;
    }
}
