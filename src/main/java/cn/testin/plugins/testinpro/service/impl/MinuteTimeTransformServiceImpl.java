package cn.testin.plugins.testinpro.service.impl;

import cn.testin.plugins.testinpro.service.TimeTransformService;

import java.util.concurrent.TimeUnit;

import static cn.testin.plugins.testinpro.utils.NumberUtils.parseLong;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019//11/7
 *
 * 分钟转换器
 */
public class MinuteTimeTransformServiceImpl implements TimeTransformService {

    private String ext;

    private static final String[] EXT = {"minute", "m"};

    private boolean extracted;

    private int numberIndex;

    private int extractedIndex;

    public MinuteTimeTransformServiceImpl(String ext) {
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
        return TimeUnit.MINUTES.toMillis(parseLong(ext));
    }

    @Override
    public String residueExt() {
        if (extracted) {
            return ext.substring(extractedIndex);
        }
        return ext;
    }
}
