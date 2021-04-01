$(function() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $(tastes).each(function(index, item) {
        $('input[value=' + item + ']').prop("checked", true);
    });

    $('#save_taste').click(function() {
        if ($('input[type="checkbox"]:checked').length === 0) {
            alert("취향을 선택해주십시오.");
            return;
        }

        let tastes = [];

        $('input[type=checkbox]:checked').each(function() {
            tastes.push($(this).val())
        })

        $.ajax({
            type: "POST",
            url: "/save-taste",
            beforeSend: function (xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            data: {
                tastes: tastes
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