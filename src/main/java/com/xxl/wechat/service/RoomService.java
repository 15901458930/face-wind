package com.xxl.wechat.service;

import com.xxl.wechat.model.generator.BookRoomTask;
import com.xxl.wechat.model.generator.SyRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomService {


    static SyRoom syRoomDao = new SyRoom().dao();


    public List<SyRoom> findSyRoom(){

        return syRoomDao.find("select * from SY_ROOM");
    }




    public Map<Integer,String> findRoomMap(){

        Map<Integer,String> map = new HashMap<>();
        List<SyRoom> syRooms = syRoomDao.find("select * from SY_ROOM");
        for(SyRoom room : syRooms){
            map.put(room.getId(),room.getName());
        }
        return map;
    }



}
