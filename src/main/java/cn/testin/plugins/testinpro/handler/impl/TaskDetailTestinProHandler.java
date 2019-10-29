package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.api.channel.OpenApi;
import cn.testin.plugins.testinpro.bean.TaskDetail;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.handler.TestinProHandler;
import hudson.model.TaskListener;
import net.sf.json.JSONObject;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/25
 *
 * 任务详情获取处理器
 */
public class TaskDetailTestinProHandler implements TestinProHandler {

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    public TaskDetailTestinProHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        this.builder = builder;
    }

    @Override
    public void handler() {
        ServiceContext serviceContext = builder.getServiceContext();
        String sid = serviceContext.getSid();
        Integer projectId = builder.getProjectId();
        String taskid = serviceContext.getTaskid();

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
        listener.getLogger().println("get task detail start...");

        String req = getReq(builder.getApikey(), sid, taskid, projectId);
        OpenApi openApi = new OpenApi(builder);
        Object res = openApi.doPress(req);
        if (isEmpty(res)){
            handlerTaskDetailException();
        }

        Integer bizCode = parseBizCode(res);
        if (isEmpty(bizCode)){
            handlerTaskDetailException();
        }
        serviceContext.setBizCode(bizCode);
        serviceContext.setTaskDetail(TaskDetail.parse(res));
        listener.getLogger().println("get task detail end\n");

    }

    private Integer parseBizCode(Object res) {
        if (res instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) res;
            String bizCode = "bizCode";
            if (jsonObject.containsKey(bizCode)){
                return jsonObject.getInt(bizCode);
            }
        }
        return null;
    }

    private String getReq(String apikey, String sid, String taskid, Integer projectId) {
        return String.format(
                "{\"mkey\":\"realtest\",\"op\":\"Task.details\",\"apikey\":\"%s\",\"sid\":\"%s\",\"data\":{\"projectid\":\"%s\",\"taskid\":\"%s\"},\"action\":\"app\",\"timestamp\":\"%s\"}",
                apikey,
                sid,
                projectId,
                taskid,
                System.currentTimeMillis()
                );
    }
}
