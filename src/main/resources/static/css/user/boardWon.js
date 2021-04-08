var check_reco = false;
var check_nonReco = false;
var check_db_reco = false;

var reco;
var nonReco;
var get;

$(function() {
    var href = window.location.href;
    var str = 'bdNum=';
    var loc = href.indexOf(str);
    var len = str.length;
    get = href.substr(loc + len, href.length);
    $('#comments_div').load('/board/comment/21040410433804N?sort=reco');
    // $('#comments_div').load('/board/comment/21040410433804N?sort=recent');
    // $('#comments_div').load('/board/comment/21040410433804N?sort=writen');

    reco = Number.parseInt($('#reco').text());
    nonReco = Number.parseInt($('#nonReco').text());

    if($('.reco_y').length > 0 || $('.nonReco_y').length > 0) {
        check_db_reco = true

        if($('.reco_y').length > 0)
            check_reco = true;
        else
            check_nonReco = true;

    }

    if($('.bookmark_y').length > 0) {
        $('#bookmark').prop('checked', true);
        check_bookmark = true;
        check_db_bookmark = true;
    }

    $('#update').click(function() {
        location.href = "/board/update/" + $('#bdCode').val();
    })

    $('#delete').click(function () {
        alert("게시글이 삭제되었습니다.")
        location.href = "/board/delete/" + $('#bdCode').val();
    });

    $('#reco').click(function() {
        if($('#reco').attr('class') == 'btn btn-lg btn-outline-secondary mx-2 reco_y') {
            $('#reco').attr('class', 'btn btn-lg btn-outline-secondary mx-2 reco_n');
            $('#reco').html('<span><i class="fa fa-level-up mr-2"></i>' + --reco + '</span>');
            check_reco = false;
            // 추천되있는데 취소한경우
            // 데이터베이스에서 지우기

            $.ajax({
                type: "POST",
                url: "board/delete/",
                data: {
                    check_reco: check_reco,
                    check_nonReco: check_nonReco,

                    check_db_reco: check_db_reco,
                    board_id: get,
                    reco: reco,
                    nonReco: nonReco
                }
            });

            check_db_reco = false;
        } else {
            $('#reco').attr('class', 'btn btn-lg btn-outline-secondary mx-2 reco_y');
            $('#reco').html('<span><i class="fa fa-level-up mr-2"></i>' + ++reco + '</span>');
            check_reco = true;



            if($('#nonReco').attr('class') == 'btn btn-lg btn-outline-secondary mx-2 nonReco_y') {
                $('#nonReco').attr('class', 'btn btn-lg btn-outline-secondary mx-2 nonReco_n');
                $('#nonReco').html('<span>' + --nonReco + '<i class="fa fa-level-down ml-2"></i></span>');
                check_nonReco = false;

                // 비추된 상태에서 추천 누르기
                // 데이터베이스 비추 N로 바꾸기

                $.ajax({
                    type: "POST",
                    url: "/board/recoInsertDelete",
                    data: {
                        check_reco: check_reco,
                        check_nonReco: check_nonReco,

                        check_db_reco: check_db_reco,
                        board_id: get,
                        reco: reco,
                        nonReco: nonReco
                    }
                });


            } else {
                // 추천 안되있는 상태에서 추천 누르기
                // 데이터베이스 추가
                $.ajax({
                    type: "POST",
                    url: "recoInsertDelete.do",
                    data: {
                        check_reco: check_reco,
                        check_nonReco: check_nonReco,

                        check_db_reco: check_db_reco,
                        board_id: get,
                        reco: reco,
                        nonReco: nonReco
                    }
                });

                check_db_reco = true;
            }
        }
    });

    $('#nonReco').click(function() {
        if($('#nonReco').attr('class') == 'btn btn-lg btn-outline-secondary mx-2 nonReco_y') {
            $('#nonReco').attr('class', 'btn btn-lg btn-outline-secondary mx-2 nonReco_n');
            $('#nonReco').html('<span>' + --nonReco + '<i class="fa fa-level-down ml-2"></i></span>');
            check_nonReco = false;

            // 비추눌러진 상태에서 취소하기
            // 데이터베이스에서 삭제

            $.ajax({
                type: "POST",
                url: "recoInsertDelete.do",
                data: {
                    check_reco: check_reco,
                    check_nonReco: check_nonReco,

                    check_db_reco: check_db_reco,
                    board_id: get,
                    reco: reco,
                    nonReco: nonReco
                }
            });

            check_db_reco = false;

        } else {
            $('#nonReco').attr('class', 'btn btn-lg btn-outline-secondary mx-2 nonReco_y');
            $('#nonReco').html('<span>' + ++nonReco + '<i class="fa fa-level-down ml-2"></i></span>');
            check_nonReco = true;

            if($('#reco').attr('class') == 'btn btn-lg btn-outline-secondary mx-2 reco_y') {
                $('#reco').attr('class', 'btn btn-lg btn-outline-secondary mx-2 reco_n');
                $('#reco').html('<span><i class="fa fa-level-up mr-2"></i>' + --reco + '</span>');
                check_reco = false;

                $.ajax({
                    type: "POST",
                    url: "recoInsertDelete.do",
                    data: {
                        check_reco: check_reco,
                        check_nonReco: check_nonReco,

                        check_db_reco: check_db_reco,
                        board_id: get,
                        reco: reco,
                        nonReco: nonReco
                    }
                });


            } else {
                // 비추 안눌러진 상태에서 비추 누르기
                // 데이터베이스 추가
                $.ajax({
                    type: "POST",
                    url: "recoInsertDelete.do",
                    data: {
                        check_reco: check_reco,
                        check_nonReco: check_nonReco,

                        check_db_reco: check_db_reco,
                        board_id: get,
                        reco: reco,
                        nonReco: nonReco
                    }
                });

                check_db_reco = true;
            }
        }


    });

    $('#bookmark').click(function() {
        if($('#bookmark').attr('class') == 'heart_box bookmark_y') {
            $('#bookmark').attr('class', 'heart_box bookmark_n');

            $.ajax({
                type: "POST",
                url: "bookmarkInsertDelete.do",
                data: {
                    board_id: get,
                    way: "delete"
                }
            });

        } else {
            $('#bookmark').attr('class', 'heart_box bookmark_y');

            $.ajax({
                type: "POST",
                url: "bookmarkInsertDelete.do",
                data: {
                    board_id: get,
                    way: "insert"
                }
            });
        }
    });

    $('.go_login').click(function() {
        window.location.href = 'loginForm.do';
    });

    // $('#board_delete').click(function() {
    //     var delete_check = confirm("글을 삭제하시겠습니까?");
    //
    //     if(delete_check) {
    //         $.ajax({
    //             type: "POST",
    //             url: "boardDelete.do",
    //             data: {
    //                 board_id: get
    //             },
    //             success: function(data) {
    //                 var str = "<p id='ck'>";
    //                 var loc = data.indexOf(str);
    //                 var len = str.length;
    //                 var check = data.substr(loc + len, 1);
    //
    //                 if(check == "1") {
    //                     window.location.href = "mainBoard.do?kind=all&sort=recent";
    //                 } else {
    //                     alert("글을 삭제하지 못했습니다.");
    //                     window.location.href = "DBFail.do";
    //                 }
    //             }
    //         });
    //     }
    // });

    $('#board_update').click(function() {
        // let data = {
        //     bdTitle: $('#bdTitle').val(),
        //     bdCont: $('#bdCont').val(),
        //     bdCode: $('#bdCode').val()
        // };
        //
        // $.ajax({
        //     type: 'POST',
        //     url: '/board/update/' + data.bdCode,
        //     contentType:'application/json; charset=utf-8',
        //     data: JSON.stringify(data)
        // }).done(function (data){
            window.location.href = "/board/list";
        // });
    });

    $('[data-toggle="tooltip"]').tooltip();
});