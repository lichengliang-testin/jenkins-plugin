package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.api.channel.OpenApi;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.handler.TestinProHandler;
import hudson.FilePath;
import hudson.model.TaskListener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/24
 * <p>
 * Testin openApi 文件上传处理器(批量)
 */
public class UpLoadTestinProHandler implements TestinProHandler {

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    public UpLoadTestinProHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        this.builder = builder;
    }

    @Override
    public void handler() {
        ServiceContext serviceContext = builder.getServiceContext();

        String sid = serviceContext.getSid();
        // upload
        if (isEmpty(sid)) {
            handlerNotLoginException();
        }

        TaskListener listener = builder.getContext().getListener();

        if (isEmpty(builder.getPackagePath())) {
            listener.getLogger().println("not have PackagePath");
            return;
        }

        List<FilePath> files = scan(builder);

        if (isEmpty(files)) {
            listener.getLogger().println("no search app file!");
            return;
        }

        StringBuilder sb = new StringBuilder("match files:\n");
        for (FilePath file : files) {
            sb.append("\t").append(file.getName()).append("\n");
        }
        sb.append("start upload files..\n");
        listener.getLogger().println(sb.toString());

        long begin = System.currentTimeMillis();

        OpenApi openApi = new OpenApi(builder);

        for (FilePath file : files) {
            String req = getReq(builder.getApikey(), sid, suffix(file.getName()));
            Object res = openApi.doPress(req, true, file);
            if (isEmpty(res)) {
                handlerUploadException();
            }
            String fileUrl = Objects.toString(res);
            builder.addAppUrl(fileUrl);
            listener.getLogger().println(String.format("\t%s upload success! url{%s}", file, fileUrl));
        }

        listener.getLogger().println(
                "upload files end\n" +
                        "totalTime:" + (System.currentTimeMillis() - begin) + "\n");
    }

    private List<FilePath> scan(TestinProBuilder builder) {
        try {
            FilePath[] list = builder.getContext().getWorkspace().list(builder.getPackagePath());
            if (!isEmpty(list)) {
                return Arrays.stream(list).collect(Collectors.toList());
            }
        } catch (Exception e) {
            builder.getContext().getListener().error(e.getMessage());
        }
        return null;
    }

    private String getReq(String apikey, String sid, String suffix) {
        return String.format("{\"apikey\":\"%s\",\"timestamp\":%s,\"sid\":\"%s\",\"mkey\":\"fs\",\"action\":\"fs\",\"op\":\"File.upload\",\"data\":{\"suffix\":\"%s\"}}",
                apikey,
                System.currentTimeMillis(),
                sid,
                suffix);
    }

    private String suffix(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
