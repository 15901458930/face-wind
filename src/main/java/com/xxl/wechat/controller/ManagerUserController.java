package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.UserService;

import java.util.List;

public class ManagerUserController extends Controller {


    static UserService userService = new UserService();


    public void index(){


        List<SyUser> allUser = userService.findAllUser();

        setAttr("userList",allUser);
        render("manager-user-list.html");



    }
}
