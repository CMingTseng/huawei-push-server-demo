package com.netease.study.pushserver;

import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

public class EduResponseBody {
    private ResponseBody mNetResponseBody;

    EduResponseBody(ResponseBody responseBody) {
        mNetResponseBody = responseBody;
    }

    public String contentType() {
        return mNetResponseBody.contentType().toString();
    }

    public long contentLength() {
        return mNetResponseBody.contentLength();
    }

    public final InputStream byteStream() {
        return mNetResponseBody.byteStream();
    }

    public final byte[] bytes() throws IOException {
        return mNetResponseBody.bytes();
    }

    public final String string() throws IOException {
        return mNetResponseBody.string();
    }

    public void close()  {
        mNetResponseBody.close();
    }
}
