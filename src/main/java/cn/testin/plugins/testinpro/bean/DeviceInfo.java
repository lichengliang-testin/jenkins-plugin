package cn.testin.plugins.testinpro.bean;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = -11L;

    // 设备串号
    private String deviceid;

    // 上位机账号
    private String ucomid;

    // 机型id
    private Integer modelid;

    // 机型版本
    private String releaseVer;

    // 机型名称
    private String modelName;

    // 品牌id
    private Integer brandid;

    // 品牌名称
    private String brandName;

    // 别名
    private String aliasName;

    // 屏宽
    private Integer dpiWidth;

    // 屏高
    private Integer dpiHeight;

    // 屏幕尺寸信息
    private Double screenSize;

    // 网络类型
    private String network;

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getUcomid() {
        return ucomid;
    }

    public void setUcomid(String ucomid) {
        this.ucomid = ucomid;
    }

    public Integer getModelid() {
        return modelid;
    }

    public void setModelid(Integer modelid) {
        this.modelid = modelid;
    }

    public String getReleaseVer() {
        return releaseVer;
    }

    public void setReleaseVer(String releaseVer) {
        this.releaseVer = releaseVer;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Integer getDpiWidth() {
        return dpiWidth;
    }

    public void setDpiWidth(Integer dpiWidth) {
        this.dpiWidth = dpiWidth;
    }

    public Integer getDpiHeight() {
        return dpiHeight;
    }

    public void setDpiHeight(Integer dpiHeight) {
        this.dpiHeight = dpiHeight;
    }

    public Double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Double screenSize) {
        this.screenSize = screenSize;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
