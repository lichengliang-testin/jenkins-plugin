package cn.testin.plugins.testinpro.context;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * @author lichengliang
 * date 2019/10/24
 *
 * Jenkins 执行上下文
 */
public class RunnerContext {

    private Run<?, ?> run;
    private FilePath workspace;
    private Launcher launcher;
    private TaskListener listener;

    public RunnerContext(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) {
        this.run = run;
        this.workspace = workspace;
        this.launcher = launcher;
        this.listener = listener;
    }

    public Run<?, ?> getRun() {
        return run;
    }

    public FilePath getWorkspace() {
        return workspace;
    }

    public Launcher getLauncher() {
        return launcher;
    }

    public TaskListener getListener() {
        return listener;
    }
}
