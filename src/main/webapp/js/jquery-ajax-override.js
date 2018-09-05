

function JqueryAjaxExtention(){
    jQuery(function($){
        // 备份jquery的ajax方法
        var _ajax=$.ajax;
        // 重写ajax方法，先判断登录在执行success函数
        $.ajax=function(opt){
            var _success = opt && opt.success || function(a, b){};
            var _complete = opt && opt.complete || function(a, b){};
            var _opt = $.extend(opt, {
                complete:function(data, textStatus){
                    if(typeof data.responseText == "string"){
                        //console.log(data);
                        if(data.responseText.indexOf("550E8400-E29B-1D4-A716-16655440000WP") != -1) {
                            parent.location.href= "/admin/login?message=登录超时，请重新登录！";
                            return;
                        }
                    }
                    // 如果后台将请求重定向到了登录页，则data里面存放的就是登录页的源码，这里需要找到data是登录页的证据(标记)
                    _complete(data, textStatus);
                }
            });
            return _ajax(_opt);
        };
    });
}
