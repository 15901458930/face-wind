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

public class LoginController extends Controller {

    static UserService userService = new UserService();

    private static Logger log = LoggerFactory.getLogger(LoginController.class);



    public void index(){

        String devModel = PropKit.get("dev.model") == null  ? "" : PropKit.get("dev.model");

        if(devModel.equals("true")){
            //开发模式
            SyUser user  = new SyUser();
            user.setId("10000");
            user.setAvatar("http://p.qlogo.cn/bizmail/r2LaXyGa7ibchfHtM3KH9ba3hticNWNTibJV8o0I7LmPPCXeMuKoHDddw/");
            user.setRealName("许小丽");

            setSessionAttr("user",user);
            //开发模式下，直接模拟一个用户session
            redirect("/index/route");

        }else {

            String appid = PropKit.get("wechat.appid");
            String secret = PropKit.get("wechat.secret");

            String agentId = PropKit.get("wechat.agentid");
            String url = PropKit.get("wechat.api.auth");

            String redirectUri = null;
            try {
                redirectUri = URLEncoder.encode(PropKit.get("wechat.redirect_uri"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("授权后重定向的回调链接地址，在使用urlencode对链接进行处理时出现异常！", e);
            }
            Cookie[] cookies = getRequest().getCookies();

            if (cookies != null && cookies.length > 0) {
                for (Cookie cc : cookies) {
                    log.warn("cookie-key:" + cc.getName() + ">>" + cc.getValue());
                }
            }

            //log.info("url:{},appid:{},redirectUrl:{},secret:{}",url, appid,redirectUri,secret);

            url = String.format(url, appid, redirectUri, agentId);
            log.warn("=======即将跳转的URL=============：{}", url);
            redirect(url);
        }

    }







}
