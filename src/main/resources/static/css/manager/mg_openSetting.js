const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });

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
});


