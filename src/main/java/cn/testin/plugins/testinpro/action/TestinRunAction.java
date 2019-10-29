package cn.testin.plugins.testinpro.action;

import hudson.model.Run;

/**
 * @author lichengliang
 * date 2019/10/24
 *
 * 用于testin自定义执行适配
 */
public interface TestinRunAction {

    Run getRun();

}
