package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.envs.TestinEnvironment;
import cn.testin.plugins.testinpro.handler.TestinProHandler;
import hudson.ExtensionList;
import hudson.model.EnvironmentContributor;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * 环境清理执行器
 */
public class CleanEnvTestinProHandler implements TestinProHandler{

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    public CleanEnvTestinProHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        this.builder = builder;
    }

    @Override
    public void handler() {
        ExtensionList<EnvironmentContributor> all = EnvironmentContributor.all();
        if (isEmpty(all)) {
            builder.getContext().getListener().getLogger().println("Environments is empty!");
            return;
        }
        for (EnvironmentContributor environmentContributor : all) {
            if (environmentContributor instanceof TestinEnvironment) {
                TestinEnvironment environment = (TestinEnvironment) environmentContributor;
                environment.clean();
            }
        }
    }
}
