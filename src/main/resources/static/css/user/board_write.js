const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });

$(function() {
    main.init();
})

var main = {
    init: function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

        $('#btn-delete').on('click', function () {
            _this.delete();
        });
    },
    save: function () {
        let data = {
            bdTitle: $('#bdTitle').val(),
            bdArea: $('#bdArea').val(),
            bdCont: $('#bdCont').val()
        };
        $.ajax({
            type: 'POST',
            url: '/board/rest/write',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data, status, xhr) {
            alert(data);
            window.location.href = '/board/list';
        }).fail(function (error) {
            console.log(error.responseText);
        });
    }, update : function () {
        var data = {
            bdTitle: $('#bdTitle').val(),
            bdCont: $('#bdCont').val(),
            bdCode: $('#bdCode').val()
        };

        $.ajax({
            type: 'post',
            url: '/board/rest/update',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/board/list';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};