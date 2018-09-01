package com.xxl.wechat.form;

public class BookRoomForm {

    private Integer id;
    private Integer roomId;
    private String startTime;
    private String endTime;

    private String depart;
    private String responsibleUser;
    private String useReason;
    private String device;
    private int needPhoto;
    private int needCamera;
    private Integer cuUserId;
    private String specialRequire;

    public String getSpecialRequire() {
        return specialRequire;
    }

    public void setSpecialRequire(String specialRequire) {
        this.specialRequire = specialRequire;
    }

    public Integer getCuUserId() {
        return cuUserId;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(String responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public String getUseReason() {
        return useReason;
    }

    public void setUseReason(String useReason) {
        this.useReason = useReason;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getNeedPhoto() {
        return needPhoto;
    }

    public void setNeedPhoto(int needPhoto) {
        this.needPhoto = needPhoto;
    }

    public int getNeedCamera() {
        return needCamera;
    }

    public void setNeedCamera(int needCamera) {
        this.needCamera = needCamera;
    }

    public void setCuUserId(Integer cuUserId) {
        this.cuUserId = cuUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
