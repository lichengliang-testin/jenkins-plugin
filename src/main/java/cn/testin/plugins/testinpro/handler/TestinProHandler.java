package cn.testin.plugins.testinpro.handler;

import cn.testin.plugins.testinpro.Messages;
import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.exception.CommonException;

/**
 * @author lichengliang
 * date 2019/10/24
 * Testin openApi 处理器
 */
public interface TestinProHandler {

    /**
     * 业务处理方法
     */
    void handler();

    /**
     * 处理之前
     * @return true 代表执行处理方法 false 跳过执行处理方法
     */
    default boolean preHandler() {
        return true;
    }

    /**
     * 处理之后
     */
    default void afterHandler() {}

    /**
     * 一定执行的处理器
     */
    default void finallyHandler () {}

    /**
     * 异常处理器
     *
     * @param throwable 异常
     */
    default void throwableHandler(Throwable throwable) {
        // ignore throwable.printStackTrace();
    }

    /**
     * 针对异步执行的任务是否需要重试
     *
     * @return true 需要重试 目前默认3次重试 false 不需要重试
     */
    default boolean needRetry() {
        return false;
    }

    /**
     * 登录失败
     */
    default void handlerLoginException() {
        throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_RunnerInfo_LoginFailure());
    }

    /**
     * 未登录
     */
    default void handlerNotLoginException() {
        throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_RunnerInfo_NotLogin());
    }

    /**
     * 上传失败
     */
    default void handlerUploadException() {
        throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_RunnerInfo_UploadFailure());
    }

    /**
     * 缺少参数异常
     * @param argName 参数名称
     */
    default void handlerNoArgException(String argName) {
        throw new CommonException(ErrorCode.notArgError.getCode(), Messages.TestinProBuilder_RunnerInfo_LackArg(argName));
    }

    /**
     * 缺少参数异常
     * @param msg 错误信息
     */
    default void handlerException(String msg) {
        throw new CommonException(ErrorCode.otherError.getCode(), msg);
    }

    /**
     * 任务执行失败
     */
    default void handlerTaskExecuteException() {
        throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_RunnerInfo_TaskExecuteException());
    }

    /**
     * 获取报告链接失败
     */
    default void handlerReportUrlException() {
        throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_RunnerInfo_GetReportUrlError());
    }

    /**
     * 获取报告链接失败
     */
    default void handlerTaskDetailException() {
        throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_RunnerInfo_GetTaskDetailError());
    }

    /**
     * 获取报告链接失败
     */
    default void handlerTaskResultException() {
        throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_RunnerInfo_GetTaskResultError());
    }

    /**
     * 测试结果失败
     */
    default void handlerTaskExecuteFailException() {
        throw new CommonException(ErrorCode.unknownError.getCode(), Messages.TestinProBuilder_RunnerInfo_TaskExecuteFailure());
    }

}
