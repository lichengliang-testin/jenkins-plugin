package cn.testin.plugins.testinpro.utils.other;

import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.exception.CommonException;

/**
 * @author lichengliang
 *
 * date 2019/11/7
 * 异常辅助工具类
 */
public class ExceptionUtils {

    public static void handlerException(Throwable throwable){
        throw new CommonException(ErrorCode.unknownError.getCode(), throwable);
    }

    public static void handlerException(String msg){
        throw new CommonException(ErrorCode.unknownError.getCode(), msg);
    }
}
