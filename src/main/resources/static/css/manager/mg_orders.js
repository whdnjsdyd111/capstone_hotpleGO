$(function() {
    $('#cur_num').text($('.current-orderBox').children().length / 3);
    $('#his_num').text($('.history-orderBox').children().length / 3);

    $('.cancel').click(function() {
        let riCode = $(this).next().val();
        swal("예약 취소", "해당 예약을 취소하시겠습니까?", "warning", {
            buttons: {
                cancel: "닫기",
                go: "예약 취소"
            }
        }).then(v => {
            if (v === "go") {
                requestServlet({
                    riCode: riCode
                }, "/manager/rest/cancel_reservation", function(data) {
                    swal("취소 완료", data, "success").then(() => location.reload());
                }, basicErrorFunc);
            }
        })
    })
})