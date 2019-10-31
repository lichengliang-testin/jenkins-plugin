package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.Messages;
import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.api.channel.OpenApi;
import cn.testin.plugins.testinpro.bean.PortalTask;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.enums.TaskStatusEnum;
import cn.testin.plugins.testinpro.exception.CommonException;
import cn.testin.plugins.testinpro.handler.TestinProHandler;
import hudson.model.TaskListener;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.PrintStream;

import static cn.testin.plugins.testinpro.bean.PortalTask.isDone;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichenglaing
 * date 2019/10/25
 * <p>
 * 任务结果获取
 */
public class TaskResultTestinProHandler implements TestinProHandler {

    /** 默认5分钟 */
    private final static long DEFAULT_SLEEP = 300_000L;

    private final long SLEEP;

    private boolean needRetry;

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    public TaskResultTestinProHandler(TestinProBuilder builder) {
        this(true, builder);
    }

    public TaskResultTestinProHandler(boolean needRetry, TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        if (isEmpty(builder.getSleep())) {
            SLEEP = DEFAULT_SLEEP;
        } else {
            SLEEP = builder.getSleep();
        }
        this.needRetry = needRetry;
        this.builder = builder;
    }

    @Override
    public void handler() {
        ServiceContext serviceContext = builder.getServiceContext();
        String sid = serviceContext.getSid();
        Integer projectId = builder.getProjectId();
        String taskid = serviceContext.getTaskid();
        Integer bizCode = serviceContext.getBizCode();

        if (isEmpty(sid)) {
            handlerNotLoginException();
        }

        if (isEmpty(projectId)) {
            handlerNoArgException("Project Id");
        }

        if (isEmpty(taskid)) {
            handlerNoArgException("Task Id");
        }

        if (isEmpty(bizCode)) {
            handlerNoArgException("BizCode");
        }

        TaskListener listener = builder.getContext().getListener();
        listener.getLogger().println("Get Task Result start...");
        String req = getReq(builder.getApikey(), sid, taskid, projectId, bizCode);
        OpenApi api = new OpenApi(builder);
        PortalTask portalTask;
        while (true) {
            Object res = api.doPress(req);
            if (isEmpty(res)) {
                handlerTaskResultException();
            }

            portalTask = parse(res);

            if (isEmpty(portalTask)) {
                handlerTaskResultException();
            }

            serviceContext.setPortalTask(portalTask);

            if (isDone(portalTask.getTaskStatus())) {
                break;
            }

            printTask(listener.getLogger(), portalTask);

            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                this.needRetry = false;
                throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_DescriptorImpl_Interrupted());
            }
        }

        listener.getLogger().println("Get Task Result end");

        printTask(listener.getLogger(), portalTask);
    }

    @Override
    public void throwableHandler(Throwable throwable) {
        if (throwable instanceof CommonException) {
            CommonException exception = (CommonException) throwable;
            if (exception.getCode() < ErrorCode.unknownError.getCode()) {
                needRetry = false;
            }
        }
    }

    @Override
    public boolean needRetry() {
        return needRetry;
    }

    private void printTask(PrintStream logger, PortalTask portalTask) {
        StringBuilder sb = new StringBuilder("【task info】\n");
        sb.append("\texecute status: ").append(TaskStatusEnum.search(portalTask.getTaskStatus()));
        logger.println(sb);
    }

    private PortalTask parse(Object res) {
        if (res instanceof JSONArray) {
            JSONArray array = (JSONArray) res;
            JSONObject resData = array.getJSONObject(0);
            return (PortalTask) resData.toBean(PortalTask.class);
        }
        return null;
    }

    private String getReq(String apikey, String sid, String taskid, Integer projectId, Integer bizCode) {
        return String.format(
                "{\"mkey\":\"realportal\",\"op\":\"Task.list\",\"apikey\":\"%s\",\"data\":{\"bizCode\":\"%s\",\"pageSize\":10,\"page\":1,\"projectid\":\"%s\",\"taskid\":\"%s\"},\"action\":\"portal\",\"timestamp\":\"%s\",\"sid\":\"%s\"}",
                apikey,
                bizCode,
                projectId,
                taskid,
                System.currentTimeMillis(),
                sid
        );
    }

}
