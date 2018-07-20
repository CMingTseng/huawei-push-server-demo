package com.netease.study.pushserver;

import okhttp3.Response;

import java.util.List;
import java.util.Map;

public class EduResponse {
    private Response mNetResponse;

    EduResponse(Response netResponse) {
        mNetResponse = netResponse;
    }

    public int code() {
        return mNetResponse.code();
    }

    public boolean isSuccessful() {
        return mNetResponse.isSuccessful();
    }

    public String message() {
        return mNetResponse.message();
    }

    public List<String> headers(String name) {
        return mNetResponse.headers(name);
    }

    public String header(String name) {
        return mNetResponse.header(name);
    }

    public String header(String name, String defaultValue) {
        return mNetResponse.header(name, defaultValue);
    }

    public Map<String, List<String>> headers() {
        return mNetResponse.headers().toMultimap();
    }

    public EduResponseBody body() {
        return new EduResponseBody(mNetResponse.body());
    }

}
