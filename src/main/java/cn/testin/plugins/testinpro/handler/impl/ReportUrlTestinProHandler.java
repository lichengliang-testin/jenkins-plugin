package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.api.channel.OpenApi;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.exception.CommonException;
import cn.testin.plugins.testinpro.handler.TestinProHandler;
import hudson.model.TaskListener;

import java.util.Objects;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/25
 * <p>
 * 获取报告url的处理器
 */
public class ReportUrlTestinProHandler implements TestinProHandler {

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    private boolean needRetry = true;

    public ReportUrlTestinProHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        this.builder = builder;
    }

    @Override
    public void handler() {
        ServiceContext serviceContext = builder.getServiceContext();
        String sid = serviceContext.getSid();
        String taskid = serviceContext.getTaskid();
        Integer projectId = builder.getProjectId();

        if (isEmpty(sid)) {
            handlerNotLoginException();
        }

        if (isEmpty(projectId)) {
            handlerNoArgException("Project Id");
        }

        if (isEmpty(taskid)) {
            handlerNoArgException("Task Id");
        }

        TaskListener listener = builder.getContext().getListener();
        listener.getLogger().println("get task report url start...");
        String req = getReq(builder.getApikey(), sid, taskid, projectId);
        long begin = System.currentTimeMillis();

        OpenApi openApi = new OpenApi(builder);
        Object o = openApi.doPress(req);
        if (isEmpty(o)) {
            handlerReportUrlException();
        }
        String url = Objects.toString(o);
        serviceContext.setReportUrl(url);
        listener.getLogger().println(
                "get task report url end\n" +
                        "totalTime: " + (System.currentTimeMillis() - begin) +
                        "\treport url is: " + url + "\n"
        );
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

    private String getReq(String apikey, String sid, String taskid, Integer projectId) {
        return String.format(
                "{\"apikey\":\"%s\",\"timestamp\":\"%s\",\"sid\":\"%s\",\"mkey\":\"realtest\",\"action\":\"report\",\"op\":\"Report.url\",\"data\":{\"taskid\":\"%s\",\"projectid\":\"%s\"}}",
                apikey,
                System.currentTimeMillis(),
                sid,
                taskid,
                projectId
        );
    }
}
