package cn.testin.plugins.testinpro.action;

import jenkins.model.RunAction2;

import java.io.Serializable;

public class TaskAction extends AbstractAction implements RunAction2, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String taskid;

    public TaskAction(String taskid) {
        this.taskid = taskid;
    }

    public String getTaskid() {
        return taskid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
