let check_nick = false;
let check_passwd = false;
let check_new_passwd = false;
let reCheck_passwd = false;

$(function () {

    $('#nickname').on("propertychange change keyup paste input", function () {
        let nick = $(this).val();

        if (nick === "") {
            $('#check_nick').text("");
            return;
        }

        // for(let i = 0; i < bad_word.length; i++) {
        //     if($(this).val().indexOf(bad_word[i]) >= 0) {
        //         $(this).val($(this).val().replace(bad_word[i], ''));
        //         alert("비속어는 사용하실 수 없습니다.");
        //         return;
        //     }
        // }

        let spe = nick.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?.,\\()_+]/gi);

        if (nick.length < 2 || nick.length > 10) {
            $('#check_nick').text("2자리 ~ 10자리 이내로 입력해주세요.")
            check_nick = false;
        } else if (spe > 0 || nick.search(/\s/) != -1) {
            $('#check_nick').text("공백 또는 특수문자는 사용하실 수 없습니다.");
            check_nick = false;
        } else {
            check_nick = true;
            $('#check_nick').text("");
        }
        if (check_nick) {
            $('#updateNick').removeAttr('disabled');
        } else {
            $('#updateNick').attr('disabled', 'disabled');
        }
    });

    $('#updateNick').click(function () {
        $.ajax({
            type: "post",
            url: "/setting-nick",
            data: JSON.stringify({
                nick: $('#nickname').val()
            }),
            contentType: "application/json;charset=UTF-8",
            async: false, //동기, 비동기 여부
            cache: false, // 캐시 여부
            success: function (data) {
                alert(data);
            },
            error: function (xhr, status, err) {
                alert("다시 시도해주십시오.");
            }
        });
    });

    // 신규 비밀번호 체크
    $('#new_password').on("propertychange change keyup paste input", function () {
        let pw = $("#new_password").val();
        let num = pw.search(/[0-9]/g);
        let eng = pw.search(/[a-z]/ig);
        let eng2 = pw.search(/[A-Z]/ig);
        let spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

        if (pw.length < 8 || pw.length > 20) {
            $('#new_check_pw').text("8자리 ~ 20자리 이내로 입력해주세요.");
            check_new_passwd = false;
        } else if (pw.search(/\s/) != -1) {
            $('#new_check_pw').text("비밀번호는 공백 없이 입력해주세요.");
            check_new_passwd = false;
        } else if (num < 0 || eng < 0 || spe < 0 || eng2 < 0) {
            $('#new_check_pw').text("영문 대소문자,숫자,특수문자를 혼합하여 입력해주세요.");
            check_new_passwd = false;
        } else {
            $('#new_check_pw').text('');
            check_new_passwd = true;
        }

        if (!($('#new_re_passwd').val() === $('#new_password').val())) {
            $('#new_re_check_pw').text("비밀번호와 같아야 합니다.");
            reCheck_passwd = false;
        }

        checkAll();
    });

    // 비밀번호 재입력 체크
    $('#new_re_passwd').on("propertychange change keyup paste input", function () {
        if ($('#new_re_passwd').val() === $('#new_password').val()) {
            $('#new_re_check_pw').text("");
            reCheck_passwd = true;
        } else {
            $('#new_re_check_pw').text("비밀번호와 같아야 합니다.");
            reCheck_passwd = false;
        }

        checkAll();
    });

    // 비밀번호 체크
    $('#password').on("propertychange change paste input", function () {
        if ($('#password').val()) {
            check_passwd = true;
        } else {
            check_passwd = false;
        }
        checkAll();
    });

    $('#submit_new_pw').click(function () {
        $.ajax({
            type: "POST",
            url: "/setting-password",
            data: {
                password: $('#password').val(),
                new_password: $('#new_password').val()
            },
            success: function (data) {
                alert(data);
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    });


    $('#guideBtn').click(function () {
        let guideCont = $('#guide-content').val();
        if (guideCont === "") {
            swal("사유 작성!", "가이드 신청 사유를 작성해주세요!", "warning");
            return false;
        }

        requestServlet({
            gCont: guideCont
        }, "/setting-guide", function (data) {
            swal({
                title: "신청 완료!",
                text: data,
                icon: "success",
                button: "확인"
            }).then(v => {
                location.reload()
            })
        }, basicErrorFunc);
    });

    $('#guiding').click(function () {
        swal({
            title: "컨펌 중!",
            text: "관리자 승인을 기다려주세요",
            icon: "warning",
            button: "확인"
        })
    });

    // 코스 삭제
    $(document).on('click', '.pickDelete', function(e) {
        let htId = $(this).parent().parent().parent().parent().children('input[type=hidden]').val();
        e.stopPropagation(); // 부모 무시
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
                    }).then(v => {
                        location.reload()
                    })
                }, basicErrorFunc);
            }
        });
    });

   /* $('article').click(function () {
        location.href = '/hotple/' + $(this).children('input[type=hidden]').val();
    });*/
    // 핫플 정보 보기
    $(document).on('click','article',function (){
        location.href = '/hotple/' + $(this).children('input[type=hidden]').val();
    });

    // $('b').click(function () {
    //     let csCode = $('#csCode1').val();
    //     location.href = '/courseDetail/' + csCode;
    // });


    // 코스 정보 보기
    $(document).on('click','.pickCourse',function (){
    /*$('b').click(function (){*/
        let csCode = $('#csCode1').val();
        location.href = '/courseDetail/' + csCode;
    });

    $(document).on('click','.swiper-container',function (e){
    /*$('.swiper-contents').click(function (e) {*/
        e.stopPropagation();
    });

    // 찜 코스 삭제
    $(document).on('click','.pickCourseDelete',function (e){
    /*$('#pickCourseDelete').click(function (e) {*/

        let csCode = $(this).parent().parent().parent().parent().find('#csCode').val();
        //let csCode = $('#csCode').val();
        e.stopPropagation();
        swal("정말 삭제하시겠습니까?",
            {
                buttons: {
                    cancel: "아니요!",
                    add: "네!"
                },
            }).then((v) => {
            if (v === "add") {
                requestServlet({
                    csCode: csCode
                }, "/pickCourse-delete", function (data) {
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
});


function checkAll() {
    if (check_new_passwd && reCheck_passwd && check_passwd) {
        $('#submit_new_pw').removeAttr('disabled');
    } else {
        $('#submit_new_pw').attr('disabled', 'disabled');
    }
}