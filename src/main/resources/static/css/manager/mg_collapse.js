function preloading(imageArray) {
    let n = imageArray.length;
    for (let i = 0; i < n; i++) {
        let img = new Image();
        img.src = imageArray[i];
    }
};

preloading(['/images/hotple_add.png', 'resource/images/index/다운로드.png' ]);

$(function() {
    if($('#content_main').width() === $('#content_add').width())
        $('#add').attr('src', 'resource/images/index/다운로드.png');
    else
        $('#add').attr('src', '/images/hotple_add.png');


    $(window).resize(function() {
        if($('#content_main').width() === $('#content_add').width())
            $('#add').attr('src', 'resource/images/index/다운로드.png');
        else
            $('#add').attr('src', '/images/hotple_add.png');
    });



    $(window).scroll(function() {
        if(window.scrollY > $('html').height() - $('#sidebar').height() - $('footer').height() - $('header').children().outerHeight(true) ) {
            $('#sidebar').css({
                position: "absolute",
                top: "calc(" + ($('#content').height() / 2 - $('#sidebar').height()) + "px + 3.5rem)"
            });
        } else {
            $('#sidebar').css({
                position: "fixed",
                top: $('header').children().outerHeight(true) + "px"
            });
        }
    });

    $('.dropdown div, .dropdown button').click(function(e) {
        e.stopPropagation();
    });
});