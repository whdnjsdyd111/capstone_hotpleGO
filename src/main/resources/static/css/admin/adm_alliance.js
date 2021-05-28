$(function() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $('.watch_content').click(function() {
        $('#alc_content').html($(this).prev().prev().val());
        $('#alc_phone').html("제휴자 휴대번호 : " + $(this).prev().val())
        $('.modal').modal('show');
    });

    $('.process').click(function() {
        if(confirm("피드백 확인을 하셨습니까?")) {
            $.ajax({
                type: "post",
                beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                    xhr.setRequestHeader(header, token);
                },
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({
                    alcCode: $(this).prev().val()
                }),
                async: true, //동기, 비동기 여부
                cache :false, // 캐시 여부
                url: "/admin/rest/changeAlc",
                success: function(data, status, xhr) {
                    alert(data);
                    self.location = "/admin/alliances?sort=processed";
                }
            });
        }
    });

    $('.delete').click(function() {
        if(confirm("피드백 확인을 하셨습니까?")) {
            $.ajax({
                type: "post",
                beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                    xhr.setRequestHeader(header, token);
                },
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({
                    alcCode: $(this).prev().val()
                }),
                async: true, //동기, 비동기 여부
                cache :false, // 캐시 여부
                url: "/admin/rest/deleteAlc",
                success: function(data, status, xhr) {
                    alert(data);
                    self.location.reload();
                },
                error: function(xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });
});