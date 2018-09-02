

layui.use(['table','jquery','layer','form','laydate','laytpl'], function(){
    var form = layui.form;
    var $ = jQuery = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var laytpl = layui.laytpl;

    var startDate = $("input[name='startDate']").val();
    var endDate = $("input[name='endDate']").val();
    var roomId = $("select[name='roomId']").val();
    var url = "/admin/book/list/"+roomId+"?startDate="+startDate+"&endDate="+endDate;

    //第一个实例
    var tabins = table.render({
        elem: '#book-list'
        ,url: url //数据接口
        ,page: true //开启分页
        ,cols: [[ //表头
            {field: 'ID', title: 'ID', width:'5%', fixed: 'left'}
            ,{field: 'ROOM_NAME', title: '场地',width:'10%'}
            ,{field: 'BOOK_DATE', title: '日期',width:'10%'}
            ,{field: 'SHORT_START_TIME', title: '开始时间',width:'10%'}
            ,{field: 'SHORT_END_TIME',  width:'20%',title: '结束时间',width:'10%'}
            ,{field: 'DEPART', title: '使用部门' ,width:'10%'}
            ,{field: 'USE_REASON', title: '使用事由', width:'10%'}
            ,{field: 'SPECIAL_REQUIRE', title: '特殊要求', width:'10%'}
            ,{field: 'RESPONSIBLE_USER', title: '责任人', width:'10%'}

            ,{field: 'DO', title: '操作',toolbar: '#book-operation',width:'15%'}
        ]],
        loading:true
    });

    laydate.render({ elem: '#startDate'});
    laydate.render({ elem: '#endDate'});


    $("#search-btn").click(function(){
        var startDate = $("input[name='startDate']").val();
        var endDate = $("input[name='endDate']").val();
        var roomId = $("select[name='roomId']").val();
        var url = "/admin/book/list/"+roomId+"?startDate="+startDate+"&endDate="+endDate;
        tabins.reload({url:url,page:{curr:1}});
    });

    $("#export-btn").click(function(){
        var startDate = $("input[name='startDate']").val();
        var endDate = $("input[name='endDate']").val();
        var roomId = $("select[name='roomId']").val();
        var url = "/admin/book/export/"+roomId+"?startDate="+startDate+"&endDate="+endDate;
        location.href= url;
    });

    //监听工具条
    table.on('tool(book-filter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象

        if(layEvent === 'detail'){ //查看

            var id = data.ID;

            get(id,function (data) {

                fillData("#book-detail-window",data);
                //打开编辑框
                var openDetail = layer.open({
                    type:1,
                    title:'详细信息',
                    content:$("#book-detail-window"),
                    area:['600px', '500px'],
                    anim: 3
                });
            },function () {
                
            });
            


            //do somehing
        } else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除该条记录吗？', function(index){
                del(data.ID,function () {
                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    layer.msg('删除成功');
                },function(){
                    layer.close(index);
                    layer.msg('删除失败，请稍后重试');
                });
            });
        }
    });

    function fillData(selector,data){


        $(selector).find(".bookId").text(data.id);
        $(selector).find(".roomName").text(data.roomName);
        $(selector).find(".bookDate").text(data.bookDate);
        $(selector).find(".bookStartTime").text(data.bookStartTime);
        $(selector).find(".bookEndTime").text(data.bookEndTime);

        $(selector).find(".depart").text(data.depart);
        $(selector).find(".responsibleUser").text(data.responsibleUser);
        $(selector).find(".useReason").text(data.useReason);
        $(selector).find(".device").text(data.device);
        $(selector).find(".needPhoto").text(data.needPhoto == 1 ? "是":"否");
        $(selector).find(".needCamera").text(data.needCamera == 1 ? "是":"否");
        $(selector).find(".specialRequire").text(data.specialRequire || "");
        $(selector).find(".createDate").text(data.createDate);
        $(selector).find(".createUserName").text(data.createUserName);



    }



    /**
     * 删除
     * @param id
     */
    function del(id,suc,fail){
        $.ajax({
            type: "get",
            dataType: 'json',
            url: "/admin/book/del/"+id,
            success: function (jsonObj) {
                if(jsonObj.success===true){
                    suc();//回调，先从数据库删除成功以后再删除列
                }else{
                   fail();
                }
            },error:function () {
                fail();
            }

        });
    }

    /**
     * 查找
     * @param id
     */
    function get(id,suc,fail){
        $.ajax({
            type: "get",
            dataType: 'json',
            url: "/admin/book/get/"+id,
            success: function (jsonObj) {
                if(jsonObj.success===true){
                    suc(jsonObj.data);//回调，先从数据库删除成功以后再删除列
                }else{
                    fail();
                }
            },error:function () {
                fail();
            }

        });
    }



});

