package cn.testin.plugins.testinpro.api.channel;

import cn.testin.plugins.testinpro.TestinProBuilder;
import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.exception.CommonException;
import hudson.FilePath;
import hudson.model.TaskListener;
import net.sf.json.JSONObject;

import java.io.File;

import static cn.testin.plugins.testinpro.utils.http.CapableHttpUtil.*;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/24
 *
 * openApi 调用封装
 * 记录请求响应报文
 * 记录请求是否成功
 * 解析响应参数并返回
 */
public class OpenApi {

    private TestinProBuilder builder;

    private static final String CODE = "code", DATA = "data", RESULT = "result", LIST = "list", OBJINFO = "objInfo";

    public OpenApi(TestinProBuilder builder) {
        this.builder = builder;
    }

    public Object doPress(final String req) {
        return doPress(req, false, null);
    }

    public Object doPress(final String req, boolean headReq, final FilePath filePath) {
        TaskListener listener = builder.getContext().getListener();
        long begin = System.currentTimeMillis();
        String response = null;
        try {
            if (headReq){
                if (isEmpty(filePath) || !filePath.exists()){
                    throw new CommonException(ErrorCode.unknownError.getCode(), String.format("invalid file or not exists: path{%s}", filePath));
                }
                response = stringData(post(builder.getUrl(), testinHeads(req), getFileRequestBody(new File(filePath.getRemote()))));
            }else {
                response = stringData(post(builder.getUrl(), defaultHeads(), req));
            }
            JSONObject jsonObject = JSONObject.fromObject(response);
            if (isSuccess(jsonObject)){
                Object result;
                if (isEmpty(result = parse(jsonObject))) {
                    result = parse(parseData(jsonObject));
                }
                return result;
            }
            return null;
        }catch (Exception e){
            listener.error(e.getMessage());
            return null;
        }finally {
            listener.getLogger().println(
                    String.format("req: %s \n", req) +
                    String.format("res: %s \n", response) +
                    String.format("totalTime: %s \n", (System.currentTimeMillis() - begin))
            );
        }
    }

    private boolean isSuccess(JSONObject jsonObject) {
        return jsonObject.containsKey(CODE) && jsonObject.getInt(CODE) == 0;
    }

    private Object parse(JSONObject jsonObject){
        if (null == jsonObject) {
            return null;
        }
        Object result ;
        if (isEmpty(result = parseResult(jsonObject))) {
            if (isEmpty(result = parseList(jsonObject))) {
                result = parseObjInfo(jsonObject);
            }
        }
        return result;
    }

    private Object parseResult(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        if (jsonObject.containsKey(RESULT))
            return jsonObject.get(RESULT);
        return null;
    }

    private Object parseList(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        if (jsonObject.containsKey(LIST))
            return jsonObject.get(LIST);
        return null;
    }

    private Object parseObjInfo(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        if (jsonObject.containsKey(OBJINFO))
            return jsonObject.get(OBJINFO);
        return null;
    }

    private JSONObject parseData(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        if (jsonObject.containsKey(DATA))
            return jsonObject.getJSONObject(DATA);
        return null;
    }
}
