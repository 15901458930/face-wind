package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.upload.ExceededSizeException;
import com.jfinal.upload.UploadFile;
import com.xxl.wechat.constant.DictConstant;
import com.xxl.wechat.model.generator.SyAttachment;
import com.xxl.wechat.service.AttachmentService;
import com.xxl.wechat.service.CategoryService;
import com.xxl.wechat.vo.CategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.xxl.wechat.controller.AttachmentController.attachmentService;

public class CategoryController extends Controller {

    private static Logger log = LoggerFactory.getLogger(CategoryController.class);

    static CategoryService categoryService = new CategoryService();


    public void get() {

        CategoryVO vo = new CategoryVO();

        //直接取缓存过的（在jfinal初始化的时候加载的）
        vo.main = DictConstant.mainCategoryList;
        vo.sub = DictConstant.subCategoryList;

        renderJson(FastJson.getJson().toJson(vo));
    }
}
