
Handlebars.registerHelper('if_eq', function(v1, v2, opts) {
    if(v1 == v2)
        return opts.fn(this);
    else
        return opts.inverse(this);
});

$(function () {

    initSearchBar();

    //绑定查询按钮
    initSearchBtn();

    //初始化上拉加载历史数据
    initPullRefresh();

    $("#weui-sb").pullToRefresh('triggerPullToRefresh');
});


function initPullRefresh(){

    var downloading = false;

    $("#weui-sb").pullToRefresh({
        onRefresh: function () {

            if(downloading){
                return;
            }

            downloading = true;


            loading();

            downloading = false;
        },
        onPull: function (percent) {
            /* 用户下拉过程中会触发，接收一个百分比表示用户下拉的比例 */

        }
        /* 下拉刷新的触发距离， 注意，如果你重新定义了这个值，那么你需要重载一部分CSS才可以，请参考下面的自定义样式部分 */

    });
}


function initDelete(){

    $(".delete-room").click(function(){
        var id = $(this).attr("fixid");
        $.confirm("是否确认删除该条记录？", function() {
            //点击确认后的回调函数
            $.ajax({
                type: "get",
                dataType: 'json',
                url: "/book/delete/"+id,
                success: function (jsonObj) {
                    if(jsonObj.success  == true){
                        $.toast("删除成功",1000);
                        loading();
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

function initSearchBar(){

    $("#search-date").calendar({
        closeOnSelect:true,
        inputReadOnly:true,
        closeByOutsideClick:true,
        dateFormat:'yyyy-mm-dd'
    });
}

function initSearchBtn() {

    $("#search-btn").click(function(){
       loading();
    });

}

function loading(){
    $(".no-data").hide();
    $(".loadding").show();
    var roomId = $("#room-id").val();
    var searchDate = $("#search-date").val();

    if(searchDate == ""){
        $.toptip('请选择查询日期', 'warning');
        return;
    }
    var reg = new RegExp( '-' , "g" );
    searchDate = searchDate.replace(reg,"");
    $(document.body).find(".book-record").remove();
    $.ajax({
        type: "get",
        dataType: 'json',
        url: "/book/list/"+roomId+"-"+searchDate,
        success: function (jsonObj) {
            if(jsonObj.success==true){

                if(jsonObj.data.length == 0){
                    $(".loadding").hide();
                    $(".no-data").show();
                    $("#weui-sb").pullToRefreshDone();
                    return;
                }
                var source   = $("#book-list-template").html();
                var template = Handlebars.compile(source);
                var context = jsonObj.data;//数据信息
                var html = template(context);

                $(".loadding").hide();

                $("#book-room-list").append(html);

                initDelete();
                $("#weui-sb").pullToRefreshDone();

                $('.weui-cell_swiped').swipeout();
            }else{
                $("#weui-sb").pullToRefreshDone();
                //失败
                $(".loadding").hide();
                $(".no-data").show();
            }

        }
    });

}