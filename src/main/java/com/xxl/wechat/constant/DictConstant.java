package com.xxl.wechat.constant;

import com.xxl.wechat.controller.FixAssetsController;
import com.xxl.wechat.entity.Category;
import com.xxl.wechat.service.CategoryService;
import com.xxl.wechat.vo.CategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictConstant {

    private static Logger log = LoggerFactory.getLogger(DictConstant.class);

    static CategoryService service = new CategoryService();

    public static List<Category> mainCategoryList = new ArrayList<>();

    public  static  Map<String,List<Category>> subCategoryList = new HashMap<>();


    public static Map<String,String> mainCategoryMap = new HashMap<>();

    public static Map<String,String> subCategoryMap = new HashMap<>();


    public static CategoryVO vo = new CategoryVO();

    public static DictConstant dictConstant = new DictConstant();

    private DictConstant(){

    }

    public static DictConstant getInstance(){
        return dictConstant;
    }


    /**
     * 初始化（）
     */
    public void init(){

        mainCategoryList = service.findMainCategory4json();

        subCategoryList = service.findSubCategory4json();

        mainCategoryMap = service.findMainCategoryMap();

        subCategoryMap = service.findSubCategoryMap();
    }

}
