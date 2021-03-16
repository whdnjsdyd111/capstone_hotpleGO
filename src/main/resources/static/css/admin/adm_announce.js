$(function() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

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
    })
})