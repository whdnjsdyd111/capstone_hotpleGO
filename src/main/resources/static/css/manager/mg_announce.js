$(function () {
    $('[data-toggle="tooltip"]').tooltip();
    let isSearch = true;

    $('.board_article').click(function() {
        location.href = "/manager/announce/" + $($(this).children()[0]).val();
    });

    $('.img-thumbnail').attr('src', function() {
        let temp = '/hotpleImage/0000/00/00/';
        let src = $(this).next().val().match(/<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>/);
        if (src === null) return '/images/no_image.png';
        src = src[1];
        src = src.substring(0, temp.length) + 's_' + src.substring(temp.length, src.length);
        return src;
    });

    let form = $('#search_form');

    form.submit(e => {
        if (isSearch) {
            if (!$(this).find("option:selected").val()) {
                swal("검색 종류 선택!", "검색할 대상의 종류를 선택해주세요.", "warning");
                return false;
            }

            if (!$('input[name=keyword]').val()) {
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
})