package cn.testin.plugins.testinpro.action;

import hudson.Functions;
import hudson.model.Run;
import jenkins.model.RunAction2;

/**
 * @author lichengliang
 * date 2019/10/24
 *
 * 适配执行方法
 */
public abstract class AbstractAction implements TestinRunAction, RunAction2 {

    /**
     * 侧边栏需要
     */
    protected transient Run run;

    /**
     * TestinPro 图标地址
     *
     */
    @Override
    public String getIconFileName() {
        return Functions.getResourcePath() + "/plugin/testinpro/testin-logo.jpg";
    }

    /**
     * 标题栏
     *
     */
    @Override
    public String getDisplayName() {
        return "TestinPro";
    }

    /**
     * URL 地址，例如 http://127.0.0.1:8080/jenkins/job/oops/12/testinpro/
     *
     */
    @Override
    public String getUrlName() {
        return "testinpro";
    }

    @Override
    public Run getRun() {
        return run;
    }

    @Override
    public void onAttached(Run<?, ?> run) {
        this.run = run;
    }

    @Override
    public void onLoad(Run<?, ?> run) {
        this.run = run;
    }
}
