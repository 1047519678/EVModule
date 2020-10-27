<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <title>Excel导入</title>
    <link rel="stylesheet" href="<%=basePath%>layui/css/layui.css">
    <style>
        .layui-table-tool {
            background-color: #fefffd;
            min-height: 20px;
        }
    </style>
</head>
<body>
<div style="flex: 1;padding: 10px">
        <div class="layui-upload" style="text-align: center">
            <div class="layui-inline" style="float: left;">
                <button type="button" class="layui-btn layui-btn-normal" id="uploadExample">选择文件</button>
                <button type="button" class="layui-btn" id="uploadExample1">开始上传</button>
            </div>
            <%--<div class="layui-inline" style="color:red;font-size: 20px">上传失败列表</div>--%>
        </div>

    <table class="layui-hide" id="test" lay-filter="test"></table>
</div>
<%-- 隐藏区域 start --%>

<%-- 隐藏区域 end --%>
<script src="<%=basePath%>js/jquery.min.js"></script>
<script src="<%=basePath%>layui/layui.all.js"></script>
<script>
    layui.use(['upload', 'table'], function () {
        var upload = layui.upload;
        var layer = layui.layer;
        var $ = layui.jquery;
        var table = layui.table;

        /*table.render({
            elem: '#test'
            , cellMinWidth: 50
            , toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            , defaultToolbar: []
            , data: []
            , title: '上传失败列表'
            , cols: [[
                 {field: 'partNo', title: '料号',minWidth: 180}
                , {field: 'selfSn', title: '物料条码',minWidth: 220}
                , {field: 'lotNo', title: 'LOTNO',minWidth: 120}
                , {field: 'poNo', title: '订单',minWidth: 120}
                , {field: 'itemNo', title: '订单项次',minWidth: 85}
                , {field: 'outCarton', title: '外箱箱号',minWidth: 90}
                , {field: 'incomingQty', title: '数量',minWidth: 70}
                , {field: 'sumQty', title: '总数量',minWidth: 70}
                , {field: 'sumPack', title: '总件数',minWidth: 70}
                , {field: 'dateCode', title: '周期',minWidth: 85}
                , {field: 'reason', title: '失败原因',minWidth: 205}
            ]]
            , page: true
            , limit: 15
            , limits: [15, 20, 40, 60, 80, 100, 500, 1000]
        });*/
        //执行实例
        var uploadInst = upload.render({
            elem: '#uploadExample'
            , url: '<%=basePath%>excel/read'
            , accept: 'file'
            , acceptMime: 'application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            , exts: 'xlsx|xls'
            , auto: false
            , bindAction: '#uploadExample1'
            , before: function (obj) {
                var index = layer.load();
            }
            , done: function (res) {
                layer.close(layer.index);
                layer.msg(res.msg, {
                    time: 5000, //5s后自动关闭
                    icon: 1
                });

                console.info(res);
                /*table.reload('test', {
                    elem: '#test'
                    , data: res.dataList
                });*/
                uploadInst.reload();
            }
            , error: function (res) {
                //请求异常回调
                console.error(res);
            }
        });
    });
</script>

</body>
</html>
