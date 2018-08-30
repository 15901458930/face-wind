package com.xxl.wechat.init;

import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.xxl.wechat.cache.AccessTokenCache;
import com.xxl.wechat.entity.AccessTokenResult;
import com.xxl.wechat.entity.WeChatPushPostJson;
import com.xxl.wechat.entity.WeChatPushResult;
import com.xxl.wechat.http.HttpUtil;
import com.xxl.wechat.model.generator.WechatNoticeTask;
import com.xxl.wechat.service.WeChatPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WeChatPushSchedule implements Runnable{


    private static Logger log = LoggerFactory.getLogger(AccessTokenSchedule.class);


    private WeChatPushService service;

    public WeChatPushSchedule(WeChatPushService service) {
        this.service = service;
    }

    @Override
    public void run() {

        try{




            String agentid = PropKit.get("wechat.agentid");

            String pushUrl = PropKit.get("wechat.api.push");

            String url = String.format(pushUrl, AccessTokenCache.token);

            log.info("===============定时任务获取推送微信  start===============");

            List<WechatNoticeTask> noProcessTask = service.findNoProcessTask();

            if(noProcessTask == null || noProcessTask.size()==0){
                log.info("暂无要推送的微信信息");
            }else{
                for (WechatNoticeTask task : noProcessTask){
                    WeChatPushPostJson json = new WeChatPushPostJson();
                    WeChatPushPostJson.Content content = new WeChatPushPostJson.Content();
                    content.setContent(task.getSendMsg());

                    json.setText(content);
                    json.setTouser(task.getUserId());
                    json.setAgentid(Integer.parseInt(agentid));
                    json.setSafe(0);
                    json.setMsgtype("text");

                    String jsonStr = FastJson.getJson().toJson(json);
                    String result = HttpUtil.httpPostRequest(url, jsonStr);

                    WeChatPushResult weChatPushResult = FastJson.getJson().parse(result,WeChatPushResult.class);

                    task.setStatus(1);
                    task.setErrorCode(weChatPushResult.getErrcode());
                    task.setErrMsg(weChatPushResult.getErrmsg());
                    task.setInvalidUser(weChatPushResult.getInvaliduser());
                }

                service.batchUpdate(noProcessTask);
            }

            log.info("===============定时任务获取推送微信  end===============");
        }catch(Exception e){

            log.error("定时任务获取推送微信出现异常",e);
        }

    }
}
