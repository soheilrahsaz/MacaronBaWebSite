$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
    AOS.init();

    $(".babakhaniDatepicker").pDatepicker({
        initialValue: false,
        observer: true,
        format: 'YYYY/MM/DD',
        altField: ' ',
        viewMode: 'year',
        altField: '.timestampInput',
        autoClose: true,
    });


    var prevScroll = 0;

    $(window).on("scroll", function () {
        var $this = $(this),
            currScroll = $this.scrollTop(),
            endPage = $this.height;
        //     $statusText = $("p > span"),

        if (currScroll > prevScroll) {
            $("#video").removeClass("videoTop");
            $("#video").addClass("videoDown");
        }// else if (currScroll >= 1548) {
         //     $("#video").removeClass("videoDown");
         //     $("#video").removeClass("videoTop");
         //     $("#video").addClass("videoEnd");
         // }
        else {
            $("#video").removeClass("videoDown");
            $("#video").addClass("videoTop");
        }

        // console.log(currScroll);

        prevScroll = currScroll;
    });


    var x = document.getElementById("demo");
    function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition);
        } else {
            x.innerHTML = "Geolocation is not supported by this browser.";
        }
    }
    function showPosition(position) {
        x.innerHTML = "Latitude: " + position.coords.latitude +
            "<br>Longitude: " + position.coords.longitude;
    }
})