package cn.testin.plugins.testinpro.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author lichenglaing
 * date 2019/10/29
 *
 * copy by real-test - 2019/10/29
 */
public enum ResultCategoryEnum {

	pending            	(-2,  "待执行"),
	running            	(-1,  "执行中"),
	noresult            	(0,  "无结果"),
	pass            		(1,  "通过"),
	installFailed		    (2,  "安装失败"),
	uninstallFailed		(3,  "卸载失败"),
	startupFailed		    (4,  "启动失败"),
	runfailed       		(5,  "运行失败"),
	funfailed       		(6,  "功能异常"),
	unexecuted		        (7,  "未执行"),		
	timeout         		(8,  "超时"),
	cancelled       		(9,  "取消"),
	monkeyFailed       	(10,  "monkey失败"),
	warn					(11, "警告"),
	appcrash				(12, "应用崩溃"),
	scripterror			(13, "脚本错误"),
	appnoresponse			(14, "应用无响应"),
	environmentexception	(15, "环境异常"),
	traversefail			(16, "遍历失败"),

	ignore			(100, "忽略"),
	;


	
	private Integer value;
	
	private String descr;

	ResultCategoryEnum(Integer value, String descr) {
		this.value = value;
		this.descr = descr;
	}
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	public static ResultCategoryEnum valueOf(Integer value) {
		ResultCategoryEnum result = null;
		if (value == null) {
			return result;
		}
		
		switch (value) {
		case 1:
			result = ResultCategoryEnum.pass;
			break;
		case 2:
			result = ResultCategoryEnum.installFailed;
			break;
		case 3:
			result = ResultCategoryEnum.uninstallFailed;
			break;
		case 4:
			result = ResultCategoryEnum.startupFailed;
			break;
		case 5:
			result = ResultCategoryEnum.runfailed;
			break;
		case 6:
			result = ResultCategoryEnum.funfailed;
			break;
		case 7:
			result = ResultCategoryEnum.unexecuted;
			break;
		case 8:
			result = ResultCategoryEnum.timeout;
			break;
		case 9:
			result = ResultCategoryEnum.cancelled;
			break;
		case 10:
			result = ResultCategoryEnum.monkeyFailed;
			break;
		case 11:
			result = ResultCategoryEnum.warn;
			break;
		case 12:
			result = ResultCategoryEnum.appcrash;
			break;
		case 13:
			result = ResultCategoryEnum.scripterror;
			break;
		case 14:
			result = ResultCategoryEnum.appnoresponse;
			break;
		case 15:
			result = ResultCategoryEnum.environmentexception;
			break;
		case 16:
			result = ResultCategoryEnum.traversefail;
			break;
		case 100:
			result = ResultCategoryEnum.ignore;
			break;
		case 0:
			result = ResultCategoryEnum.noresult;
			break;
		case -1:
			result = ResultCategoryEnum.running;
			break;
		case -2:
			result = ResultCategoryEnum.pending;
			break;
		default:
			break;
		}
		return result;		
	}

	public static Integer[] errorResultCategoryEnums(){
		return errorcodes;
	}

	public static List<Integer> implementErrorcodes(){
		return Arrays.asList(implementErrorcodes);
	}
	public static final Integer[] errorOrder = new Integer[] {
			cancelled.getValue(),
			timeout.getValue(),

			unexecuted.getValue(),

			installFailed.getValue(),
			startupFailed.getValue(),
			monkeyFailed.getValue(),
			runfailed.getValue(),
			funfailed.getValue(),
			appcrash.getValue(),
			appnoresponse.getValue(),
			scripterror.getValue(),
			warn.getValue(),
			environmentexception.getValue(),
			traversefail.getValue(),

			uninstallFailed.getValue(),

			pass.getValue(),

			ignore.getValue()
	};

	private static Integer[] errorcodes = new Integer[]{
			noresult.getValue(),
			installFailed.getValue(),
			uninstallFailed.getValue(),
			startupFailed.getValue(),
			runfailed.getValue(),
			funfailed.getValue(),
			unexecuted.getValue(),
			monkeyFailed.getValue(),
			warn.getValue(),
			appcrash.getValue(),
			scripterror.getValue(),
			appnoresponse.getValue(),
			environmentexception.getValue(),
			traversefail.getValue()
	};

	// 执行详情排序
	private static Integer[] implementErrorcodes = new Integer[]{

			installFailed .getValue(),
			startupFailed .getValue(),
			traversefail .getValue(),
			monkeyFailed .getValue(),
			appcrash .getValue(),
			appnoresponse.getValue(),
			scripterror .getValue(),
			uninstallFailed .getValue(),
			warn.getValue(),
			environmentexception.getValue(),

			noresult.getValue(),
			runfailed.getValue(),
			funfailed.getValue(),
			unexecuted.getValue(),
			timeout.getValue(),
			cancelled.getValue(),
			pass.getValue(),
			ignore.getValue()
	};

	public static boolean isError(Integer ResultCategoryEnum){
		for (Integer category : errorcodes) {
			if (category.equals(ResultCategoryEnum) && !ResultCategoryEnum.equals(noresult.getValue())) {
				return true;
			}
		}
		return false;
	}
}
