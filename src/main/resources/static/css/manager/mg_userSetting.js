let check_nick = false;
let check_account = false;
let check_passwd = false;
let check_new_passwd = false;
let reCheck_passwd = false;

$(function() {
    $('#nickname').on("propertychange change keyup paste input", function() {
        let nick = $(this).val();

        if(nick === "") {
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

        if(nick.length < 2 || nick.length > 10) {
            $('#check_nick').text("2자리 ~ 10자리 이내로 입력해주세요.")
            check_nick = false;
        } else if(spe > 0 || nick.search(/\s/) != -1) {
            $('#check_nick').text("공백 또는 특수문자는 사용하실 수 없습니다.");
            check_nick = false;
        } else {
            check_nick = true;
            $('#check_nick').text("");
        }
        if(check_nick) {
            $('#updateNick').removeAttr('disabled');
        } else {
            $('#updateNick').attr('disabled', 'disabled');
        }
    });

    $('#mAccount').on("propertychange change keyup paste input", function() {
        let account = $(this).val();

        if (account === "") {
            $('#check_acc').text("");
            return;
        }
        let regexp = /^[0-9]*$/;
        if (account.length < 11 || account.length > 14) {
            $('#check_acc').text("계좌번호는 11 ~ 14자까지 입력하십시오.");
            check_account = false;
        } else if (!regexp.test(account)) {
            $('#check_acc').text("숫자만 입력해주십시오.");
            check_account = false;
        } else {
            $('#check_acc').text("");
            check_account = true;
        }

        if(check_account) {
            $('#updateAccount').removeAttr('disabled');
        } else {
            $('#updateAccount').attr('disabled', 'disabled');
        }
    })

    $('#updateNick').click(function() {
        $.ajax({
            type: "post",
            url: "/manager/rest/setting-nick",
            data: JSON.stringify({
                nick: $('#nickname').val()
            }),
            contentType: "application/json;charset=UTF-8",
            async: false, //동기, 비동기 여부
            cache : false, // 캐시 여부
            success: function(data) {
                alert(data);
            },
            error: function (xhr, status, err) {
                alert("다시 시도해주십시오.");
            }
        });
    });

    $('#updateAccount').click(function() {
        $.ajax({
            type: "post",
            url: "/manager/rest/setting-account",
            data: JSON.stringify({
                mBank : $('#bank').val(),
                mAccount : $('#mAccount').val()
            }),
            contentType: "application/json;charset=UTF-8",
            async: false, //동기, 비동기 여부
            cache : false, // 캐시 여부
            success: function(data) {
                alert(data);
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    });

    $("#bank").val(origin_bank).prop("selected", true);


    // 신규 비밀번호 체크
    $('#new_password').on("propertychange change keyup paste input", function() {
        let pw = $("#new_password").val();
        let num = pw.search(/[0-9]/g);
        let eng = pw.search(/[a-z]/ig);
        let eng2 = pw.search(/[A-Z]/ig);
        let spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

        if(pw.length < 8 || pw.length > 20){
            $('#new_check_pw').text("8자리 ~ 20자리 이내로 입력해주세요.");
            check_new_passwd = false;
        }else if(pw.search(/\s/) != -1){
            $('#new_check_pw').text("비밀번호는 공백 없이 입력해주세요.");
            check_new_passwd = false;
        }else if(num < 0 || eng < 0 || spe < 0 || eng2 < 0){
            $('#new_check_pw').text("영문 대소문자,숫자,특수문자를 혼합하여 입력해주세요.");
            check_new_passwd = false;
        }else {
            $('#new_check_pw').text('');
            check_new_passwd = true;
        }

        if(!($('#new_re_passwd').val() === $('#new_password').val())) {
            $('#new_re_check_pw').text("비밀번호와 같아야 합니다.");
            reCheck_passwd = false;
        }

        checkAll();
    });

    // 비밀번호 재입력 체크
    $('#new_re_passwd').on("propertychange change keyup paste input", function() {
        if($('#new_re_passwd').val() === $('#new_password').val()) {
            $('#new_re_check_pw').text("");
            reCheck_passwd = true;
        } else {
            $('#new_re_check_pw').text("비밀번호와 같아야 합니다.");
            reCheck_passwd = false;
        }

        checkAll();
    });

    // 비밀번호 체크
    $('#password').on("propertychange change paste input", function() {
        if($('#password').val()) {
            check_passwd = true;
        } else {
            check_passwd = false;
        }
        checkAll();
    });

    $('#submit_new_pw').click(function() {
        $.ajax({
            type: "POST",
            url: "/manager/rest/setting-password",
            data: {
                password: $('#password').val(),
                new_password: $('#new_password').val()
            },
            success: function(data) {
                alert(data);
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    });
});

function checkAll() {
    if(check_new_passwd && reCheck_passwd && check_passwd) {
        $('#submit_new_pw').removeAttr('disabled');
    } else {
        $('#submit_new_pw').attr('disabled', 'disabled');
    }
}