package cn.testin.plugins.testinpro;

import cn.testin.plugins.testinpro.annotation.Nullable;
import cn.testin.plugins.testinpro.context.RunnerContext;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.exception.CommonException;
import cn.testin.plugins.testinpro.handler.TestinProHandler;
import cn.testin.plugins.testinpro.handler.impl.*;
import cn.testin.plugins.testinpro.service.TestinProService;
import cn.testin.plugins.testinpro.service.impl.TestinProServiceImpl;
import cn.testin.plugins.testinpro.utils.ResourceUtils;
import cn.testin.plugins.testinpro.utils.http.CapableHttpUtil;
import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;

import static cn.testin.plugins.testinpro.utils.BeanUtils.instantiation;
import static cn.testin.plugins.testinpro.utils.StringUtils.tokenizeToSplit;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/24
 * <p>
 * testin jenkins plugin builder
 */
public class TestinProBuilder extends Builder implements SimpleBuildStep, Serializable {

    private static final long serialVersionUID = 1L;
    private static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";
    private static final String CONFIG_CONF = "config.conf";
    private static final Properties CONF;

    private String url;
    private String apikey;
    private String email;
    private String pwd;
    private Integer projectId;
    private Integer jobId;
    private String taskDescr;
    private String packagePath;
    private String packageUrl;

    private Long sleep;
    private Integer share;

    private RunnerContext context;
    private ServiceContext serviceContext = new ServiceContext();

    private List<String> appUrls = new ArrayList<>();

    @DataBoundSetter
    public void setUrl(String url) {
        this.url = url;
    }

    @DataBoundSetter
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    @DataBoundSetter
    public void setEmail(String email) {
        this.email = email;
    }

    @DataBoundSetter
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @DataBoundSetter
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @DataBoundSetter
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @DataBoundSetter
    public void setTaskDescr(String taskDescr) {
        this.taskDescr = taskDescr;
    }

    @DataBoundSetter
    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    @DataBoundSetter
    public void setSleep(Long sleep) {
        this.sleep = sleep;
    }

