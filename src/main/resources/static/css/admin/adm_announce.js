$(function() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    let isSearch = true;

    $('.board_article').click(function() {
        location.href = "/admin/announce/" + $($(this).children()[0]).val();
    });

    $('#delete').click(function() {
        if (confirm("삭제하시겠습니까?")) {
            $.ajax({
                type: "post",
                beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                    xhr.setRequestHeader(header, token);
                },
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({
                    eveCode: $('#eveCode').val()
                }),
                async: true, //동기, 비동기 여부
                cache :false, // 캐시 여부
                url: "/admin/rest/deleteEvent",
                success: function(data, status, xhr) {
                    alert(data);
                    self.location = "/admin/announce";
                }
            });
        }
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
});