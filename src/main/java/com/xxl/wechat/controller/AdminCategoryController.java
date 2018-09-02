package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.xxl.wechat.model.generator.SySubCategory;
import com.xxl.wechat.service.CategoryService;
import com.xxl.wechat.vo.LayuiResultVO;

public class AdminCategoryController extends Controller {

    static CategoryService categoryService = new CategoryService();

    public void index() {
        render("admin-category-list.html");
    }

    public void list() {
        String name = getPara("name");
        String mainCategoryId = getPara(0);
        String page = getPara("page");
        String limitStr = getPara("limit");
        int curPage = (page == null) ? 1 : Integer.parseInt(page);
        int limit = (limitStr == null) ? 10 : Integer.parseInt(limitStr);
        LayuiResultVO<SySubCategory> SySubCategory = categoryService.findAllSubCategory(curPage, limit, name, mainCategoryId);

        renderJson(SySubCategory);
    }

}
