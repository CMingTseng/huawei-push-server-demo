package com.netease.study.pushserver;

import java.io.IOException;

public interface EduCallback {
    void onFailure(IOException e);

    void onResponse(EduResponse response) throws IOException;
}
