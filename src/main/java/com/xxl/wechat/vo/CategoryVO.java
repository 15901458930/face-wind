package com.xxl.wechat.vo;

import com.xxl.wechat.entity.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryVO {

    public List<Category> main = new ArrayList<>();

    public Map<String,List<Category>> sub = new HashMap<>();
}
