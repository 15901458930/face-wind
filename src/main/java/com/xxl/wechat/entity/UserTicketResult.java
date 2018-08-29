package com.xxl.wechat.entity;

public class UserTicketResult {

    //企业号授权返回的
    private String UserId;

    private String DeviceId;

    private String user_ticket;

    private int expires_in;

    //非企业号授权返回的
    private String OpenId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getUser_ticket() {
        return user_ticket;
    }

    public void setUser_ticket(String user_ticket) {
        this.user_ticket = user_ticket;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
