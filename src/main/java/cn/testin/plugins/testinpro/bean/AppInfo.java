package cn.testin.plugins.testinpro.bean;

import java.io.Serializable;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 *
 * app info
 */
public class AppInfo implements Serializable {

    private static final long serialVersionUID = -10L;

    // 应用名称
    private String appName;

    private String originalPkgUrl;

    // 应用包地址
    private String packageUrl;

    // 应用包名
    private String packageName;

    // 应用包大小
    private Long appSize;

    // 应用包版本
    private String appVersion;

    // 系统平台
    private Integer syspfId;

    // 应用id
    private Integer appId;

    // 包id
    private Integer pkgId;

    // 启动路劲
    private String startupPath;

    // 应用logo
    private String iconUrl;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageUrl() {
        if (!isEmpty(originalPkgUrl)) {
            return originalPkgUrl;
        }
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Long getAppSize() {
        return appSize;
    }

    public void setAppSize(Long appSize) {
        this.appSize = appSize;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getSyspfId() {
        return syspfId;
    }

    public void setSyspfId(Integer syspfId) {
        this.syspfId = syspfId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getPkgId() {
        return pkgId;
    }

    public void setPkgId(Integer pkgId) {
        this.pkgId = pkgId;
    }

    public String getStartupPath() {
        return startupPath;
    }

    public void setStartupPath(String startupPath) {
        this.startupPath = startupPath;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getOriginalPkgUrl() {
        return originalPkgUrl;
    }

    public void setOriginalPkgUrl(String originalPkgUrl) {
        this.originalPkgUrl = originalPkgUrl;
    }
}
