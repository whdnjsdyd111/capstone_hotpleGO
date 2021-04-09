$(function() {
    main.init();

    $('article').click(function() {
        location.href = '/board/view/' + $(this).children('input[type=hidden]').val();
    })
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

        $('#comment_input').on('click', function () {
            _this.comSave();
        });

    },
    save: function () {
        let data = {
            bdTitle: $('#bdTitle').val(),
            bdArea: $('#bdArea').val(),
            bdCont: CKEDITOR.instances.bdCont.getData()
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
        let data = {
            bdTitle: $('#bdTitle').val(),
            bdCont: CKEDITOR.instances.bdCont.getData(),
            bdCode: $('#bdCode').val()
        };

        $.ajax({
            type: 'POST',
            url: '/board/rest/update',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data, status, xhr){
            alert(data);
            window.location.href = '/board/list';
        }).fail(function (error) {
            console.log(error.responseText);
        });
    }, delete: function () {
        let data = {
            bdCode: $('#bdCode').val()
        };

        $.ajax({
            type: 'DELETE',
            url: '/board/delete/'+bdCode,
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data, status, xhr) {
            alert(data);
            window.location.href = '/board/list';
        }).fail(function (error) {
            console.log(error.responseText);
        });
    }
};