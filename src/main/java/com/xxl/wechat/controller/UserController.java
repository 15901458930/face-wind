package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.BookRoomService;
import com.xxl.wechat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController extends Controller {


    private static Logger log = LoggerFactory.getLogger(IndexController.class);

    static UserService userService = new UserService();

    /**
     * 选择
     */
    public void choose(){

        Integer userType = getParaToInt(0);
        SyUser user = (SyUser)getSessionAttr("user");

        user.setUserType(userType);
        userService.update(userType,user.getId());

        redirect("/index/route");

    }

    /**
     * 选择
     */
    public void me(){

        setAttr("belong","user");
        render("user-me.html");

    }
}
