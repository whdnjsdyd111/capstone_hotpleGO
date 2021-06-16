$(function() {
    $(document).on('click', '.mbti', function() {
        $('div.bg-dark').removeClass('bg-dark');
        $(this).addClass('bg-dark');
    });

    $('.' + mbti).addClass('bg-dark');

    $('#save_mbti').click(function() {
        swal("이 mbti를 선택하시겠습니까?",
            {
                buttons: {
                    cancel: "아니오!",
                    add: "네!"
                },
            }).then(v => {
            if (v === "add") {
                $.ajax({
                    type: "POST",
                    url: "/save-mbti",
                    data: {
                        mbti : $('div.bg-dark').children().first().text()
                    },
                    success: function(data) {
                        // alert(data);
                        swal({
                            title: "등록 완료!",
                            text: data,
                            icon: "success",
                            button: "확인"
                        }).then((v) => {
                            location.href = "/myCourse?kind=myCourse";
                        })
                    },
                    error: function (xhr, status, err) {
                        alert(xhr.responseText);
                    }
                });
            }
        })
    });


})