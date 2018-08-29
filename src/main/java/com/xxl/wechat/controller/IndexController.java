package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.xxl.wechat.cache.AccessTokenCache;
import com.xxl.wechat.entity.UserInfoResult;
import com.xxl.wechat.entity.UserTicketResult;
import com.xxl.wechat.http.HttpUtil;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class IndexController extends Controller {

    static UserService userService = new UserService();

    private static Logger log = LoggerFactory.getLogger(IndexController.class);





    public void getCode() {

        String code = getPara("code");

        String state = getPara("state");

        log.warn("回调成功，code:{} state:{},accessToken:{},准备获取企业号user_id",code,state);


        String userIdUrl = PropKit.get("wechat.api.userid");

        String userInfoUrl = PropKit.get("wechat.api.info");

        userIdUrl = String.format(userIdUrl, AccessTokenCache.token,code);

        log.info("===============开始获取USER-TICKET  START===============");
        String userStr = HttpUtil.httpGetRequest(userIdUrl);
        log.info("通过URL:{}，code:{} 获取用户TICKET返回的RESULT:{}",userIdUrl,code,userStr);

        UserTicketResult userTicketResult = FastJson.getJson().parse(userStr, UserTicketResult.class);
        log.info("===============开始获取USER-TICKET  END===============");


        if(userTicketResult.getUserId() == null){
            //如果没有USER_ID则不是企业认证用户
            render("no-power-warning.html");
        }else{
            //如果是企业内部用户，则通过USER_ID查询该用户是否在用户表中存在

            SyUser user  = userService.getUser(userTicketResult.getUserId());

            if(user == null ){

                log.info("===============开始获取USER-DETAIL  START===============");
                //表示改用户第一次关注该公众号，继续 用http请求该用户的详细信息
                userInfoUrl = String.format(userInfoUrl, AccessTokenCache.token);
                Map<String,Object> map  = new HashMap<>();
                map.put("user_ticket",userTicketResult.getUser_ticket());

                String jsonParam = FastJson.getJson().toJson(map);

                String userDetailStr = HttpUtil.httpPostRequest(userInfoUrl,jsonParam);

                log.info("通过URL:{}，accesstoken:{} ticket:{}获取用户信息返回的RESULT:{}",userInfoUrl,AccessTokenCache.token,userTicketResult.getUser_ticket(),userDetailStr);
                UserInfoResult userInfoResult = FastJson.getJson().parse(userDetailStr, UserInfoResult.class);
                log.info("===============开始获取USER-DETAIL  END===============");

                user = userService.save(userInfoResult);

            }
                //!!!!!!!!!!!!在此存入session
            setSessionAttr("user",user);

            redirect("/index/route");
        }
    }

    public void route(){

        SyUser user = (SyUser)getSessionAttr("user");
        log.warn("<<<<<<<<<<<<<USER_TYPE的值为》》》》"+user.getUserType());
        if(user.getUserType() == null){
            render("choose-role.html");
        }else if(user.getUserType() == 2){
            redirect("/fix/index");
        }else if(user.getUserType() == 3){
            redirect("/repair/index");
        }else{
            log.warn("USER_TYPE的值为》》》》"+user.getUserType());
        }
    }


}
