package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.xxl.wechat.entity.ResponseResult;
import com.xxl.wechat.form.BookRoomForm;
import com.xxl.wechat.form.FixForm;
import com.xxl.wechat.model.generator.BookRoomTask;
import com.xxl.wechat.model.generator.SyRoom;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.BookRoomService;
import com.xxl.wechat.service.FixAssetsService;
import com.xxl.wechat.service.RoomService;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.vo.BookRoomVO;
import com.xxl.wechat.vo.FixVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRoomController extends Controller {

    private static Logger log = LoggerFactory.getLogger(FixAssetsController.class);

    static BookRoomService bookRoomService = new BookRoomService();


    static RoomService roomService = new RoomService();


    public void list(){


        String roomId = getPara(0);
        String date = getPara(1);


       SyUser user = (SyUser)getSessionAttr("user");

        List<BookRoomVO> list = bookRoomService.findRoomByDateAndRoom(roomId,date,user.getId());

        ResponseResult<BookRoomVO> result = ResponseResult.instance().setInstance(true,list);

        renderJson(result);
    }

    public void delete(){
        int id = this.getParaToInt(0);
        //TODO 取openid再次判断

        bookRoomService.delete(id);
        ResponseResult<String> result = ResponseResult.instance().setInstance(true,"");
        renderJson(result);
    }


    public void update(){
        int id = this.getParaToInt(0);
        //TODO 取openid再次判断

        BookRoomTask bookRoomTask = bookRoomService.get(id);
        setAttr("book",bookRoomTask);

        List<SyRoom> syRoomList = roomService.findSyRoom();
        setAttr("roomList",syRoomList);
        render("main-book-add.html");

    }
    public void index(){

        Integer roomId = this.getPara("roomId") == null ? -1 : Integer.parseInt(this.getPara("roomId"));
        setAttr("roomId",roomId);

        List<SyRoom> syRoomList = roomService.findSyRoom();
        setAttr("roomList",syRoomList);

        setAttr("curDate",DateUtil.getYMDStr());
        setAttr("belong","book");
        render("main-book-index.html");
    }

    public void add(){
        List<SyRoom> syRoomList = roomService.findSyRoom();
        setAttr("roomList",syRoomList);

        BookRoomTask bookRoomTask = new BookRoomTask();
        setAttr("book",bookRoomTask);
        render("main-book-add.html");
    }


    public void save(){

        String json = getPara("book");
       SyUser user = (SyUser)getSessionAttr("user");
        BookRoomForm form = FastJson.getJson().parse(json, BookRoomForm.class);
        form.setCuUserId(user.getId());
        List<BookRoomVO> list = bookRoomService.save(form);

        ResponseResult<BookRoomVO> result = ResponseResult.instance().setInstance(true,list);

        renderJson(result);
    }
}
