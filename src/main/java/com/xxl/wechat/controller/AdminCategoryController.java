package com.xxl.wechat.controller;

import com.jfinal.core.Controller;
import com.xxl.wechat.entity.ResponseResult;
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

        LayuiResultVO<SySubCategory> sySubCategory = categoryService.findAllSubCategoryByCondition(curPage, limit, name, mainCategoryId);

        renderJson(sySubCategory);
    }

    public void del(){

        String id = getPara(0);

        boolean flag = categoryService.delSub(id);

        ResponseResult<String> result = null;
        if(flag){
           result = ResponseResult.instance().setSuccessData(flag,"");

        }else{
            result = ResponseResult.instance().setErrorMsg(flag,"该分类已经存在维修单中有关联，不能删除");
        }

        renderJson(result);
    }

}
