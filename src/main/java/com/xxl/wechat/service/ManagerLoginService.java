package com.xxl.wechat.service;

import com.xxl.wechat.model.generator.SyUser;

public class ManagerLoginService {

    static SyUser SyUserDao = new SyUser().dao();

    public SyUser findSyUser(String username, String password) {
        String sql = "select * from SY_USER where（USER_NAME='？' and PASSWORD='？'）";
        SyUser user = SyUserDao.findFirst(sql, username, password);
        return user;
    }
}
