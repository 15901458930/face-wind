package com.xxl.wechat.service;

import com.jfinal.plugin.activerecord.Db;
import com.xxl.wechat.model.generator.SyMainCategory;
import com.xxl.wechat.model.generator.WechatNoticeTask;
import com.xxl.wechat.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.calendar.BaseCalendar;

import java.util.Arrays;
import java.util.List;

public class WeChatPushService {


    private static Logger log = LoggerFactory.getLogger(WeChatPushService.class);

    static WechatNoticeTask wechatNoticeTaskDao = new WechatNoticeTask().dao();


    public void save(String userIds,String msg){

        WechatNoticeTask task = new WechatNoticeTask();

        task.setUserId(userIds);//TODO
        task.setSendMsg(msg);
        task.setStatus(0);
        task.setCreateDate(DateUtil.getCurrentDate());
        task.save();
    }


    public List<WechatNoticeTask> findNoProcessTask(){

        String sql = "select * from WECHAT_NOTICE_TASK where STATUS = 0 LIMIT 0,20";


        return wechatNoticeTaskDao.find(sql);
    }



    public void batchUpdate(List<WechatNoticeTask> list){


        int[] batchResult = Db.batchUpdate(list,30);

        log.info("批量更新微信推送结果：{}", Arrays.toString(batchResult));
    }
}
