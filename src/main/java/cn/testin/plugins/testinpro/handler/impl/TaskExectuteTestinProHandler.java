package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.api.channel.OpenApi;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.handler.TestinProHandler;
import hudson.model.TaskListener;

import java.util.Objects;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/24
 * <p>
 * Testin openApi 任务立即执行处理器
 */
public class TaskExectuteTestinProHandler implements TestinProHandler {

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    public TaskExectuteTestinProHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        this.builder = builder;
    }

    @Override
    public void handler() {
        ServiceContext serviceContext = builder.getServiceContext();

        String sid = serviceContext.getSid();

        // task execute
        if (isEmpty(builder.getJobId())) {
            handlerNoArgException("Job Id");
        }
        if (isEmpty(builder.getProjectId())) {
            handlerNoArgException("Project Id");
        }
        if (isEmpty(sid)) {
            handlerNotLoginException();
        }
        TaskListener listener = builder.getContext().getListener();
        if (isEmpty(builder.getAppUrls())) {
            listener.getLogger().println("not have app url, skip ???");
            return;
        }

        Integer share = 0;
        if (!isEmpty(builder.getShare())) {
            share = builder.getShare();
        }

        listener.getLogger().println(
                "task execute start...\n" +
                        "jobId:" + builder.getJobId() + "\n");

        String req = getReq(builder.getApikey(), sid, builder.getJobId(), builder.getProjectId(), builder.getAppUrls().get(0), isEmpty(builder.getTaskDescr()) ? "" : builder.getTaskDescr(), share);

        long begin = System.currentTimeMillis();
        OpenApi openApi = new OpenApi(builder);
        Object res = openApi.doPress(req);
        if (isEmpty(res)) {
            handlerTaskExectuteException();
        }

        String taskid = Objects.toString(res);
        serviceContext.setTaskid(taskid);
        listener.getLogger().println(
                "task execute end\n" +
                        "\ttaskid is " + taskid +
                        "\ntotalTime:" + (System.currentTimeMillis() - begin) + "\n");
    }

    private String getReq(String apikey, String sid, Integer jobId, Integer projectId, String packageUrl, String taskDescr, Integer share) {
        return String.format("{\"apikey\":\"%s\",\"timestamp\":%s,\"sid\":\"%s\",\"mkey\":\"realtest\",\"action\":\"app\",\"op\":\"ScheduledJob.execute\",\"data\":{\"jobId\":%s,\"projectid\":%s,\"appinf\":{\"packageUrl\":\"%s\"},\"taskDescr\":\"%s\",\"share\":\"%s\"}}",
                apikey,
                System.currentTimeMillis(),
                sid,
                jobId,
                projectId,
                packageUrl,
                taskDescr,
                share
        );
    }
}
