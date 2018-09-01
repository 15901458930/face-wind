package com.xxl.wechat.config;

import com.jfinal.config.*;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.config.routes.AdminRoute;
import com.xxl.wechat.config.routes.FrontRoute;
import com.xxl.wechat.controller.*;
import com.xxl.wechat.init.AccessTokenSchedule;
import com.xxl.wechat.init.WeChatPushSchedule;
import com.xxl.wechat.interceptor.ExceptionInterceptor;
import com.xxl.wechat.model.generator._MappingKit;
import com.xxl.wechat.service.WeChatPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WeChatFinalConfig extends  JFinalConfig {

    private static Logger log = LoggerFactory.getLogger(FixAssetsController.class);


    @Override
    public void configConstant(Constants constants) {

        constants.setDevMode(true);
        PropKit.use("config.properties");
        constants.setError404View("/WEB-INF/view/404.html");
        constants.setError500View("/WEB-INF/view/500.html");
    }

    @Override
    public void configRoute(Routes routes) {

        routes.add(new FrontRoute());  // 前端路由
        routes.add(new AdminRoute());  // 后端路由
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
        interceptors.add(new ExceptionInterceptor());

    }

    @Override
    public void configHandler(Handlers handlers) {

    }


    @Override
    public void afterJFinalStart() {


        log.warn("afterJFinalStart ===1.准备定时获取access-token===");

        //启动定时任务，获取access_token
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> schedule = executor.scheduleAtFixedRate(new AccessTokenSchedule(), 0,3600, TimeUnit.SECONDS);

        log.warn("afterJFinalStart ===2.准备加载大类，子类(只加一次)===");
        DictCache.getInstance().init();


        WeChatPushService service = new WeChatPushService();

        log.warn("afterJFinalStart ===3.准备定时推送微信，子类(只加一次)===");
        //启动定时任务，获取access_token
        ScheduledThreadPoolExecutor executor1 = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> schedule1 = executor1.scheduleAtFixedRate(new WeChatPushSchedule(service), 0,5, TimeUnit.SECONDS);


    }



}
