package com.netease.study.pushserver.Dto;

/**
 * Created by hanpfei0306 on 18-7-19.
 */
public class AccessTokenDto {
    private String access_token;
    private long expires_in;
    private String token_type;

    public AccessTokenDto() {
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getToken_type() {
        return token_type;
    }
}
