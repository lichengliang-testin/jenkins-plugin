package cn.testin.plugins.testinpro.bean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * task info
 */
public class TaskDetail implements Serializable {

    private static final long serialVersionUID = -13L;

    /**
     * 任务ID
     */
    private String taskid;

    /**
     * 业务编码
     */
    private Integer bizCode;

    /**
     * 系统类型
     */
    private Integer syspfId;

    /**
     * 邮件通知人
     */
    private List<String> emails;

    /**
     * 网络
     */
    private Map<String, Object> networks;

    /**
     * 应用信息
     */
    private AppInfo appInfo;

    /**
     * 设备信息
     */
    private List<DeviceInfo> deviceInfos;

    /**
     * 脚本信息
     */
    private List<ScriptInfo> scriptInfos;

    public static TaskDetail parse(Object res) {
        if (res instanceof JSONObject) {
            return parseJSONObject((JSONObject) res);
        }
        return null;
    }

    private static TaskDetail parseJSONObject(JSONObject res) {
        TaskDetail taskDetail = new TaskDetail();
        parseBase(taskDetail, res);
        parseAppInfo(taskDetail, res);
        parseDeviceInfos(taskDetail, res);
        parseScriptInfos(taskDetail, res);
        return taskDetail;
    }

    private static void parseScriptInfos(TaskDetail taskDetail, JSONObject res) {
        String scripts = "scripts";
        if (res.containsKey(scripts)) {
            JSONArray array = res.getJSONArray(scripts);
            if (!isEmpty(array)) {
                List<ScriptInfo> scriptInfos = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    Object bean = array.getJSONObject(i).toBean(ScriptInfo.class);
                    if (!isEmpty(bean)) {
                        scriptInfos.add((ScriptInfo) bean);
                    }
                }
                taskDetail.scriptInfos = scriptInfos;
            }
        }
    }

    private static void parseDeviceInfos(TaskDetail taskDetail, JSONObject res) {
        String devices = "devices";
        if (res.containsKey(devices)) {
            JSONArray array = res.getJSONArray(devices);
            if (!isEmpty(array)) {
                List<DeviceInfo> deviceInfos = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    Object bean = array.getJSONObject(i).toBean(DeviceInfo.class);
                    if (!isEmpty(bean)) {
                        deviceInfos.add((DeviceInfo) bean);
                    }
                }
                taskDetail.deviceInfos = deviceInfos;
            }
        }
    }

    private static void parseAppInfo(TaskDetail taskDetail, JSONObject res) {
        String appInfo = "appInfo";
        if (res.containsKey(appInfo)) {
            Object bean = res.getJSONObject(appInfo).toBean(AppInfo.class);
            if (!isEmpty(bean)) {
                taskDetail.appInfo = (AppInfo) bean;
            }
        }
    }

    private static void parseBase(TaskDetail taskDetail, JSONObject res) {
        String taskid = "taskid", bizCode = "bizCode", syspfId = "syspfId", emails = "emails", networks = "networks";

        if (res.containsKey(taskid)) {
            taskDetail.taskid = res.getString(taskid);
        }
        if (res.containsKey(bizCode)) {
            taskDetail.bizCode = res.getInt(bizCode);
        }
        if (res.containsKey(syspfId)) {
            taskDetail.syspfId = res.getInt(syspfId);
        }
        if (res.containsKey(emails)) {
            List<String> es = new ArrayList<>();
            JSONArray array = JSONArray.fromObject(res.getString(emails));
            if (!isEmpty(array)) {
                for (int i = 0; i < array.size(); i++) {
                    es.add(array.getString(i));
                }
                taskDetail.emails = es;
            }
        }
        if (res.containsKey(networks)) {
            //taskDetail.networks = new HashMap<>(JSONObject.fromObject(res.getString(networks)));
        }
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public Integer getBizCode() {
        return bizCode;
    }

    public void setBizCode(Integer bizCode) {
        this.bizCode = bizCode;
    }

    public Integer getSyspfId() {
        return syspfId;
    }

    public void setSyspfId(Integer syspfId) {
        this.syspfId = syspfId;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public Map<String, Object> getNetworks() {
        return networks;
    }

    public void setNetworks(Map<String, Object> networks) {
        this.networks = networks;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public List<DeviceInfo> getDeviceInfos() {
        return deviceInfos;
    }

    public void setDeviceInfos(List<DeviceInfo> deviceInfos) {
        this.deviceInfos = deviceInfos;
    }

    public List<ScriptInfo> getScriptInfos() {
        return scriptInfos;
    }

    public void setScriptInfos(List<ScriptInfo> scriptInfos) {
        this.scriptInfos = scriptInfos;
    }
}
