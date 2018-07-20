package com.netease.study.pushserver.Dto;

public class HWPushMsgDto {
    private PushMessageDto msg;
    private ExtDto ext;

    public HWPushMsgDto() {
    }

    public void setMsg(PushMessageDto msg) {
        this.msg = msg;
    }

    public PushMessageDto getMsg() {
        return msg;
    }

    public void setExt(ExtDto ext) {
        this.ext = ext;
    }

    public ExtDto getExt() {
        return ext;
    }
}
