$(function() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $(document).on('click', '.mbti', function() {
        $('div.bg-dark').removeClass('bg-dark');
        $(this).addClass('bg-dark');
    });

    $('#save_mbti').click(function() {
        $.ajax({
            type: "POST",
            url: "/save-mbti",
            beforeSend: function (xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
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