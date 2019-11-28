package cn.testin.plugins.testinpro.service.impl;

import cn.testin.plugins.testinpro.service.TimeTransformService;

import java.util.concurrent.TimeUnit;

import static cn.testin.plugins.testinpro.utils.NumberUtils.parseLong;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019//11/7
 *
 * 默认转换器 单位秒
 */
public class DefaultTimeTransformServiceImpl implements TimeTransformService {

    private String ext;

    public DefaultTimeTransformServiceImpl(String ext) {
        this.ext = ext;
    }

    @Override
    public long getTime() {
        return TimeUnit.SECONDS.toMillis(parseLong(ext));
    }

    @Override
    public String residueExt() {
        return null;
    }
}
