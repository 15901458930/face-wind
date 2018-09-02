package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.UserService;

public class AdminLoginController extends Controller {



    static UserService userService = new UserService();

    public void index(){
        setAttr("userName","");
        render("admin-login.html");
    }


    public void signin(){
        String userName = getPara("userName");

        String password = getPara("password");

        SyUser user = userService.findUserByUserNameAndPassword(userName, password);

        if(user != null){
            setSessionAttr("user",user);
            redirect("/admin/main");
        }else{
            setAttr("userName",userName);
            setAttr("msg","用户名或密码错误");
            render("admin-login.html");
        }
    }
}
