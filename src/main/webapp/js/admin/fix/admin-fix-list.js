layui.use(['table','jquery','layer','form','laydate'], function(){
    var form = layui.form;
    var $ = jQuery = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var laydate = layui.laydate;

    var realName = $("input[name='realName']").val();
    var userType = $("select[name='userType']").val();
    var url = "/admin/fix/list/"+userType+"?realName"+realName;

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
        var realName = $("input[name='realName']").val();
        var userType = $("select[name='userType']").val();
        var url = "/admin/fix/list/"+userType+"?realName="+realName;
        tabins.reload({url:url,page:1});
    });

    //监听工具条
    table.on('tool(fix-filter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象

        if(layEvent === 'detail'){ //查看

            fillData("#user-detail-window",data);

            //打开编辑框
            var openDetail = layer.open({
                type:1,
                title:'详细信息',
                content:$("#user-detail-window"),
                area:['600px', '500px'],
                anim: 3
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
        } else if(layEvent === 'edit'){ //编辑

            //打开编辑框
            var openEdit = layer.open({
                type:1,
                title:'详细信息',
                content:$("#user-edit-window"),
                area:['600px', '500px'],
                anim: 2
            });

            fillData("#user-edit-window",data);

            //表单初始赋值
            form.val('user-form-filter', {
                "userType":data.USER_TYPE
            });


            //其它预览数据赋值



            //监听提交
            form.on('submit(save-user)', function(record){
                var userType = record.field.userType;

                var userTypeName = $("#edit-user-type").find("option:selected").text();

                var id = data.ID;

                changeStatus(id,userType,function () {
                    layer.msg('修改成功');
                    obj.update({
                        USER_TYPE:userType,
                        USER_TYPE_NAME:userTypeName
                    });
                    layer.close(openEdit)

                },function () {
                    layer.msg('修改失败，请稍后重试');
                });

                return false;
            });
        }
    });

    function fillData(selector,data){


        $(selector).find(".userId").text(data.ID);
        $(selector).find(".realName").text(data.REAL_NAME);
        $(selector).find(".phone").text(data.PHONE);
        $(selector).find(".createDate").text(data.CREATE_DATE);
        $(selector).find(".userTypeName").text(data.USER_TYPE_NAME);



    }



    /**
     * 删除
     * @param id
     */
    function del(id,suc,fail){
        $.ajax({
            type: "get",
            dataType: 'json',
            url: "/admin/user/del/"+id,
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
     * 删除
     * @param id
     */
    function changeStatus(id,userType,suc,fail){
        $.ajax({
            type: "get",
            dataType: 'json',
            url: "/admin/user/change/"+id+"-"+userType,
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


});

