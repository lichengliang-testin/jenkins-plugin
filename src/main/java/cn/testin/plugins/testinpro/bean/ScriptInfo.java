package cn.testin.plugins.testinpro.bean;

import java.io.Serializable;

public class ScriptInfo implements Serializable {
    private static final long serialVersionUID = -15L;

    private Integer scriptid;
    private Integer scriptNo;
    private Integer osType;
    private Integer orderNum;
    private String scriptTags;
    private Integer scriptType;
    private String scriptDescr;

    public Integer getScriptid() {
        return scriptid;
    }

    public void setScriptid(Integer scriptid) {
        this.scriptid = scriptid;
    }

    public Integer getScriptNo() {
        return scriptNo;
    }

    public void setScriptNo(Integer scriptNo) {
        this.scriptNo = scriptNo;
    }

    public Integer getOsType() {
        return osType;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getScriptTags() {
        return scriptTags;
    }

    public void setScriptTags(String scriptTags) {
        this.scriptTags = scriptTags;
    }

    public Integer getScriptType() {
        return scriptType;
    }

    public void setScriptType(Integer scriptType) {
        this.scriptType = scriptType;
    }

    public String getScriptDescr() {
        return scriptDescr;
    }

    public void setScriptDescr(String scriptDescr) {
        this.scriptDescr = scriptDescr;
    }
}
