var maxId = 0;
var minId = 0;
var uploading = false;
var downloading = false;


$(function() {


    //去掉延迟点击事件
    //FastClick.attach(document.body);

    initPullRefresh();

    //立即刷新一次再说，安排
    $(document.body).pullToRefresh('triggerPullToRefresh');




});



/**
 * 点击修改
 */
function addListDetailListener(){
    console.log("准备绑定列表事件");

    $(".fix-record").off("click");

    $(".fix-record").on("click",function() {

        console.log("点击A标签。。。");
        var fixId = $(this).find("#fixId").val();

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

                $(".record-"+fixId).find(".statusName").text(statusName);

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

    $(document.body).pullToRefresh({
        onRefresh: function () {
            console.log("下拉触发。。。");
            /* 当下拉刷新触发的时候执行的回调 */
            loadingData();

        },
        onPull: function (percent) {
            /* 用户下拉过程中会触发，接收一个百分比表示用户下拉的比例 */

        }
        /* 下拉刷新的触发距离， 注意，如果你重新定义了这个值，那么你需要重载一部分CSS才可以，请参考下面的自定义样式部分 */

    });
}

function loadingData(){

    if(downloading){

        return;
    }

    downloading = true;
    if(!maxId){
        maxId = 0;
    }

    $.ajax({
        type: "get",
        dataType: 'json',
        url: "/repair/list/2-"+maxId,
        success: function (jsonObj) {
            if(jsonObj.success===true){

                if(jsonObj.data.length == 0){
                    $(document.body).pullToRefreshDone();
                    downloading = false;
                    return;
                }

                console.log(jsonObj.data);
                var source   = $("#fix-list-template").html();
                var template = Handlebars.compile(source);
                var context = jsonObj.data;//数据信息
                var html = template(context);
                $("#fix-asset-list").append(html);

                $.each(jsonObj.data,function(i,item){
                    if(parseInt(item.id) > maxId){
                        maxId = item.id;
                    }
                });

                downloading = false;
                //当下拉刷新的工作完成之后，需要重置下拉刷新的状态
                $(document.body).pullToRefreshDone();

                addListDetailListener();

                //document.getElementById("div1").innerHTML= html;
            }else{

            }

        }
    });

}

