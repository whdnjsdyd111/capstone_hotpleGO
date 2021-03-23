$(function() {
    if ($(window).width() < 975) {
        $('div.sidebar').addClass('collapse')
    } else {
        $('div.sidebar').removeClass('collapse')
    }

    $(window).bind("load resize", function() {
        if ($(this).width() < 975) {
            $('div.sidebar').addClass('collapse')
        } else {
            $('div.sidebar').removeClass('collapse')
        }
    });
});