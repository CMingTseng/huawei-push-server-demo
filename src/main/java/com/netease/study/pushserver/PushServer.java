package com.netease.study.pushserver;

import com.alibaba.fastjson.JSONArray;
import com.netease.study.pushserver.Dto.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushServer {
    private static String appSecret = "080bf0556572b57015da9b5778c6ef5b";
    private static String appId = "100341497";//用户在华为开发者联盟申请的appId和appSecret（会员中心->应用管理，点击应用名称的链接）


    private static List<String> getDeviceTokenList() {
        List<String> deviceTokenList = new ArrayList<>();
        deviceTokenList.add("AGGxODrs9vPCnRqRkClSRZG6-odoRngCJTWeiW6GmCgYj0_ZIgfUZZCUOTjCLB1ad5Xq3zKssLQIk1NL0jS4yFrpXHJCgfYpYwPmRZBETWyzgrVjVZoDM-Bp_bW2xDh6RQ");
        return deviceTokenList;
    }

    public static void main(String[] args) throws IOException {
        HWPushMsgSendHelper.getInstance().initialize(appId, appSecret);
        if (HWPushMsgSendHelper.getInstance().checkAccessTokenValidity()) {
            List<String> deviceTokenList = getDeviceTokenList();

            sendNotifyPushMessage(deviceTokenList);
        }
    }

    //发送Push消息
    private static void sendNotifyPushMessage(List<String> deviceTokenList) throws IOException {
        JSONArray deviceTokens = HWPushMsgSendHelper.getInstance().getDeviceTokenArray(deviceTokenList);

        MessageActionDto messageActionDto = HWPushMsgSendHelper.getInstance().constructOpenAppAction("com.netease.edu.study.message");

        String msgTitle = "Push message title";
        String msgContent = "Push message content";

        PushMessageDto messageDto = HWPushMsgSendHelper.getInstance().constructNotificationMsg(msgTitle, msgContent, messageActionDto);

        Map<String, Object> customize = new HashMap<>();
        customize.put("icon", "http://pic.qiantucdn.com/58pic/12/38/18/13758PIC4GV.jpg");
        List<Map> extlist = new ArrayList<>();
        extlist.add(customize);
        ExtDto extDto = HWPushMsgSendHelper.getInstance().constructExtDto("Trump", extlist);

        String payload = HWPushMsgSendHelper.getInstance().constuctPayload(extDto, messageDto);
        HWPushMsgSendHelper.getInstance().sendPushMessage(deviceTokens, payload);
    }
}
