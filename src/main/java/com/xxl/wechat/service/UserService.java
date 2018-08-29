package com.xxl.wechat.service;

import com.jfinal.json.FastJson;
import com.xxl.wechat.entity.UserInfoResult;
import com.xxl.wechat.http.HttpUtil;
import com.xxl.wechat.model.generator.SyRoom;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.util.DateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {



    static SyUser syUserDao = new SyUser().dao();


    public SyUser getUser(String id){

        return syUserDao.findById(id);
    }

    public SyUser save(UserInfoResult result){


        SyUser user = new SyUser();


        user.setId(result.getUserid());
        user.setPhone(result.getMobile());
        user.setAvatar(result.getAvatar());
        user.setPosition(result.getPosition());
        user.setEmail(result.getEmail());
        user.setRealName(result.getName());
        user.setStatus(1);
        user.setCreateUserId(result.getUserid());
        user.setCreateDate(DateUtil.getCurrentDate());
        user.save();
        return user;
    }

    public void update(Integer userType,String userId){


        SyUser user = new SyUser();
        user.setId(userId);
        user.setUserType(userType);

        user.update();
    }

}
