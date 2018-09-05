package com.xxl.wechat.service;

import com.jfinal.json.FastJson;
import com.jfinal.plugin.activerecord.Page;
import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.entity.UserInfoResult;
import com.xxl.wechat.http.HttpUtil;
import com.xxl.wechat.model.generator.SyRoom;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.vo.LayuiResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);


    static SyUser syUserDao = new SyUser().dao();


    public SyUser findUserByUserNameAndPassword(String userName,String password){
        String sql = "select * from SY_USER WHERE USER_NAME = ? AND PASSWORD = ?";
        return syUserDao.findFirst(sql,userName,password);
    }

    public SyUser getUser(int id){

        return syUserDao.findById(id);
    }

    public SyUser getUserByWechatUserId(String id){

        return syUserDao.findFirst("select * from SY_USER WHERE WECHAT_USER_ID = ? " ,id);
    }

    public void delete(int id){

        SyUser user = getUser(id);
        String str = FastJson.getJson().toJson(user);
        log.warn("删除用户操作日志：{}",str);
        syUserDao.deleteById(id);

    }

    /**
     * layui后台用
     * @param page
     * @param pageSize
     * @param realName
     * @param userType
     * @return
     */
    public LayuiResultVO<SyUser> findAllUser( int page,int pageSize,String realName,String userType){

        String sqlExceptSelect = "from SY_USER WHERE 1=1 ";
        if(StringUtils.isNotBlank(realName)){
            sqlExceptSelect += " AND REAL_NAME LIKE '%"+realName+"%'";
        }
        if(StringUtils.isNotBlank(userType)){
            sqlExceptSelect += " AND USER_TYPE =  "+userType;
        }
        Page<SyUser> paginate = syUserDao.paginate(page, pageSize, "select * ", sqlExceptSelect);

        for(SyUser u : paginate.getList()){
            u.put("USER_TYPE_NAME",DictCache.userTypeMap.get(u.getUserType()));
            u.put("BOOK_AUTHORITY_NAME",u.getBookAuthority() == 1 ? "有":"无");
        }

        LayuiResultVO<SyUser> vo = LayuiResultVO.getInstance().assemblySuccess(paginate.getTotalRow(),paginate.getList());

        return vo;

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

        log.info("保存用户信息：{}",FastJson.getJson().toJson(user));

        return user;
    }

    public void updateStatus(Integer id,Integer userType,Integer authority){


        SyUser user = new SyUser();
        user.setId(id);
        user.setUserType(userType);
        user.setBookAuthority(authority);

        log.info("管理端修改用户信息{}",FastJson.getJson().toJson(user));

        user.update();
    }


    public void updateStatus(Integer id,Integer userType){


        SyUser user = new SyUser();
        user.setId(id);
        user.setUserType(userType);

        log.info("用户ID：{} 微信端初次关注时选择用户角色为 {}",id,userType);

        user.update();
    }


    public String findFixUser(String userType){

        String sql = "select * from SY_USER WHERE USER_TYPE in ("+userType+")";
        List<SyUser> syUsers = syUserDao.find(sql);

        StringBuilder sb = new StringBuilder();

        for(SyUser user : syUsers){
            sb.append(user.getWechatUserId()).append("|");
        }

        return  StringUtils.chop(sb.toString());
    }

}
