package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.xxl.wechat.entity.ResponseResult;
import com.xxl.wechat.model.generator.BookRoomTask;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.model.generator.SyRoom;
import com.xxl.wechat.service.BookRoomService;
import com.xxl.wechat.service.FixAssetsService;
import com.xxl.wechat.service.RoomService;
import com.xxl.wechat.service.office.BookRoomExcelService;
import com.xxl.wechat.service.office.FixExcelService;
import com.xxl.wechat.vo.BookRoomVO;
import com.xxl.wechat.vo.FixVO;
import com.xxl.wechat.vo.LayuiResultVO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminBookController extends Controller {

    static RoomService roomService = new RoomService();


    static BookRoomService bookRoomService = new BookRoomService();

    public void index(){

        List<SyRoom> syRoomList = roomService.findSyRoom();
        setAttr("roomList",syRoomList);
        render("admin-book-list.html");
    }

    public void list(){

        String roomId = getPara(0);
        String startDate = getPara("startDate");
        String endDate = getPara("endDate");

        String page = getPara("page");
        String limitStr = getPara("limit");
        int curPage = (page == null) ? 1 : Integer.parseInt(page);
        int limit = (limitStr == null) ? 10 : Integer.parseInt(limitStr);
        LayuiResultVO<BookRoomTask> bookTasks = bookRoomService.findAllBookRooms(curPage,limit,roomId,startDate,endDate);
        renderJson(bookTasks);
    }

    public void del(){
        int id = getParaToInt(0);
        bookRoomService.delete(id);
        ResponseResult<String> result = ResponseResult.instance().setSuccessData(true,"");
        renderJson(result);
    }

    public void get(){
        int id = getParaToInt(0);
        BookRoomVO bookVO = bookRoomService.getVO(id);
        ResponseResult<BookRoomVO> result = ResponseResult.instance().setSuccessData(true,bookVO);
        renderJson(result);
    }

    public void export(){
        String status = getPara(0);
        String startDate = getPara("startDate");
        String endDate = getPara("endDate");

        List<BookRoomTask> tasks = new ArrayList<>();

        Page<BookRoomTask> firstTasks = bookRoomService.list(1,100, status,startDate,endDate);
        tasks.addAll(firstTasks.getList());
        for (int i = firstTasks.getPageNumber() + 1; i <= firstTasks.getTotalPage(); i++) {
            Page<BookRoomTask> pageList = bookRoomService.list(i, 100, status,startDate,endDate);
            tasks.addAll(pageList.getList());
        }

        BookRoomExcelService fixExcelService = new BookRoomExcelService(tasks);
        String path = PropKit.get("excel.saveDir") + File.separator + "book";
        fixExcelService.mkdir(path);
        String filePath = path + File.separator + "预订场地列表.xlsx";
        fixExcelService.generateExcel("", filePath);

        renderFile(new File(filePath));
    }


}
