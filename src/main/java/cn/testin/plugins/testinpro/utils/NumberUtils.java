package cn.testin.plugins.testinpro.utils;

import static cn.testin.plugins.testinpro.utils.other.ExceptionUtils.handlerException;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

public class NumberUtils {
    public static Long parseLong(String ext) {
        if (isEmpty(ext)) {
            return 0L;
        }
        try {
            return Long.parseLong(ext.trim());
        } catch (Exception e) {
            handlerException(String.format("ext(%s) is invalid!", ext));
        }
        return 0L;
    }
}
