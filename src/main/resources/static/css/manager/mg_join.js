        $(function() {
            let nameReg = false;
            let phoneReg = false;
            let emailReg = false;
            let passwordReg = false;
            let pwCheckReg = false;
            let nickReg = false;
            let accountReg = false;
            let birthCheck = false;

            function checkAll() {
                if (nameReg && phoneReg && emailReg && passwordReg && pwCheckReg && nickReg && accountReg && birthCheck) {
                    $('#regBtn').removeAttr('disabled');
                } else {
                    $('#regBtn').attr('disabled', 'disabled');
                }
            }

            $(".onlyHangul").on("properties change paste input", function(event) {
                regexp = /[a-z0-9]|[ \[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g;
                v = $(this).val();
                nameReg = false;
                if (v == '') {
                    $('#check_name').text('');
                } else if (regexp.test(v)) {
                    $("#check_name").text("한글만 입력가능 합니다.");
                    $(this).val(v.replace(regexp, ''));
                    $(this).focus();
                } else {
                    $("#check_name").text("");
                    nameReg = true;
                }
                checkAll();
            });

            $("#phoneNumber").on("properties change paste input", function(event) {
                reg = /[^0-9]/gi;
                v = $(this).val();
                phoneReg = false;
                if (v == '') {
                    $('#check_phone').text('');
                } else if (reg.test(v)) {
                    $("#check_phone").text("숫자만 입력가능 합니다.");
                    $(this).val(v.replace(reg, ''));
                    $(this).focus();
                } else {
                    $("#check_phone").text("");
                    phoneReg = true;
                }
                checkAll();
            });

            $("#AccNumber").on("properties change paste input", function(event) {
                reg = /[^0-9]/gi;
                v = $(this).val();
                accountReg = false;
                if (v == '') {
                    $('#check_acc').text('');
                } else if (reg.test(v)) {
                    $("#check_acc").text("숫자만 입력가능 합니다.");
                    $(this).val(v.replace(reg, ''));
                    $(this).focus();
                } else {
                    $("#check_acc").text("");
                    accountReg = true;
                }
                checkAll();
            });

            $('#email').on("properties change paste input", function(event) {
                regexp = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
                v = $(this).val();
                emailReg = false;
                if (true === regexp.test(v)) {
                    $("#check_mail").text("");
                    emailReg = true;
                } else {
                    $("#check_mail").text("이메일 형식을 지켜주세요.");
                    $(this).val(v.replace(regexp, ''));
                }
                checkAll();
            });

            $('#password').on("properties change paste input", function(event) {
                regexp = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
                var v = $("#password").val();
                passwordReg = false;
                if (v.search(/\s/) != -1) {
                    $("#check_pass").text("공백없이 입력해주세요.");
                    return false;
                } else if (v == "") {
                    $("#check_pass").text("");
                } else if (false === regexp.test(v)) {
                    $("#check_pass").text("비밀번호는 8자 이상이어야 하며, 숫자/대문자/소문자/특수문자를 모두 포함해야 합니다.");
                    return;
                } else {
                    passwordReg = true;
                    $("#check_pass").text("");
                }
                checkAll();
            });


            $('#nickname').on("properties change paste input", function(event) {
                regexp = /[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi;
                v = $(this).val();
                nickReg = false;
                if (regexp.test(v)) {
                    $("#check_nickname").text("한글,숫자,영어만 이용하세요.");
                } else {
                    $("#check_nickname").text("");
                    $(this).val(v.replace(regexp, ''));
                    nickReg = true;
                }
                checkAll();
            });

            $('#passwordCheck').on("properties change paste input", function(event) {
                let v = $(this).val();
                let pw = $('#password').val();
                pwCheckReg = false;
                if (v == '') {
                    $('#check_pass_repeat').text('');
                } else if (v != pw) {
                    $('#check_pass_repeat').text('패스워드와 일치해야 합니다.');
                } else if (v == pw) {
                    $('#check_pass_repeat').text('');
                    pwCheckReg = true;
                }
                checkAll();
            });

            $('#birth').on("properties change paste input", function(event) {
                birthCheck = true;
            });
        });