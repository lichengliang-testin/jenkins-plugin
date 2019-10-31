package cn.testin.plugins.testinpro.service.impl;

import cn.testin.plugins.testinpro.Messages;
import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.exception.CommonException;
import cn.testin.plugins.testinpro.service.TestinProService;
import cn.testin.plugins.testinpro.handler.TestinProHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lichengliang
 * date 2019/10/24
 * <p>
 * Testin 插件执行业务实现
 */
public class TestinProServiceImpl implements TestinProService {

    private TestinProBuilder builder;

    private List<TestinProHandler> handlers = new ArrayList<>();

    /**
     * 重试时间间隔
     */
    private final static long SLEEP = 60_000L;

    /**
     * 重试次数
     */
    private final static int RETRY_NUM = 3;

    public TestinProServiceImpl(TestinProBuilder builder) {
        this.builder = builder;
    }


    @Override
    public void execute() {
        executeHandler();
    }

    private void executeHandler() {
        for (TestinProHandler handler : handlers) {
            int index = 0;
            do {
                index++;
                try {
                    if (handler.preHandler()) {
                        handler.handler();
                    }
                    index = Integer.MAX_VALUE;
                    handler.afterHandler();
                } catch (Exception e) {
                    handler.throwableHandler(e);
                    if (e instanceof CommonException) {
                        if (!handler.needRetry()) {
                            throw e;
                        }
                        try {
                            builder.getContext().getListener().getLogger().println(Messages._TestinProBuilder_RunnerInfo_TryToRetry());
                            Thread.sleep(SLEEP);
                        } catch (InterruptedException e1) {
                            throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_DescriptorImpl_Interrupted());
                        }
                    }
                } finally {
                    handler.finallyHandler();
                }
            } while (index < RETRY_NUM);
        }
    }

    @Override
    public void register(TestinProHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void remove(TestinProHandler handler) {
        handlers.remove(handler);
    }
}
