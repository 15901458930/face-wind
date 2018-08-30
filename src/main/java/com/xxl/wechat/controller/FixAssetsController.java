package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.xxl.wechat.constant.GlobalConstant;
import com.xxl.wechat.entity.ResponseResult;
import com.xxl.wechat.form.FixForm;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.model.generator.SyUser;
import com.xxl.wechat.service.FixAssetsService;
import com.xxl.wechat.vo.FixVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixAssetsController extends Controller {

    private static Logger log = LoggerFactory.getLogger(FixAssetsController.class);

    static FixAssetsService fixAssetsService = new FixAssetsService();


    public void index() {

        SyUser user = (SyUser)getSessionAttr("user");
        setAttr("belong","fix");
        render("main-fix-index.html");

    }


    public void change(){
        int id = this.getParaToInt(0);
        int status = this.getParaToInt(1);
        SyUser user = (SyUser)getSessionAttr("user");
        fixAssetsService.change(id,status,user.getId());
        ResponseResult<String> result = ResponseResult.instance().setInstance(true,"");
        renderJson(result);
    }

    public void get(){
        int id = this.getParaToInt(0);

        FixVO fixVO = fixAssetsService.get(id);
        ResponseResult<FixVO> result = ResponseResult.instance().setInstance(true,fixVO);
        renderJson(result);
    }

    public void delete(){
        int id = this.getParaToInt(0);

        fixAssetsService.delete(id);
        ResponseResult<String> result = ResponseResult.instance().setInstance(true,"");
        renderJson(result);
    }

    public void accept(){

        int id = this.getParaToInt(0);
        int version = this.getParaToInt(1);
        SyUser user = (SyUser)getSessionAttr("user");
        boolean accept = fixAssetsService.accept(id, version, user.getId());
        String msg = accept ? "该报修信息已被其它维修人员领取！" : "";
        ResponseResult<String> result = ResponseResult.instance().setErrorMsg(!accept,msg);
        renderJson(result);

    }

    /**
     * 我的报修
     */
    public void list() {
        String code = this.getPara("code");
        log.info("测试code:"+code);

       SyUser user = (SyUser)getSessionAttr("user");


        int upOrDown = this.getParaToInt(0);
        int primaryId = this.getParaToInt(1);

        List<FixVO> fixAssets = fixAssetsService.findFixAssets(user.getId(), primaryId,GlobalConstant.FIX_APPLY_USER_TYPE,upOrDown, "");
        ResponseResult<FixVO> result = ResponseResult.instance().setInstance(true,fixAssets);
        renderJson(result);
    }

    public void save(){
       SyUser user = (SyUser)getSessionAttr("user");

        String code = this.getPara("code");
        log.info("测试code:"+code);

        String json = getPara("fix");

        log.info("json>>>{}",json);
        FixForm form = FastJson.getJson().parse(json, FixForm.class);

        form.setCurUserId(user.getId());

        fixAssetsService.save(form);

        Map<String,String> map  = new HashMap<>();
        map.put("result","success");

        renderJson(map);

    }


}
