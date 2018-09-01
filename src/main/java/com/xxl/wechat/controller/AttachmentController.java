package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.ExceededSizeException;
import com.jfinal.upload.UploadFile;
import com.xxl.wechat.constant.GlobalConstant;
import com.xxl.wechat.entity.ResponseResult;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.model.generator.SyAttachment;
import com.xxl.wechat.service.AttachmentService;
import com.xxl.wechat.service.FixAssetsService;
import com.xxl.wechat.util.FileUtil;
import com.xxl.wechat.vo.FixVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttachmentController extends Controller {

    private static Logger log = LoggerFactory.getLogger(AttachmentController.class);

    static AttachmentService attachmentService = new AttachmentService();



    public void upload() {

        Map<String, Object> map = new HashMap<String, Object>();

        long size = 0;
        try {
            UploadFile uploadFile = getFile();
            String img = getPara("images");
            SyAttachment syAttachment = attachmentService.uploadImg(img);
            map.put("result", "success");
            map.put("attachmentId",syAttachment.getId());
        } catch (ExceededSizeException e) {

            map.put("result", "error");
            map.put("msg", "文件必须小于10M");
            log.error("上传文件失败", e);
        }catch (Exception e){

            map.put("result", "error");
            map.put("msg", "上传文件失败！");
            log.error("上传文件失败", e);
        }
        renderJson(map);
    }

    public void delete(){
        String id = getPara(0);
        attachmentService.delete(id);

        ResponseResult<String> result = ResponseResult.instance().setSuccessData(true,"");
        renderJson(result);
    }
}
