package com.xxl.wechat.vo;

import java.util.Date;

public class BookRoomVO {

    private Integer id;

    private Integer roomId;

    private String roomName;

    private String userName;

    private String bookStartTime;
    private String bookEndTime;

    private Date bookStartDate;
    private Date bookEndDate;

    private String bookDate;


    private String depart;
    private String responsibleUser;
    private String useReason;
    private String device;
    private int needPhoto;
    private int needCamera;
    private String specialRequire;

    private boolean hasPower;//是否有接单权限

    private String createDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isHasPower() {
        return hasPower;
    }

    public void setHasPower(boolean hasPower) {
        this.hasPower = hasPower;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookStartTime() {
        return bookStartTime;
    }

    public void setBookStartTime(String bookStartTime) {
        this.bookStartTime = bookStartTime;
    }

    public String getBookEndTime() {
        return bookEndTime;
    }

    public void setBookEndTime(String bookEndTime) {
        this.bookEndTime = bookEndTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
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

    public String getSpecialRequire() {
        return specialRequire;
    }

    public Date getBookStartDate() {
        return bookStartDate;
    }

    public void setBookStartDate(Date bookStartDate) {
        this.bookStartDate = bookStartDate;
    }

    public Date getBookEndDate() {
        return bookEndDate;
    }

    public void setBookEndDate(Date bookEndDate) {
        this.bookEndDate = bookEndDate;
    }

    public void setSpecialRequire(String specialRequire) {
        this.specialRequire = specialRequire;
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

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