    @DataBoundSetter
    public void setShare(Integer share) {
        this.share = share;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUrl() {
        return url;
    }

    public String getApikey() {
        return apikey;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public String getTaskDescr() {
        return taskDescr;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public Long getSleep() {
        return sleep;
    }

    public Integer getShare() {
        return share;
    }

    public RunnerContext getContext() {
        return context;
    }

    public ServiceContext getServiceContext() {
        return serviceContext;
    }

    public List<String> getAppUrls() {
        return Collections.unmodifiableList(appUrls);
    }

    public void addAppUrl(@Nullable String url) {
        if (isEmpty(url)) {
            return;
        }
        appUrls.add(url);
    }

    public String getPackageUrl() {
        if (isEmpty(packageUrl) && !isEmpty(appUrls)) {
            return appUrls.get(0);
        }
        return packageUrl;
    }

    private static Properties loadProperties() {
        InputStream inputStream = ResourceUtils.getInputStream(CONFIG_CONF);
        if (isEmpty(inputStream)) {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    /**
     * 脚本配置增加处理 -- 后期调整
     */
    private TestinProService getDefaultTestinProService() {
        TestinProServiceImpl testinProService = new TestinProServiceImpl(this);

        List<TestinProHandler> handlers = getHandlers();

        if (isEmpty(handlers)) {
            registerDefaultHandlers(testinProService);
            return testinProService;
        }

        registerHandlers(handlers, testinProService);

        return testinProService;
    }

    private void registerHandlers(List<TestinProHandler> handlers, TestinProServiceImpl testinProService) {
        for (TestinProHandler handler : handlers) {
            testinProService.register(handler);
        }
    }

    private List<TestinProHandler> getHandlers() {
        String handlerKey = "handlers";
        String handlers;
        if (isEmpty(CONF) || isEmpty(handlers = CONF.getProperty(handlerKey))) {
            return null;
        }

        return getHandlers(handlers);
    }

    private List<TestinProHandler> getHandlers(String handlers) {
        List<String> handlerList = tokenizeToSplit(handlers, MULTI_VALUE_ATTRIBUTE_DELIMITERS);
        List<TestinProHandler> result = new ArrayList<>(handlerList.size() << 1);
        for (String handler : handlerList) {
            try {
                TestinProHandler testinProHandler = instantiation(handler, this);
                result.add(testinProHandler);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new CommonException(ErrorCode.unknownError.getCode(), e);
            }
        }
        return result;
    }

    private void registerDefaultHandlers(TestinProServiceImpl testinProService) {
        if (isEmpty(testinProService)) {
            return;
        }
        testinProService.register(new LoginTestinProHandler(this));
        testinProService.register(new UpLoadTestinProHandler(this));
        testinProService.register(new TaskExectuteTestinProHandler(this));
        testinProService.register(new ReportUrlTestinProHandler(this));
        testinProService.register(new TaskDetailTestinProHandler(this));
        testinProService.register(new TaskResultTestinProHandler(this));
        testinProService.register(new EnvPublisherHandler(this));
        testinProService.register(new DefaultDisplayTestinProHandler(this));
    }

    @DataBoundConstructor
    public TestinProBuilder(String url, String apikey, String email, String pwd, Integer projectId, Integer jobId, String taskDescr, String packagePath) {
        super();
        this.url = url;
        this.apikey = apikey;
        this.email = email;
        this.pwd = pwd;
        this.projectId = projectId;
        this.jobId = jobId;
        this.packagePath = packagePath;
        if (!isEmpty(taskDescr)) {
            this.taskDescr = taskDescr;
        }
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {

        listener.getLogger().println("TestinPro plugin is running with parameters\n" +

                "Build Version:" + run.getNumber() + "\n" +
                "Build Workspace=" + workspace + "\n" +

                "OpenAPI Configuration:" + "\n" +
                "server url: " + url + "\n" +
                "apikey: " + apikey + "\n" +
                "account: " + email + "\n" +
                "projectId: " + projectId + "\n" +
                "jobId: " + jobId + "\n" +
                "share: " + share + "\n" +
                "packagePath: " + packagePath + "\n" +
                (isEmpty(taskDescr) ? "" : "taskDescr: " + taskDescr)
        );

        this.context = new RunnerContext(run, workspace, launcher, listener);

        if (isEmpty(serviceContext)) {
            this.serviceContext = new ServiceContext();
        }

        // 初始化 Service
        TestinProService service = getDefaultTestinProService();

        service.execute();
    }

    @Symbol("testinPro")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckUrl(@QueryParameter String url) {
            if (CapableHttpUtil.isValid(url))
                return FormValidation.ok();
            return FormValidation.error(Messages.TestinProBuilder_DescriptorImpl_errors_missingUrl());
        }

        public FormValidation doCheckApikey(@QueryParameter String apikey) {
            return check(apikey, Messages.TestinProBuilder_DescriptorImpl_errors_missingApikey());
        }

        public FormValidation doCheckEmail(@QueryParameter String email) {
            return check(email, Messages.TestinProBuilder_DescriptorImpl_errors_missingEmail());
        }

        public FormValidation doCheckPwd(@QueryParameter String pwd) {
            return check(pwd, Messages.TestinProBuilder_DescriptorImpl_errors_missingPwd());
        }

        public FormValidation doCheckProjectId(@QueryParameter Integer projectId) {
            return check(projectId, Messages.TestinProBuilder_DescriptorImpl_errors_missingProjectId());
        }

        public FormValidation doCheckJobId(@QueryParameter Integer jobId) {
            return check(jobId, Messages.TestinProBuilder_DescriptorImpl_errors_missingJobId());
        }

        public FormValidation doCheckPackagePath(@QueryParameter("packagePath") String path) {
            return check(path, Messages.TestinProBuilder_DescriptorImpl_errors_missingPackagePath());
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // 适用于所有构建类型
            return true;
        }

        @Override
        @Nullable
        public String getDisplayName() {
            // 插件的名称，支持国际化
            return Messages.TestinProBuilder_DescriptorImpl_DisplayName();
        }

        private FormValidation check(Object value, String msg) {
            if (isEmpty(value))
                return FormValidation.error(msg);
            return FormValidation.ok();
        }
    }

    static {
        CONF = loadProperties();
    }
}
