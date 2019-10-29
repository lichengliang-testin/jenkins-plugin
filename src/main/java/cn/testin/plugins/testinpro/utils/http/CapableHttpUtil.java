package cn.testin.plugins.testinpro.utils.http;

import cn.testin.plugins.testinpro.annotation.Nullable;
import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.exception.CommonException;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/24
 * <p>
 * HTTP的工具类
 */
public class CapableHttpUtil {

    private CapableHttpUtil() {
    }

    private final static String[] HTTP_PROTOCOLS = {"http://", "https://"};

    /**
     * 验证http是否有效
     *
     * @param url url地址
     * @return 是否有效
     */
    public static boolean isValid(@Nullable final String url) {
        return isValid(url, true);
    }

    public static boolean isValid(@Nullable final String url, boolean testConnect) {

        if (isEmpty(url)) {
            return false;
        }

        if (!isValidProtocol(url, true)) {
            return false;
        }

        if (!testConnect) {
            return true;
        }

        CapableHttpUtil capable = new CapableHttpUtil();
        try {

            return capable.execute(
                    capable.getClient(),
                    capable.getHeadRequest(url)
            ).isSuccessful();
        } catch (Exception e) {
            // ignore
        }

        return false;

    }

    public static Headers defaultHeads(){
        return Headers.of("Content-Type", "text/plain");
    }

    public static Headers testinHeads(String req){
        return Headers.of("UPLOAD-JSON", req);
    }

    public static boolean isValidProtocol(String url, boolean ignoreCase) {
        for (String protocol : HTTP_PROTOCOLS) {
            if (protocol.regionMatches(ignoreCase, 0, url, 0, protocol.length())) {
                return true;
            }
        }
        return false;
    }

    public static Response post(@Nullable final String url, String req) {

        if (isValid(url, false)) {
            CapableHttpUtil capable = new CapableHttpUtil();
            try {
                return capable.execute(
                        capable.getClient(),
                        capable.getPostRequest(
                                url,
                                defaultRequestBody(req)
                        )
                );

            } catch (IOException e) {
                e.printStackTrace();
                // TODO
            }
        }

        throw new CommonException(ErrorCode.unknownError.getCode(), String.format("url is invalid: Message.{%s}", url));
    }

    public static Response post(@Nullable final String url, Headers headers, String req) {
        return post(url, headers, defaultRequestBody(req));
    }

    public static Response post(@Nullable final String url, Headers headers, RequestBody body) {
        if (isValid(url, false)) {
            CapableHttpUtil capable = new CapableHttpUtil();
            try {
                return capable.execute(
                        capable.getClient(),
                        capable.getPostRequest(
                                url,
                                headers,
                                body
                        )
                );

            } catch (IOException e) {
                e.printStackTrace();
                // TODO
            }
        }

        throw new CommonException(ErrorCode.unknownError.getCode(), String.format("url is invalid: Message.{%s}", url));
    }

    @Nullable
    public static String stringData(Response response) {
        if (isEmpty(response)) {
            return null;
        }
        return getStringData(
                getResponseBody(response)
        );
    }

    public static RequestBody defaultRequestBody(String req) {
        return RequestBody.create(req, MediaType.parse("text/plain"));
    }

    public static RequestBody getFileRequestBody(File file) {
        return RequestBody.create(file, MediaType.parse("application/octet-stream"));
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder().build();
    }

    private Headers getHeads(Map<String, String> heads) {
        Headers.Builder builder = new Headers.Builder();
        heads.forEach((k, v) -> builder.add(k, v));
        return builder.build();
    }

    private RequestBody getFormRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        params.forEach((k, v) -> builder.add(k, v));
        return builder.build();
    }

    private Request getHeadRequest(final String url) {
        return new Request.Builder()
                .url(url)
                .head()
                .build();
    }

    private Request getGetRequest(final String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Request getPostRequest(final String url, final RequestBody body) {
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    private Request getPostRequest(final String url, Headers headers, final RequestBody body) {
        return new Request.Builder()
                .url(url)
                .post(body)
                .headers(headers)
                .build();
    }

    private Response execute(OkHttpClient client, Request request) throws IOException {
        Call call = client.newCall(request);
        return call.execute();
    }

    private void asyncExecute(OkHttpClient client, Request request, Callback callback) {
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    @Nullable
    private static ResponseBody getResponseBody(Response response) {
        if (!isEmpty(response)) {
            if (response.isSuccessful()) {
                return response.body();
            }
            handlerHttpException(response);
        }
        return null;
    }

    @Nullable
    private static String getStringData(ResponseBody body) {
        if (!isEmpty(body)) {
            try {
                return body.string();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO
            }
        }
        return null;
    }

    private static void handlerHttpException(Response response) {
        throw new CommonException(response.code(), String.format("http req is failure: Message.{%s}", response.message()));
    }
}
