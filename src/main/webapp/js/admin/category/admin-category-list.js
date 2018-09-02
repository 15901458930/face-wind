layui.use(['table','jquery','layer','form'], function(){
    var form = layui.form;
    var $ = jQuery = layui.$;
    var table = layui.table;
    var layer = layui.layer;

    var searchName = $("input[name='searchName']").val();
    var parentId = $("select[name='parentId']").val();
    var url = "/admin/category/list/"+parentId+"?name"+searchName;

    //第一个实例
    var tabins = table.render({
        elem: '#category-list'
        ,url: url //数据接口
        ,page: true //开启分页
        ,cols: [[ //表头
            {field: 'ID', title: 'ID', sort: true, fixed: 'left', width:'10%'},
            ,{field: 'PARENT_NAME',title: '物品类型', width:'20%'}
            ,{field: 'NAME',title:'名称', width:'30%'}
            ,{field: 'CREATE_DATE', title: '创建时间', width:'20%'}
            ,{field: 'DO', title: '操作',toolbar: '#category-operation', width:'20%'}
            ,{field: 'MAIN_CATEGORY_ID',title:'',hide:true}
        ]],
        loading:true
    });

    $("#search-btn").click(function(){
        var searchName = $("input[name='searchName']").val();
        var parentId = $("select[name='parentId']").val();
        var url = "/admin/category/list/"+parentId+"?name="+searchName;
        tabins.reload({url:url,page:{curr:1}});
    });

    $("#add-btn").click(function(){

        openModify();
    });

    //监听工具条
    table.on('tool(category-filter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象

        if(layEvent === 'del'){ //删除
            layer.confirm('确定删除该条记录吗？', function(index){
                del(data.ID,function () {
                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.msg('删除成功');
                },function(message){
                    if(message){
                        layer.msg(message);
                    }else{
                        layer.msg('删除失败，请稍后重试');
                    }

                });
            });
        } else if(layEvent === 'edit'){
            openModify(obj);
        }
    });


    function openModify(obj){

        //打开编辑框
        var openEdit = layer.open({
            type:1,
            title:'详细信息',
            content:$("#category-edit-window"),
            area:['600px', '500px'],
            anim: 2
        });

        if(obj){
            //表单初始赋值
            form.val('category-form-filter', {
                "id":obj.data.ID||"",
                "mainCategoryId":obj.data.MAIN_CATEGORY_ID||"",
                "name":obj.data.NAME||""
            });
        }else{
            $("input[name='name']").val("");
            $("select[name='mainCategoryId']").val("");
        }


        //监听提交
        form.on('submit(save-category)', function(record){
            var id =  $("#id").val();
            var name = record.field.name;
            var mainCategoryId = record.field.mainCategoryId;

            var parentName = $("#main-category-id").find("option:selected").text();

            var obj ={
                id:id,
                name:name,
                mainCategoryId:mainCategoryId
            }
            var objStr = JSON.stringify(obj);
            saveCategory(objStr,function () {
                layer.msg('修改成功');
                if(id && obj){
                    obj.update({
                        NAME:name,
                        PARENT_NAME:parentName
                    });
                }
                layer.close(openEdit)

            },function () {

                layer.msg('修改失败，请稍后重试');


            });

            return false;
        });
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
                    fail("该物品类型已在报修单中关联数据，无法删除！");
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
    function saveCategory(objStr,suc,fail){
        $.ajax({
            type: "post",
            dataType: 'json',
            url: "/admin/category/save/",
            data:{"category":objStr},
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

