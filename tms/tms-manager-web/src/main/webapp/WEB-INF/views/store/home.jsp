<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>TMS | 售票点管理</title>
    <%@include file="../include/css.jsp"%>
</head>
<body class="hold-transition skin-purple sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/navhead.jsp"%>

    <!-- =============================================== -->

    <jsp:include page="../include/sider.jsp">
        <jsp:param name="menu" value="ticket_store"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                售票点管理
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="box">
                <div class="box-body">
                    <form method="get" class="form-inline">
                        <input type="text" name="storeName" class="form-control" placeholder="售票点名称" value="${param.storeName}">
                        <input type="text" name="storeManager" class="form-control" placeholder="联系人" value="${param.storeManager}">
                        <input type="text" name="storeTel" class="form-control" placeholder="联系电话" value="${param.storeTel}">
                        <button class="btn btn-default">搜索</button>
                    </form>
                </div>
            </div>

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">销售点列表</h3>
                    <div class="box-tools">
                        <a href="/ticketstore/new" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增销售点</a>
                    </div>
                </div>

                <div class="box-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>销售点名称</th>
                            <th>联系人</th>
                            <th>联系电话</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${pageInfo.list}" var="store">
                            <tr>
                                <td><a href="/ticketstore/${store.id}">${store.storeName}</a></td>
                                <td>${store.storeManager}</td>
                                <td>${store.storeTel}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    共${pageInfo.total}条数据
                    <c:if test="${page.pages > 1}">
                    <ul id="pagination-demo" class="pagination pull-right"></ul>
                    </c:if>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>

<script src="/static/js/jquery-2.2.3.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/jquery.twbsPagination.js"></script>

<script>
    $(function () {

        //分页
        $('#pagination-demo').twbsPagination({
            totalPages: ${pageInfo.pages},
            visiblePages: 10,
            first:'首页',
            last:'末页',
            prev:'←',
            next:'→',
            href:"?productName="+encodeURIComponent('${param.productName}')
            +"&place="+encodeURIComponent('${param.place}')+"&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&typeId=${param.typeId}&p={{number}}"
        });
        //删除
        $(".delLink").click(function () {
            var id = $(this).attr("rel");
            if(confirm("确定要删除吗")) {
                window.location.href = "/product/"+id+"/del";
            }
        });
    });
</script>

</body>
</html>