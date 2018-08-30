package com.xxl.wechat.service;

import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.xxl.wechat.constant.DictConstant;
import com.xxl.wechat.constant.GlobalConstant;
import com.xxl.wechat.constant.StatusConstant;
import com.xxl.wechat.form.FixForm;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.vo.FixVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FixAssetsService {

    private static Logger log = LoggerFactory.getLogger(FixAssetsService.class);

    static FixAssetTask fixAssetTaskDao = new FixAssetTask().dao();

    static AttachmentService attachmentService = new AttachmentService();

    static WeChatPushService weChatPushService = new WeChatPushService();

    static UserService userService = new UserService();


    public List<FixVO> findFixAssets(int userId, int primaryId, int userType, int upOrDown, String keywords){
        String sql = "";
        //我的【报修】【上拉】刷新
        if(userType == GlobalConstant.FIX_APPLY_USER_TYPE && upOrDown == GlobalConstant.UP){
            sql = "select F.*,U.REAL_NAME from FIX_ASSET_TASK F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID where F.APPLY_USER_ID = ? and F.ID < ?  order by F.ID desc limit 0,?";
        }else if(userType == GlobalConstant.FIX_APPLY_USER_TYPE && upOrDown == GlobalConstant.DOWN){
            sql = "select F.*,U.REAL_NAME from FIX_ASSET_TASK  F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID where  F.APPLY_USER_ID = ?  order by F.ID desc  limit 0,?";
        }else if(userType == GlobalConstant.FIX_REPAIR_USER_TYPE && upOrDown == GlobalConstant.UP){
            sql = "select F.*,U.REAL_NAME from FIX_ASSET_TASK  F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID where (F.FIX_USER_ID = ?  or F.STATUS = 1) and F.ID < ? order by F.ID desc limit 0,?";
        }else if(userType == GlobalConstant.FIX_REPAIR_USER_TYPE && upOrDown == GlobalConstant.DOWN){
            sql = "select F.*,U.REAL_NAME from FIX_ASSET_TASK  F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID where (F.FIX_USER_ID = ?  or F.STATUS = 1) order by F.ID desc  limit 0,?";
        }
        SqlPara sqlPara = new SqlPara();
        sqlPara.setSql(sql);
        sqlPara.addPara(userId);

        if(GlobalConstant.UP == upOrDown){
            sqlPara.addPara(primaryId);
        }

        sqlPara.addPara(GlobalConstant.DEFAULT_PAGE_SIZE);
        List<FixAssetTask> fixAssetTasks = fixAssetTaskDao.find(sqlPara);

        List<FixVO> voList = new ArrayList<>();

        if(fixAssetTasks == null || fixAssetTasks.size() == 0){
            return voList;
        }

        StringBuilder sb = new StringBuilder();
        for(FixAssetTask task : fixAssetTasks){
            sb.append(task.getId()).append(",");
            FixVO vo = new FixVO();
            vo.setId(task.getId());
            vo.setAssetType(DictConstant.mainCategoryMap.get(task.getAssetType()));
            vo.setApplyDate(DateUtil.format(task.getApplyDate(),DateUtil.DEFAULT_PATTERN));
            vo.setApplyUserName(task.get("REAL_NAME"));
            vo.setFixReason(task.getFixReason());
            vo.setStatusName(StatusConstant.getStatusMap(task.getStatus()));
            voList.add(vo);
        }
        String sqlIn = StringUtils.chop(sb.toString());
        Map<Integer,String> map = attachmentService.findAttachmentByBids(sqlIn);

       for(FixVO vo : voList){
           vo.setAttachmentId(map.get(vo.getId())==null ? "" : map.get(vo.getId()));
       }

        return voList;
    }

    public void change(int id,int status,int userId){
        FixAssetTask task = fixAssetTaskDao.findById(id);

        if(task.getStatus() != status){
            task.setStatus(status);
            task.setFixUserId(userId);
            //task.setStartFixDate(DateUtil.getCurrentDate());
            task.update();

            String date = DateUtil.format(task.getApplyDate(),DateUtil.MM_DD_HH_PATTERN);

            SyUser user = userService.getUser(task.getApplyUserId());

            //添加到推送队列
            weChatPushService.save(user.getWechatUserId(),"您"+date+"的报修状态有变化！");
        }
    }


    public boolean accept(int id,int version,int userId){

        FixAssetTask fixAssetTask = fixAssetTaskDao.findById(id);

        int newVersion = version + 1;
        int update = Db.update("update FIX_ASSET_TASK set FIX_USER_ID = '" + userId + "',VERSION = " + newVersion + ",STATUS = "+StatusConstant.IN_PROCESS+",START_FIX_DATE = '"+DateUtil.getCurrentDateStr()+"' where ID = " + id + " and VERSION = " + version);

        boolean result = update == 0;

        if(!result){
            SyUser user = userService.getUser(fixAssetTask.getApplyUserId());

            //添加到推送队列
            weChatPushService.save(user.getWechatUserId(),"您的报修申请已被人认领，请耐心等待！");
        }

        return result;

    }

    public void delete(int id){
        fixAssetTaskDao.deleteById(id);
    }

    public FixVO get(int id){

        FixAssetTask task = fixAssetTaskDao.findFirst("select F.*,U.REAL_NAME,S.REAL_NAME AS FIX_USER_REAL_NAME from FIX_ASSET_TASK F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID LEFT JOIN SY_USER S ON F.FIX_USER_ID = S.ID WHERE F.ID = ? ",id);

        FixVO vo = new FixVO();

        if(task == null){
            return vo;
        }
        vo.setFixUserName(task.get("FIX_USER_REAL_NAME"));

        vo.setStartFixDate(task.getStartFixDate() == null ? "" : DateUtil.format(task.getStartFixDate(),DateUtil.DEFAULT_PATTERN));
        vo.setApplyUserName(task.get("REAL_NAME"));
        vo.setAssetLocation(task.getAssetLocation());
        vo.setAssetName(task.getAssetName());
        vo.setAssetType(task.getAssetType());
        vo.setAssetName(task.getAssetName());
        vo.setFixReason(task.getFixReason());
        vo.setStatus(task.getStatus());
        vo.setStatusName(StatusConstant.getStatusMap(task.getStatus()));
        vo.setApplyDate(DateUtil.format(task.getApplyDate(),DateUtil.DEFAULT_PATTERN));
        vo.setVersion(task.getVersion());
        vo.setAssetTypeName(DictConstant.mainCategoryMap.get(task.getAssetType()));

        if(StringUtils.isNotBlank(task.getAssetType()) && StringUtils.isNotBlank(task.getAssetSubType())){

            String[] assetSubTypes = StringUtils.split(task.getAssetSubType(),",");
            StringBuilder sb = new StringBuilder();
            for(String singleSubType : assetSubTypes){
                sb.append(DictConstant.subCategoryMap.get(singleSubType));
                sb.append(",");
            }
            String assetSubTypeName = StringUtils.chop(sb.toString());
            vo.setAssetSubTypeName(assetSubTypeName);
            log.info("{}>>>{}",task.getAssetSubType(),assetSubTypeName);
        }

        vo.setAssetSubType(task.getAssetSubType());
        vo.setId(task.getId());

        vo.setVersion(task.getVersion());

        vo.setAttachmentIds(attachmentService.findAttachmentIdsByBid(task.getId()));

        return vo;
    }


    public void save(FixForm form){

        //保存报修信息
        FixAssetTask task = new FixAssetTask();
        task.setAssetType(form.getAssetType());
        task.setAssetSubType(form.getAssetSubType());
        task.setAssetName(form.getAssetName());
        task.setFixReason(form.getFixReason());
        task.setAssetLocation(form.getAssetLocation());

        if(form.getId() == null){
            task.setApplyUserId(form.getCurUserId());
            task.setVersion(1);
            task.setApplyDate(DateUtil.getCurrentDate());
            task.setStatus(StatusConstant.WAIT_PROCESS);
            task.save();

            //添加到推送队列
            weChatPushService.save(userService.findFixUser(),"有新的报修！");

        }else{
            task.setId(form.getId());
            task.update();
        }

        //关联附件
        attachmentService.batchUpdateAttachment(form.getAttachmentIds(), task.getId());

    }

}
