<%-- 
    Document   : Admins
    Created on : Jul 23, 2018, 7:10:44 PM
    Author     : Moses
--%>

<%@page import="Objects.Admin.Admin"%>
<%@page import="Objects.Printers.AdminPanel.Admins"%>
<%@page import="Objects.Printers.AdminPanel.Users"%>
<%@page import="Objects.Panel.AlertCode"%>
<%@page import="SQL.Driver.SQLDriverMacaronBa"%>
<%@page import="Objects.Authenticators.Admin.AdminSafe"%>
<%@page import="java.sql.Connection"%>
<%@page import="Objects.Printers.AdminPanel.Products"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Connection connection=new SQLDriverMacaronBa().getConnection();
    AdminSafe adminSafe=new AdminSafe(request, connection);
    if(!adminSafe.getLogged())
    {
        connection.close();
        response.sendRedirect("../Login.jsp");
        return;
    }
    Admin admin=adminSafe.getAdmin();
    if(!admin.getAccess().isMaster())
    {
        connection.close();
        response.sendRedirect("../index.jsp?att=noaccess");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>لیست مدیر ها</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="../dist/css/bootstrap-theme.css">
  <!-- Bootstrap rtl -->
  <link rel="stylesheet" href="../dist/css/rtl.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="../bower_components/font-awesome/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="../bower_components/Ionicons/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="../dist/css/AdminLTE.css">
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link rel="stylesheet" href="../dist/css/skins/_all-skins.min.css">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->

  <!-- Google Font -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

  <header class="main-header">
    <!-- Logo -->
    <a href="#" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini">پنل</span>
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg"><b>کنترل پنل مدیریت</b></span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <%@include file="../NavBar.jsp" %>
  </header>
     <!-- right side column. contains the logo and sidebar -->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- Sidebar user panel -->
      <div class="user-panel">
        <div class="pull-right image">
          <img src="../dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
        </div>
        <div class="pull-right info">
          <p>شیرینی قصر فردوس</p>
          <a href="#"><i class="fa fa-circle text-success"></i> آنلاین</a>
        </div>
      </div>
      <!-- search form -->
      <form action="#" method="get" class="sidebar-form">
        <div class="input-group">
          <input type="text" name="q" class="form-control" placeholder="جستجو">
          <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
        </div>
      </form>
    <%@include file="../SideBar.jsp" %>
    </section>
    <!-- /.sidebar -->
  </aside>

    
   
  <!-- -------------------------------------------------------------------------------------------------------------------- -->
  
  
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
         <%
                  out.println(new AlertCode().getSentence(request));
          %>
          
      <section class="content">
          <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">لیست مدیران</h3>

<!--              <div class="box-tools">
                <div class="input-group input-group-sm" style="width: 150px;">
                  <input type="text" name="table_search" class="form-control pull-right" placeholder="جستجو">

                  <div class="input-group-btn">
                    <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                  </div>
                </div>
              </div>-->
            </div>
            <!-- /.box-header -->
            <div class="box-body table-responsive no-padding">
              <table class="table table-hover">
                <tr>
                  <th>نام کاربری</th>
                  <th>تاریخ افزودن</th>
                </tr>
                
                <%
                    try {
                            Admins admins=new Admins(connection);
                            out.println(admins.getAdminRows());
                        } catch (Exception e) {
                            out.println(e.getMessage());
                        }
                    %>
               
              </table>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
      </div>
      </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  
  
<!-- -------------------------------------------------------------------------------------------------------------------- -->





  <footer class="main-footer text-left">
    <strong>Copyleft &copy; Coderof</strong>
  </footer>
  <script src="../bower_components/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="../bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="../bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="../bower_components/fastclick/lib/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="../dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="../dist/js/demo.js"></script>
<script>
  $(document).ready(function () {
    $('.sidebar-menu').tree()
  })
</script>
</body>
</html>

<%
  connection.close();
%>


