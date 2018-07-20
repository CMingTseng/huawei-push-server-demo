package com.netease.study.pushserver.Dto;

/**
 * Created by hanpfei0306 on 18-7-19.
 */
public class MessageActionDto {
    public static final int ACTION_TYPE_CUSTOMIZED = 1;
    public static final int ACTION_TYPE_OPEN_URL = 2;
    public static final int ACTION_TYPE_DEFAULT = 3;

    private int type;
    private ActionParamDto param;

    public MessageActionDto() {
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setParam(ActionParamDto param) {
        this.param = param;
    }

    public ActionParamDto getParam() {
        return param;
    }

}
