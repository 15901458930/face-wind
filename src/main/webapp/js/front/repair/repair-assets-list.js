Handlebars.registerHelper('if_eq', function(v1, v2, opts) {
    if(v1 == v2)
        return opts.fn(this);
    else
        return opts.inverse(this);
});
Handlebars.registerHelper('if_n_eq', function(v1, v2, opts) {
    if(v1 != v2)
        return opts.fn(this);
    else
        return opts.inverse(this);
});

var minId = 99999999;

$(function() {


    //去掉延迟点击事件
    //FastClick.attach(document.body);

    initPullRefresh();

    //初始化上拉加载历史数据
    initPushRefresh();

    //立即刷新一次再说，安排
    $("#weui-sb").pullToRefresh('triggerPullToRefresh');

});

/**
 * 初始化明细页面预览图片事件
 */
function initViewHandler(){
    //点击预览大图
    $("#detailFiles").on("click", "li", function() {
        index = $(this).index();
        $("#v_galleryImg").attr("style", this.getAttribute("style"));
        $("#v_gallery").fadeIn(100);
    });
    //点击取消预览大图
    $("#v_gallery").on("click", function() {
        $("#v_gallery").fadeOut(100);
    });
}


/**
 * 上拉加载历史记录
 */
function initPushRefresh(){


    var uploadding = false;

    $(document.body).infinite(1).on("infinite", function() {


        if(uploadding || minId == -1){
            return;
        }

        uploadding = true;


        var url = "/repair/list/1-"+minId;

        $(".just-waiting").show();

        loadingData(url,function(jsonObj){

            if(jsonObj.data.length == 0){

                if(minId < 999999999){
                    //不等于原始值，并且还没取到数据，就认为已经没有历史 数据 了，后续不再取
                    minId = -1;
                }

                uploadding = false;

                $(".just-waiting").hide();
                $(".just-no-more").show();
                return;
            }

            $(".just-waiting").hide();
            var source   = $("#fix-list-template").html();
            var template = Handlebars.compile(source);
            var context = jsonObj.data;//数据信息
            var html = template(context);

            var reg = new RegExp( 'will-be-d' , "g" );
            html = html.replace(reg, "will-be-delete");

            $("#fix-asset-list").append(html);

            $.each(jsonObj.data,function(i,item){
                if(parseInt(item.id) < minId){
                    minId = item.id;
                }
            });

            $('.weui-cell_swiped').swipeout();

            uploadding = false;
            //当下拉刷新的工作完成之后，需要重置下拉刷新的状态

            addListDetailListener();
        });
    });
}


/**
 * 点击修改
 */
function addListDetailListener(){
    console.log("准备绑定列表事件");

    $(".fix-record").off("click");

    $(".fix-record").on("click",function() {


        var fixId = $(this).find("#fixId").val();

        console.log("fixId"+fixId);

        getFix(fixId,"preview");
    });


    $(".update-fix-asset").off("click");
    $(".update-fix-asset").on("click",function() {


        var fixId = $(this).attr("fixId");

        console.log("fixId"+fixId);

        getFix(fixId,"preview");
    });

}

function getFix(fixId,type){
    $.ajax({
        type: "get",
        dataType: 'json',
        url: "/fix/get/"+fixId,
        success: function (jsonObj) {
            if(jsonObj.success===true){

            //编辑
            assemblyHandlerBars(jsonObj.data,"#repair-add-template","#repair-add");

            $("#repair-add").popup();

            bindChangeStatusListener();

            bindAcceptOrderListener();

            initViewHandler();


            }else{
                //失败

            }

        }
    });
}


function bindChangeStatusListener(){
    $(".change-status").click(function(){
        var fixId = $(this).attr("data-id");
        console.log("<>>><><>"+fixId);
        $.actions({
            actions: [{
                text: "处理中",
                onClick: function() {
                    changeStatusByAjax(fixId,2,"处理中");                }
            },{
                text: "没有问题",
                onClick: function() {
                    changeStatusByAjax(fixId,3,"没有问题");                  }
            },{
                text: "上报领导",
                onClick: function() {
                    changeStatusByAjax(fixId,4,"上报领导");                  }
            },{
                text: "慢慢等待",
                onClick: function() {
                    changeStatusByAjax(fixId,5,"慢慢等待");                  }
            },{
                text: "联系厂商",
                onClick: function() {
                    changeStatusByAjax(fixId,6,"联系厂商");                  }
            },{
                text: "处理完成",
                onClick: function() {
                    changeStatusByAjax(fixId,7,"处理完成");                  }
            }]
        });
    });

}

