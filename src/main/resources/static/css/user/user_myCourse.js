$(function() {
    $('#custom_submit').click(function() {
        let csWith = $('#with').val();
        let csNum = $('#person').val();
        let csTitle = $('#title').val();

        if (csWith === "" || csNum === "" || csTitle === "") {
            swal("빈 입력사항이 있습니다.", "모두 입력해주십시오.", "warning");
            return;
        }

        requestBody({
            csWith : csWith,
            csNum : csNum,
            csTitle: csTitle
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

    $('#mbti').click(function() {
        location.href = "/python/mbti_course";
    });

    $('#custom').click(function() {
        let set = $('#setting');
        if (set.is(':visible')) set.hide();
        else set.show();
    })
})