Handlebars.registerHelper('if_eq', function(v1, v2, opts) {
    if(v1 == v2)
        return opts.fn(this);
    else
        return opts.inverse(this);
});


var minId = 999999999; //上拉显示历史数据的最小ID

var categoryJson = {};

$(function() {

    //FastClick.attach(document.body);

    //初始化下拉加载最新
   initPullRefresh();

   //初始化上拉加载历史数据
    initPushRefresh();

    loadding();


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

    $(".refresh-it").click(function () {
        loadding();
    });
}


function loadding(){

    $(".top-just-waiting").show();

    var url = "/fix/list/2-0";
    loadingData(url,function (jsonObj){
        $(".just-no-more").hide();
        if(jsonObj.data.length == 0){

            $(".just-no-data").show();
            $(".top-just-waiting").hide();

        }else{

            $(".just-no-data").hide();
            $(".top-just-waiting").hide();
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

            //当下拉刷新的工作完成之后，需要重置下拉刷新的状态

            addListDetailListener();
        }


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

