var info1 = [
    {
        title: "办公室电脑",
        value: "1001"
    },
    {
        title: "办公室打印机",
        value: "1002"
    },
    {
        title: "笔记本",
        value: "1003"
    },
    {
        title: "手机",
        value: "1004"
    },
    {
        title: "班机展台",
        value: "1005"
    },
    {
        title: "ipad",
        value: "1006"
    },
    {
        title: "办公室IP电话",
        value: "1007"
    },
    {
        title: "班级多媒体",
        value: "1008"
    }
];


var info2 = [
    {
        title: "后1",
        value: "2001"
    },
    {
        title: "后2",
        value: "1002"
    }
];


/**
 * 绑定我要报修事件
 */
function addPopupListener(){
    $(".add-popup").click(function(){

        assemblyHandlerBars(new Object(),"#fix-add-template","#fix-add");

        $("#fix-add").popup();


        initFormHandler();
    });

}




/**
 * handlerbar渲染明细页面通用函数
 * @param data
 */
function assemblyHandlerBars(data,template1,target) {

    $(".just-for-delete").remove();

    var source = $(template1).html();
    var template = Handlebars.compile(source);

    var html = template(data);

    html = html.replace("just-for-d", "just-for-delete");

    //var ss =  $(html).find("#asset-sub-type");

    $(target).append(html);

}

/**
 * 通用打开明细页面后的一系列初始化操作
 * @param data
 */
function initFormHandler(){

    //单选select事件
    initAssetTypeSelect();

    //多选select事件
    initAssetSubTypeSelect();

    //初始化图片上传事件
    initUploaderImg();

    //保存事件
    initSubmitButton();
}

/**
 * 通过主键取详细报修信息
 * @param fixId
 * @param type
 */
function getFix(fixId,type){
    $.ajax({
        type: "get",
        dataType: 'json',
        url: "/fix/get/"+fixId,
        success: function (jsonObj) {
            if(jsonObj.success===true){

                if(type == "update"){
                    //编辑
                    assemblyHandlerBars(jsonObj.data,"#fix-add-template","#fix-add");

                    $("#fix-add").popup();
                    initFormHandler();

                    //2个select无法被选中
                    //$("#asset-type").attr("value",jsonObj.data.assetTypeName);
                    //$("#asset-type").attr("data-values",jsonObj.data.assetTypeName);

                }else{
                    //预览
                    assemblyHandlerBars(jsonObj.data,"#fix-detail-template","#fix-detail");
                    $("#fix-detail").popup();
                    $(".swiper-container").swiper();
                }
            }else{
                //失败
                $.toast("查询失败，请稍后重试！",1200);
            }

        }
    });
}


/**
 * 图片上传组件
 */
