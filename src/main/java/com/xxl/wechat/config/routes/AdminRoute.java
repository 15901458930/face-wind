package com.xxl.wechat.config.routes;

import com.jfinal.config.Routes;
import com.xxl.wechat.controller.*;

public class AdminRoute extends Routes {

    @Override
    public void config() {
        setBaseViewPath("WEB-INF/");
        add("/admin/main", AdminIndexController.class);
        add("/admin/login", AdminLoginController.class);
        add("/admin/user", AdminUserController.class);
        add("/admin/book", AdminBookController.class);
        add("/admin/fix", AdminFixController.class);
        add("/admin/category", AdminCategoryController.class);


    }
}
