let total_file = 0;
let image_files = [];

let nest_total_file = 0;
let nest_image_files = [];

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
    $(document).on('click','#comment_input',function (){
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
            alert(data);
            reload(recent);
        }).fail(function (error) {
            console.log(error.responseText);
        });
    });

    $(document).on('click','#comment_delete',function (){
        alert("댓글이 삭제되었습니다.");
        location.href = "/board/delete/comment/" + $('#comCode').val();
    });

    $(document).on('click','.write_reply',function (){
        cur_comCode = $(this).next().val();
        $('.reply').remove();
        nest_total_file = 0;
        nest_image_files = [];

        target_mem_id = $(this).next().val();
        target_mem_nickname = $(this).nextAll('input[name=nickname]').val();

        var span_nickname = '<span class="mr-2 bg-success text-white rounded" contenteditable="false">' + target_mem_nickname + '</span><br>';

        if (target_mem_nickname === undefined)
            span_nickname = "";


        var com_id = $(this).parents('.comments').children('form').children('input[name=com_id]').val();
        var reply =
                // '<div id="re-reply-edit" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">' + // 각 id는 해당 댓글의 아이디
            "<div class='reply p-3 d-flex flex-md-row' style='width:80%; margin: 0 auto;'>" +
            "<div class='border border-right-0 border-top-0 border-dark ml-5' style='width: 30px; height: 30px'></div>" +
            '<div id="reUCode" class="w-100"><div class="border border-dark write_comment_top mx-3">' +
            "<div class='ml-3 write_comment_member text-left'>닉네임</div><div class='write_comment_middle text-left' style='width: 98%'>" +
            '<div id="reComCont" class="px-3" contentEditable="true"></div></div></div>' +
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

        $('#nest_comment_image').on("change", nest_handleImageFile);

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
                alert(data);
                reload(recent);
            }).fail(function (error) {
                console.log(error.responseText);
            });

            // var index = 0;
            // for(let i = 0; i < nest_image_files.length; i++) {
            //     if(!$('#nest_img' + i).attr('src')) {
            //         nest_image_files.splice(index--, 1);
            //     }
            //     index++;
            // }
            //
            // var form = new FormData();
            //
            // for(let i = 0; i < nest_image_files.length; i++) {
            //     form.append("file" + i, nest_image_files[i]);
            // }
            //
            // $.ajax({
            //     type: "post",
            //     enctype: 'multipart/form-data',
            //     url: "imageUploadComment.do",
            //     data: form,
            //     processData: false,
            //     contentType: false,
            //     success: function(data) {
            //         var str1 = "<p id='images'>";
            //         var str2 = "image_p_tag</p>";
            //         var loc1 = data.indexOf(str1);
            //         var loc2 = data.indexOf(str2);
            //         var len = str1.length;
            //         var check = data.substr(loc1 + len, loc2 - (loc1 + len));
            //
            //         var check_files = check.split(',');
            //
            //         var index1 = check_files.length - 1;
            //
            //         for(let i = 0; i < nest_total_file; i++) {
            //             if($('#nest_img' + i).attr('src')) {
            //                 $('#nest_img' + i).attr('src', check_files[index1 = index1 - 1].trim());
            //                 $('#nest_img' + i).removeAttr('id');
            //             }
            //         }
            //
            //         var href = window.location.href;
            //         var str = 'bdNum=';
            //         var loc = href.indexOf(str);
            //         var len = str.length;
            //         var get = href.substr(loc + len, href.length);
            //         get = get.replace("#start_com", "");
            //
            //         var notice_content = $('#nestComCont').html().replace(span_nickname.replace('<br>', ""), "").replace(/(<([^>]+)>)/ig,"");
            //
            //         $.ajax({
            //             type: "post",
            //             url: "nestCommentInsert.do",
            //             data: {
            //                 board_id: get,
            //                 target_mem_id: target_mem_id,
            //                 com_id: target_com_id,
            //                 content: $('#nestComCont').html(),
            //                 notice_content: notice_content
            //             },
            //             success: function() {
            //                 $('#comments_div').load("/YeungJinFunnyBone/member/board/comment.jsp?bdNum=" + get);
            //             }
            //         });
            //
            //     }
            // });

        });
    });

    /* -------------------- 리-리댓달기------------------------------ */
    $(document).on('click','.write_re_reply',function (){
        cur_comCode = $(this).next().val();
        $('.reply').remove();
        nest_total_file = 0;
        nest_image_files = [];

        target_mem_id = $(this).next().val();
        target_mem_nickname = $(this).nextAll('input[name=nickname]').val();

        var span_nickname = '<span class="mr-2 bg-success text-white rounded" contenteditable="false">' + target_mem_nickname + '</span><br>';

        if (target_mem_nickname === undefined)
            span_nickname = "";


        var com_id = $(this).parents('.comments').children('form').children('input[name=com_id]').val();
        var reply =
                /*'<div id="re-reply-edit" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">' + // 각 id는 해당 댓글의 아이디*/
            "<div class='reply p-3 d-flex flex-md-row' style='width:80%; margin: 0 auto;'>" +
            "<div class='border border-right-0 border-top-0 border-dark ml-5' style='width: 30px; height: 30px'></div>" +
            '<div id="reUCode" class="w-100"><div class="border border-dark write_comment_top p-3 mx-3">' +
            "<div class='ml-3 write_comment_member text-left'>닉네임</div><div class='write_comment_middle p-2 text-left' style='width:98%'>" +
            '<a class="text-primary" >@' + $(this).parent().parent().find('.fa-user-circle').text() + '</a><div id="reComCont" class="px-3 d-inline" contentEditable="true"></div></div></div>' +
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

        $('#nest_comment_image').on("change", nest_handleImageFile);

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
                alert(data);
                $('#comments_div').load('/board/comment/21040410433804N?sort=recent');
            }).fail(function (error) {
                console.log(error.responseText);
            });
        });
    });

    // 성공
    $('.write_nest_reply').click(function () {
        $('.reply').remove();

        nest_total_file = 0;
        nest_image_files = [];

        target_mem_id = $(this).next().val();
        target_mem_nickname = $(this).nextAll('input[name=nickname]').val();

        var span_nickname = '<span class="mr-2 bg-success text-white rounded" contenteditable="false">' + target_mem_nickname + '</span><br>';

        if (target_mem_nickname === undefined)
            span_nickname = "";

        var com_id = $(this).parents('.nestComments').prevAll('.comments').children('form').children('input[name=com_id]').val();
        var reply = "<div class='reply p-3 d-flex flex-md-row'>" +
            "<div class='border border-right-0 border-top-0 border-dark ml-5' style='width: 30px; height: 30px'></div>" +
            '<div class="w-100"><div class="border border-dark write_comment_top mx-3">' +
            '<div class="ml-3 write_comment_member">' + $('#mem_nickname').text() + '</div><div class="w-100 write_comment_middle">' +
            '<div id="nestComCont" class="px-3" contentEditable="true">' +
            span_nickname +
            '</div></div></div>' +
            '<div class="border border-dark border-top-0 mx-3">' +
            '<div class="w-100 d-flex"><div class="custom-file w-75">' +
            '<input type="file" class="custom-file-input" id="nest_comment_image">' +
            '<label class="custom-file-label" for="nest_comment_image"><i class="fa fa-upload mr-2"></i>이미지 삽입</label></div>' +
            "<input type='hidden' name='com_id' value='" + com_id + "'>" +
            '<button id="nestCommentBtn" class="btn btn-info w-25">작성</button></div></div><div>';

        $(this).parents('.nestComments').after(reply);


        $(".custom-file-input").on("change", function () {
            var fileName = $(this).val().split("\\").pop();
            $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
        });

        $('#nest_comment_image').on("change", nest_handleImageFile);

        $('#nestCommentBtn').click(function () {

            target_com_id = $(this).prevAll('input[name=com_id]').val();

            if (!$('#nestComCont').html()) {
                alert("댓글 내용을 입력해주세요.");
                return false;
            }

            var index = 0;
            for (let i = 0; i < nest_image_files.length; i++) {
                if (!$('#nest_img' + i).attr('src')) {
                    nest_image_files.splice(index--, 1);
                }
                index++;
            }

            var form = new FormData();

            for (let i = 0; i < nest_image_files.length; i++) {
                form.append("file" + i, nest_image_files[i]);
            }

            $.ajax({
                type: "post",
                enctype: 'multipart/form-data',
                url: "imageUploadComment.do",
                data: form,
                processData: false,
                contentType: false,
                success: function (data) {
                    var str1 = "<p id='images'>";
                    var str2 = "image_p_tag</p>";
                    var loc1 = data.indexOf(str1);
                    var loc2 = data.indexOf(str2);
                    var len = str1.length;
                    var check = data.substr(loc1 + len, loc2 - (loc1 + len));

                    var check_files = check.split(',');

                    var index1 = check_files.length - 1;

                    for (let i = 0; i < nest_total_file; i++) {
                        if ($('#nest_img' + i).attr('src')) {
                            $('#nest_img' + i).attr('src', check_files[index1 = index1 - 1].trim());
                            $('#nest_img' + i).removeAttr('id');
                        }
                    }

                    var href = window.location.href;
                    var str = 'bdNum=';
                    var loc = href.indexOf(str);
                    var len = str.length;
                    var get = href.substr(loc + len, href.length);
                    get = get.replace("#start_com", "");

                    var notice_content = $('#nestComCont').html().replace(span_nickname.replace('<br>', ""), "").replace(/(<([^>]+)>)/ig, "");

                    $.ajax({
                        type: "post",
                        url: "nestCommentInsert.do",
                        data: {
                            board_id: get,
                            target_mem_id: target_mem_id,
                            com_id: target_com_id,
                            content: $('#nestComCont').html(),
                            notice_content: notice_content
                        },
                        success: function () {
                            $('#comments_div').load("/YeungJinFunnyBone/member/board/comment.jsp?bdNum=" + get);
                        }
                    });
                }
            });
        });
    });

    $(document).on('click','.recoBtn',function (){
        var com_id = $(this).nextAll('input[name=com_id]').val();
        var btn = $(this);
        var check_reco = $(btn).attr('id') == 'comReco_y';
        var check_nonReco = $(btn).nextAll('.nonRecoBtn').attr('id') == 'comNonReco_y';
        var reco_count = $(btn).nextAll('.reco_count');

        if (check_reco) {
            // 이미 체크가 된 상태에서 눌렀을 시
            // db에서 삭제
            $(btn).attr('id', 'comReco_n');

            $.ajax({
                type: "post",
                url: "insertCommentReco.do",
                data: {
                    com_id: com_id,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco,
                    check_db: true,
                    btn: "reco"
                },
                success: function (data) {
                    var str1 = "<p id='reco'>";
                    var str2 = "reco</p>";
                    var loc1 = data.indexOf(str1);
                    var loc2 = data.indexOf(str2);
                    var len = str1.length;
                    var check = data.substr(loc1 + len, loc2 - (loc1 + len));

                    reco_count.text(check);
                }
            });
        } else if (!check_reco && check_nonReco) {
            // 비추가 된 상태에서 눌렀을 시
            // db 수정
            $(btn).attr('id', 'comReco_y');
            $(btn).nextAll('.nonRecoBtn').attr('id', 'comNonReco_n');

            $.ajax({
                type: "post",
                url: "insertCommentReco.do",
                data: {
                    com_id: com_id,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco,
                    check_db: true,
                    btn: "reco"
                },
                success: function (data) {
                    var str1 = "<p id='reco'>";
                    var str2 = "reco</p>";
                    var loc1 = data.indexOf(str1);
                    var loc2 = data.indexOf(str2);
                    var len = str1.length;
                    var check = data.substr(loc1 + len, loc2 - (loc1 + len));

                    reco_count.text(check);
                }
            });
        } else if (!check_reco) {
            // 체크 안된 상태에서 눌렀을 시
            // db 삽입
            $(btn).attr('id', 'comReco_y');

            $.ajax({
                type: "post",
                url: "insertCommentReco.do",
                data: {
                    com_id: com_id,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco,
                    check_db: false,
                    btn: "reco"
                },
                success: function (data) {
                    var str1 = "<p id='reco'>";
                    var str2 = "reco</p>";
                    var loc1 = data.indexOf(str1);
                    var loc2 = data.indexOf(str2);
                    var len = str1.length;
                    var check = data.substr(loc1 + len, loc2 - (loc1 + len));

                    reco_count.text(check);
                }
            });
        }

    });

    $('.nonRecoBtn').click(function () {
        var com_id = $(this).prevAll('input[name=com_id]').val();
        var btn = $(this);
        var check_nonReco = $(btn).attr('id') == 'comNonReco_y';
        var check_reco = $(btn).prevAll('.recoBtn').attr('id') == 'comReco_y';
        var reco_count = $(btn).prevAll('.reco_count');

        if (check_nonReco) {
            // 이미 체크가 된 상태에서 눌렀을 시
            // db에서 삭제
            $(btn).attr('id', 'comNonReco_n');

            $.ajax({
                type: "post",
                url: "insertCommentReco.do",
                data: {
                    com_id: com_id,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco,
                    check_db: true,
                    btn: "nonReco"
                },
                success: function (data) {
                    var str1 = "<p id='reco'>";
                    var str2 = "reco</p>";
                    var loc1 = data.indexOf(str1);
                    var loc2 = data.indexOf(str2);
                    var len = str1.length;
                    var check = data.substr(loc1 + len, loc2 - (loc1 + len));

                    reco_count.text(check);
                }
            });
        } else if (check_reco && !check_nonReco) {
            // 추천이 된 상태에서 눌렀을 시
            // db 수정
            $(btn).attr('id', 'comNonReco_y');
            $(btn).prevAll('.recoBtn').attr('id', 'comReco_n');

            $.ajax({
                type: "post",
                url: "insertCommentReco.do",
                data: {
                    com_id: com_id,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco,
                    check_db: true,
                    btn: "nonReco"
                },
                success: function (data) {
                    var str1 = "<p id='reco'>";
                    var str2 = "reco</p>";
                    var loc1 = data.indexOf(str1);
                    var loc2 = data.indexOf(str2);
                    var len = str1.length;
                    var check = data.substr(loc1 + len, loc2 - (loc1 + len));

                    reco_count.text(check);
                }
            });
        } else if (!check_nonReco) {
            // 체크 안된 상태에서 눌렀을 시
            // db 삽입
            $(btn).attr('id', 'comNonReco_y');

            $.ajax({
                type: "post",
                url: "insertCommentReco.do",
                data: {
                    com_id: com_id,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco,
                    check_db: false,
                    btn: "nonReco"
                },
                success: function (data) {
                    var str1 = "<p id='reco'>";
                    var str2 = "reco</p>";
                    var loc1 = data.indexOf(str1);
                    var loc2 = data.indexOf(str2);
                    var len = str1.length;
                    var check = data.substr(loc1 + len, loc2 - (loc1 + len));

                    reco_count.text(check);
                }
            });
        }
    });

    $('[data-toggle="tooltip"]').tooltip();

    $('.delete_nest_com').click(function () {
        var conf = confirm("댓글을 삭제하시겠습니까?");

        if (conf) {
            $.ajax({
                type: "post",
                url: "nestCommentDelete.do",
                data: {
                    nest_com_id: $(this).next().val()
                },
                success: function (data) {
                    var str = "<p id='ck'>";
                    var loc = data.indexOf(str);
                    var len = str.length;
                    var check = data.substr(loc + len, 1);

                    if (check == "1") {
                        alert("댓글 삭제를 완료하였습니다.");
                        $('#comments_div').load("/YeungJinFunnyBone/member/board/comment.jsp?bdNum=" + get);
                    } else {
                        window.location.href = "DBFail.do";
                    }
                }
            });
        }
    });

    $(".custom-file-input").on("change", function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });

    $('#comment_image').on("change", handleImageFile);

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

