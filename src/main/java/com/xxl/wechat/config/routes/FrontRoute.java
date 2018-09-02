package com.xxl.wechat.config.routes;

import com.jfinal.config.Routes;
import com.xxl.wechat.controller.*;
import com.xxl.wechat.interceptor.FrontLoginInterceptor;

public class FrontRoute extends Routes {
    @Override
    public void config() {
        setBaseViewPath("WEB-INF/front");
        addInterceptor(new FrontLoginInterceptor());
        add("/fix", FixAssetsController.class);
        add("/repair", RepairAssetsController.class);
        add("/book", BookRoomController.class);
        add("/index", IndexController.class);
        add("/login", LoginController.class);
        add("/user", UserController.class);
        add("/attachment", AttachmentController.class);
        add("/category", CategoryController.class);
    }
}
