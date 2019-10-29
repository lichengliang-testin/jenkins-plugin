package cn.testin.plugins.testinpro.action;

import cn.testin.plugins.testinpro.bean.TaskDetail;
import cn.testin.plugins.testinpro.bean.TaskSummary;
import cn.testin.plugins.testinpro.enums.ResultCategoryEnum;
import cn.testin.plugins.testinpro.enums.TaskStatusEnum;
import jenkins.model.RunAction2;

import java.io.Serializable;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/25
 * <p>
 * TestinPro 侧边栏通道
 */
public class TestinProAction extends AbstractAction implements RunAction2, Serializable {

    private static final long serialVersionUID = 10000L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private Integer jobId;
    private Integer projectId;
    private String taskid;
    private String reportUrl;
    private Integer taskStatus;
    private String taskStatusDesc;
    private Integer resultCategory;
    private String resultResult;
    private int success;
    private int fail;
    private int other ;
    private TaskDetail taskDetail;
    private TaskSummary taskSummary;
    private boolean isError;

    public Integer getJobId() {
        return jobId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getTaskid() {
        return taskid;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public Integer getResultCategory() {
        return resultCategory;
    }

    public String getResultResult() {
        return resultResult;
    }

    public String getTaskStatusDesc() {
        return taskStatusDesc;
    }

    public TaskDetail getTaskDetail() {
        return taskDetail;
    }

    public Integer getSuccess() {
        return success;
    }

    public Integer getFail() {
        return fail;
    }

    public Integer getOther() {
        return other;
    }

    public TaskSummary getTaskSummary() {
        return taskSummary;
    }

    public boolean isError() {
        return isError;
    }

    public static class Builder {

        private TestinProAction action = new TestinProAction();

        public Builder jobId(Integer jobId) {
            action.jobId = jobId;
            return this;
        }

        public Builder projectId(Integer projectId) {
            action.projectId = projectId;
            return this;
        }

        public Builder taskid(String taskid) {
            action.taskid = taskid;
            return this;
        }

        public Builder reportUrl(String reportUrl) {
            action.reportUrl = reportUrl;
            return this;
        }

        public Builder taskStatus(Integer taskStatus) {
            action.taskStatus = taskStatus;
            return this;
        }

        public Builder resultCategory(Integer resultCategory) {
            action.resultCategory = resultCategory;
            return this;
        }

        public Builder taskDetail(TaskDetail taskDetail) {
            action.taskDetail = taskDetail;
            return this;
        }

        public Builder taskSummary(TaskSummary taskSummary) {
            action.taskSummary = taskSummary;
            return this;
        }

        public TestinProAction build() {
            if (!isEmpty(action.resultCategory)) {
                action.isError = ResultCategoryEnum.isError(action.resultCategory);
                action.resultResult = ResultCategoryEnum.valueOf(action.resultCategory).getDescr();
            }

            if (!isEmpty(action.taskStatus)) {
                action.taskStatusDesc = TaskStatusEnum.search(action.taskStatus).name();
            }

            if (!isEmpty(action.taskSummary)
                    && !isEmpty(action.taskSummary.getTestResult())
                        && !isEmpty(action.taskSummary.getTestResult().getNodes())) {
                for (TaskSummary.Node node : action.taskSummary.getTestResult().getNodes()) {
                    Integer resultCategory = node.getResultCategory();
                    Integer val = node.getVal();
                    if (isEmpty(val) && isEmpty(resultCategory)) {
                        continue;
                    }
                    if (ResultCategoryEnum.pass.getValue().equals(action.resultCategory)) {
                        action.success += val;
                        continue;
                    }
                    if (ResultCategoryEnum.isError(action.resultCategory)) {
                        action.fail += val;
                        continue;
                    }
                    action.other += val;
                }
            }
            return action;
        }
    }
}
