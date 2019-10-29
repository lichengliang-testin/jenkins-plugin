package cn.testin.plugins.testinpro.action;

import jenkins.model.RunAction2;

import java.io.Serializable;

public class ReportAction extends AbstractAction implements RunAction2, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报告的地址
     */
    private String url;

    public ReportAction(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
