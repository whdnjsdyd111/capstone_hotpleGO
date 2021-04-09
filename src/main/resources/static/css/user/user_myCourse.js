$(function() {
    $('#custom_submit').click(function() {
        requestBody({
            csWith : $('#with').val(),
            csNum : $('#person').val()
        }, '/custom-course', function(data) {
            swal({
                title : "코스를 생성했습니다.",
                text : "원하는 장소들을 검색하여 추가하여 보세요!",
                icon : "success",
                button: "핫플 GO!"
            }).then(() => {
                location.href = "/courseDetail/" + data.replaceAll('/', '');
            });
        }, basicErrorFunc);
    });

    $('#custom').click(function() {
        let set = $('#setting');
        if (set.is(':visible')) set.hide();
        else set.show();
    })
})