$(function() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $('#alc_submit').click(function() {
        if(confirm("제휴 신청을 하시겠습니까?")) {
            $.ajax({
                type: "post",
                beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                    xhr.setRequestHeader(header, token);
                },
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({
                    name: $('#name').val(),
                    email: $('#email').val(),
                    phone: $('#phone').val(),
                    content: $('#content').val()
                }),
                async : true, //동기, 비동기 여부
                cache : false, // 캐시 여부
                url: "/alliance",
                success: function(data, status, xhr) {
                    alert(data);
                    self.location.reload();
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });
});