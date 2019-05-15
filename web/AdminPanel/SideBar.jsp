<%-- 
    Document   : SideBar
    Created on : Jun 27, 2018, 8:53:55 AM
    Author     : Moses
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
 
      <!-- /.search form -->
      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu" data-widget="tree">
        <li class="header">منو</li>
        
        <li class="active treeview">
          <a href="#">
            <i class="fa fa-dashboard"></i> <span>داشبرد</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/index.jsp"><i class="fa fa-circle-o"></i>داشبرد</a></li>
          </ul>
        </li>
        
        <li class="treeview">
          <a href="#">
            <i class="fa fa-fw fa-gears"></i> <span>تنظیمات سایت</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/Texts/Texts.jsp"><i class="fa fa-circle-o"></i>متن ها</a></li>
            <li class="active"><a href="/AdminPanel/SiteSetting/Advertisment.jsp"><i class="fa fa-circle-o"></i>تبلیغات</a></li>
            <li class="active"><a href="/AdminPanel/SiteSetting/SlideShow.jsp"><i class="fa fa-circle-o"></i>اسلایدشو</a></li>
            <li class="active"><a href="/AdminPanel/SiteSetting/SiteInfo.jsp"><i class="fa fa-circle-o"></i>اطلاعات سایت</a></li>
            <li class="active"><a href="/AdminPanel/SiteSetting/MainVideo.jsp"><i class="fa fa-fw fa-file-video-o"></i>ویدیو صفحه اصلی</a></li>
            <li class="active"><a href="/AdminPanel/SiteSetting/Upload.jsp"><i class="fa fa-fw fa-arrow-up"></i>اپلود فیلم</a></li>
            <li class="active"><a href="/AdminPanel/SiteSetting/Sections.jsp"><i class="fa fa-fw fa-folder-o"></i>بخش بندی فیلم ها</a></li>
          </ul>
        </li>
        
        
        
        <li class=" treeview">
          <a href="#">
            <i class="fa fa-fw fa-shopping-cart"></i> <span>محصولات</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
              <li class="active"><a href="/AdminPanel/Products/ProductsList.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست محصولات</a></li>
            <li class="active"><a href="/AdminPanel/Products/AddProduct.jsp"><i class="fa fa-fw fa-plus"></i>اضافه کردن محصول</a></li>
            <li class="active"><a href="/AdminPanel/Products/AddProductWindow.jsp"><i class="fa fa-fw fa-plus"></i>اضافه کردن به ویترین</a></li>
            <li class="active"><a href="/AdminPanel/Products/Windows.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست ویترین</a></li>
            <li class="active"><a href="/AdminPanel/Products/Categories.jsp"><i class="fa fa-fw fa-folder-o"></i>دسته بندی</a></li>
            <li class="active"><a href="/AdminPanel/Products/ColorMap.jsp"><i class="fa fa-fw fa-folder-o"></i>رنگ بندی طعم</a></li>
          </ul>
        </li>
        <li class=" treeview">
          <a href="#">
           <i class="fa fa-fw fa-reorder"></i> <span>سفارشات</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/Orders/Orders.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست سفارشات</a></li>
           
          </ul>
        </li>
        <li class=" treeview">
          <a href="#">
            <i class="fa fa-fw fa-user"></i> <span>کاربران</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/Users/Users.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست کاربران</a></li>
            <li class="active"><a href="/AdminPanel/Users/AddUser.jsp"><i class="fa fa-fw fa-plus"></i>افزودن کاربر</a></li>
            <li class="active"><a href="/AdminPanel/Users/Config.jsp"><i class="fa fa-fw fa-gears"></i>تنظیمات</a></li>
            <li class="active"><a href="/AdminPanel/Users/Birthdays.jsp"><i class="fa fa-fw fa-list-ol"></i>تولد های نزدیک</a></li>
          </ul>
        </li>
        
        <li class=" treeview">
          <a href="#">
            <i class="fa fa-fw fa-shopping-cart"></i> <span>همکاران</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
              <li class="active"><a href="/AdminPanel/Partners/Partners.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست همکاران</a></li>
            <li class="active"><a href="/AdminPanel/Partners/AddPartner.jsp"><i class="fa fa-fw fa-plus"></i>اضافه کردن همکار</a></li>
            <li class="active"><a href="/AdminPanel/Partners/AddProduct.jsp"><i class="fa fa-fw fa-plus"></i>اضافه کردن محصول</a></li>
            <li class="active"><a href="/AdminPanel/Partners/Products.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست محصولات</a></li>
            <li class="active"><a href="/AdminPanel/Partners/Orders.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست سفارشات</a></li>
            <li class="active"><a href="/AdminPanel/Partners/Categories.jsp"><i class="fa fa-fw fa-folder-o"></i>دسته بندی</a></li>
            <li class="active"><a href="/AdminPanel/Partners/PartnerSeries.jsp"><i class="fa fa-fw fa-folder-o"></i>دسته بندی همکاران</a></li>

          </ul>
        </li>
        
        <li class="treeview">
          <a href="#">
            <i class="fa fa-fw fa-user-secret"></i> <span>مدیریت</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/Admins/Admins.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست مدیران</a></li>
            <li class="active"><a href="/AdminPanel/Admins/AddAdmin.jsp"><i class="fa fa-fw fa-plus"></i>اضافه کردن مدیر</a></li>
          </ul>
        </li>
        
        <li class="treeview">
          <a href="#">
            <i class="fa fa-fw fa-truck"></i> <span>حمل و نقل</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/Ship/ShipTypes.jsp"><i class="fa fa-fw fa-ellipsis-v"></i>روش های حمل و نقل</a></li>
            <li class="active"><a href="/AdminPanel/Ship/Ships.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست حمل و نقل</a></li>
          </ul>
        </li>
        
        <li class="treeview">
          <a href="#">
            <i class="fa fa-fw fa-money"></i> <span>مالی</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/Invoice/PaymentTypes.jsp"><i class="fa fa-fw fa-ellipsis-v"></i>روش های پرداخت</a></li>
            <li class="active"><a href="/AdminPanel/Invoice/Payments.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست پرداخت ها</a></li>
          </ul>
        </li>
        
         <li class="treeview">
          <a href="#">
            <i class="fa fa-fw fa-wifi"></i> <span>ارتباطات</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/Communications/ContactUs.jsp"><i class="fa fa-fw fa-comments-o"></i>ارتباط با ما</a></li>
            <li class="active"><a href="/AdminPanel/Communications/Comments.jsp"><i class="fa fa-fw fa-comment-o"></i>نظرات</a></li>
            <li class="active"><a target="blank" href="/AdminPanel/Communications/Chat.jsp"><i class="fa fa-fw fa-comment-o"></i>چت پشتیبانی</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-fw fa-mobile"></i> <span>پنل اس ام اس</span>
            <span class="pull-left-container">
              <i class="fa fa-angle-right pull-left"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a href="/AdminPanel/Sms/SmsConfig.jsp"><i class="fa fa-fw fa-gears"></i>تنظیمات</a></li>
            <li class="active"><a href="/AdminPanel/Sms/Smses.jsp"><i class="fa fa-fw fa-list-ol"></i>لیست پیغام ها</a></li>
          </ul>
        </li>
        
        
        
      </ul>
    

