package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.xxl.wechat.constant.GlobalConstant;
import com.xxl.wechat.entity.ResponseResult;
import com.xxl.wechat.form.FixForm;
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

public class RepairAssetsController extends Controller {

    private static Logger log = LoggerFactory.getLogger(RepairAssetsController.class);

    static FixAssetsService fixAssetsService = new FixAssetsService();


    public void index() {
        setAttr("belong","repair");
        render("main-repair-index.html");

    }

    public void get(){
        int id = this.getParaToInt(0);

        FixVO fixVO = fixAssetsService.get(id);
        ResponseResult<FixVO> result = ResponseResult.instance().setSuccessData(true,fixVO);
        renderJson(result);
    }

    /**
     * 我的维修
     */
    public void list() {
       SyUser user = (SyUser)getSessionAttr("user");
        int upOrDown = this.getParaToInt(0);
        int primaryId = this.getParaToInt(1);
        List<FixVO> fixAssets = fixAssetsService.findRepairAssets(user.getId(), primaryId,user.getUserType(),upOrDown, "");
        ResponseResult<FixVO> result = ResponseResult.instance().setSuccessData(true,fixAssets);
        renderJson(result);
    }


    public void save(){


        String json = getPara("fix");
       SyUser user = (SyUser)getSessionAttr("user");

        log.info("json>>>{}",json);
        FixForm form = FastJson.getJson().parse(json, FixForm.class);
        form.setCurUserId(user.getId());
        fixAssetsService.save(form);

        Map<String,String> map  = new HashMap<>();
        map.put("result","success");

        renderJson(map);

    }


}