/**
 * 接单
 */
function bindAcceptOrderListener(){
    $(".accept-order").click(function(){
        var fixId = $(this).attr("data-id");
        var ver = $(this).attr("data-ver");


        $.confirm("您确定接此报修吗?", "确认?", function() {
            $.ajax({
                type: "get",
                dataType: 'json',
                url: "/fix/accept/"+fixId+"-"+ver,
                success: function (jsonObj) {
                    if(jsonObj.success===true){

                        $(".just-for-delete").remove();
                        $("#repair-add").hide();//奇怪

                        $.closePopup();

                        $.toast("接单成功",1200);

                        $(".record-"+fixId).find("#red-status").text("处理中");

                    }else{

                        $.alert(jsonObj.message, "提示",function(){
                            $.closePopup();
                        });

                    }

                }
            });
        }, function() {
            //取消操作
        });


    });

}

function changeStatusByAjax(fixId,status,statusName){
    $.ajax({
        type: "get",
        dataType: 'json',
        url: "/fix/change/"+fixId+"-"+status,
        success: function (jsonObj) {
            if(jsonObj.success===true){

                $(".just-for-delete").remove();
                $("#repair-add").hide();//奇怪
                $.closePopup();


                $.toast("修改成功",1200);
                $(".record-"+fixId).find("#red-status").text(statusName);

                //bindChangeStatusListener(fixId);

            }else{
                //失败
            }

        }
    });
}

/**
 * 通用打开明细页面popup
 * @param data
 */
function assemblyHandlerBars(data,template,target) {
    var source = $(template).html();
    var template = Handlebars.compile(source);

    var html = template(data);

    html = html.replace("just-for-d", "just-for-delete");

    //var ss =  $(html).find("#asset-sub-type");

    $(target).append(html);

}

function initPullRefresh(){

    var downloading = false;

    $("#weui-sb").pullToRefresh({
        onRefresh: function () {

            if(downloading){
                return;
            }

            downloading = true;

            /* 当下拉刷新触发的时候执行的回调 */
            var url = "/repair/list/2-0";
            loadingData(url,function (jsonObj){
                $(".just-no-more").hide();
                if(jsonObj.data.length == 0){
                    $("#weui-sb").pullToRefreshDone();
                    downloading = false;

                    $(".just-no-data").show();
                    $(".loadding").hide();

                }else{

                    $(".just-no-data").hide();
                    $(".will-be-delete").remove();

                    console.log(jsonObj.data);
                    var source   = $("#fix-list-template").html();
                    var template = Handlebars.compile(source);
                    var context = jsonObj.data;//数据信息
                    var html = template(context);

                    var reg = new RegExp( 'will-be-d' , "g" );
                    html = html.replace(reg, "will-be-delete");

                    $("#fix-asset-list").append(html);

                    $.each(jsonObj.data,function(i,item){
                        if(parseInt(item.id) < minId){
                            minId = item.id;
                        }
                    });

                    $('.weui-cell_swiped').swipeout();

                    downloading = false;
                    //当下拉刷新的工作完成之后，需要重置下拉刷新的状态
                    $("#weui-sb").pullToRefreshDone();

                    addListDetailListener();
                }


            });

        },
        onPull: function (percent) {
            /* 用户下拉过程中会触发，接收一个百分比表示用户下拉的比例 */

        }
        /* 下拉刷新的触发距离， 注意，如果你重新定义了这个值，那么你需要重载一部分CSS才可以，请参考下面的自定义样式部分 */

    });
}





function loadingData(url,callback){

    $.ajax({
        type: "get",
        dataType: 'json',
        url: url,
        success: function (jsonObj) {
            if(jsonObj.success===true){
                callback(jsonObj);
                //document.getElementById("div1").innerHTML= html;
            }else{
                //失败
            }

        }
    });

}

