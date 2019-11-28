package cn.testin.plugins.testinpro.utils;

import cn.testin.plugins.testinpro.service.TimeTransformService;
import cn.testin.plugins.testinpro.service.impl.MasterTimeTransformServiceImpl;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * <p>
 * date 2019/11/7
 * 时间操作工具类
 */
public class TimeUtils {

    /**
     * 分解时间表达式
     *
     * @param ext 表达式
     * @return 时间 单位 毫秒
     */
    public static Long parseExt(String ext) {
        Long res = 0L;
        if (isEmpty(ext)) {
            return res;
        }
        TimeTransformService timeTransformService = getDefaultTimeTransformService(ext);
        return timeTransformService.getTime();
    }

    private static TimeTransformService getDefaultTimeTransformService(String ext) {
        return new MasterTimeTransformServiceImpl(ext);
    }

}
