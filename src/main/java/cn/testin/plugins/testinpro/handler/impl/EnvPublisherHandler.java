package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.bean.*;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.enums.ResultCategoryEnum;
import cn.testin.plugins.testinpro.envs.TestinEnvironment;
import cn.testin.plugins.testinpro.handler.TestinProHandler;
import hudson.ExtensionList;
import hudson.model.EnvironmentContributor;

import java.util.List;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichegnliang
 * date 2019/10/29
 * <p>
 * tesin 环境添加处理器
 * <p>
 * TESTIN_JOB_ID
 * TESTIN_PROJECT_ID
 * TESTIN_TASKID_ID
 * TESTIN_REPORT_URL
 * TESTIN_APP_NAME
 * TESTIN_APP_VERSION
 * TESTIN_PACKAGE_URL
 * TESTIN_APP_OS
 * TESTIN_TASK_RESULT
 * TESTIN_SCRIPT_SUCCESS_COUNT
 * TESTIN_SCRIPT_FAIL_COUNT
 */
public class EnvPublisherHandler implements TestinProHandler {

    private TestinProBuilder builder;

    public EnvPublisherHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("build ");
        }
        this.builder = builder;
    }

    @Override
    public void handler() {
        ExtensionList<EnvironmentContributor> all = EnvironmentContributor.all();
        if (isEmpty(all)) {
            builder.getContext().getListener().getLogger().println("Environments is empty!");
            return;
        }
        boolean findTestinEnvironment = false;
        for (EnvironmentContributor environmentContributor : all) {
            if (environmentContributor instanceof TestinEnvironment) {
                findTestinEnvironment = true;
                TestinEnvironment environment = (TestinEnvironment) environmentContributor;
                pushEnvs(environment);
            }
        }
        if (!findTestinEnvironment) {
            builder.getContext().getListener().getLogger().println("no match testin envs!");
        }
    }

    private void pushEnvs(TestinEnvironment environment) {
        if (isEmpty(environment)) {
            return;
        }
        ServiceContext serviceContext = builder.getServiceContext();
        if (!isEmpty(serviceContext.getTaskDetail())) {
            TaskDetail taskDetail = serviceContext.getTaskDetail();
            pushAppEnvs(taskDetail.getAppInfo(), environment);
            pushScriptEnvs(taskDetail.getScriptInfos(), environment);
            pushDeviceEnvs(taskDetail.getDeviceInfos(), environment);
            pushTaskDetailEnvs(taskDetail, environment);
        }
        if (!isEmpty(serviceContext.getPortalTask())) {
            PortalTask portalTask = serviceContext.getPortalTask();
            pushTaskResultEnvs(portalTask.analysisResultCategory(), environment);
            pushSummyEnvs(portalTask.getTaskSummary(), environment);
            pushOtherEnvs(portalTask, environment);
        }

        pushBaseEnvs(environment);
    }

    private void pushTaskDetailEnvs(TaskDetail taskDetail, TestinEnvironment environment) {
    }

    private void pushOtherEnvs(PortalTask portalTask, TestinEnvironment environment) {
    }

    private void pushSummyEnvs(TaskSummary taskSummary, TestinEnvironment environment) {
        if (isEmpty(taskSummary)) {
            return;
        }
        pushScriptResultCountEnvs(taskSummary.getTestResult(), environment);
    }

    private void pushScriptResultCountEnvs(TaskSummary.SummaryInfo summaryInfo, TestinEnvironment environment) {
        if (isEmpty(summaryInfo)) {
            return;
        }
        int success = 0, fail = 0;
        if (!isEmpty(summaryInfo.getNodes())) {
            for (TaskSummary.Node node : summaryInfo.getNodes()) {
                Integer resultCategory = node.getResultCategory();
                Integer val = node.getVal();
                if (isEmpty(val) && isEmpty(resultCategory)) {
                    continue;
                }
                if (ResultCategoryEnum.pass.getValue().equals(resultCategory)) {
                    success += val;
                    continue;
                }
                if (ResultCategoryEnum.isError(resultCategory)) {
                    fail += val;
                }
            }
        }
        environment.addEnv("TESTIN_SCRIPT_SUCCESS_COUNT", String.valueOf(success));
        environment.addEnv("TESTIN_SCRIPT_FAIL_COUNT", String.valueOf(fail));
    }

    private void pushTaskResultEnvs(Integer resultCategory, TestinEnvironment environment) {
        if (isEmpty(resultCategory)) {
            return;
        }
        environment.addEnv("TESTIN_TASK_RESULT", ResultCategoryEnum.valueOf(resultCategory).getDescr());
    }

    private void pushBaseEnvs(TestinEnvironment environment) {
        ServiceContext serviceContext = builder.getServiceContext();
        Integer jobId = builder.getJobId();
        Integer projectId = builder.getProjectId();
        String taskid = serviceContext.getTaskid();
        String reportUrl = serviceContext.getReportUrl();

        environment.addEnv("TESTIN_JOB_ID", String.valueOf(jobId));
        environment.addEnv("TESTIN_PROJECT_ID", String.valueOf(projectId));
        environment.addEnv("TESTIN_TASKID_ID", taskid);
        environment.addEnv("TESTIN_REPORT_URL", reportUrl);
    }

    private void pushDeviceEnvs(List<DeviceInfo> deviceInfos, TestinEnvironment environment) {

    }

    private void pushScriptEnvs(List<ScriptInfo> scriptInfos, TestinEnvironment environment) {

    }

    private void pushAppEnvs(AppInfo appInfo, TestinEnvironment environment) {
        if (!isEmpty(appInfo)) {
            String appName = appInfo.getAppName();
            String appVersion = appInfo.getAppVersion();
            String packageUrl = appInfo.getPackageUrl();
            Integer syspfId = appInfo.getSyspfId();
            String os = syspfId == 1 ? "Android" : "IOS";

            environment.addEnv("TESTIN_APP_NAME", appName);
            environment.addEnv("TESTIN_APP_VERSION", appVersion);
            environment.addEnv("TESTIN_PACKAGE_URL", packageUrl);
            environment.addEnv("TESTIN_APP_OS", os);
        }
    }
}
