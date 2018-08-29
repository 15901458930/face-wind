
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



    loading();
});


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
    var reg = new RegExp( '-' , "g" )
    searchDate = searchDate.replace(reg,"");
    $(document.body).find(".book-record").remove();
    $.ajax({
        type: "get",
        dataType: 'json',
        url: "/book/list/"+roomId+"-"+searchDate,
        success: function (jsonObj) {
            if(jsonObj.success===true){

                if(jsonObj.data.length == 0){
                    $(".loadding").hide();
                    $(".no-data").show();

                    return;
                }
                console.log(jsonObj.data);
                var source   = $("#book-list-template").html();
                var template = Handlebars.compile(source);
                var context = jsonObj.data;//数据信息
                var html = template(context);
                console.log(html);
                $(".loadding").hide();

                $("#book-room-list").append(html);

                initDelete();

                $('.weui-cell_swiped').swipeout();
            }else{
                //失败
                $(".loadding").hide();
                $(".no-data").show();
            }

        }
    });

}