package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.Messages;
import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.action.TestinProAction;
import cn.testin.plugins.testinpro.annotation.Nullable;
import cn.testin.plugins.testinpro.bean.PortalTask;
import cn.testin.plugins.testinpro.bean.TaskDetail;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.enums.ResultCategoryEnum;
import cn.testin.plugins.testinpro.enums.TaskStatusEnum;
import cn.testin.plugins.testinpro.handler.TestinProHandler;

import java.io.PrintStream;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/25
 * <p>
 * TestinPro 默认展示页
 */
public class DefaultDisplayTestinProHandler implements TestinProHandler {

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    public DefaultDisplayTestinProHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        this.builder = builder;
    }

    @Override
    public boolean preHandler() {
        ServiceContext serviceContext = builder.getServiceContext();

        PortalTask portalTask = serviceContext.getPortalTask();

        Integer jobId = builder.getJobId();

        Integer projectId = builder.getProjectId();

        String taskid = serviceContext.getTaskid();

        String reportUrl = serviceContext.getReportUrl();

        TaskDetail taskDetail = serviceContext.getTaskDetail();

        Integer resultCategory = portalTask.analysisResultCategory();

        builder.getContext().getRun().addAction(
                new TestinProAction.Builder()
                        .jobId(jobId)
                        .projectId(projectId)
                        .taskid(taskid)
                        .reportUrl(reportUrl)
                        .taskStatus(isEmpty(portalTask) ? null : portalTask.getTaskStatus())
                        .resultCategory(resultCategory)
                        .taskDetail(taskDetail)
                        .taskSummary(portalTask.getTaskSummary())
                        .build());
        return true;
    }

    @Override
    public void handler() {
        PrintStream logger = builder.getContext().getListener().getLogger();
        ServiceContext serviceContext = builder.getServiceContext();

        PortalTask portalTask = serviceContext.getPortalTask();

        Integer jobId = builder.getJobId();

        Integer projectId = builder.getProjectId();

        String taskid = serviceContext.getTaskid();

        String reportUrl = serviceContext.getReportUrl();

        String taskStatus = analysisStatus(portalTask);

        Integer resultCategory = portalTask.analysisResultCategory();

        ResultCategoryEnum resultCategoryEnum = ResultCategoryEnum.valueOf(resultCategory);

        StringBuffer sb = new StringBuffer("【TestinPro Default Display】\n");

        draw(sb, Messages.TestinProBuilder_TaskInfo_jobId(), jobId);
        draw(sb, Messages.TestinProBuilder_TaskInfo_projectId(), projectId);
        draw(sb, Messages.TestinProBuilder_TaskInfo_taskId(), taskid);
        draw(sb, Messages.TestinProBuilder_TaskInfo_reportUrl(), reportUrl);
        draw(sb, Messages.TestinProBuilder_TaskInfo_taskStatus(), taskStatus);
        draw(sb, Messages.TestinProBuilder_TaskInfo_taskResult(), null == resultCategoryEnum ? null : resultCategoryEnum.getDescr());

        logger.println(sb);
    }

    private void draw(StringBuffer sb, String key, Object value) {
        if (isEmpty(value)) {
            return;
        }
        sb.append("\t").append(key).append(": ");
        sb.append("\t").append(value).append("\n");
    }

    @Nullable
    private String analysisStatus(PortalTask portalTask) {
        if (isEmpty(portalTask)) {
            return null;
        }

        return TaskStatusEnum.search(portalTask.getTaskStatus()).name();
    }
}
