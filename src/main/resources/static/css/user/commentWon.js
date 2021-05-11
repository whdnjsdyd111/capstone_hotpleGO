let target_mem_id;
let target_mem_nickname = $('#reComCont');
let target_com_id;

let contents_id;
let contents_kind;

let cur_comCode;
const bdCode = $('#bdCode').val();
const written = "?sort=writen";
const recent = "?sort=recent";
const rec = "?sort=reco";

$(function () {

    $('.go_login').click(function() {
        window.location.href = '/login';
    });
    $(document).on('click','.btn-modify',function (){
        let val = $(this).next().val();
        let kind = $(this).next().next().val();
        $('#'+kind+val).hide();
        $('#'+kind+'-modify'+val).show();
    });
    $(document).on('click','.modify-cancel',function (){
        let val = $(this).next().val();
        let kind = $(this).next().next().val();
        $('#'+kind+val).show();
        $('#'+kind+'-modify'+val).hide();
    });
    $(document).on('click','#comment_input',function (){
        /*$('#comment_input').click(function () {*/
        if (!$('#comCont').html()) {
            alert("댓글 내용을 입력해주세요.");
            return false;
        }
        let data = {
            comCont: $('#comCont').html(),
            bdCode: $('#bdCode').val(),
            uCode: $('#uCode').val()
        }
        $.ajax({
            type: 'POST',
            url: '/board/rest/view/' + bdCode,
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (data, status, xhr) {
            swal({
                title: "등록 완료!",
                text: data,
                icon: "success",
                button: "확인"
            })
            reload(recent);
        }).fail(function (error) {
            console.log(error.responseText);
        });
    });

    $(document).on('click','.comment-delete',function (){
        let comCode = $(this).next().val().replaceAll('/','');
        swal("정말 삭제하시겠습니까?",
            {
                buttons: {
                    cancel: "아니오!",
                    add: "네!"
                },
            }).then((v) => {
            if (v === "add") {
                requestServlet({
                    comCode: comCode
                }, "/board/rest/delete/comment/" + comCode, function (data){
                    swal({
                        title: "삭제 완료!",
                        text: data,
                        icon: "success",
                        button: "확인"
                    }).then(v => reload(rec))
                }, basicErrorFunc)
            }
        });
    });

    $('#comment_image').on("change", function() {
        let formData = new FormData();
        formData.append("upload", $(this)[0].files[0]);
        $.ajax({
            type: "post",
            processData: false,
            contentType: false,
            data: formData,
            url: "/board/rest/upload_comm",
            success: function (data, status, xhr) {
                let cont = $('#comCont')
                cont.html(cont.html() + "<img src='" + data + "' />");
            },
            error: function (xhr, status, err) {
                swal("업로드 에러 발생!", "다시 시도해주십시오.", "error");
            }
        });
    });

    $(document).on('click','.write_reply',function (){

        /*$('.write_reply').click(function () {*/
        cur_comCode = $(this).next().val();
        $('.reply').remove();

        target_mem_id = $(this).next().val();
        target_mem_nickname = $(this).nextAll('input[name=nickname]').val();

        var span_nickname = '<span class="mr-2 bg-success text-white rounded" contenteditable="false">' + target_mem_nickname + '</span><br>';

        if (target_mem_nickname === undefined)
            span_nickname = "";


        var com_id = $(this).parents('.comments').children('form').children('input[name=com_id]').val();
        var reply =
                // '<div id="re-reply-edit" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">' + // 각 id는 해당 댓글의 아이디
            "<div class='reply p-3 d-flex flex-md-row' style='width:80%; margin: 0 auto;'>" +
            "<div class='border border-right-0 border-top-0 border-dark ml-5' style='width: 90px; height: 45px'></div>" +
            '<div id="reUCode" class="w-100"><div class="border border-dark write_comment_top mx-3">' +
            "<div class='ml-3 write_comment_member text-left'>닉네임</div><div class='write_comment_middle text-left'>" +
            '<div id="reComCont" class="px-3 d-inline" contentEditable="true">' +
            '<a class="text-primary" >@' + $(this).parent().parent().find('.fa-user-circle').text() + '</a><br></div></div></div>' +
            '<div class="border border-dark border-top-0 mx-3">' +
            '<div class="w-100 d-flex"><div class="custom-file w-75">' +
            '<input type="file" class="custom-file-input" id="nest_comment_image">' +
            '<label class="custom-file-label" for="reply_comment_image"><i class="fa fa-upload mr-2"></i>이미지 삽입</label></div>' +
            "<input type='hidden' name='com_id' value='" + com_id + "'>" +
            '<button onclick="reply_submit()" class="btn btn-info w-25" style="margin: 0px; height: 38px">작성</button></div></div><div>'
            // '</div>'
        ;

        $(this).parents('.comments').after(reply);


        $(".custom-file-input").on("change", function () {
            var fileName = $(this).val().split("\\").pop();
            $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
        });

        $('#nestCommentBtn').click(function () {

            target_com_id = $(this).prevAll('input[name=com_id]').val();

            if (!$('#nestComCont').html()) {
                alert("댓글 내용을 입력해주세요.");
                return false;
            }
            let replyData = {
                comCont: $('#comCont').html(),
                bdCode: $('#bdCode').val(),
                uCode: $('#uCode').val(),
                replyCode: $('#replyCode').val()
            }
            $.ajax({
                type: 'POST',
                url: '/board/rest/view',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(replyData)
            }).done(function (data, status, xhr) {
                swal({
                    title: "등록 완료!",
                    text: data,
                    icon: "success",
                    button: "확인"
                })
                reload(recent);
            }).fail(function (error) {
                console.log(error.responseText);
            });

        });

        $('#nest_comment_image').change(function() {
            let formData = new FormData();
            formData.append("upload", $(this)[0].files[0]);
            $.ajax({
                type: "post",
                processData: false,
                contentType: false,
                data: formData,
                url: "/board/rest/upload_comm",
                success: function (data, status, xhr) {
                    let cont = $('#reComCont');
                    cont.html(cont.html() + "<img src='" + data + "' />");
                },
                error: function (xhr, status, err) {
                    swal("업로드 에러 발생!", "다시 시도해주십시오.", "error");
                }
            });
        })

    });

    /* -------------------- 리-리댓달기------------------------------ */
    $(document).on('click','.write_re_reply',function (){

        /*$('.write_re_reply').click(function () {*/
        cur_comCode = $(this).next().val();
        $('.reply').remove();

        target_mem_id = $(this).next().val();
        target_mem_nickname = $(this).nextAll('input[name=nickname]').val();

        var span_nickname = '<span class="mr-2 bg-success text-white rounded" contenteditable="false">' + target_mem_nickname + '</span><br>';

        if (target_mem_nickname === undefined)
            span_nickname = "";


        var com_id = $(this).parents('.comments').children('form').children('input[name=com_id]').val();
        var reply =
                /*'<div id="re-reply-edit" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">' + // 각 id는 해당 댓글의 아이디*/
            "<div class='reply p-3 d-flex flex-md-row' style='width:80%; margin: 0 auto;'>" +
            "<div class='border border-right-0 border-top-0 border-dark ml-5' style='width: 90px; height: 45px'></div>" +
            '<div id="reUCode" class="w-100"><div class="border border-dark write_comment_top p-3 mx-3">' +
            "<div class='ml-3 write_comment_member text-left'>닉네임</div><div class='write_comment_middle p-2 text-left' style='width:98%'>" +
            '<div id="reComCont" class="px-3 d-inline" contentEditable="true">' +
            '<a class="text-primary" >@' + $(this).parent().parent().find('.fa-user-circle').text() + '</a><br></div></div></div>' +
            '<div class="border border-dark border-top-0 mx-3">' +
            '<div class="w-100 d-flex"><div class="custom-file w-75">' +
            '<input type="file" class="custom-file-input" id="nest_comment_image">' +
            '<label class="custom-file-label" for="reply_comment_image"><i class="fa fa-upload mr-2"></i>이미지 삽입</label></div>' +
            "<input type='hidden' name='com_id' value='" + com_id + "'>" +
            '<button onclick="reply_submit()" class="btn btn-info w-25" style="margin: 0px; height: 38px">작성</button></div></div><div>'
            /* '</div>'*/
        ;

        $(this).parents('.comments').after(reply);


        $(".custom-file-input").on("change", function () {
            var fileName = $(this).val().split("\\").pop();
            $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
        });

        $('#nest_comment_image').change(function() {
            let formData = new FormData();
            formData.append("upload", $(this)[0].files[0]);
            $.ajax({
                type: "post",
                processData: false,
                contentType: false,
                data: formData,
                url: "/board/rest/upload_comm",
                success: function (data, status, xhr) {
                    let cont = $('#reComCont');
                    cont.html(cont.html() + "<img src='" + data + "' />");
                },
                error: function (xhr, status, err) {
                    swal("업로드 에러 발생!", "다시 시도해주십시오.", "error");
                }
            });
        })
    });

    $(document).on('click','.comReco',function (){

        let comCode = $(this).next().next().val();
        let reco = $(this).children();
        let nonReco = $(this).next().children();
        if (reco.hasClass('comReco_y')) {
            // 추천되있는데 취소한경우
            // db에서 지우기
            requestServlet({
                comCode: comCode,
                check_com_reco: reco.hasClass('comReco_y'),
                check_com_nonReco: nonReco.hasClass('nonComReco_y')
            }, "/board/rest/comLike", function (data) {
                reco.removeClass('comReco_y');
                reco.addClass('comReco_n');
            }, basicErrorFunc);
            $(this).children().text(+$(this).children().text() - 1);
        } else{
            if (nonReco.hasClass('nonComReco_y')) {
                // 비추된 상태에서 추천 누르기
                // db 비추 n 으로 바꾸기
                requestServlet({
                    comCode: comCode,
                    check_com_reco: reco.hasClass('comReco_y'),
                    check_com_nonReco: nonReco.hasClass('nonComReco_y')
                }, "/board/rest/comLike", function (data) {
                    nonReco.removeClass('nonComReco_y');
                    nonReco.addClass('nonComReco_n');
                    // css 먹이기
                }, basicErrorFunc);
                $(this).next().children().text(+$(this).next().children().text() - 1);
            } else {
                // 추천 안되있는 상태 -> 추천 누르기
                // db 추가
                requestServlet({
                    comCode: comCode,
                    check_com_reco: reco.hasClass('comReco_y'),
                    check_com_nonReco: nonReco.hasClass('nonComReco_y')
                }, "/board/rest/comLike", function () {}, basicErrorFunc);
            }
            reco.removeClass('comReco_n');
            reco.addClass('comReco_y');
            $(this).children().text(+$(this).children().text() + 1);
        }

    });

    $(document).on('click','.nonComReco',function (){
        let comCode = $(this).next().val();
        let reco = $(this).prev().children();
        let nonReco = $(this).children();
        if (nonReco.hasClass('nonComReco_y')) {
            // 비추 -> 취소
            // 데이터베이스에서 지우기

            requestServlet({
                comCode: comCode,
                check_com_reco: reco.hasClass('comReco_y'),
                check_com_nonReco: nonReco.hasClass('nonComReco_y')
            }, "/board/rest/unComLike", function (data) {
                nonReco.removeClass('nonComReco_y');
                nonReco.addClass('nonComReco_n');
            }, basicErrorFunc);
            $(this).children().text(+$(this).children().text() - 1);
        } else {
            if (reco.hasClass('comReco_y')) {
                // 추천 -> 비추
                requestServlet({
                    comCode: comCode,
                    check_com_reco: reco.hasClass('comReco_y'),
                    check_com_nonReco: nonReco.hasClass('nonComReco_y')
                }, "/board/rest/unComLike", function (data) {
                    reco.removeClass('comReco_y');
                    reco.addClass('comReco_n');
                    // css 먹이기
                }, basicErrorFunc);
                $(this).prev().children().text(+$(this).prev().children().text() - 1);
            } else {
                // 비추x -> 비추o
                requestServlet({
                    comCode: comCode,
                    check_com_reco: reco.hasClass('comReco_y'),
                    check_com_nonReco: nonReco.hasClass('nonComReco_y')
                }, "/board/rest/unComLike", function (){}, basicErrorFunc);
            }

            nonReco.removeClass('nonComReco_n');
            nonReco.addClass('nonComReco_y');
            $(this).children().text(+$(this).children().text() + 1);
        }
    });

    $('[data-toggle="tooltip"]').tooltip();

    $(".custom-file-input").on("change", function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });



    $('#report_content').val("");

    $(".report").click(function () {
        $('#reportModal').modal();
        contents_id = $(this).next().val();
        contents_kind = $(this).next().next().val();
    });

    $('#modal_close').click(function () {
        $('#radioGroup input:radio:checked').prop('checked', false);
        $('#report_content').val("");
    });

    $('#modal_report').click(function () {
        if (!$('#radioGroup input:radio:checked').val()) {
            alert("신고 사유를 선택하십시오.");
            return false;
        }

        $.ajax({
            type: "post",
            url: "report.do",
            data: {
                rep_content: $('#report_content').val(),
                rep_kind: $('#radioGroup input:radio:checked').val(),
                contents_id: contents_id,
                contents_kind: contents_kind
            },
            success: function (data) {
                var str = "<p id='ck'>";
                var loc = data.indexOf(str);
                var len = str.length;
                var check = data.substr(loc + len, 1);

                if (check == "1") {
                    $('#reportModal').modal("hide");
                    alert("신고 감사합니다.");
                } else {
                    alert("다시 신고해주십시오.")
                }
            }
        });
    });
});

// 성공
function reply_submit() {
    if (!$('#reComCont').html()) {
        alert("답글 내용을 입력해주세요.");
        return false;
    }
    let data = {
        comCont: $('#reComCont').html(),
        bdCode: $('#reBdCode').val(),
        uCode: $('#reUCode').val(),
        replyCode: cur_comCode
    }
    $.ajax({
        type: 'POST',
        url: '/board/rest/view/' + bdCode,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function (data, status, xhr) {
        swal({
            title: "등록 완료!",
            text: data,
            icon: "success",
            button: "확인"
        })
        reload(written);
    }).fail(function (error) {
        console.log(error.responseText);
    });
}

function reload(kind) {
    $('#comments_div').load('/board/comment/' + bdCode + kind);
}