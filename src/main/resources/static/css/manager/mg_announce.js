$(function () {
    $('[data-toggle="tooltip"]').tooltip();

    $('.board_article').click(function() {
        location.href = "/manager/announce/" + $($(this).children()[0]).val();
    });
})