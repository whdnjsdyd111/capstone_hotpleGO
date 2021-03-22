$(function () {
    $('[data-toggle="tooltip"]').tooltip();

    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $('.board_article').click(function() {
        location.href = "/manager/announce/" + $($(this).children()[0]).val();
    });
})