$(function() {
    /* $("#date1").calendar({
         onChange:function (p, values, displayValues){
                 console.log(p+">>"+values+">>"+displayValues);

         },
         closeOnSelect:true,
         inputReadOnly:true,
         closeByOutsideClick:true,
         dateFormat:'yyyy-mm-dd'
     });*/

    $("#start-time").datetimePicker({});
    $("#end1-time").datetimePicker({});


    $(".asset-btn-submit").click(function(){
        $.showLoading("保存中");
        var formObj = new Object();


        var id = $("#id").val();
        var roomId = $("#room-id").val();
        var startTime = $("#start-time").val();
        var endTime = $("#end1-time").val();
        formObj.roomId = roomId;
        formObj.id = id;
        formObj.startTime = startTime;
        formObj.endTime = endTime;

        if(startTime == endTime){
            $.toptip('开始时间和结束时间不能相同', 'warning');
            $.hideLoading();
            return;
        }


        var jsonStr = JSON.stringify(formObj);

        $.ajax({
            type: "post",
            dataType: 'json',
            url: '/book/save',
            data: {"book": jsonStr},
            success:function(res){
                if(res.success == true){
                    $.hideLoading();

                    if(res.data.length > 0 ){
                        var msg = "";
                        console.log("预订不成功，有冲突");
                        $.each(res.data,function(i,item){
                            msg += item.userName+" "+item.bookStartDate+" 至 "+item.bookEndDate +"\n </br> "
                        });
                        $.alert(msg, "提示:预订时间冲突！");
                        return;
                    }

                    $.toast("保存成功",500,function() {
                        location.href="/book/index?roomId="+roomId+"&time="+new Date().getTime()

                    });
                }else{
                    $.toast("保存失败，请稍后再试",1200);
                    $.hideLoading();
                    $(".asset-btn-submit").removeClass("weui-btn_disabled");
                }
            },error:function(){
                $.hideLoading();

            }
        });
    });
});
