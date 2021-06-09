$(function () {
    $('.selector').change(function (){
        let str = $(this).val();
        switch (str){
            case '평일':
                $('#weekday-box').show();
                $('#weekday-box').find('.selector').val('평일');
                $('#weekend-box').hide();
                $('#holiday-box').hide();

                break;
            case "주말":
                $('#weekday-box').hide();
                $('#weekend-box').show();
                $('#holiday-box').hide();
                $('#weekend-box').find('.selector').val('주말');
                $(this).val('주말');
                break;
            case "정기 휴무":
                $('#weekday-box').hide();
                $('#weekend-box').hide();
                $('#holiday-box').show();
                $('#holiday-box').find('.selector').val('정기 휴무');
                $(this).val('정기 휴무');
                break;
        }
    });

    $('.datetimepicker').datetimepicker({
        datepicker: false,
        step:5,
        lang:'ko',
        format:'H:i',
        theme:'dark',
        widgetPositioning: {
            horizontal: 'auto',
            vertical: 'bottom'
        }
    });

    $('#submit-weekday').click(function() {
        if (confirm("저장하시겠습니까?")) {
            $.ajax({
                type: "POST",
                url: "/manager/rest/save-weekday",
                data: {
                    wost : $('#weekday-start-time').val(),
                    woet : $('#weekday-end-time').val(),
                    wbst : $('#weekday-break-start-time').val(),
                    wbet : $('#weekday-break-end-time').val()
                },
                success: function(data) {
                    alert(data);
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('#submit-weekend-sat').click(function() {
        if (confirm("저장하시겠습니까?")) {
            $.ajax({
                type: "POST",
                url: "/manager/rest/save-weekend-sat",
                data: {
                    wost : $('#weekend-sat-start-time').val(),
                    woet : $('#weekend-sat-end-time').val(),
                    wbst : $('#weekend-sat-break-start-time').val(),
                    wbet : $('#weekend-sat-break-end-time').val()
                },
                success: function(data) {
                    alert(data);
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('#submit-weekend-sun').click(function() {
        if (confirm("저장하시겠습니까?")) {
            $.ajax({
                type: "POST",
                url: "/manager/rest/save-weekend-sun",
                data: {
                    wost : $('#weekend-sun-start-time').val(),
                    woet : $('#weekend-sun-end-time').val(),
                    wbst : $('#weekend-sun-break-start-time').val(),
                    wbet : $('#weekend-sun-break-end-time').val()
                },
                success: function(data) {
                    alert(data);
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    let holi = $('#holidays')

    $('#submit-holiday').click(function() {
        if (confirm("저장하시겠습니까?")) {
            $.ajax({
                type: "POST",
                url: "/manager/rest/save-holiday",
                data: {
                    whatWeek : $('#holiday-week').val(),
                    whatDay : $('#holiday-day').val(),
                },
                success: function(data) {
                    alert(data);
                    holi.append("<tr><td>" + week($('#holiday-week').val()) + "</td><td>" + day($('#holiday-day').val()) + "</td>" +
                        "<td><button type='button' class='btn btn-danger dh'>삭제</button><input type='hidden' value='" +
                        ($('#holiday-week').val() + "/" + $('#holiday-day').val()) +"' /></td></tr>")
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    for (let key in holiday) {
        let d = holiday[key].split('/');

        holi.append("<tr><td>" + week(d[0]) + "</td><td>" + day(d[1]) + "</td>" +
            "<td><button type='button' class='btn btn-danger dh'>" +
            "삭제</button><input type='hidden' value='" + holiday[key] +"' /></td></tr>")
    }

    $(document).on('click', '.dh', function() {
        let code = $(this).next().val();
        let ele = $(this);
        requestServlet({
            code: code
        }, "/manager/rest/delete-holiday", function(data) {
            alert(data)
            ele.parents('tr')[0].remove();
        }, basicErrorFunc)
    })
});

function week(w) {
    switch (w) {
        case "1":
            return "첫째 주"
        case "2":
            return "둘째 주"
        case "3":
            return "셋째 주"
        case "4":
            return "넷째 주"
        case "0":
            return "매주"
    }
}

function day(d) {
    switch (d) {
        case "0":
            return "일"
        case "1":
            return "월"
        case "2":
            return "화"
        case "3":
            return "수"
        case "4":
            return "목"
        case "5":
            return "금"
        case "6":
            return "토"
    }
}