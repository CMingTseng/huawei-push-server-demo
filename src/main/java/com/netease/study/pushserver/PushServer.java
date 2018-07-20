package com.netease.study.pushserver;

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
        if (HWPushMsgSendHelper.getInstance().isAccessTokenValid()) {
            List<String> deviceTokenList = getDeviceTokenList();

//            sendOpenAppNotifyPushMessageWithExt(deviceTokenList);
//
//            sendOpenAppNotifyPushMessageWithNULLExt(deviceTokenList);

            sendOpenUrlNotifyPushMessage(deviceTokenList);
            sendTransparentPushMessage(deviceTokenList);
        }
    }

    private static ExtDto getMockExt() {
        Map<String, Object> customize = new HashMap<>();
        customize.put("icon", "http://pic.qiantucdn.com/58pic/12/38/18/13758PIC4GV.jpg");
        List<Map> extlist = new ArrayList<>();
        extlist.add(customize);
        ExtDto extDto = HWPushMsgSendHelper.getInstance().constructExtDto("Trump", extlist);
        return extDto;
    }

    private static void sendOpenAppNotifyPushMessage(List<String> deviceTokenList, String packageName,
                                                     String title, String content, ExtDto extDto) throws IOException {
        PushMessageDto messageDto = HWPushMsgSendHelper.getInstance().constructOpenAppNotificationMsg(packageName,
                title, content);

        HWPushMsgSendHelper.getInstance().sendPushMessage(deviceTokenList, messageDto, extDto);
    }

    //发送Push消息
    private static void sendOpenAppNotifyPushMessageWithExt(List<String> deviceTokenList) throws IOException {
        String appPackageName = "com.netease.edu.study.message";

        String msgTitle = "Push message title (contain ext)";
        String msgContent = "Push message content (contain ext)";

        ExtDto extDto = getMockExt();

        sendOpenAppNotifyPushMessage(deviceTokenList, appPackageName, msgTitle, msgContent, extDto);
    }

    //发送Push消息
    private static void sendOpenAppNotifyPushMessageWithNULLExt(List<String> deviceTokenList) throws IOException {
        String appPackageName = "com.netease.edu.study.message";

        String msgTitle = "Push message title (non ext)";
        String msgContent = "Push message content (non ext)";

        PushMessageDto messageDto = HWPushMsgSendHelper.getInstance().constructOpenAppNotificationMsg(appPackageName,
                msgTitle, msgContent);

        ExtDto extDto = null;

        HWPushMsgSendHelper.getInstance().sendPushMessage(deviceTokenList, messageDto, extDto);
    }

    //发送Push消息
    private static void sendOpenUrlNotifyPushMessage(List<String> deviceTokenList) throws IOException {
        String url = "https://rong.36kr.com/";

        String msgTitle = "Push message title (url)";
        String msgContent = "Push message content (url)";

        PushMessageDto messageDto = HWPushMsgSendHelper.getInstance().constructOpenUrlNotificationMsg(url,
                msgTitle, msgContent);

        ExtDto extDto = null;

        HWPushMsgSendHelper.getInstance().sendPushMessage(deviceTokenList, messageDto, extDto);
    }

    //发送Push消息
    private static void sendTransparentPushMessage(List<String> deviceTokenList) throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("transparent_message", "transparent message content");

        PushMessageDto messageDto = HWPushMsgSendHelper.getInstance().constructMessage(PushMessageDto.TYPE_TRANSPARENT_MESSAGE,
                body, null);

        ExtDto extDto = null;

        HWPushMsgSendHelper.getInstance().sendPushMessage(deviceTokenList, messageDto, extDto);
    }
}
