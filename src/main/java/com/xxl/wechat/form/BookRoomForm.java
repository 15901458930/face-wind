package com.xxl.wechat.form;

public class BookRoomForm {

    private Integer id;
    private Integer roomId;
    private String startTime;
    private String endTime;

    private String cuUserId;

    public String getCuUserId() {
        return cuUserId;
    }

    public void setCuUserId(String cuUserId) {
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