function upload() {

    var index = 0;
    for (let i = 0; i < image_files.length; i++) {
        if (!$('#img' + i).attr('src')) {
            image_files.splice(index--, 1);
        }
        index++;
    }

    var form = new FormData();

    for (let i = 0; i < image_files.length; i++) {
        form.append("file" + i, image_files[i]);
    }

    $.ajax({
        type: "post",
        enctype: 'multipart/form-data',
        url: "/board/rest/comment",
        data: form,
        processData: false,
        contentType: false,
        success: function (data) {
            var str1 = "<p id='images'>";
            var str2 = "image_p_tag</p>";
            var loc1 = data.indexOf(str1);
            var loc2 = data.indexOf(str2);
            var len = str1.length;
            var check = data.substr(loc1 + len, loc2 - (loc1 + len));

            var check_files = check.split(',');

            var index1 = check_files.length - 1;

            for (let i = 0; i < total_file; i++) {
                if ($('#img' + i).attr('src')) {
                    index1 = index1 - 1;
                    document.getElementById('img' + i).setAttribute('src', check_files[index1].trim());
                    document.getElementById('img' + i).removeAttribute('id');
                    $('#img' + i).attr('src', check_files[index1].trim());
                    $('#img' + i).removeAttr('id');
                }
            }

            comment_comple();
        }
    });
}

