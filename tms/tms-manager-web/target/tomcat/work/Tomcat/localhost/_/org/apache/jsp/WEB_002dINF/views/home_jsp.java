/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2018-04-17 03:25:59 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class home_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(3);
    _jspx_dependants.put("/WEB-INF/views/include/css.jsp", Long.valueOf(1523536674684L));
    _jspx_dependants.put("/WEB-INF/views/include/navhead.jsp", Long.valueOf(1523536699976L));
    _jspx_dependants.put("/WEB-INF/views/include/js.jsp", Long.valueOf(1523536685433L));
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("    <meta charset=\"utf-8\">\r\n");
      out.write("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n");
      out.write("    <title>AdminLTE 2 | Blank Page</title>\r\n");
      out.write("    ");
      out.write("\r\n");
      out.write("<!-- Tell the browser to be responsive to screen width -->\r\n");
      out.write("<meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n");
      out.write("<!-- Bootstrap 3.3.6 -->\r\n");
      out.write("<link rel=\"stylesheet\" href=\"/static/bootstrap/css/bootstrap.min.css\">\r\n");
      out.write("<!-- Font Awesome -->\r\n");
      out.write("<link rel=\"stylesheet\" href=\"/static/dist/css/font-awesome.min.css\">\r\n");
      out.write("<!-- Theme style -->\r\n");
      out.write("<link rel=\"stylesheet\" href=\"/static/dist/css/AdminLTE.min.css\">\r\n");
      out.write("<!-- AdminLTE Skins. Choose a skin from the css/skins\r\n");
      out.write("folder instead of downloading all of them to reduce the load. -->\r\n");
      out.write("<link rel=\"stylesheet\" href=\"/static/dist/css/skins/_all-skins.min.css\">\r\n");
      out.write("\r\n");
      out.write("<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\r\n");
      out.write("<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->\r\n");
      out.write("<!--[if lt IE 9]>\r\n");
      out.write("<script src=\"https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js\"></script>\r\n");
      out.write("<script src=\"https://oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>\r\n");
      out.write("<![endif]-->");
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("<body class=\"hold-transition skin-purple sidebar-mini\">\r\n");
      out.write("<!-- Site wrapper -->\r\n");
      out.write("<div class=\"wrapper\">\r\n");
      out.write("\r\n");
      out.write("    ");
      out.write("\r\n");
      out.write("<!-- 顶部导航栏部分 -->\r\n");
      out.write("<header class=\"main-header\">\r\n");
      out.write("    <!-- Logo -->\r\n");
      out.write("    <a href=\"/home\" class=\"logo\">\r\n");
      out.write("        <!-- mini logo for sidebar mini 50x50 pixels -->\r\n");
      out.write("        <span class=\"logo-mini\"><b>TMS</b></span>\r\n");
      out.write("        <!-- logo for regular state and mobile devices -->\r\n");
      out.write("        <span class=\"logo-lg\"><b>TMS</b>系统</span>\r\n");
      out.write("    </a>\r\n");
      out.write("    <!-- Header Navbar: style can be found in header.less -->\r\n");
      out.write("    <nav class=\"navbar navbar-static-top\">\r\n");
      out.write("        <!-- Sidebar toggle button-->\r\n");
      out.write("        <a href=\"#\" class=\"sidebar-toggle\" data-toggle=\"offcanvas\" role=\"button\">\r\n");
      out.write("            <span class=\"sr-only\">Toggle navigation</span>\r\n");
      out.write("            <span class=\"icon-bar\"></span>\r\n");
      out.write("            <span class=\"icon-bar\"></span>\r\n");
      out.write("            <span class=\"icon-bar\"></span>\r\n");
      out.write("        </a>\r\n");
      out.write("\r\n");
      out.write("        <div class=\"navbar-custom-menu\">\r\n");
      out.write("            <ul class=\"nav navbar-nav\">\r\n");
      out.write("                <!-- User Account: style can be found in dropdown.less -->\r\n");
      out.write("                <li class=\"dropdown user user-menu\">\r\n");
      out.write("                    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">\r\n");
      out.write("                        <img src=\"/static/dist/img/default-avatar.png\" class=\"user-image\" alt=\"User Image\">\r\n");
      out.write("                        <span class=\"hidden-xs\">李美苏</span>\r\n");
      out.write("                    </a>\r\n");
      out.write("                    <ul class=\"dropdown-menu\">\r\n");
      out.write("                        <!-- User image -->\r\n");
      out.write("                        <li class=\"user-header\">\r\n");
      out.write("                            <img src=\"/static/dist/img/default-avatar.png\" class=\"img-circle\" alt=\"User Image\">\r\n");
      out.write("\r\n");
      out.write("                            <p>\r\n");
      out.write("                                李美苏\r\n");
      out.write("                                <small>海外事业部</small>\r\n");
      out.write("                            </p>\r\n");
      out.write("                        </li>\r\n");
      out.write("                        <!-- Menu Footer-->\r\n");
      out.write("                        <li class=\"user-footer\">\r\n");
      out.write("                            <div class=\"pull-left\">\r\n");
      out.write("                                <a href=\"#\" class=\"btn btn-default btn-flat\">个人设置</a>\r\n");
      out.write("                            </div>\r\n");
      out.write("                            <div class=\"pull-right\">\r\n");
      out.write("                                <a href=\"#\" class=\"btn btn-default btn-flat\">安全退出</a>\r\n");
      out.write("                            </div>\r\n");
      out.write("                        </li>\r\n");
      out.write("                    </ul>\r\n");
      out.write("                </li>\r\n");
      out.write("            </ul>\r\n");
      out.write("        </div>\r\n");
      out.write("    </nav>\r\n");
      out.write("</header>");
      out.write("\r\n");
      out.write("\r\n");
      out.write("    <!-- =============================================== -->\r\n");
      out.write("\r\n");
      out.write("    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "include/sider.jsp" + "?" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("menu", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("home", request.getCharacterEncoding()), out, false);
      out.write("\r\n");
      out.write("\r\n");
      out.write("    <!-- =============================================== -->\r\n");
      out.write("\r\n");
      out.write("    <!-- 右侧内容部分 -->\r\n");
      out.write("    <div class=\"content-wrapper\">\r\n");
      out.write("        <!-- Content Header (Page header) -->\r\n");
      out.write("        <section class=\"content-header\">\r\n");
      out.write("            <h1>\r\n");
      out.write("                Blank page\r\n");
      out.write("                <small>it all starts here</small>\r\n");
      out.write("            </h1>\r\n");
      out.write("        </section>\r\n");
      out.write("\r\n");
      out.write("        <!-- Main content -->\r\n");
      out.write("        <section class=\"content\">\r\n");
      out.write("\r\n");
      out.write("        </section>\r\n");
      out.write("        <!-- /.content -->\r\n");
      out.write("    </div>\r\n");
      out.write("    <!-- /.content-wrapper -->\r\n");
      out.write("</div>\r\n");
      out.write("<!-- ./wrapper -->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!-- jQuery 2.2.3 -->\r\n");
      out.write("<script src=\"/static/plugins/jQuery/jquery-2.2.3.min.js\"></script>\r\n");
      out.write("<!-- Bootstrap 3.3.6 -->\r\n");
      out.write("<script src=\"/static/bootstrap/js/bootstrap.min.js\"></script>\r\n");
      out.write("<!-- SlimScroll -->\r\n");
      out.write("<script src=\"/static/plugins/slimScroll/jquery.slimscroll.min.js\"></script>\r\n");
      out.write("<!-- FastClick -->\r\n");
      out.write("<script src=\"/static/plugins/fastclick/fastclick.js\"></script>\r\n");
      out.write("<!-- AdminLTE App -->\r\n");
      out.write("<script src=\"/static/dist/js/app.min.js\"></script>");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}