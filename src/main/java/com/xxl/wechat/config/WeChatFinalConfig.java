package com.xxl.wechat.config;

import com.jfinal.config.*;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.xxl.wechat.controller.*;
import com.xxl.wechat.form.FixForm;
import com.xxl.wechat.http.HttpUtil;
import com.xxl.wechat.init.AccessTokenSchedule;
import com.xxl.wechat.interceptor.ExceptionInterceptor;
import com.xxl.wechat.interceptor.ListInterceptor;
import com.xxl.wechat.model.generator._MappingKit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WeChatFinalConfig extends  JFinalConfig {


    @Override
    public void configConstant(Constants constants) {

        constants.setDevMode(true);
        PropKit.use("config.properties");
        constants.setError404View("/WEB-INF/view/404.html");
        constants.setError500View("/WEB-INF/view/500.html");
    }

    @Override
    public void configRoute(Routes routes) {


        routes.setBaseViewPath("WEB-INF/front");
        routes.add("/fix", FixAssetsController.class);
        routes.add("/repair", RepairAssetsController.class);
        routes.add("/book", BookRoomController.class);
        routes.add("/index", IndexController.class);
        routes.add("/login", LoginController.class);
        routes.add("/user", UserController.class);
        routes.add("/attachment", AttachmentController.class);


        


    }


    @Override
    public void configEngine(Engine engine) {

//        engine.addSharedFunction("WEB-INF/view/common/share/_container.html");
//        engine.addSharedFunction("WEB-INF/view/common/share/_paginate.html");
        engine.addSharedFunction("WEB-INF/front/fix/sub-fix-add.html");
        engine.addSharedFunction("WEB-INF/front/fix/sub-fix-detail.html");

        engine.addSharedFunction("WEB-INF/front/repair/sub-repair-add.html");
        engine.addSharedFunction("WEB-INF/front/share/_tab-bar.html");

        //用于判断是否包含某些功能权限
        //engine.addSharedMethod(com.datong.cms.util.SessionUtil.class);
    }

    @Override
    public void configPlugin(Plugins plugins) {

     /*   DruidPlugin dp = new DruidPlugin(jdbcUrl, userName, password);
        plugins.add(dp);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        //arp.addMapping("user", User.class);
        plugins.add(arp);*/
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbc.url"), PropKit.get("jdbc.user"), PropKit.get("jdbc.password").trim());
        plugins.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp);
        plugins.add(arp);

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {
        interceptors.add(new SessionInViewInterceptor());
        interceptors.add(new ListInterceptor());
    }

    @Override
    public void configHandler(Handlers handlers) {

    }


    @Override
    public void afterJFinalStart() {

        //启动定时任务，获取access_token
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1);

        final ScheduledFuture<?> schedule = executor.scheduleAtFixedRate(new AccessTokenSchedule(), 0,3600, TimeUnit.SECONDS);

    }



}
