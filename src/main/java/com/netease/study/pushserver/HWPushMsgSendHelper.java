package com.netease.study.pushserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.netease.study.pushserver.Dto.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HWPushMsgSendHelper {

    private static final String REFRESH_TOKEN_BODY_FORMAT
            = "grant_type=client_credentials&client_secret={0}&client_id={1}";

    //获取认证Token的URL
    private static String HUAWEI_TOKEN_URL = "https://login.cloud.huawei.com/oauth2/v2/token";
    //应用级消息下发API
    private static String HUAWEI_PUSH_API_URL = "https://api.push.hicloud.com/pushsend.do";


    private static HWPushMsgSendHelper sInstance;

    public static synchronized HWPushMsgSendHelper getInstance() {
        if (sInstance == null) {
            sInstance = new HWPushMsgSendHelper();
        }
        return sInstance;
    }

    private boolean mInitialized;
    private String mAppId;
    private String mAppSecret;

    private AccessTokenDto mAccessToken;

    private HWPushMsgSendHelper() {
    }

    private AccessTokenDto getAccessToken() throws IOException {
        AccessTokenDto accessTokenDto = null;
        String msgBody = MessageFormat.format(REFRESH_TOKEN_BODY_FORMAT,
                URLEncoder.encode(mAppSecret, "UTF-8"), mAppId);
        String response = httpPost(HUAWEI_TOKEN_URL, msgBody);

        if (response != null) {
            accessTokenDto = JSON.parseObject(response, AccessTokenDto.class);
            if (accessTokenDto != null) {
                long now = System.currentTimeMillis();
                long expired_in = accessTokenDto.getExpires_in();
                long expiredTime = now + expired_in * 1000 - 5 * 60 * 1000;
                accessTokenDto.setExpires_in(expiredTime);
            }
        }
        return accessTokenDto;
    }

    public synchronized boolean initialize(String appId, String appSecret) throws IOException {
        if (mInitialized) {
            return true;
        }

        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret)) {
            throw new IllegalArgumentException("Appid is " + appId + ", app secret is " + appSecret);
        }
        mAppId = appId;
        mAppSecret = appSecret;

        mAccessToken = getAccessToken();
        if (mAccessToken == null) {
            LogUtil.println("Get access token failed.");
            return false;
        }
        mInitialized = true;
        return true;
    }

    //获取下发通知消息的认证Token
    public boolean refreshToken() throws IOException {
        AccessTokenDto accessToken = getAccessToken();
        if (accessToken == null) {
            LogUtil.println("Get access token failed.");
            return false;
        }
        synchronized (this) {
            mAccessToken = accessToken;
        }
        return true;
    }

    public boolean isAccessTokenValid() {
        AccessTokenDto accessTokenDto;
        synchronized (this) {
            accessTokenDto = mAccessToken;
            if (accessTokenDto == null) {
                return false;
            }
        }
        long now = System.currentTimeMillis();
        long expired_in = accessTokenDto.getExpires_in();
        if (expired_in < now) {
            return false;
        }
        return true;
    }

    private JSONArray getDeviceTokenArray(List<String> deviceTokens) {
        JSONArray deviceTokenArray = null;
        if (deviceTokens != null && deviceTokens.size() > 0) {
            deviceTokenArray = new JSONArray();
            for (String deviceToken : deviceTokens) {
                deviceTokenArray.add(deviceToken);
            }
        }
        return deviceTokenArray;
    }

    private MessageActionDto constructAction(int type, String param) {
        ActionParamDto actionParam = new ActionParamDto();
        switch (type) {
            case MessageActionDto.ACTION_TYPE_CUSTOMIZED:
                actionParam.setIntent(param);
                break;
            case MessageActionDto.ACTION_TYPE_OPEN_URL:
                actionParam.setUrl(param);
                break;
            case MessageActionDto.ACTION_TYPE_DEFAULT:
                actionParam.setAppPkgName(param);
                break;
        }

        MessageActionDto messageActionDto = new MessageActionDto();
        messageActionDto.setType(type);
        messageActionDto.setParam(actionParam);

        return messageActionDto;
    }

    public MessageActionDto constructOpenAppAction(String packageName) {
        MessageActionDto messageActionDto = constructAction(MessageActionDto.ACTION_TYPE_DEFAULT, packageName);

        return messageActionDto;
    }

    public MessageActionDto constructOpenUrlAction(String url) {
        MessageActionDto messageActionDto = constructAction(MessageActionDto.ACTION_TYPE_OPEN_URL, url);

        return messageActionDto;
    }

    public PushMessageDto constructMessage(int type, Object body, MessageActionDto messageActionDto) {
        PushMessageDto messageDto = new PushMessageDto();
        messageDto.setType(type);
        messageDto.setAction(messageActionDto);
        messageDto.setBody(body);

        return messageDto;
    }

    public PushMessageDto constructNotificationMsg(String msgTitle, String msgContent,
                                                           MessageActionDto messageActionDto) {
        NotifyMessageBodyDto body = new NotifyMessageBodyDto();
        body.setTitle(msgTitle);
        body.setContent(msgContent);

        PushMessageDto messageDto = constructMessage(PushMessageDto.TYPE_NOTIFICATION_MESSAGE,
                body, messageActionDto);

        return messageDto;
    }

    public PushMessageDto constructOpenAppNotificationMsg(String packageName, String msgTitle, String msgContent) {
        MessageActionDto messageActionDto = constructOpenAppAction(packageName);
        PushMessageDto messageDto = constructNotificationMsg(msgTitle, msgContent, messageActionDto);
        return messageDto;
    }

    public PushMessageDto constructOpenUrlNotificationMsg(String url, String msgTitle, String msgContent) {
        MessageActionDto messageActionDto = constructOpenUrlAction(url);
        PushMessageDto messageDto = constructNotificationMsg(msgTitle, msgContent, messageActionDto);
        return messageDto;
    }

    public ExtDto constructExtDto(String biTag, List<Map> customize) {
        ExtDto extDto = new ExtDto();
        extDto.setBiTag(biTag);
        extDto.setCustomize(customize);

        return extDto;
    }

    private String constuctPayload(ExtDto extDto, PushMessageDto messageDto) {
        HWPushMsgDto msgDto = new HWPushMsgDto();
        msgDto.setExt(extDto);
        msgDto.setMsg(messageDto);

        HpsDto hps = new HpsDto();
        hps.setHps(msgDto);

        String payload = JSON.toJSONString(hps);

        return payload;
    }

    private String httpPost(String httpUrl, String data) throws IOException {
        EduResponse response = HttpEngine.getInstance().postSync(httpUrl, null, data, HttpEngine.FORM);
        String result = response.body().string();
        return result;
    }

    public void sendPushMessage(List<String> deviceTokenList, PushMessageDto messageDto, ExtDto extDto) throws IOException {
        String payload = constuctPayload(extDto, messageDto);

        JSONArray deviceTokens = getDeviceTokenArray(deviceTokenList);
        String accessToken = mAccessToken.getAccess_token();
        String appId = mAppId;
        String postBody = MessageFormat.format(
                "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}",
                URLEncoder.encode(accessToken, "UTF-8"),
                URLEncoder.encode("openpush.message.api.send", "UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
                URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
                URLEncoder.encode(payload, "UTF-8"));

        String postUrl = HUAWEI_PUSH_API_URL + "?nsp_ctx=" + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + appId + "\"}", "UTF-8");
        httpPost(postUrl, postBody);
    }
}
