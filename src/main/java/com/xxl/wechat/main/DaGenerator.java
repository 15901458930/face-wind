package com.xxl.wechat.main;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 代码生成
 */
public class DaGenerator {

    public static void main(String[] args) {
        // base model 所使用的包名
        String baseModelPkg = "com.xxl.wechat.model.generator.base";
        // base model 文件保存路径
        String baseModelDir = PathKit.getWebRootPath() + "/src/main/java/com/xxl/wechat/model/generator/base";

        // model 所使用的包名
        String modelPkg = "com.xxl.wechat.model.generator";
        // model 文件保存路径
        String modelDir = PathKit.getWebRootPath() + "/src/main/java/com/xxl/wechat/model/generator";

        Properties p = new Properties();
        p.put("initialSize", "1");
        p.put("minIdle", "1");
        p.put("maxActive", "20");
        p.put("maxWait", "60000");
        p.put("timeBetweenEvictionRunsMillis", "60000");
        p.put("minEvictableIdleTimeMillis", "300000");
        p.put("validationQuery", "SELECT 'x' from dual");
        p.put("testWhileIdle", "true");
        p.put("testOnBorrow", "false");
        p.put("testOnReturn", "false");
        p.put("poolPreparedStatements", "true");
        p.put("maxPoolPreparedStatementPerConnectionSize", "20");
        p.put("filters", "stat");
        p.put("url", "jdbc:mysql://101.201.235.213:3306/wechat");
        p.put("username", "wechat");
        p.put("password", "1234+asdf");
        DataSource ds = null;
        try {
            ds = DruidDataSourceFactory.createDataSource(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Generator gernerator = new Generator(ds, baseModelPkg, baseModelDir, modelPkg, modelDir);
        gernerator.setMetaBuilder(new WechatMediaBuilder(ds));

        //gernerator.addExcludedTable();//自动生成代码时排除掉ACT工作流的表
        gernerator.generate();
    }

    public  static class WechatMediaBuilder extends MetaBuilder {

         public  WechatMediaBuilder(DataSource dataSource) {
            super(dataSource);
        }


        @Override
        protected String buildAttrName(String colName) {
            colName = colName.toLowerCase();
            return StrKit.toCamelCase(colName);
        }

    }
}
