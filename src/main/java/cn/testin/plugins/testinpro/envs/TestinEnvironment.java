package cn.testin.plugins.testinpro.envs;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentContributor;
import hudson.model.Run;
import hudson.model.TaskListener;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lichegnliang
 * date 2019/10/29
 *
 * tesin 环境变量
 */
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

@Extension
public class TestinEnvironment extends EnvironmentContributor {

    private final Map<String, String> interiorEnvs = new HashMap<>(16);

    @Override
    public void buildEnvironmentFor(@Nonnull Run r, @Nonnull EnvVars envs, @Nonnull TaskListener listener) throws IOException, InterruptedException {
        super.buildEnvironmentFor(r, envs, listener);
        if (isEmpty(interiorEnvs) || isEmpty(envs)) {
            return;
        }
        envs.putAll(interiorEnvs);
    }

    public void addEnvs(Map<String, String> envs) {
        interiorEnvs.putAll(envs);
    }

    public void addEnv(String key, String value) {
        if (null == value) {
            return;
        }
        interiorEnvs.put(key, value);
    }
}
