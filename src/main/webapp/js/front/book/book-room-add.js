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

        var depart = $("#depart").val();
        var responsibleUser = $("#responsibleUser").val();
        var useReason = $("#useReason").val();
        var device = $("#device").val();
        var needCamera = $("#needCamera").prop("checked");
        var needPhoto = $("#needPhoto").prop("checked");
        var specialRequire = $("#specialRequire").val();

        formObj.roomId = roomId;
        formObj.id = id;
        formObj.startTime = startTime;
        formObj.endTime = endTime;
        formObj.depart = depart;
        formObj.responsibleUser = responsibleUser;
        formObj.useReason = useReason;
        formObj.device = device;
        formObj.needCamera = needCamera;
        formObj.needPhoto = needPhoto;
        formObj.specialRequire = specialRequire;

        if(!valid(formObj)){
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
                            msg += item.userName+" "+item.bookStartTime+" 至 "+item.bookEndTime +"\n </br> "
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

function CompareDate(d1,d2) {
    return ((new Date(d1.replace(/-/g,"\/"))) < (new Date(d2.replace(/-/g,"\/"))));
}


function IsSameDay(d1,d2) {

    var s1 = new Date(d1.replace(/-/g,"\/"));
    var s2 = new Date(d2.replace(/-/g,"\/"));

    if((s1.getFullYear() == s2.getFullYear()) && (s1.getMonth() == s2.getMonth()) && (s1.getDate() == s2.getDate())){
        return true;
    }
    return false;

}

function valid(data){

    if(!CompareDate(data.startTime,data.endTime)){
        $.toptip('结束时间必须大于开始时间',2000,'warning');
        $("#book-start-valid").addClass("weui-cell_warn");
        $("#book-end-valid").addClass("weui-cell_warn");
        return false;
    }


    if(!IsSameDay(data.startTime,data.endTime)){
        $.toptip('开始时间和结束时间必须是同一天',2000,'warning');
        $("#book-start-valid").addClass("weui-cell_warn");
        $("#book-end-valid").addClass("weui-cell_warn");
        return false;
    }


    //先清掉所有的提示
    $(".weui-cell").removeClass("weui-cell_warn");

    if(!data.startTime || !data.endTime || !data.depart || !data.responsibleUser || !data.useReason || !data.device){
        //先判断非空选项
        if(!data.startTime){
            $("#book-start-valid").addClass("weui-cell_warn");
        }
        if(!data.endTime){
            $("#book-end-valid").addClass("weui-cell_warn");
        }
        if(!data.depart){
            $("#depart-valid").addClass("weui-cell_warn");
        }
        if(!data.responsibleUser){
            $("#responsible-user-valid").addClass("weui-cell_warn");
        }
        if(!data.useReason){
            $("#use-reason-valid").addClass("weui-cell_warn");
        }
        if(!data.device){
            $("#device-valid").addClass("weui-cell_warn");
        }

        $.toptip('带红色星号为必填项',2000,'warning');
        return false;

    }else{
        //如果都不为空，继续判断有没有超过字符长度
        var warnnn = false;
        if(data.specialRequire && data.specialRequire.length > 200){
            warnnn = true;
            $("#specia-require-valid").addClass("weui-cell_warn");
        }
        if(data.device.length > 200){
            warnnn = true;
            $("#device-valid").addClass("weui-cell_warn");
        }
        if(data.depart.length > 50){
            warnnn = true;
            $("#depart-valid").addClass("weui-cell_warn");
        }
        if(data.responsibleUser.length > 50){
            warnnn = true;
            $("#responsible-user-valid").addClass("weui-cell_warn");
        }
        if(data.useReason.length > 50){
            warnnn = true;
            $("#use-reason-valid").addClass("weui-cell_warn");
        }
        if(warnnn){
            $.toptip('请不要超过限制长度',2000,'warning');
            return false;
        }
    }






    return true;



}