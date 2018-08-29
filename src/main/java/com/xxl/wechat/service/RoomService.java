package com.xxl.wechat.service;

import com.xxl.wechat.model.generator.BookRoomTask;
import com.xxl.wechat.model.generator.SyRoom;

import java.util.List;

public class RoomService {


    static SyRoom syRoomDao = new SyRoom().dao();


    public List<SyRoom> findSyRoom(){

        return syRoomDao.find("select * from SY_ROOM");
    }






}
