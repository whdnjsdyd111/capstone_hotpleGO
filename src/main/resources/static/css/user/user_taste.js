$(function() {
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
            data: {
                tastes: tastes
            },
            success: function(data) {
                alert(data);
                location.href='/setting';
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    });
})