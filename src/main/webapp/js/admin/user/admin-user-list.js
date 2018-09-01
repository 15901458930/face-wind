layui.use(['table','jquery'], function(){
    var $ = jQuery = layui.$;
    var table = layui.table;


    var realName = $("input[name='realName']").val();
    var userType = $("select[name='userType']").val();
    var url = "/admin/user/list/"+userType+"?realName"+realName;

    //第一个实例
    var tabins = table.render({
        elem: '#user-list'
        ,url: url //数据接口
        ,page: true //开启分页
        ,cols: [[ //表头
            {field: 'ID', title: 'ID',  sort: true, fixed: 'left'}
            ,{field: 'REAL_NAME', title: '真实姓名'}
            ,{field: 'EMAIL', title: '邮箱', sort: true}
            ,{field: 'PHONE', title: '手机号'}
            ,{field: 'USER_TYPE', title: '用户类型'}
        ]]
    });

    $("#search-btn").click(function(){
        var realName = $("input[name='realName']").val();
        var userType = $("select[name='userType']").val();
        var url = "/admin/user/list/"+userType+"?realName="+realName;
        tabins.reload({url:url,page:1});
    });

});