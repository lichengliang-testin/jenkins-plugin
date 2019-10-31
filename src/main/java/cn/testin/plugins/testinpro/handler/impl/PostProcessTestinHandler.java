package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.bean.PortalTask;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.enums.ResultCategoryEnum;
import cn.testin.plugins.testinpro.handler.TestinProHandler;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 *
 * 后置处理器
 * 处理响应中断相关
 */
public class PostProcessTestinHandler implements TestinProHandler {

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    public PostProcessTestinHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        this.builder = builder;
    }

    @Override
    public void handler() {
        ServiceContext serviceContext = builder.getServiceContext();

        PortalTask portalTask = serviceContext.getPortalTask();

        if (!isEmpty(portalTask)) {
            postProcess(portalTask);
        }
    }

    private void postProcess(PortalTask portalTask) {

        Integer resultCategory = portalTask.analysisResultCategory();

        if (ResultCategoryEnum.isError(resultCategory)) {
            handlerTaskExecuteFailException();
        }
    }
}
