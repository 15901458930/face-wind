package com.xxl.wechat.service;

import com.jfinal.plugin.activerecord.Page;
import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.entity.Category;
import com.xxl.wechat.model.generator.SyMainCategory;
import com.xxl.wechat.model.generator.SySubCategory;
import com.xxl.wechat.vo.LayuiResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {


    private static Logger log = LoggerFactory.getLogger(CategoryService.class);

    static SyMainCategory mainCategoryDao = new SyMainCategory().dao();

    static SySubCategory subCategoryDao = new SySubCategory().dao();

    public List<SyMainCategory> findAllMainCategory() {

        return mainCategoryDao.find("select * from SY_MAIN_CATEGORY");
    }

    public LayuiResultVO<SySubCategory> findAllSubCategory(int page, int pageSize, String name, String mainCategoryId) {

        String sqlExceptSelect = "from SY_SUB_CATEGORY WHERE 1=1 ";
        if (StringUtils.isNotBlank(name)) {
            sqlExceptSelect += " AND NAME LIKE '%" + name + "%'";
        }
        if (StringUtils.isNotBlank(mainCategoryId)) {
            sqlExceptSelect += " AND MAIN_CATEGORY_ID =  " + mainCategoryId;
        }
        Page<SySubCategory> paginate = subCategoryDao.paginate(page, pageSize, "select * ", sqlExceptSelect);

        for (SySubCategory u : paginate.getList()) {
            u.put("CATEGORY_TYPE_NAME", DictCache.subCategoryMap.get(u.getMainCategoryId()));

        }

        LayuiResultVO<SySubCategory> vo = LayuiResultVO.getInstance().assemblySuccess(paginate.getList().size(), paginate.getList());

        return vo;

    }


    /**
     * 用于前端渲染成Json给下拉框用的
     *
     * @return
     */
    /*public Map<String, List<Category>> findSubCategory4json() {

        List<SySubCategory> subCategoryList = findAllSubCategory();
        Map<String, List<Category>> map = new HashMap<>();

        for (SySubCategory sub : subCategoryList) {
            if (map.containsKey(String.valueOf(sub.getMainCategoryId()))) {
                Category c1 = new Category(sub.getName(), String.valueOf(sub.getId()));
                map.get(String.valueOf(sub.getMainCategoryId())).add(c1);

            } else {
                List<Category> list = new ArrayList<>();
                Category c = new Category(sub.getName(), String.valueOf(sub.getId()));
                list.add(c);
                map.put(String.valueOf(sub.getMainCategoryId()), list);
            }
        }
        log.warn("加载物品子类成功，共计：{}", subCategoryList.size());
        return map;
    }*/

    /**
     * 用于前端渲染成Json给下拉框用的
     *
     * @return
     */
    public List<Category> findMainCategory4json() {
        List<SyMainCategory> mainCategoryList = findAllMainCategory();
        List<Category> list = new ArrayList<>();

        for (SyMainCategory main : mainCategoryList) {
            Category c = new Category(main.getName(), String.valueOf(main.getId()));
            list.add(c);
        }

        log.warn("加载物品大类成功，共计：{}", list.size());

        return list;
    }


    /**
     * 返回map
     *
     * @return
     */
   /* public Map<String, String> findSubCategoryMap() {

        List<SySubCategory> subCategoryList = findAllSubCategory();
        Map<String, String> map = new HashMap<>();

        for (SySubCategory sub : subCategoryList) {
            map.put(String.valueOf(sub.getId()), sub.getName());
        }
        return map;
    }*/

    /**
     * 返回map
     *
     * @return
     */
    public Map<String, String> findMainCategoryMap() {
        List<SyMainCategory> mainCategoryList = findAllMainCategory();
        Map<String, String> map = new HashMap<>();

        for (SyMainCategory main : mainCategoryList) {
            map.put(String.valueOf(main.getId()), main.getName());
        }
        return map;
    }
}
