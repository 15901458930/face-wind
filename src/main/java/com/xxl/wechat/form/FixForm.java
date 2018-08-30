package com.xxl.wechat.form;

public class FixForm {


    private Integer id;
    private String assetType;
    private String assetTypeName;


    private String assetSubType;
    private String assetSubTypeName;

    private String assetName;
    private String assetLocation;


    private String fixReason;
    private String[] attachmentIds;

    private Integer curUserId;

    private Integer version;

    public Integer getCurUserId() {
        return curUserId;
    }

    public void setCurUserId(Integer curUserId) {
        this.curUserId = curUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetSubType() {
        return assetSubType;
    }

    public void setAssetSubType(String assetSubType) {
        this.assetSubType = assetSubType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetLocation() {
        return assetLocation;
    }

    public void setAssetLocation(String assetLocation) {
        this.assetLocation = assetLocation;
    }

    public String getFixReason() {
        return fixReason;
    }

    public void setFixReason(String fixReason) {
        this.fixReason = fixReason;
    }

    public String[] getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(String[] attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public String getAssetTypeName() {
        return assetTypeName;
    }

    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }

    public String getAssetSubTypeName() {
        return assetSubTypeName;
    }

    public void setAssetSubTypeName(String assetSubTypeName) {
        this.assetSubTypeName = assetSubTypeName;
    }
}
