package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.xxl.wechat.entity.ResponseResult;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.FixAssetsService;
import com.xxl.wechat.service.UserService;
import com.xxl.wechat.service.office.FixExcelService;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.vo.FixVO;
import com.xxl.wechat.vo.LayuiResultVO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminFixController extends Controller {


    static FixAssetsService fixAssetsService = new FixAssetsService();

    public void index(){
        render("admin-fix-list.html");
    }

    public void list(){

        String status = getPara(0);
        String startDate = getPara("startDate");
        String endDate = getPara("endDate");

        String page = getPara("page");
        String limitStr = getPara("limit");
        int curPage = (page == null) ? 1 : Integer.parseInt(page);
        int limit = (limitStr == null) ? 10 : Integer.parseInt(limitStr);
        LayuiResultVO<FixAssetTask> fixTasks = fixAssetsService.findAllFixAsset(curPage,limit,status,startDate,endDate);
        renderJson(fixTasks);
    }

    public void del(){
        int id = getParaToInt(0);
        fixAssetsService.delete(id);
        ResponseResult<String> result = ResponseResult.instance().setSuccessData(true,"");
        renderJson(result);
    }

    public void get(){
        int id = getParaToInt(0);
        FixVO fixVO = fixAssetsService.get(id);
        ResponseResult<FixVO> result = ResponseResult.instance().setSuccessData(true,fixVO);
        renderJson(result);
    }

    public void export(){
        String status = getPara(0);
        String startDate = getPara("startDate");
        String endDate = getPara("endDate");

        List<FixAssetTask> tasks = new ArrayList<>();

        Page<FixAssetTask> firstTasks = fixAssetsService.list(1,100, status,startDate,endDate);
        tasks.addAll(firstTasks.getList());
        for (int i = firstTasks.getPageNumber() + 1; i <= firstTasks.getTotalPage(); i++) {
            Page<FixAssetTask> pageList = fixAssetsService.list(i, 100, status,startDate,endDate);
            tasks.addAll(pageList.getList());
        }

        FixExcelService fixExcelService = new FixExcelService(tasks);
        String path = PropKit.get("excel.saveDir") + File.separator + "fix";
        fixExcelService.mkdir(path);
        String filePath = path + File.separator + "报修单列表.xlsx";
        fixExcelService.generateExcel("", filePath);

        renderFile(new File(filePath));
    }

}