function initUploaderImg() {
    // 允许上传的图片类型
    var allowTypes = ['image/jpg', 'image/jpeg', 'image/png', 'image/gif'];
    // 1024KB，也就是 1MB
    var maxSize = 2048 * 2048;
    // 图片最大宽度
    var maxWidth = 10000;
    // 最大上传图片数量
    var maxCount = 6;
    $('#uploaderInput').on('change', function (event) {
        var files = event.target.files;
        //console.log(files);return false;
        // 如果没有选中文件，直接返回
        if (files.length === 0) {
            return;
        }

        for (var i = 0, len = files.length; i < len; i++) {
            var file = files[i];
            var reader = new FileReader();

            // 如果类型不在允许的类型范围内
            if (allowTypes.indexOf(file.type) === -1) {

                $.alert("该类型不允许上传！", "警告！");
                continue;
            }

            if (file.size > maxSize) {
                //$.weui.alert({text: '图片太大，不允许上传'});
                $.alert("图片太大，不允许上传", "警告！");
                continue;
            }

            if ($('.weui-uploader__file').length >= maxCount) {
                $.weui.alert({text: '最多只能上传' + maxCount + '张图片'});
                return;
            }
            reader.readAsDataURL(file);
            reader.onload = function (e) {
                //console.log(e);
                var img = new Image();
                img.src = e.target.result;
                img.onload = function () {
                    // 不要超出最大宽度
                    var w = Math.min(maxWidth, img.width);
                    // 高度按比例计算
                    var h = img.height * (w / img.width);
                    var canvas = document.createElement('canvas');
                    var ctx = canvas.getContext('2d');
                    // 设置 canvas 的宽度和高度
                    canvas.width = w;
                    canvas.height = h;
                    ctx.drawImage(img, 0, 0, w, h);

                    var base64 = canvas.toDataURL('image/jpeg',0.8);
                    //console.log(base64);
                    // 插入到预览区
                    var $preview = $('<li class="weui-uploader__file weui-uploader__file_status" style="background-image:url(' + img.src + ')"><div class="weui-uploader__file-content">0%</div></li>');
                    $('#uploaderFiles').append($preview);
                    var num = $('.weui-uploader__file').length;
                    $('.weui-uploader__info').text(num + '/' + maxCount);


                    var formData = new FormData();

                    formData.append("images", base64);
                    //console.log(img.src);
                    $.ajax({

                        url: "/attachment/upload",

                        type: 'POST',

                        data: formData,

                        contentType:false,

                        processData:false,

                        success: function(data){

                            $preview.removeClass('weui-uploader__file_status');
                            //返回当前上传的图片主键，用于点击保存以后，JS方便获取刚才上传的附件ID
                            $preview.append('<input type="hidden" class="attachmentId" name="attachmentId" value="'+data.attachmentId+'"/>');

                        },
                        error: function(xhr, type){
                            $.toast("上传失败！", function() {
                                //console.log('close');
                            });
                        }
                    });
                };
            };
        }
    });
}


function initAssetTypeSelect(){
    $("#asset-type").select({
        title: "选择物品分类",
       // input:assetType,
        items: [
            {
                title: "信息分类",
                value: "20"
            }
        ]
        /*onChange:function(){

            var type = $("#asset-type").attr("data-values");
            if(type=="10"){
                $("#asset-sub-type").select("update", {items:info2});

            }else if(type == "20"){
                $("#asset-sub-type").select("update", {items:info1});

            }

        }*/
    });
}

function initAssetSubTypeSelect() {
    $("#asset-sub-type").select({
        title: "选择物品子分类",
        multi: true,
       // input:assetSubType,
        items:info1
    });
}

function initSubmitButton(){

    $(".asset-btn-submit").click(function(){

        save();

    });

}
function save(){
    $(".asset-btn-submit").addClass("weui-btn_disabled");
    $.showLoading("保存中");

    var _data  = _assembleForm();

    $.ajax({
        type: "post",
        dataType: 'json',
        url: '/fix/save',
        data: {"fix": _data},
        success:function(res){
            if(res.result == "success"){
                $.hideLoading();
                $.toast("保存成功",800, function() {
                    $("#fix-add").hide();//奇怪
                    $.closePopup();
                    $('.weui-cell_swiped').swipeout('close');
                    $(document.body).pullToRefresh('triggerPullToRefresh');

                });
            }else{
                $.toast("保存失败，请稍后再试",2000);
                $.hideLoading();
                $(".asset-btn-submit").removeClass("weui-btn_disabled");
            }
        },error:function(){
            $.hideLoading();

        }
    });

}

function _assembleForm() {
    var formObj = new Object();

    var assetType = $("input[name='assetType']").attr("data-values");
    var assetSubType = $("input[name='assetSubType']").attr("data-values");
    var assetName = $("input[name='assetName']").val();
    var assetLocation = $("input[name='assetLocation']").val();
    var fixReason = $("#fixReason").val();
    var id = $("input[name='id']").val();
    var version = $("input[name='version']").val();

    formObj.assetType = assetType;
    formObj.assetSubType = assetSubType;
    formObj.assetName = assetName;
    formObj.assetLocation = assetLocation;
    formObj.fixReason = fixReason;
    formObj.id = id;

    //附件ID
    var attchmentIds = new Array();
    $(".attachmentId").each(function(){
        var attchmentId = $(this).val();
        attchmentIds.push(attchmentId);
    });

    formObj.attachmentIds = attchmentIds;
    var jsonStr = JSON.stringify(formObj);
    return jsonStr;
}