var minId = 999999999; //上拉显示历史数据的最小ID

var categoryJson = {};

$(function() {

    //FastClick.attach(document.body);

    //初始化下拉加载最新
   initPullRefresh();

   //初始化上拉加载历史数据
    initPushRefresh();

    //立即刷新一次再说，先安排再说（不要绑定在body上，都是坑啊）
    $("#weui-sb").pullToRefresh('triggerPullToRefresh');

    //绑定+++++我要报修+++++事件
    addPopupListener();

    //添加报修页面下拉列表要用的json数据
    getCategoryJson();
});

function getCategoryJson() {

    $.ajax({
        type: "get",
        dataType: 'json',
        url: "/category/get/",
        success: function (jsonObj) {
            console.log(jsonObj);
            categoryJson = jsonObj;
        }
    });
}

/**
 * 点击修改
 */
function addListDetailListener(){

    $(".fix-record").off("click");

    $(".fix-record").on("click",function() {

        var fixId = $(this).find("#fixId").val();

        getFix(fixId,"preview");
    });


    $(".update-fix-asset").off("click");
    $(".update-fix-asset").on("click",function() {

        console.log("点击A标签。。。");
        var fixId = $(this).attr("fixid");

        console.log("fixId"+fixId);

        getFix(fixId,"update");
    });

    $(".delete-fix-asset").off("click");
    $(".delete-fix-asset").on("click",function() {

        console.log("点击A标签。。。");
        var fixId = $(this).attr("fixid");

        $.confirm("是否确认删除该条记录？", function() {
            //点击确认后的回调函数
            $.ajax({
                type: "get",
                dataType: 'json',
                url: "/fix/delete/"+fixId,
                success: function (jsonObj) {
                    if(jsonObj.success  == true){
                        $.toast("删除成功",1000);
                        $(".record-"+fixId).remove();
                    }else{
                        $.toast("删除失败，请稍后再试",1000);
                    }

                }
            });
        }, function() {
            //点击取消后的回调函数
        });

    });
}

/**
 * 上拉加载历史记录
 */
function initPushRefresh(){


    var uploadding = false;

    $(document.body).infinite(1).on("infinite", function() {


        if(uploadding || minId == -1){
            $(".just-no-more").show();
            return;
        }

        uploadding = true;


        var url = "/fix/list/1-"+minId;

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

            //$(".will-be-delete").remove();
            $(".just-waiting").hide();
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

            uploadding = false;
            //当下拉刷新的工作完成之后，需要重置下拉刷新的状态
            $("#weui-sb").pullToRefreshDone();

            addListDetailListener();
        });

    //     setTimeout(function() {
    //         $("#list").append("<p> 我是新加载的内容 </p>");
    //         loading = false;
    //     }, 1500);   //模拟延迟
    });
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
            var url = "/fix/list/2-0";
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

