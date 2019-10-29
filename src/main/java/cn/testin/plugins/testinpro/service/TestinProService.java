package cn.testin.plugins.testinpro.service;

import cn.testin.plugins.testinpro.handler.TestinProHandler;

/**
 * @author lichengliang
 *
 * testin Jenkins plugin 业务处理链
 */
public interface TestinProService {

    /**
     * 业务执行
     */
    void execute();

    /**
     * 业务注册处理器
     * @param handler 增加的处理器
     */
    void register(TestinProHandler handler);

    /**
     * 业务移除处理器
     * @param handler 被移除的处理器
     */
    void remove(TestinProHandler handler);
}
