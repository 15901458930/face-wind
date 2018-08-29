package com.xxl.wechat.constant;


/**
 * 存放微信token专用
 */
public class WechatAccessTokenConstant {


    private static String accessToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        WechatAccessTokenConstant.accessToken = accessToken;
    }


    public static void main(String[] args) {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_privateinfo&agentid=%s&state=fromWebView#wechat_redirect";
        url = String.format(url, "A","B","C");
        System.out.println(url);

    }

}
