$(function() {
    main.init();
    $('#comment_content').load('/board/comment/21040410433804N');
})

var main = {
    init: function () {
        var _this = this;
        $('#comment_input').on('click', function () {
            if(!$('#comCont').html()){
                alert("댓글 내용을 입력해주세요.");
                return false;
            }
            _this.comSave();
        });
        $('#comment_update').on('click', function () {
            _this.comUpdate();
        });
        $('#comment_delete').on('click', function () {
            _this.comDelete();
        });
    },
    comSave: function () {
        let data = {
            comCont: $('#comCont').val(),
            bdCode: $('#bdCode').val(),
            uCode: $('#uCode').val()
        };
        $.ajax({
            type: 'POST',
            url: '/board/rest/boardDetail',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data, status, xhr) {
            alert(data);
            $('#comment_content').load('/board/comment/21040410433804N');
        }).fail(function (error) {
            console.log(error.responseText);
        });
    },
    comUpdate: function () {
        let data = {
            comCont: $('#comCont').val()
        };
        $.ajax({
            type: 'POST',
            url: '/board/rest/boardDetail',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data, status, xhr) {
            alert(data);
            $('#comment_content').load('/board/comment/21040410433804N');
        }).fail(function (error) {
            console.log(error.responseText);
        });
    },
    comDelete: function () {
        let data = {
            comCode: $('#comCode').val()
        };
        $.ajax({
            type: 'POST',
            url: '/board/delete/21040410433804N',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data, status, xhr) {
            alert(data);
            $('#comment_delete').load('/board/comment/21040410433804N');
        }).fail(function (error) {
            console.log(error.responseText);
        });
    }
};