function comment_comple() {
    var href = window.location.href;
    var str = 'bdNum=';
    var loc = href.indexOf(str);
    var len = str.length;
    var get = href.substr(loc + len, href.length);

    $.ajax({
        type: "POST",
        url: "/board/rest/comment",
        data: {
            board_id: get,
            content: $('#comment_content').html()
        },
        success: function () {
            $('#comments_div').load('/board/comment/21040410433804N?sort=recent');
        }

    });
}

function handleImageFile() {
    if (!$('#comment_image').val())
        return false;

    total_file++;
    image_files.push($('#comment_image')[0].files[0]);

    var reader = new FileReader();

    reader.onload = function (e) {
        var img_html = "<div><br></div><img class='img-fluid img-thumbnail comment_upload' id='img" + (image_files.length - 1) + "' src='" + e.target.result + "' /><div><br></div>";
        $('#comment_content').append(img_html);
    }

    reader.readAsDataURL(image_files[image_files.length - 1]);
}

function nest_handleImageFile() {
    if (!$('#nest_comment_image').val())
        return false;

    nest_total_file++;
    nest_image_files.push($('#nest_comment_image')[0].files[0]);

    var reader = new FileReader();

    reader.onload = function (e) {
        var img_html = "<div><br></div><img class='img-fluid img-thumbnail comment_upload' id='nest_img" + (nest_image_files.length - 1) + "' src='" + e.target.result + "' /><div><br></div>";
        $('#nestComCont').append(img_html);
    }

    reader.readAsDataURL(nest_image_files[nest_image_files.length - 1]);
}

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
        alert(data);
        reload(written);
    }).fail(function (error) {
        console.log(error.responseText);
    });
}

function reload(kind) {
    $('#comments_div').load('/board/comment/' + bdCode + kind);
}