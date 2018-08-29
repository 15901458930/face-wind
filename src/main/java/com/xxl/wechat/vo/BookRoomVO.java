package com.xxl.wechat.vo;

public class BookRoomVO {

    private Integer id;

    private String roomName;

    private String userName;

    private String bookStartDate;

    private String bookEndDate;

    private String bookDate;

    private boolean hasPower;


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

    public String getBookStartDate() {
        return bookStartDate;
    }

    public void setBookStartDate(String bookStartDate) {
        this.bookStartDate = bookStartDate;
    }

    public String getBookEndDate() {
        return bookEndDate;
    }

    public void setBookEndDate(String bookEndDate) {
        this.bookEndDate = bookEndDate;
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
}
