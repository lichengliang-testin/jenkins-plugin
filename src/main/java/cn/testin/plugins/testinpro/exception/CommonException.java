package cn.testin.plugins.testinpro.exception;

/**
 * @author lichengliang
 * date 2019/10/24
 *
 * 异常处理类
 */
public class CommonException extends RuntimeException{

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public CommonException(Integer code) {
        super();
        this.code = code;
    }

    public CommonException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CommonException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public CommonException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
