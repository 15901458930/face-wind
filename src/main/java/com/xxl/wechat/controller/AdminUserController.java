package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.xxl.wechat.entity.ResponseResult;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.UserService;
import com.xxl.wechat.vo.LayuiResultVO;

public class AdminUserController extends Controller {


    static UserService userService = new UserService();

    public void index(){
        render("admin-user-list.html");
    }

    public void list(){

        String userType = getPara(0);
        String realName = getPara("realName");

        String page = getPara("page");

        String limitStr = getPara("limit");
        int curPage = (page == null) ? 1 : Integer.parseInt(page);
        int limit = (limitStr == null) ? 10 : Integer.parseInt(limitStr);
        LayuiResultVO<SyUser> syUser = userService.findAllUser(curPage,limit,realName,userType);
        renderJson(syUser);
    }

    public void loginOut(){
        setSessionAttr("user",null);
        redirect("/admin/login");
    }



    public void del(){
        int id = getParaToInt(0);
        userService.delete(id);
        ResponseResult<String> result = ResponseResult.instance().setSuccessData(true,"");
        renderJson(result);
    }

    public void change(){
        int id = getParaToInt(0);
        int type = getParaToInt(1);
        int authority = getParaToInt(2);

        userService.updateStatus(id,type,authority);
        ResponseResult<String> result = ResponseResult.instance().setSuccessData(true,"");
        renderJson(result);
    }
}
