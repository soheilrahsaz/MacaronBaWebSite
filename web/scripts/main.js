(function($){

    var $basketToggle = $('.basket-toggle'),
        $hasSub=$('.has-sub'),
        $subToggle=$('.sub-menu-toggle'),
        $subMenu = $('.nav__sub');

    $basketToggle.on('click', function(e){
        $('body').toggleClass('show-basket');
        $(this).toggleClass('is-active');
        $(this).toggleClass('fa fa-times');
    })
    
    $hasSub.on('click',function (e) {
        $subMenu.toggleClass('is-active');
        $subToggle.toggleClass('is-active');
    })
    

})(jQuery);