package cn.testin.plugins.testinpro.utils;

import cn.testin.plugins.testinpro.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static cn.testin.plugins.testinpro.utils.ArrayUtils.exists;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * <p>
 * date 2019/10/29
 */
public abstract class StringUtils {

    private static final String EMPTY = " ";

    /**
     * 分割特殊表达式
     *
     * @param str        待分割的表达式
     * @param delimiters 分割表达式
     * @return 分割结果
     */
    public static List<String> tokenizeToSplit(@Nullable String str, String delimiters) {
        if (isEmpty(str)) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();

        byte[] res = new byte[str.length()];

        byte[] bytes = str.getBytes();
        byte[] emptyBytes = EMPTY.getBytes();
        byte[] delimitersBytes = delimiters.getBytes();

        int len = 0;
        for (byte aByte : bytes) {
            if (exists(delimitersBytes, aByte)) {
                if (len != 0) {
                    int offset = 0;
                    for (byte b : res) {
                        if (exists(emptyBytes, b)) {
                            offset++;
                            continue;
                        }
                        break;
                    }
                    while (len > 0) {
                        byte b = res[len - 1];
                        if (exists(emptyBytes, b)) {
                            len--;
                            continue;
                        }
                        break;
                    }
                    result.add(new String(res, offset, len - offset));
                    len = 0;
                }
                continue;
            }
            res[len++] = aByte;
        }
        if (len != 0) {
            result.add(new String(res, 0, len));
        }

        return result;
    }

}
