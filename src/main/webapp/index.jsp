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
    <title>Title</title>
    <link rel="stylesheet" href="<%=basePath%>layui/css/layui.css">
</head>
<body>

<script src="<%=basePath%>layui/layui.all.js"></script>
<script src="<%=basePath%>js/jquery.min.js"></script>
<script>
    window.location.href = '<%=basePath%>excel/jumpShow';
</script>
</body>
</html>
