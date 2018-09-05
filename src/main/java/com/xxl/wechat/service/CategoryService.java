package com.xxl.wechat.service;

import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.entity.Category;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.model.generator.SyAttachment;
import com.xxl.wechat.model.generator.SyMainCategory;
import com.xxl.wechat.model.generator.SySubCategory;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.util.FileUtil;
import com.xxl.wechat.vo.LayuiResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class CategoryService {


    private static Logger log = LoggerFactory.getLogger(CategoryService.class);

    static SyMainCategory mainCategoryDao = new SyMainCategory().dao();


    static FixAssetsService fixAssetsService = new FixAssetsService();

    static SySubCategory subCategoryDao = new SySubCategory().dao();

    public List<SyMainCategory> findAllMainCategory() {

        return mainCategoryDao.find("select * from SY_MAIN_CATEGORY");
    }

    public List<SySubCategory> findAllSubCategory() {

        return subCategoryDao.find("select * from SY_SUB_CATEGORY");

    }

    public LayuiResultVO<SySubCategory> findAllSubCategoryByCondition(int page,int pageSize,String name,String parentId) {
        String sqlExceptSelect = "  from SY_SUB_CATEGORY S LEFT JOIN SY_MAIN_CATEGORY M ON S.MAIN_CATEGORY_ID = M.ID  WHERE 1=1 ";
        if(StringUtils.isNotBlank(name)){
            sqlExceptSelect += " AND S.NAME LIKE '%"+name+"%'";
        }
        if(StringUtils.isNotBlank(parentId)){
            sqlExceptSelect += " AND S.MAIN_CATEGORY_ID =  "+parentId;
        }

        sqlExceptSelect += " order by S.ID desc";
        Page<SySubCategory> pagnate = subCategoryDao.paginate(page, pageSize, "select S.*,M.NAME AS PARENT_NAME ",sqlExceptSelect);

        return LayuiResultVO.getInstance().assemblySuccess(pagnate.getTotalRow(),pagnate.getList());

    }

    /**
     * 用于前端渲染成Json给下拉框用的
     * @return
     */
    public Map<String,List<Category>> findSubCategory4json(){

        List<SySubCategory> subCategoryList = findAllSubCategory();
        Map<String,List<Category>> map = new HashMap<>();

        for(SySubCategory sub : subCategoryList){
            if(map.containsKey(String.valueOf(sub.getMainCategoryId()))){
                Category c1 = new Category(sub.getName(),String.valueOf(sub.getId()));
                map.get(String.valueOf(sub.getMainCategoryId())).add(c1);

            }else{
                List<Category> list = new ArrayList<>();
                Category c = new Category(sub.getName(),String.valueOf(sub.getId()));
                list.add(c);
                map.put(String.valueOf(sub.getMainCategoryId()),list);
            }
        }
        log.warn("加载物品子类成功，共计：{}",subCategoryList.size());
        return map;
    }

    /**
     * 用于前端渲染成Json给下拉框用的
     * @return
     */
    public List<Category> findMainCategory4json(){
        List<SyMainCategory> mainCategoryList = findAllMainCategory();
        List<Category> list = new ArrayList<>();

        for(SyMainCategory main : mainCategoryList){
            Category c = new Category(main.getName(),String.valueOf(main.getId()));
            list.add(c);
        }

        log.warn("加载物品大类成功，共计：{}",list.size());

        return list;
    }



    /**
     * 返回map
     * @return
     */
    public Map<String,String> findSubCategoryMap(){

        List<SySubCategory> subCategoryList = findAllSubCategory();
        Map<String,String> map = new HashMap<>();

        for(SySubCategory sub : subCategoryList){
            map.put(String.valueOf(sub.getId()),sub.getName());
        }
        return map;
    }

    /**
     * 返回map
     * @return
     */
    public  Map<String,String> findMainCategoryMap(){
        List<SyMainCategory> mainCategoryList = findAllMainCategory();
        Map<String,String> map = new HashMap<>();

        for(SyMainCategory main : mainCategoryList){
            map.put(String.valueOf(main.getId()),main.getName());
        }
        return map;
    }

    public boolean delSub(String id){

      boolean flag =  fixAssetsService.isExistsAsstSubType(id);
      if(flag){
          SySubCategory sub = subCategoryDao.findById(id);
          subCategoryDao.deleteById(id);
            log.info("删除SY_SUB_CATEGORY单条数据成功，记录：{}",FastJson.getJson().toJson(sub));
            return true;

      }
      log.warn("删除SY_SUB_CATEGORY表的主键{}失败，FIX_ASSET_TASK中已经存在该子类的数据!");
      return false;

    }

    public void save(SySubCategory cate){
        if(cate.getId() == null){
            cate.setCreateDate(DateUtil.getCurrentDate());
            cate.save();

        }else{
            cate.update();
        }


        DictCache.getInstance().refreshSubCategory();

        log.info("子分类有更新和添加操作，DictCache缓存刷新中。。。。");



    }

}
