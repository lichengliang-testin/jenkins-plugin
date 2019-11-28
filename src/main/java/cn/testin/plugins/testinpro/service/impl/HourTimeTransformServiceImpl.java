package cn.testin.plugins.testinpro.service.impl;

import cn.testin.plugins.testinpro.service.TimeTransformService;

import java.util.concurrent.TimeUnit;

import static cn.testin.plugins.testinpro.utils.NumberUtils.parseLong;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019//11/7
 *
 * 小时转换器
 */
public class HourTimeTransformServiceImpl implements TimeTransformService {

    /** 待处理的表达式 */
    private String ext;

    /** 处理的单位 */
    private static final String[] EXT = {"hour", "h"};

    /** 是否进行处理过 */
    private boolean extracted;

    /** 需解析的数字下标 */
    private int numberIndex;

    /** 剩余未处理的表达式下标 */
    private int extractedIndex;

    public HourTimeTransformServiceImpl(String ext) {
        this.ext = ext;
        Integer[] extracted = extracted(ext, EXT);
        if (!isEmpty(extracted)) {
            this.extracted = true;
            this.numberIndex = extracted[0];
            this.extractedIndex = extracted[1];
        }
    }


    @Override
    public long getTime() {
        String ext = this.ext.substring(0, numberIndex);
        return TimeUnit.HOURS.toMillis(parseLong(ext));
    }

    @Override
    public String residueExt() {
        if (extracted) {
            return ext.substring(extractedIndex);
        }
        return ext;
    }
}
