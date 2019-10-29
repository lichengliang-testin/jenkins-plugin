package cn.testin.plugins.testinpro.handler.impl;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.api.channel.OpenApi;
import cn.testin.plugins.testinpro.context.ServiceContext;
import cn.testin.plugins.testinpro.handler.TestinProHandler;

import java.io.PrintStream;
import java.util.Objects;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/24
 * <p>
 * Testin openApi 登录处理器
 */
public class LoginTestinProHandler implements TestinProHandler {

    /**
     * 处理构建器
     */
    private TestinProBuilder builder;

    public LoginTestinProHandler(TestinProBuilder builder) {
        if (isEmpty(builder)) {
            handlerNoArgException("builder ");
        }
        this.builder = builder;
    }

    @Override
    public void handler() {
        ServiceContext serviceContext = builder.getServiceContext();
        String sid = serviceContext.getSid();
        if (isEmpty(sid)) {
            // login
            PrintStream logger = builder.getContext().getListener().getLogger();
            logger.println(
                    "running login handler start...\n");

            OpenApi openApi = new OpenApi(builder);
            Object res = openApi.doPress(
                    getReq(
                            builder.getApikey(),
                            builder.getEmail(),
                            builder.getPwd())
            );

            if (isEmpty(res)) {
                handlerLoginException();
            }

            sid = Objects.toString(res);

            logger.println(
                    "running login handler end\n" +
                            "\tsid is: " + sid + "\n" +
                            "login success\n"
            );
            serviceContext.setSid(sid);
        }
    }

    private String getReq(String apikey, String email, String pwd) {
        return String.format("{\"apikey\":\"%s\",\"mkey\":\"usermanager\",\"op\":\"Login.login\",\"data\":{\"email\":\"%s\",\"pwd\":\"%s\"},\"action\":\"user\",\"timestamp\":%s}", apikey, email, pwd, System.currentTimeMillis());
    }

}
