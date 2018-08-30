package com.xxl.wechat.service;

import com.jfinal.json.FastJson;
import com.xxl.wechat.entity.UserInfoResult;
import com.xxl.wechat.http.HttpUtil;
import com.xxl.wechat.model.generator.SyRoom;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {



    static SyUser syUserDao = new SyUser().dao();


    public SyUser getUser(int id){

        return syUserDao.findById(id);
    }

    public List<SyUser> findAllUser(){

        return syUserDao.find("select * from SY_USER");
    }


    public SyUser save(UserInfoResult result){


        SyUser user = new SyUser();

        user.setWechatUserId(result.getUserid());
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

    public void update(Integer userType,Integer userId){


        SyUser user = new SyUser();
        user.setId(userId);
        user.setUserType(userType);

        user.update();
    }


    public String findFixUser(){

        String sql = "select * from SY_USER WHERE USER_TYPE = 3";
        List<SyUser> syUsers = syUserDao.find(sql);

        StringBuilder sb = new StringBuilder();

        for(SyUser user : syUsers){
            sb.append(user.getId()).append("|");
        }

        return  StringUtils.chop(sb.toString());
    }

}
