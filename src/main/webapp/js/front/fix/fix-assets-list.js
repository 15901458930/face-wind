var maxId = 0; //下拉显示的最新的ID
var minId = 0; //上拉显示历史数据的最小ID
var uploading = false; //是否在上拉
var downloading = false; //是否在下拉


$(function() {


    //去掉延迟点击事件 左滑删除和编辑，所以要注掉
    //FastClick.attach(document.body);

    //初始化下拉加载最新
   initPullRefresh();
    //loadingData();
    //立即刷新一次再说，先安排再说
   $(document.body).pullToRefresh('triggerPullToRefresh');

    //绑定+++++我要报修+++++事件
    addPopupListener();

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
        url: "/fix/list/2-"+maxId,
        success: function (jsonObj) {
            if(jsonObj.success===true){

                if(jsonObj.data.length == 0){
                    $(document.body).pullToRefreshDone();
                    downloading = false;
                    $(".loadding").hide();
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

                $('.weui-cell_swiped').swipeout();

                downloading = false;
                //当下拉刷新的工作完成之后，需要重置下拉刷新的状态
                $(document.body).pullToRefreshDone();
                addListDetailListener();
              //document.getElementById("div1").innerHTML= html;
            }else{
               //失败
            }

        }
    });

}

