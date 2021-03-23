$(function() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $('#back').click(function() {
        history.back();
    })

    $('#kind').on("change", function() {
        let v = $(this).val();

        if (v == 100) {
            $('#datetimepickerDiv').remove();
        } else {
            let str = "<div class='input-group' id='datetimepickerDiv'>" +
                "<input type='text' id='start_time' class='form-control datetimepickers' autocomplete='off'>" +
                "<div class='input-group-append'>" +
                "<label class='input-group-text'><i class='fa fa-clock-o mr-2'></i>시작시간</label>" +
                "</div>" +
                "<input type='text' id='expi_time' class='form-control datetimepickers' autocomplete='off'>" +
                "<div class='input-group-append'>" +
                "<label class='input-group-text'><i class='fa fa-clock-o mr-2'></i>종료시간</label>" +
                "</div>" +
                "</div>";

            $('#titleDiv').after(str);
            $('.datetimepickers').datetimepicker({
                lang:'ko',
                format: 'Y-m-d H:i',
                theme: 'dark'
            });
        }
    });

    $('#complete').click(function() {
        $.ajax({
            type: "post",
            beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify({
                eveTitle: $('#title').val(),
                eveCont: CKEDITOR.instances.content.getData(),
                eveStart: Date.parse($('#start_time').val()),
                eveExpi: Date.parse($('#expi_time').val())
            }),
            async: true, //동기, 비동기 여부
            cache :false, // 캐시 여부
            url: "/admin/rest/registerEvent",
            success: function(data, status, xhr) {
                alert(data);
                location.href = "/admin/announce";
            }
        });
    });

    $('#update').click(function() {
        $.ajax({
            type: "post",
            beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify({
                eveCode: $('#eveCode').val(),
                eveTitle: $('#title').val(),
                eveCont: CKEDITOR.instances.content.getData(),
                eveStart: Date.parse($('#start_time').val()),
                eveExpi: Date.parse($('#expi_time').val())
            }),
            async: true, //동기, 비동기 여부
            cache :false, // 캐시 여부
            url: "/admin/rest/updateEvent",
            success: function(data, status, xhr) {
                alert(data);
                location.href = "/admin/announce";
            }
        });
    });
});