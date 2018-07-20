package com.netease.study.pushserver;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpEngine {
    private static final long CONNECT_TIMEOUT_SECONDS = 2;
    private static final long READ_TIMEOUT_SECONDS = 3;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");

    private static HttpEngine sHttpEngine;

    public static final synchronized HttpEngine getInstance() {
        if (sHttpEngine == null) {
            sHttpEngine = new HttpEngine();
        }
        return sHttpEngine;
    }

    private OkHttpClient mOkHttpClient;

    private HttpEngine() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }

    public void post(String url, String content, EduCallback eduCallback) {
        post(url, null, content, eduCallback);
    }

    public void post(String url, Map<String, String> headers, String content, EduCallback eduCallback) {
        post(url, headers, content, null, eduCallback);
    }

    private String constructUrl(String url, Map<String, String> params) {
        String finalUrl = url;
        String paramStr = constructParamsString(params);
        if (!StringUtils.isEmpty(paramStr)) {
            if (finalUrl.contains("?")) {
                finalUrl = finalUrl + "&" + paramStr;
            } else {
                finalUrl = finalUrl + "?" + paramStr;
            }
        }
        return finalUrl;
    }

    public void post(String url, Map<String, String> headers, String content, Map<String, String> params,
                     EduCallback eduCallback) {
        String finalUrl = constructUrl(url, params);

        RequestBody body = RequestBody.create(JSON, content);
        Request.Builder reqBuilder = new Request.Builder()
                .url(finalUrl)
                .post(body);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                reqBuilder.header(header.getKey(), header.getValue());
            }
        }
        Request request = reqBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new CallbackAdapter(eduCallback));
    }

    public EduResponse postSync(String url, Map<String, String> headers, String content, MediaType mediaType) {
        return postSync(url, headers, content, null, mediaType);
    }

    public EduResponse postSync(String url, Map<String, String> headers, String content, Map<String, String> params,
                                MediaType mediaType) {
        String finalUrl = constructUrl(url, params);

        MediaType realMediaType = JSON;
        if (mediaType != null) {
            realMediaType = mediaType;
        }
        RequestBody body = RequestBody.create(realMediaType, content);

        Request.Builder reqBuilder = new Request.Builder()
                .url(finalUrl)
                .post(body);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                reqBuilder.header(header.getKey(), header.getValue());
            }
        }
        Request request = reqBuilder.build();

        EduResponse eduResponse = null;
        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
        } catch (IOException e) {
        }
        if (response != null) {
            eduResponse = new EduResponse(response);
        }
        return eduResponse;
    }

    private String constructParamsString(Map<String, String> params) {
        StringBuilder paramStr = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                paramStr.append(param.getKey());
                paramStr.append("=");
                paramStr.append(param.getValue());
                paramStr.append("&");
            }
            paramStr.deleteCharAt(paramStr.length() - 1);
        }
        return paramStr.toString();
    }

    private Request buildGetRequest(String url, Map<String, String> headers, Map<String, String> params) {
        String finalUrl = constructUrl(url, params);

        Request.Builder reqBuilder = new Request.Builder()
                .url(finalUrl);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                reqBuilder.header(header.getKey(), header.getValue());
            }
        }
        Request request = reqBuilder.build();
        return request;
    }

    public void get(String url, Map<String, String> headers, Map<String, String> params, EduCallback eduCallback) {
        Request request = buildGetRequest(url, headers, params);
        mOkHttpClient.newCall(request).enqueue(new CallbackAdapter(eduCallback));
    }

    public EduResponse get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        Request request = buildGetRequest(url, headers, params);
        Response response = mOkHttpClient.newCall(request).execute();
        EduResponse eduResponse = new EduResponse(response);
        return eduResponse;
    }

    private static final class CallbackAdapter implements Callback {
        private EduCallback mRealCallback;

        CallbackAdapter(EduCallback eduCallback) {
            mRealCallback = eduCallback;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mRealCallback.onFailure(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            EduResponse eduResponse = new EduResponse(response);
            mRealCallback.onResponse(eduResponse);
        }
    }

}
