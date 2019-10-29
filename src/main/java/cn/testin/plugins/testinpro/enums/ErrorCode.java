package cn.testin.plugins.testinpro.enums;

/**
 * @author lichengliang
 */
public enum ErrorCode {

    /**
     * 系统处理未知情况使用
     */
    unknownError(10000, "未知异常"),
    notArgError(100, "缺少参数"),
    ;

    private int code;
    private String desc;

    ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
