let check_reco = false;
let check_nonReco = false;

let reco;
let nonReco;
let get;

$(function() {
    let bdCode = $('#bdCode').val();
    var href = window.location.href;
    var str = 'bdNum=';
    var loc = href.indexOf(str);
    var len = str.length;
    get = href.substr(loc + len, href.length);
    $('#comments_div').load('/board/comment/' + bdCode + '?sort=reco');

    reco = Number.parseInt($('#reco').text());
    nonReco = Number.parseInt($('#nonReco').text());

    if($('.reco_y').length > 0 || $('.nonReco_y').length > 0) {
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
        var update_check = confirm("글을 수정하시겠습니까?");
        if(update_check){
            location.href = "/board/update/" + $('#bdCode').val();
        }
    });

    $('#reco').click(function() {
        let btn = $(this);
        if($(this).hasClass('reco_y')) {
            // 추천되있는데 취소한경우
            // 데이터베이스에서 지우기
            requestServlet({
                bdCode: bdCode,
                check_reco: check_reco,
                check_nonReco: check_nonReco
            }, "/board/rest/like", function (data) {
                btn.removeClass('reco_y');
                btn.addClass('reco_n');
                btn.css('background', '');
                btn.css('border', '');
                btn.html('<span><i class="fa fa-level-up mr-2"></i>' + --reco + '</span>');
                check_reco = false;
            }, basicErrorFunc); // 여기!
        } else {
            let nonBtn = $('#nonReco');
            if(nonBtn.hasClass('nonReco_y')) {
                // 비추된 상태에서 추천 누르기
                // 데이터베이스 비추 N로 바꾸기
                requestServlet({
                    bdCode: bdCode,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco
                }, "/board/rest/like", function (data) {
                    nonBtn.removeClass('nonReco_y');
                    nonBtn.addClass('nonReco_n');
                    $('#reco').css('background', '#BEF53D');
                    $('#reco').css('border', '3px solid #BEF53D');
                    nonBtn.css('background', '');
                    nonBtn.css('border', '');
                    nonBtn.html('<span>' + --nonReco + '<i class="fa fa-level-down ml-2"></i></span>');
                }, basicErrorFunc);


            } else {
                // 추천 안되있는 상태에서 추천 누르기
                // 데이터베이스 추가
                requestServlet({
                    bdCode: bdCode,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco
                }, "/board/rest/like", function() {}, basicErrorFunc);
            }

            btn.removeClass('reco_n');
            btn.addClass('reco_y');
            btn.html('<span><i class="fa fa-level-up mr-2"></i>' + ++reco + '</span>');
            btn.css('background', '#BEF53D');
            btn.css('border', '3px solid #BEF53D');
            check_reco = true;
            check_nonReco = false;
        }
    });

    $('#nonReco').click(function() {
        let nonBtn = $('#nonReco');
        if(nonBtn.hasClass('nonReco_y')) {
            // 비추되있는데 취소한경우
            // 데이터베이스에서 지우기
            requestServlet({
                bdCode: bdCode,
                check_reco: check_reco,
                check_nonReco: check_nonReco
            }, "/board/rest/unLike", function (data) {
                nonBtn.removeClass('nonReco_y');
                nonBtn.addClass('nonReco_n');
                nonBtn.css('background', '');
                nonBtn.css('border', '');
                nonBtn.html('<span>' + --nonReco + '<i class="fa fa-level-down ml-2"></i></span>');
                check_nonReco = false;
            }, basicErrorFunc);
        } else {
            let btn = $('#reco');
            if(btn.hasClass('reco_y')) {
                // 추천된 상태에서 비추 누르기
                // 데이터베이스 비추 N로 바꾸기
                requestServlet({
                    bdCode: bdCode,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco
                }, "/board/rest/unLike", function (data) {
                    btn.removeClass('reco_y');
                    btn.addClass('reco_n');
                    btn.css('background', '');
                    btn.css('border', '');
                    $('#nonReco').css('background', '#F47070');
                    $('#nonReco').css('border', '3px solid #F47070');
                    btn.html('<span><i class="fa fa-level-up mr-2"></i>' + --reco + '</span>');
                }, basicErrorFunc);


            } else {
                // 비추 안되있는 상태에서 비추 누르기
                // 데이터베이스 추가
                requestServlet({
                    bdCode: bdCode,
                    check_reco: check_reco,
                    check_nonReco: check_nonReco
                }, "/board/rest/unLike", function() {}, basicErrorFunc);
            }

            nonBtn.removeClass('nonReco_n');
            nonBtn.addClass('nonReco_y');
            nonBtn.css('background', '#F47070');
            nonBtn.css('border', '3px solid #F47070');
            nonBtn.html('<span>' + ++nonReco + '<i class="fa fa-level-down ml-2"></i></span>');
            check_reco = false;
            check_nonReco = true;
        }
    });

    $('#bookmark').click(function() {
        if($(this).hasClass('bookmark_n')) {
            $(this).removeClass('bookmark_n');
            $(this).addClass('bookmark_y')

            requestServlet({
                bookmark: "Y",
                bdCode: bdCode
            }, "/board/rest/bookmark", function() {}, basicErrorFunc);

        } else {
            $(this).removeClass('bookmark_y');
            $(this).addClass('bookmark_n')

            requestServlet({
                bookmark: "N",
                bdCode: bdCode
            }, "/board/rest/bookmark", function() {}, basicErrorFunc);
        }
    });

    $('.go_login').click(function() {
        window.location.href = '/login';
    });


    $('#delete').click(function() {
        let delete_check = confirm("글을 삭제하시겠습니까?");

        if (delete_check) {
            requestServlet({
                bdCode: bdCode
            }, "/board/rest/delete", function (data) {
                swal("삭제 완료!", data, "success").then(() => location.href = "/board/list");
            });
        }
    });


    $('[data-toggle="tooltip"]').tooltip();
});