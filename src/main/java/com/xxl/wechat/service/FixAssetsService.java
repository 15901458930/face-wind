package com.xxl.wechat.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.constant.GlobalConstant;
import com.xxl.wechat.constant.StatusConstant;
import com.xxl.wechat.form.FixForm;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.vo.FixVO;
import com.xxl.wechat.vo.LayuiResultVO;
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


    /**
     * layui后台用
     * @param page
     * @param pageSize
     * @return
     */
    public LayuiResultVO<FixAssetTask> findAllFixAsset(int page, int pageSize, String status, String startDate,String endDate){



        Page<FixAssetTask> paginate = list(page,pageSize,status,startDate,endDate);

        for(FixAssetTask task : paginate.getList()){
            task.put("STATUS_NAME",StatusConstant.getStatusMap(task.getStatus()));
        }

        LayuiResultVO<FixAssetTask> vo = LayuiResultVO.getInstance().assemblySuccess(paginate.getTotalRow(),paginate.getList());

        return vo;
    }

    /**
     * layui后台用
     * @param page
     * @param pageSize
     * @return
     */
    public Page<FixAssetTask> list(int page, int pageSize, String status, String startDate,String endDate) {

        String sqlExceptSelect = "from FIX_ASSET_TASK F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID LEFT JOIN SY_USER U2 ON F.FIX_USER_ID = U2.ID LEFT JOIN SY_MAIN_CATEGORY C ON F.ASSET_TYPE = C.ID  WHERE 1=1 ";

        if (StringUtils.isNotBlank(status)) {
            sqlExceptSelect += "  and  F.STATUS = " + status;
        }
        if (StringUtils.isNotBlank(startDate)) {
            sqlExceptSelect += "  and  F.APPLY_DATE >= '" + startDate + "'";
        }
        if (StringUtils.isNotBlank(endDate)) {
            endDate += " 23:59:59";
            sqlExceptSelect += "  and  F.APPLY_DATE <= '" + endDate + "'";
        }

        return fixAssetTaskDao.paginate(page, pageSize, "select F.*,U.REAL_NAME AS APPLY_USER_NAME,U2.REAL_NAME AS FIX_USER_NAME,C.NAME AS ASSET_TYPE_NAME ", sqlExceptSelect);
    }

    public List<FixVO> findFixAssets(int userId, int primaryId, int userType, int upOrDown, String keywords){
        StringBuilder sql = new StringBuilder();
        //我的【报修】【上拉】刷新
        if(upOrDown == GlobalConstant.UP){
            sql.append("select F.*,U.REAL_NAME from FIX_ASSET_TASK F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID where F.ID <   ").append(primaryId);
        }else{
            sql.append("select F.*,U.REAL_NAME from FIX_ASSET_TASK  F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID WHERE 1=1 ");
        }

        if(userType == 2){
            //老师只能看到自己的，领导不做限制
            sql.append(" AND  F.APPLY_USER_ID = "+userId);
        }else if(userType == 3 || userType == 4){
            sql.append(" AND 3=4 ");//强行看?，不好意
        }
        sql.append(" order by F.ID desc limit 0,"+GlobalConstant.DEFAULT_PAGE_SIZE);

        List<FixAssetTask> fixAssetTasks = fixAssetTaskDao.find(sql.toString());

        return convert(fixAssetTasks);
    }


    public List<FixVO> findRepairAssets(int userId, int primaryId, int userType, int upOrDown, String keywords){
        String sql = "";
        //我的【报修】【上拉】刷新
        if(upOrDown == GlobalConstant.UP){
            sql = "select F.*,U.REAL_NAME from FIX_ASSET_TASK  F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID where (F.FIX_USER_ID = ?  or F.STATUS = 1) AND F.ASSET_TYPE = ? and F.ID < ? order by F.ID desc limit 0,?";
        }else if(upOrDown == GlobalConstant.DOWN){
            sql = "select F.*,U.REAL_NAME from FIX_ASSET_TASK  F LEFT JOIN SY_USER U ON F.APPLY_USER_ID = U.ID where (F.FIX_USER_ID = ?  or F.STATUS = 1) AND F.ASSET_TYPE = ? order by F.ID desc  limit 0,?";
        }
        SqlPara sqlPara = new SqlPara();
        sqlPara.setSql(sql);
        sqlPara.addPara(userId);

        sqlPara.addPara(userType);
        if(GlobalConstant.UP == upOrDown){
            sqlPara.addPara(primaryId);
        }

        sqlPara.addPara(GlobalConstant.DEFAULT_PAGE_SIZE);
        List<FixAssetTask> fixAssetTasks = fixAssetTaskDao.find(sqlPara);

        return convert(fixAssetTasks);
    }

    private List<FixVO> convert(List<FixAssetTask> list){
        List<FixVO> voList = new ArrayList<>();

        if(list == null || list.size() == 0){
            return voList;
        }

        StringBuilder sb = new StringBuilder();
        for(FixAssetTask task : list){
            sb.append(task.getId()).append(",");
            FixVO vo = new FixVO();
            vo.setId(task.getId());
            vo.setAssetType(DictCache.mainCategoryMap.get(task.getAssetType()));
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
            weChatPushService.save(user.getWechatUserId(),"您"+date+"提交的报修状态有变化！");
            weChatPushService.save(userService.findFixUser("5"),"流水号:"+id+"的报修申请状态发生变化！");

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
            weChatPushService.save(user.getWechatUserId(),"您的报修申请(流水号:"+fixAssetTask.getId()+")已被人认领，请耐心等待！");
            weChatPushService.save(userService.findFixUser("5"),"流水号:"+fixAssetTask.getId()+"的报修申请已被人认领！");
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
        vo.setBelongCampusName(DictCache.belongCampusMap.get(task.getBelongCampus()));
        vo.setStatus(task.getStatus());
        vo.setBelongCampus(task.getBelongCampus());
        vo.setStatusName(StatusConstant.getStatusMap(task.getStatus()));
        vo.setApplyDate(DateUtil.format(task.getApplyDate(),DateUtil.DEFAULT_PATTERN));
        vo.setVersion(task.getVersion());
        vo.setAssetTypeName(DictCache.mainCategoryMap.get(task.getAssetType()));

        if(StringUtils.isNotBlank(task.getAssetType()) && StringUtils.isNotBlank(task.getAssetSubType())){

            String[] assetSubTypes = StringUtils.split(task.getAssetSubType(),",");
            StringBuilder sb = new StringBuilder();
            for(String singleSubType : assetSubTypes){
                sb.append(DictCache.subCategoryMap.get(singleSubType));
                sb.append(",");
            }
            String assetSubTypeName = StringUtils.chop(sb.toString());
            vo.setAssetSubTypeName(assetSubTypeName);
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
        task.setBelongCampus(form.getBeLongCampus());

        if(form.getId() == null){
            task.setApplyUserId(form.getCurUserId());
            task.setVersion(1);
            task.setApplyDate(DateUtil.getCurrentDate());
            task.setStatus(StatusConstant.WAIT_PROCESS);
            task.save();

            //添加到推送队列(发送给维修人员和领导)
            String userTypes = task.getAssetType()+",5";
            weChatPushService.save(userService.findFixUser(userTypes),"有新的报修！");

        }else{
            task.setId(form.getId());
            task.update();
        }

        //关联附件
        attachmentService.batchUpdateAttachment(form.getAttachmentIds(), task.getId());

    }


    /**
     * 返回map
     * @return
     */
    public  boolean isExistsAsstSubType(String id){


        String sql = "select * from FIX_ASSET_TASK WHERE ASSET_SUB_TYPE LIKE '%"+id+"%'";

        List<FixAssetTask> list = fixAssetTaskDao.find(sql);

        return list.size() == 0;

    }
}
