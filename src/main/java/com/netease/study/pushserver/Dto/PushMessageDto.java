package com.netease.study.pushserver.Dto;

/**
 * Created by hanpfei0306 on 18-7-19.
 */
public class PushMessageDto {
    public static final int TYPE_TRANSPARENT_MESSAGE = 1;
    public static final int TYPE_NOTIFICATION_MESSAGE = 3;

    private int type;
    private Object body;
    private MessageActionDto action;

    public PushMessageDto() {
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    public void setAction(MessageActionDto action) {
        this.action = action;
    }

    public MessageActionDto getAction() {
        return action;
    }
}
