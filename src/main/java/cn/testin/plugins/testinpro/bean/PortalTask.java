package cn.testin.plugins.testinpro.bean;

import cn.testin.plugins.testinpro.annotation.Nullable;
import cn.testin.plugins.testinpro.enums.TaskStatusEnum;

import java.io.Serializable;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 *
 * 门户任务实体类
 */
public class PortalTask implements Serializable {

	private static final long serialVersionUID = -12L;

	/** 项目组id */
	private Integer projectid;
	
	private Integer eid;

	/** 用户id */
	private Integer userid;

	/** 任务id */
	private String taskid;

	/** 应用id */
	private Integer appid;

	/** 包id */
	private Integer pkgid;

	/** 应用渠道号 */
	private String channelId;

	/** 应用名 */
	private String appName;

	/** 包名 */
	private String packageName;

	/** 应用包地址 */
	private String packageUrl;

	/** 应用版本 */
	private String appVersion;

	/** 业务编码 */
	private Integer bizCode;

	/** 系统平台 */
	private Integer syspfId;

	/** 任务状态 */
	private Integer taskStatus;

	/** 任务执行结果 */
	private Integer execResult;

	/** 任务描述 */
	private String taskDescr;

	/** 内容 里面存测试扭转信息， 测试结果统计信息 */
	private String content;

	private String createPerson;

	private String iconUrl;

	public Integer getEid() {
		return eid;
	}

	public void setEid(Integer eid) {
		this.eid = eid;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public Integer getProjectid() {
		return projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getAppid() {
		return appid;
	}

	public void setAppid(Integer appid) {
		this.appid = appid;
	}

	public Integer getPkgid() {
		return pkgid;
	}

	public void setPkgid(Integer pkgid) {
		this.pkgid = pkgid;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageUrl() {
		return packageUrl;
	}

	public void setPackageUrl(String packageUrl) {
		this.packageUrl = packageUrl;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Integer getBizCode() {
		return bizCode;
	}

	public void setBizCode(Integer bizCode) {
		this.bizCode = bizCode;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskDescr() {
		return taskDescr;
	}

	public void setTaskDescr(String taskDescr) {
		this.taskDescr = taskDescr;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSyspfId() {
		return syspfId;
	}

	public void setSyspfId(Integer syspfId) {
		this.syspfId = syspfId;
	}

	public Integer getExecResult() {
		return execResult;
	}

	public void setExecResult(Integer execResult) {
		this.execResult = execResult;
	}

	@Nullable
	public Integer analysisResultCategory() {
		TaskSummary taskSummary ;
		if (!isEmpty(taskSummary = getTaskSummary())) {
			return taskSummary.getTaskResultCategory();
		}
		return null;
	}

	@Nullable
	public TaskSummary getTaskSummary(){
		if (!isEmpty(this) &&
				isDone(this.getTaskStatus())) {
			String content = this.getContent();
			if (!isEmpty(content)) {
				return TaskSummary.parse(content);
			}
		}
		return null;
	}

	/**
	 * 任务状态【1：已取消；2：已删除；3：等待测试；4：执行中；5：测试完成；6：取消中】
	 */
	public static boolean isDone(Integer taskStatus) {
		if (isEmpty(taskStatus)) {
			return false;
		}

		return 1 == TaskStatusEnum.search(taskStatus).getCode();
	}
}

