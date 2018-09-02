package com.xxl.wechat.cache;

import com.xxl.wechat.entity.Category;
import com.xxl.wechat.service.CategoryService;
import com.xxl.wechat.service.RoomService;
import com.xxl.wechat.vo.CategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictCache {

    private static Logger log = LoggerFactory.getLogger(DictCache.class);

    static CategoryService service = new CategoryService();

    static RoomService roomService = new RoomService();


    /**
     * 前台weui select组件专用
     */
    public static List<Category> mainCategoryList = new ArrayList<>();
    public  static  Map<String,List<Category>> subCategoryList = new HashMap<>();
    public static List<Category> belongCampusList = new ArrayList<>();

    /**
     * 后台拼装name专用
     */
    public static Map<String,String> mainCategoryMap = new HashMap<>();
    public static Map<String,String> subCategoryMap = new HashMap<>();
    public static Map<String,String> belongCampusMap = new HashMap<>();

    public static Map<Integer,String> userTypeMap = new HashMap<>();

    /**
     * room
     *
     */
    public static Map<Integer,String> roomMap = new HashMap<>();


    public static CategoryVO vo = new CategoryVO();

    public static DictCache dictConstant = new DictCache();

    private DictCache(){

    }

    public static DictCache getInstance(){
        return dictConstant;
    }


    static {

        //校区直接写死
        initBelongCampusMapAndList();

        initUserTypeMap();
    }


    /**
     * 初始化（）
     */
    public void init(){

        mainCategoryList = service.findMainCategory4json();

        subCategoryList = service.findSubCategory4json();

        mainCategoryMap = service.findMainCategoryMap();

        subCategoryMap = service.findSubCategoryMap();

        roomMap = roomService.findRoomMap();

    }


    public  static void  initBelongCampusMapAndList(){

        belongCampusMap.put("1","嘉园校区");
        belongCampusMap.put("2","北辛庄校区");
        belongCampusMap.put("3","西冉校区");


        Category c1 = new Category("嘉园校区","1");
        Category c2 = new Category("北辛庄校区","2");
        Category c3 = new Category("西冉校区","3");
        belongCampusList.add(c1);
        belongCampusList.add(c2);
        belongCampusList.add(c3);


    }

    public  static void  initUserTypeMap() {
        userTypeMap.put(2,"教师");
        userTypeMap.put(3,"后勤中心-维修");
        userTypeMap.put(4,"信息中心-维修");
        userTypeMap.put(5,"领导");

    }



}
