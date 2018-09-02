package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.xxl.wechat.entity.ResponseResult;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.FixAssetsService;
import com.xxl.wechat.service.UserService;
import com.xxl.wechat.vo.LayuiResultVO;

public class AdminFixController extends Controller {


    static FixAssetsService fixAssetsService = new FixAssetsService();

    public void index(){
        render("admin-fix-list.html");
    }

    public void list(){

        String userType = getPara(0);
        String realName = getPara("roomId");
        String useReason = getPara("useReason");

        String page = getPara("pageSize");
        String limitStr = getPara("limit");
        int curPage = (page == null) ? 1 : Integer.parseInt(page);
        int limit = (limitStr == null) ? 10 : Integer.parseInt(limitStr);
        LayuiResultVO<FixAssetTask> fixTasks = fixAssetsService.findAllFixAsset(curPage,limit,realName,userType);
        renderJson(fixTasks);
    }

}
