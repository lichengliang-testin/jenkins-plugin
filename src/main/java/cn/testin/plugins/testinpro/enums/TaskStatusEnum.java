package cn.testin.plugins.testinpro.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * data 2019/10/25
 * <p>
 * 任务执行状态枚举
 */
public enum TaskStatusEnum {

    /**
     * 任务关闭
     */
    cancel(1, 1),
    /**
     * 任务删除
     */
    delete(2, 1),
    /**
     * 任务等待
     */
    waiting(3, 0),
    /**
     * 任务执行
     */
    running(4, 0),
    /**
     * 任务完成
     */
    complete(5, 1),
    /**
     * 任务取消中
     */
    to_cancel(6, 0),
    /**
     * 未知
     */
    unknown(100, 1),;

    private int status, code;

    TaskStatusEnum(int status, int code) {
        this.status = status;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TaskStatusEnum search(Integer status) {
        TaskStatusEnum e = M.get(status);
        if (isEmpty(e)) {
            return unknown;
        }
        return e;
    }

    private static final Map<Integer, TaskStatusEnum> M = new HashMap<>(16);

    static {
        Arrays.stream(TaskStatusEnum.values()).forEach(e -> M.put(e.status, e));
    }
}
