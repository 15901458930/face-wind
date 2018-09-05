layui.use(['table','jquery','layer','form'], function(){
    var form = layui.form;
    var $ = jQuery = layui.$;
    var table = layui.table;
    var layer = layui.layer;

    //重写ajax请求，session超时能正常跳转到登录页面
    JqueryAjaxExtention();


    var realName = $("input[name='realName']").val();
    var userConditionType = $("select[name='userConditionType']").val();
    var url = "/admin/user/list/"+userConditionType+"?realName"+realName;

    //第一个实例
    var tabins = table.render({
        elem: '#user-list'
        ,url: url //数据接口
        ,page: true //开启分页
        ,cols: [[ //表头
            {field: 'ID', title: 'ID', width:'10%', sort: true, fixed: 'left'},
            ,{field: 'USER_TYPE', title: 'hhh', hide:true}
            ,{field: 'BOOK_AUTHORITY', title: 'ccc', hide:true}
            ,{field: 'REAL_NAME',  width:'15%',title: '真实姓名'}
            ,{field: 'PHONE', title: '手机号' ,width:'15%'}
            ,{field: 'USER_TYPE_NAME', title: '用户类型', width:'15%'}
            ,{field: 'BOOK_AUTHORITY_NAME', title: '预约场地权限', width:'15%'}
            ,{field: 'CREATE_DATE', title: '创建时间', width:'10%'}
            ,{field: 'DO', title: '操作',toolbar: '#user-operation', width:'20%'}
        ]],
        loading:true
    });

    $("#search-btn").click(function(){
        var realName = $("input[name='realName']").val();
        var userConditionType = $("select[name='userConditionType']").val();
        var url = "/admin/user/list/"+userConditionType+"?realName="+realName;
        tabins.reload({url:url,page:{curr:1}});
    });

    //监听工具条
    table.on('tool(user-filter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
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
                "userType":data.USER_TYPE,
                "bookAuthority":data.BOOK_AUTHORITY
            });


            //其它预览数据赋值



            //监听提交
            form.on('submit(save-user)', function(record){

                var userType = record.field.userType;
                var userTypeName = $("#edit-user-type").find("option:selected").text();

                var bookAuthority =  record.field.bookAuthority;
                var bookAuthorityName = $("#edit-book-authority").find("option:selected").text();

                var id = data.ID;

                changeStatus(id,userType,bookAuthority,function () {
                    layer.msg('修改成功');
                    obj.update({
                        USER_TYPE:userType,
                        USER_TYPE_NAME:userTypeName,
                        BOOK_AUTHORITY:bookAuthority,
                        BOOK_AUTHORITY_NAME:bookAuthorityName

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
        $(selector).find(".bookAuthority").text(data.BOOK_AUTHORITY_NAME);
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
    function changeStatus(id,userType,bookAuthority,suc,fail){
        $.ajax({
            type: "get",
            dataType: 'json',
            url: "/admin/user/change/"+id+"-"+userType+"-"+bookAuthority,
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

