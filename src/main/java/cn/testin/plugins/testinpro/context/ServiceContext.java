package cn.testin.plugins.testinpro.context;

import cn.testin.plugins.testinpro.bean.PortalTask;
import cn.testin.plugins.testinpro.bean.TaskDetail;

import java.io.Serializable;

/**
 * @author lichenglaing
 * date 2019/10/25
 *
 * 业务上下文
 */
public class ServiceContext implements Serializable {

    private static final long serialVersionUID = -1L;

    private String sid;
    private String taskid;
    private String reportUrl;
    private Integer bizCode;

    private PortalTask portalTask;

    private TaskDetail taskDetail;

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getSid() {
        return sid;
    }

    public String getTaskid() {
        return taskid;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public Integer getBizCode() {
        return bizCode;
    }

    public void setBizCode(Integer bizCode) {
        this.bizCode = bizCode;
    }

    public PortalTask getPortalTask() {
        return portalTask;
    }

    public void setPortalTask(PortalTask portalTask) {
        this.portalTask = portalTask;
    }

    public TaskDetail getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}
