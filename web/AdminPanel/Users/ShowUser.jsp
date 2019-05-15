<%-- 
    Document   : ShowUser
    Created on : Jul 16, 2018, 3:35:13 PM
    Author     : Moses
--%>


<%@page import="Objects.Admin.Admin"%>
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
        response.sendRedirect("/AdminPanel/Login.jsp");
        return;
    }
    Admin admin=adminSafe.getAdmin();
    if(!admin.haveAccessTo("viewUsers"))
    {
        response.sendRedirect("/AdminPanel/index.jsp?att=noaccess");
        return;
    }
%>
<!DOCTYPE html>
<html>
    
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>نمایش کاربر</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="/AdminPanel/dist/css/bootstrap-theme.css">
  <!-- Bootstrap rtl -->
  <link rel="stylesheet" href="/AdminPanel/dist/css/rtl.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="/AdminPanel/bower_components/font-awesome/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="/AdminPanel/bower_components/Ionicons/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="/AdminPanel/dist/css/AdminLTE.css">
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link rel="stylesheet" href="/AdminPanel/dist/css/skins/_all-skins.min.css">
  <!-- babakhani datepicker -->
  <link rel="stylesheet" href="/AdminPanel/dist/css/persian-datepicker-0.4.5.min.css" />
  <!-- daterange picker -->
  <link rel="stylesheet" href="/AdminPanel/bower_components/bootstrap-daterangepicker/daterangepicker.css">
  <!-- bootstrap datepicker -->
  <link rel="stylesheet" href="/AdminPanel/bower_components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css">
  <!-- Bootstrap time Picker -->
  <link rel="stylesheet" href="/AdminPanel/plugins/timepicker/bootstrap-timepicker.min.css">
  <link rel="stylesheet" href="/AdminPanel/dist/css/mmdstyle.css" >

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
    <%@include file="/AdminPanel/NavBar.jsp" %>
  </header>
     <!-- right side column. contains the logo and sidebar -->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- Sidebar user panel -->
      <div class="user-panel">
        <div class="pull-right image">
          <img src="/AdminPanel/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
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
      
      <%@include file="/AdminPanel/SideBar.jsp" %>
    
    
    </section>
    <!-- /.sidebar -->
  </aside>

    
   
  <!-- -------------------------------------------------------------------------------------------------------------------- -->
  
  
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
      
          <%
              try {
                    out.println(new AlertCode().getSentence(request));
                    String uri=request.getRequestURI();
                    Users users=new Users(connection);
                    out.println(users.printUser(uri));
                  
                  } catch (Exception e) {
                      out.println(e.getMessage());
                  }
          %>
       <!-- Main content -->
   
  </div>
  <!-- /.content-wrapper -->
  
  
<!-- -------------------------------------------------------------------------------------------------------------------- -->





  <footer class="main-footer text-left">
    <strong>Copyleft &copy; Coderof</strong>
  </footer>
  <script src="/AdminPanel/bower_components/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="/AdminPanel/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="/AdminPanel/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="/AdminPanel/bower_components/fastclick/lib/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="/AdminPanel/dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="/AdminPanel/dist/js/demo.js"></script>
<!-- babakhani datepicker -->
<script src="/AdminPanel/dist/js/persian-date-0.1.8.min.js"></script>
<script src="/AdminPanel/dist/js/persian-datepicker-0.4.5.min.js"></script>
<!-- bootstrap time picker -->
<script src="/AdminPanel/plugins/timepicker/bootstrap-timepicker.min.js"></script>
<!-- date-range-picker -->
<script src="/AdminPanel/bower_components/moment/min/moment.min.js"></script>
<script src="/AdminPanel/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<!-- bootstrap datepicker -->
<script src="/AdminPanel/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script>
  $(document).ready(function () {
    $('.sidebar-menu').tree()
  })
</script>
<script>
    $(document).ready(function () {
        $('#tarikh').persianDatepicker({
            altField: '#tarikhAlt',
            altFormat: 'X',
            format: 'D MMMM YYYY HH:mm a',
            observer: true,
            timePicker: {
                enabled: true
            },
        });
    });
  $(function () {
    //Initialize Select2 Elements
    $('.select2').select2()

    //Datemask dd/mm/yyyy
    $('#datemask').inputmask('dd/mm/yyyy', { 'placeholder': 'dd/mm/yyyy' })
    //Datemask2 mm/dd/yyyy
    $('#datemask2').inputmask('mm/dd/yyyy', { 'placeholder': 'mm/dd/yyyy' })
    //Money Euro
    $('[data-mask]').inputmask()

    //Date range picker
    $('#reservation').daterangepicker()
    //Date range picker with time picker
    $('#reservationtime').daterangepicker({ timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A' })
    //Date range as a button
    $('#daterange-btn').daterangepicker(
      {
        ranges   : {
          'Today'       : [moment(), moment()],
          'Yesterday'   : [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
          'Last 7 Days' : [moment().subtract(6, 'days'), moment()],
          'Last 30 Days': [moment().subtract(29, 'days'), moment()],
          'This Month'  : [moment().startOf('month'), moment().endOf('month')],
          'Last Month'  : [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        startDate: moment().subtract(29, 'days'),
        endDate  : moment()
      },
      function (start, end) {
        $('#daterange-btn span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'))
      }
    )

    //Date picker
    $('#datepicker').datepicker({
      autoclose: true
    })

    //iCheck for checkbox and radio inputs
    $('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
      checkboxClass: 'icheckbox_minimal-blue',
      radioClass   : 'iradio_minimal-blue'
    })
    //Red color scheme for iCheck
    $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
      checkboxClass: 'icheckbox_minimal-red',
      radioClass   : 'iradio_minimal-red'
    })
    //Flat red color scheme for iCheck
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
      checkboxClass: 'icheckbox_flat-green',
      radioClass   : 'iradio_flat-green'
    })

    //Colorpicker
    $('.my-colorpicker1').colorpicker()
    //color picker with addon
    $('.my-colorpicker2').colorpicker()

    //Timepicker
    $('.timepicker').timepicker({
      showInputs: false
    })
  })
</script>
</body>
</html>
<%
  connection.close();
%>