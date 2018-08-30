package com.xxl.wechat.config.routes;

import com.jfinal.config.Routes;
import com.xxl.wechat.controller.*;

public class AdminRoute extends Routes {

    @Override
    public void config() {
        setBaseViewPath("WEB-INF/");
        add("/manager/main", ManagerIndexController.class);
        add("/manager/login", ManagerLoginController.class);
        add("/manager/user", ManagerUserController.class);

    }
}
