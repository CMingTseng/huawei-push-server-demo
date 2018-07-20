package com.netease.study.pushserver.Dto;

/**
 * Created by hanpfei0306 on 18-7-19.
 */
public class ActionParamDto {

    private String intent;
    private String url;
    private String appPkgName;

    public ActionParamDto() {
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getIntent() {
        return intent;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setAppPkgName(String appPkgName) {
        this.appPkgName = appPkgName;
    }

    public String getAppPkgName() {
        return appPkgName;
    }

}
