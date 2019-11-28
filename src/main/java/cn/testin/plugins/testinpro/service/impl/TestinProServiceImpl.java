package cn.testin.plugins.testinpro.service.impl;

import cn.testin.plugins.testinpro.Messages;
import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.bean.ExecResponse;
import cn.testin.plugins.testinpro.exception.CommonException;
import cn.testin.plugins.testinpro.service.TestinProService;
import cn.testin.plugins.testinpro.handler.TestinProHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static cn.testin.plugins.testinpro.utils.TimeUtils.parseExt;
import static cn.testin.plugins.testinpro.utils.other.ExceptionUtils.handlerException;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/24
 * <p>
 * Testin 插件执行业务实现
 */
public class TestinProServiceImpl implements TestinProService {

    private final TestinProBuilder builder;

    private final List<TestinProHandler> handlers = new ArrayList<>();

    /**
     * 重试时间间隔
     */
    private final static long SLEEP = 60_000L;

    /**
     * 重试次数
     */
    private final static int RETRY_NUM = 3;

    /**
     * 执行器
     */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /***
     * 构建结果
     */
    private Future<ExecResponse> res;

    public TestinProServiceImpl(TestinProBuilder builder) {
        this.builder = builder;
    }


    @Override
    public void execute() {

        // 执行任务
        executeTask();

        // 预估任务是否完成或者是否超时
        if (judgeTask()) {
            return;
        }

        // 超时中断
        interrupt();
    }

    private void executeTask() {
        Callable<ExecResponse> callable = new Callable<ExecResponse>() {

            private final ExecResponse.Builder b = new ExecResponse.Builder();

            @Override
            public ExecResponse call() {
                try {
                    executeHandler();
                } catch (Exception e) {
                    b.throwable(e);
                } finally {
                    synchronized (builder) {
                        builder.notify();
                    }
                }
                return b.build();
            }
        };

        res = executor.submit(callable);
    }

    private boolean judgeTask() {

        if (isEmpty(this.builder.getTimeoutExt())) {
            analyzeTaskResponse();
            return true;
        }

        long timeout = parseExt(this.builder.getTimeoutExt());

        try {
            synchronized (this.builder) {
                this.builder.wait(timeout);
                if (res.isDone()) {
                    analyzeTaskResponse();
                    return true;
                }
            }
        } catch (InterruptedException e) {
            handlerException(e);
        }
        return false;
    }

    private void interrupt() {
        this.builder.getContext().getListener().getLogger().println(Messages.TestinProBuilder_TaskInfo_taskTimeout());
        executor.shutdownNow();
        handlerException(Messages.TestinProBuilder_DescriptorImpl_Interrupted());
    }

    private void analyzeTaskResponse() {
        try {
            ExecResponse execResponse = res.get();
            if (execResponse.isHasError()) {
                handlerException(execResponse.getThrowable());
            }
        } catch (ExecutionException | InterruptedException e) {
            handlerException(e);
        }
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
                            handlerException(Messages.TestinProBuilder_DescriptorImpl_Interrupted());
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
