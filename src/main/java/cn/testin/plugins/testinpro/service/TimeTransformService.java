package cn.testin.plugins.testinpro.service;

import static cn.testin.plugins.testinpro.utils.ArrayUtils.exists;
import static cn.testin.plugins.testinpro.utils.other.ExceptionUtils.handlerException;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/11/7
 *
 * 用于时间表达式解析
 */
public interface TimeTransformService {

    /**
     * 获取时间
     * @return 毫秒值
     */
    default long getTime() {
        return 0;
    }

    /**
     * 用于获取为处理的表达式
     * @return 剩下未处理的表达式
     */
    String residueExt();

    String NUM = "1234567890 ";

    default Integer[] extracted(String ext, String[] exts) {
        if (isEmpty(exts) || isEmpty(ext)) {
            return null;
        }
        for (String str : exts) {
            int i = ext.indexOf(str);
            if (i != -1) {
                int index = i + str.length();
                if (ext.length() <= index) {
                    return new Integer[]{i ,index};
                }
                String s = ext.substring(index);
                byte[] bytes = s.getBytes();
                for (byte b : bytes) {
                    if (exists(" ".getBytes(), b)){
                        continue;
                    }
                    if (!exists(NUM.getBytes(), b)){
                        handlerException("ext is invalid!");
                    }
                    break;
                }
                return new Integer[]{i ,index};
            }
        }
        return null;
    }

}
