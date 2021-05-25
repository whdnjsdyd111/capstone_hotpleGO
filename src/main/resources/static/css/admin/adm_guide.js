$(function () {
    $('button[data-toggle=modal]').click(function() {
        $('#gCont').text($(this).next().text());
        $('#uCode').text($($(this).prev().children()[0]).text())
    });

    $(document).on('click','.confirmBtn',function (){
        requestServlet({
            uCode: $(this).parent().find(".cfGuide").text()
        },"/admin/rest/confirmGuide", function (data){
            swal({
                title: "가이드 등록 완료!",
                text: data,
                icon: "success",
                button: "확인"
            }).then(v => {location.reload()})
        }, basicErrorFunc);
    });

    $(document).on('click','.deleteBtn',function (){
        let remove_check = confirm("정말로 거절하시겠습니까?");
        if (remove_check){
            requestServlet({
                uCode: $(this).parent().find(".cfGuide").text()
            }, "/admin/rest/deleteGuideApply", function (data) {
                swal("거절 완료", data, "success").then(v => {location.reload()})
            });
        }
    });

    $(document).on('click', '.deleteGuide', function () {
        let uCode = $(this).parent().find(".thisGuide").text();
        swal("삭제하시겠습니까?",{
            buttons: {
                cancel: "아니오!",
                add: "네!"
            },
        }).then((v) => {
            if (v === "add"){
                requestServlet({
                    uCode: uCode
                }, "/admin/rest/deleteGuide", function (data){
                    swal({
                        title: "삭제 완료!",
                        text: data,
                        icon: "success",
                        button: "확인"
                    }).then(v => location.reload())
                }, basicErrorFunc);
            }
        });
    });
})