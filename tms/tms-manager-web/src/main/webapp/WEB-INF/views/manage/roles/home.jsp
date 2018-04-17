<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>TMS - 系统管理 - 角色管理</title>
    <%@include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/treegrid/css/jquery.treegrid.css">
    <style>
        #roles{
            background-color:#ECF0F5;
        }
        .td-left{
            border-radius: 7px 0px 0px 7px;
        }
        .td-right{
            border-radius: 0px 7px 7px 0px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">


    <%@include file="../../include/navhead.jsp"%>

    <!-- =============================================== -->

    <jsp:include page="../../include/sider.jsp">
        <jsp:param name="menu" value="manager_roles"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                角色管理
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">角色列表</h3>
                    <div class="box-tools">
                        <a href="/manage/roles/new" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增角色</a>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table tree">
                        <tbody>
                        <c:forEach items="${rolesList}" var="roles">
                            <tr class="bg-blue">
                                <td>
                                    角色名称：<strong>${roles.rolesName}</strong>
                                    <span class="pull-right">
                                            <a style="color: red;" href="/manage/roles/${roles.id}/edit"><i class="fa fa-pencil">编辑</i></a>
                                             <a style="color: red;" class="delLink" rel="${roles.id}" href="javascript:;"><i class="fa fa-trash">删除</i></a>
                                        </span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <c:forEach items="${roles.permissionList}" var="per">
                                        <i class="fa fa-circle"></i> ${per.permissionName}
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
</div>
<!-- ./wrapper -->

<%@include file="../../include/js.jsp"%>
<script src="/static/plugins/treegrid/js/jquery.treegrid.min.js"></script>
<script src="/static/plugins/treegrid/js/jquery.treegrid.bootstrap3.js"></script>
<script src="/static/plugins/layer/layer.js"></script>

<script>
    $(function () {
        $('.tree').treegrid();
        //删除
        $(".delLink").click(function () {
            var id = $(this).attr("rel");
            layer.confirm("确定要删除该角色？",function (index) {
                layer.close(index);
                $.get("/manage/roles/"+id+"/del").done(function (result) {
                    if(result.status == 'success') {
                        window.history.go(0);
                    } else {
                        layer.msg(result.message);
                    }
                }).error(function () {
                    layer.msg("服务器忙");
                });
            })
        });
    });
</script>
</body>
</html>