package com.xxl.wechat.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.xxl.wechat.controller.AttachmentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ExceptionInterceptor implements Interceptor {

    private static Logger log = LoggerFactory.getLogger(ExceptionInterceptor.class);


    public boolean isAjax(Controller controller){
        HttpServletRequest request = controller.getRequest();
        String requestedWith = request.getHeader("x-requested-with");
        if(StrKit.notBlank(requestedWith) && requestedWith.equals("XMLHttpRequest")){
            return true;
        }
        return false;
    }

    public void intercept(Invocation ai) {
        try {
            ai.invoke();
        } catch (Exception e) {

            log.error("URL:{} 异常 ",ai.getController().getViewPath(),e);

            if(isAjax(ai.getController())){
                Map<String,String> map  = new HashMap<>();
                map.put("result","error");
                ai.getController().renderJson(map);
            }else{
                ai.getController().renderError(500);
            }
        }
    }

}
