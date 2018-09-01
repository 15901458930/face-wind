package com.xxl.wechat.vo;

public class FixVO {

    private Integer id;

    private String url;

    private String assetType;
    private String assetTypeName;

    private String assetSubType;
    private String assetSubTypeName;

    private String assetName;

    private String assetLocation;

    private String belongCampus;
    private String belongCampusName;


    private String fixReason;

    private String applyUserName;

    private String applyDate;

    private Integer status;

    private String statusName;

    private Integer version;

    private String attachmentId;

    private String fixUserName;

    private String startFixDate;

    private String fixedDate;

    private String[] attachmentIds;

    public String getBelongCampus() {
        return belongCampus;
    }

    public void setBelongCampus(String belongCampus) {
        this.belongCampus = belongCampus;
    }

    public String getFixUserName() {
        return fixUserName;
    }

    public void setFixUserName(String fixUserName) {
        this.fixUserName = fixUserName;
    }

    public String getStartFixDate() {
        return startFixDate;
    }

    public void setStartFixDate(String startFixDate) {
        this.startFixDate = startFixDate;
    }

    public String getFixedDate() {
        return fixedDate;
    }

    public void setFixedDate(String fixedDate) {
        this.fixedDate = fixedDate;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }


    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetTypeName() {
        return assetTypeName;
    }

    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }

    public String getAssetSubType() {
        return assetSubType;
    }

    public void setAssetSubType(String assetSubType) {
        this.assetSubType = assetSubType;
    }

    public String getAssetSubTypeName() {
        return assetSubTypeName;
    }

    public void setAssetSubTypeName(String assetSubTypeName) {
        this.assetSubTypeName = assetSubTypeName;
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

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String[] getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(String[] attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public String getBelongCampusName() {
        return belongCampusName;
    }

    public void setBelongCampusName(String belongCampusName) {
        this.belongCampusName = belongCampusName;
    }
}
