package com.xxl.wechat.init;


import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.xxl.wechat.cache.AccessTokenCache;
import com.xxl.wechat.controller.FixAssetsController;
import com.xxl.wechat.entity.AccessTokenResult;
import com.xxl.wechat.form.FixForm;
import com.xxl.wechat.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时任务获取access_token
 */
public class AccessTokenSchedule implements Runnable{


    private static Logger log = LoggerFactory.getLogger(AccessTokenSchedule.class);


    @Override
    public void run() {

        try{

            String appid = PropKit.get("wechat.appid");
            String secret = PropKit.get("wechat.secret");
            String tokenUrl = PropKit.get("wechat.api.token");

            String url = String.format(tokenUrl, appid,secret);

            log.info("===============开始获取ACCESSTOKEN===============");
            String accessToken = HttpUtil.httpGetRequest(url);
            log.info("通过URL:{}获取access-token返回的RESULT:{}",url,accessToken);

            AccessTokenResult form = FastJson.getJson().parse(accessToken, AccessTokenResult.class);
            AccessTokenCache.token = form.getAccess_token();

        }catch(Exception e){

            log.error("获取ACCESS_TOKEN出现异常",e);
        }

    }
}
