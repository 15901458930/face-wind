layui.use(['table','jquery','layer','form','laydate'], function(){
    var form = layui.form;
    var $ = jQuery = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var laydate = layui.laydate;

    //重写ajax请求，session超时能正常跳转到登录页面
    JqueryAjaxExtention();

    var startDate = $("input[name='startDate']").val();
    var endDate = $("input[name='endDate']").val();
    var status = $("select[name='status']").val();
    var url = "/admin/fix/list/"+status+"?startDate="+startDate+"&endDate="+endDate;

    //第一个实例
    var tabins = table.render({
        elem: '#fix-list'
        ,url: url //数据接口
        ,page: true //开启分页
        ,cols: [[ //表头
            {field: 'ID', title: 'ID', width:'5%', fixed: 'left'}
            ,{field: 'ASSET_TYPE_NAME', title: '物品分类',width:'10%'}
            ,{field: 'FIX_REASON', title: '报修原因',width:'15%'}
            ,{field: 'ASSET_LOCATION',  width:'20%',title: '物品位置',width:'15%'}
            ,{field: 'STATUS_NAME', title: '状态' ,width:'15%'}
            ,{field: 'APPLY_DATE', title: '申请时间', width:'15%'}
            ,{field: 'APPLY_USER_NAME', title: '申请人', width:'10%'}
            ,{field: 'DO', title: '操作',toolbar: '#fix-operation',width:'15%'}
        ]],
        loading:true
    });

    laydate.render({ elem: '#startDate'});
    laydate.render({ elem: '#endDate'});


    $("#search-btn").click(function(){
        var startDate = $("input[name='startDate']").val();
        var endDate = $("input[name='endDate']").val();
        var status = $("select[name='status']").val();
        var url = "/admin/fix/list/"+status+"?startDate="+startDate+"&endDate="+endDate;
        tabins.reload({url:url,page:{curr:1}});
    });

    $("#export-btn").click(function(){
        var startDate = $("input[name='startDate']").val();
        var endDate = $("input[name='endDate']").val();
        var status = $("select[name='status']").val();
        var url = "/admin/fix/export/"+status+"?startDate="+startDate+"&endDate="+endDate;
        location.href= url;
    });

    //监听工具条
    table.on('tool(fix-filter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象

        if(layEvent === 'detail'){ //查看

            var id = data.ID;

            get(id,function (data) {

                fillData("#fix-detail-window",data);
                //打开编辑框
                var openDetail = layer.open({
                    type:1,
                    title:'详细信息',
                    content:$("#fix-detail-window"),
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


        $(selector).find(".fixId").text(data.id);
        $(selector).find(".assetSubTypeName").text(data.assetSubTypeName);
        $(selector).find(".assetTypeName").text(data.assetTypeName);
        $(selector).find(".assetName").text(data.assetName);
        $(selector).find(".assetLocation").text(data.assetLocation);
        $(selector).find(".belongCampusName").text(data.belongCampusName);
        $(selector).find(".applyDate").text(data.applyDate);
        $(selector).find(".applyUserName").text(data.applyUserName);
        $(selector).find(".fixReason").text(data.fixReason);
        $(selector).find(".status").text(data.statusName);
        $(selector).find(".fixUserName").text(data.fixUserName || "");
        $(selector).find(".startFixDate").text(data.startFixDate);



    }



    /**
     * 删除
     * @param id
     */
    function del(id,suc,fail){
        $.ajax({
            type: "get",
            dataType: 'json',
            url: "/admin/fix/del/"+id,
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
            url: "/admin/fix/get/"+id,
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

