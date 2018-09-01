package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.service.CategoryService;
import com.xxl.wechat.vo.CategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryController extends Controller {

    private static Logger log = LoggerFactory.getLogger(CategoryController.class);

    static CategoryService categoryService = new CategoryService();


    public void get() {

        CategoryVO vo = new CategoryVO();

        //直接取缓存过的（在jfinal初始化的时候加载）
        vo.main =DictCache.mainCategoryList;
        vo.sub = DictCache.subCategoryList;
        vo.campus = DictCache.belongCampusList;

        //一次全取到前台用于select的渲染
        renderJson(FastJson.getJson().toJson(vo));
    }
}
