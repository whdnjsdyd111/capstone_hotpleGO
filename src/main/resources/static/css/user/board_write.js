$(function() {
    main.init();

    let isSearch = true;

    let form = $('#search_form');

    form.submit(e => {
        if (isSearch) {
            if (!$(this).find("option:selected").val()) {
                swal("검색 종류 선택!", "검색할 대상의 종류를 선택해주세요.", "warning");
                return false;
            }

            if (!form.find('input[name=keyword]').val()) {
                swal("검색 키워드 입력!", "검색할 키워드를 입력해주세요.", "warning");
                return false;
            }

            $(this).find('input[name=pageNum]').val("1");
            return true;
        } else {
            return true;
        }
    });

    $('.pagination-custom a').on('click', function(e) {
        e.preventDefault();
        isSearch = false;
        let href = $(this).attr('href');
        form.find('input[name=pageNum]').val(href.substr(href.indexOf('pageNum=') + 8, 10));
        form.submit();
    });

    $('article').click(function() {
        location.href = '/board/view/' + $(this).children('input[type=hidden]').val();
    })

    $('.img-thumbnail').attr('src', function() {
        let temp = '/hotpleImage/0000/00/00/';
        let src = $(this).next().val().match(/<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>/);
        if (src === null) return '/images/no_image.png';
        src = src[1];
        src = src.substring(0, temp.length) + 's_' + src.substring(temp.length, src.length);
        return src;
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