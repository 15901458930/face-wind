layui.use(['table','jquery','layer','form'], function(){
    var form = layui.form;
    var $ = jQuery = layui.$;
    var table = layui.table;
    var layer = layui.layer;

    var name = $("input[name='name']").val();
    var parentId = $("select[name='parentId']").val();
    var url = "/admin/category/list/"+parentId+"?name"+name;

    //第一个实例
    var tabins = table.render({
        elem: '#category-list'
        ,url: url //数据接口
        ,page: true //开启分页
        ,cols: [[ //表头
            {field: 'ID', title: 'ID', sort: true, fixed: 'left', width:'10%'},
            ,{field: 'NAME',title: '名称', width:'30%'}
            ,{field: 'PARENT_NAME',title: '物品类型', width:'20%'}
            ,{field: 'CREATE_DATE', title: '创建时间', width:'20%'}
            ,{field: 'DO', title: '操作',toolbar: '#category-operation', width:'20%'}
        ]],
        loading:true
    });

    $("#search-btn").click(function(){
        var name = $("input[name='name']").val();
        var parentId = $("select[name='parentId']").val();
        var url = "/admin/category/list/"+parentId+"?name="+name;
        tabins.reload({url:url,page:{curr:1}});
    });

    //监听工具条
    table.on('tool(category-filter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象

        if(layEvent === 'detail'){ //查看

            fillData("#category-detail-window",data);

            //打开编辑框
            var openDetail = layer.open({
                type:1,
                title:'详细信息',
                content:$("#category-detail-window"),
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
                content:$("#category-edit-window"),
                area:['600px', '500px'],
                anim: 2
            });

            fillData("#category-edit-window",data);

            //表单初始赋值
            form.val('category-form-filter', {
                "userType":data.USER_TYPE
            });


            //其它预览数据赋值



            //监听提交
            form.on('submit(save-category)', function(record){
                var userType = record.field.userType;

                var userTypeName = $("#edit-category-type").find("option:selected").text();

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


        $(selector).find(".id").text(data.ID);
        $(selector).find(".name").text(data.NAME);
        $(selector).find(".shortName").text(data.SHORT_NAME);
        $(selector).find(".createDate").text(data.CREATE_DATE);
        $(selector).find(".mainCategoryName").text(data.MAIN_CATEGORY_ID);
    }



    /**
     * 删除
     * @param id
     */
    function del(id,suc,fail){
        $.ajax({
            type: "get",
            dataType: 'json',
            url: "/admin/category/del/"+id,
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
            url: "/admin/category/change/"+id+"-"+userType,
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

