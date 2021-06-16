$(function() {
    $(tastes).each(function(index, item) {
        $('input[value=' + item + ']').prop("checked", true);
    });

    $('#save_taste').click(function() {
        if ($('input[type="checkbox"]:checked').length === 0) {
            alert("취향을 선택해주십시오.");
            return;
        }

        let tastes = [];

        $('input[type=checkbox]:checked').each(function() {
            tastes.push($(this).val())
        })

        swal("정말 삭제하시겠습니까?",
            {
                buttons: {
                    cancel: "아니요!",
                    add: "네!"
                },
            }).then((v) => {
            if (v === "add") {
                requestServlet({
                    htId: htId
                }, "/pick-delete", function (data) {
                    swal({
                        title: "삭제 완료!",
                        text: data,
                        icon: "success",
                        button: "확인"
                    }).then(v => location.reload())
                }, basicErrorFunc);
            }
        });

        swal("취향을 선택하시겠습니까?",
            {
                buttons: {
                    cancel: "아니오!",
                    add: "네!"
                },
            }).then(v => {
            if (v === "add") {
                $.ajax({
                    type: "POST",
                    url: "/save-taste",
                    data: {
                        tastes: tastes
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