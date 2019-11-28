package cn.testin.plugins.testinpro.service.impl;

import cn.testin.plugins.testinpro.service.TimeTransformService;

import java.util.ArrayList;
import java.util.List;

import static cn.testin.plugins.testinpro.utils.other.ExceptionUtils.handlerException;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/11/7
 * 组合日期解析器
 */
public class MasterTimeTransformServiceImpl implements TimeTransformService {

    private List<TimeTransformService> transformServices = new ArrayList<>();

    public MasterTimeTransformServiceImpl(String ext) {
        if (isEmpty(ext)) {
            handlerException("ext must not be null!");
        }
        // add hour
        TimeTransformService hourTimeTransformService = new HourTimeTransformServiceImpl(ext);
        transformServices.add(hourTimeTransformService);

        // add minute
        TimeTransformService minuteTimeTransformService = new MinuteTimeTransformServiceImpl(hourTimeTransformService.residueExt());
        transformServices.add(minuteTimeTransformService);

        // add second
        SecondTimeTransformServiceImpl secondTimeTransformService = new SecondTimeTransformServiceImpl(minuteTimeTransformService.residueExt());
        transformServices.add(secondTimeTransformService);

        // add default
        transformServices.add(new DefaultTimeTransformServiceImpl(secondTimeTransformService.residueExt()));
    }


    @Override
    public long getTime() {
        long res = 0L;
        for (TimeTransformService transformService : transformServices) {
            res += transformService.getTime();
        }
        return res;
    }

    @Override
    public String residueExt() {
        return transformServices.get(transformServices.size() - 1).residueExt();
    }
}
