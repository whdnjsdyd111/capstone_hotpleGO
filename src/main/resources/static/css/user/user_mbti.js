$(function() {
    $(document).on('click', '.mbti', function() {
        $('div.bg-dark').removeClass('bg-dark');
        $(this).addClass('bg-dark');
    });

    $('.' + mbti).addClass('bg-dark');

    $('#save_mbti').click(function() {
        $.ajax({
            type: "POST",
            url: "/save-mbti",
            data: {
                mbti : $('div.bg-dark').children().first().text()
            },
            success: function(data) {
                alert(data);
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    });
})