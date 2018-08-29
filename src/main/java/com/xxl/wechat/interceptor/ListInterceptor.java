package com.xxl.wechat.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.xxl.wechat.controller.AttachmentController;
import com.xxl.wechat.model.generator.SyUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每个请求都检查sesssion，如果session为null,则直接拿code+accesstoken重新获取下USER_ID放到session中
 * @author  wangpeng
 * @since  2018/06/08
 */
public class ListInterceptor implements Interceptor {

    private static Logger log = LoggerFactory.getLogger(ListInterceptor.class);


    @Override
    public void intercept(Invocation invocation) {

        Controller c = invocation.getController();

        String code =  c.getPara("code");

        String uri = c.getRequest().getRequestURI() == null ? "" : c.getRequest().getRequestURI();
        String queryString = c.getRequest().getQueryString() == null ? "" : "?"+c.getRequest().getQueryString();
        String param = uri+queryString;


        SyUser user = c.getSessionAttr("user");

        if(user == null  && (!uri.contains("/login") && !uri.contains("/getCode"))){
            c.redirect("/login");
        }else {
            invocation.invoke();
        }
    }
}