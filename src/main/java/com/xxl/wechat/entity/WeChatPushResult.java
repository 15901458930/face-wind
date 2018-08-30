package com.xxl.wechat.entity;

public class WeChatPushResult {

    private int errcode;

    private String errmsg;

    private String invaliduser;

    private String invalidparty;

    private int invalidtag;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getInvaliduser() {
        return invaliduser;
    }

    public void setInvaliduser(String invaliduser) {
        this.invaliduser = invaliduser;
    }

    public String getInvalidparty() {
        return invalidparty;
    }

    public void setInvalidparty(String invalidparty) {
        this.invalidparty = invalidparty;
    }

    public int getInvalidtag() {
        return invalidtag;
    }

    public void setInvalidtag(int invalidtag) {
        this.invalidtag = invalidtag;
    }
}
