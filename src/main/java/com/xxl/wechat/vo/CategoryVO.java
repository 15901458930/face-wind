package com.xxl.wechat.vo;

import com.xxl.wechat.entity.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryVO {

    /**
     * 大类
     */
    public List<Category> main = new ArrayList<>();

    /**
     * 小类
     */
    public Map<String,List<Category>> sub = new HashMap<>();

    /**
     * 校区
     */
    public List<Category> campus = new ArrayList<>();

}
