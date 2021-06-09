const menu = [];
let addFive = false;
var payTitle;
var payPrice;
var payName;
var payZipNo;
var payAddr;
var odNum;

$(function () {
    $('.datetimepicker').datetimepicker({
        step: 5,
        lang: 'ko',
        format: 'Y-m-d H:i',
        theme: 'dark'
    });

    $(document).on('click', '.menu-select-btn', function () {
        let menuTitle = $(this).find('.menu-title').text();
        let menuPrice = $(this).find('.menu-price').text();
        let menuCode = $(this).find('.menu-code').val();
        addMenu(menuCode, menuTitle, menuPrice);
        changeTotalPrice();
        $('#empty-cart').hide();
    });

    $(document).on('click', '.btn-plus', function () {
        let list = $(this).parents('.list-group-item');
        if (list.length === 0) {
            let cnt = +$(this).prev().text();
            if (cnt < 4) {
                $(this).prev().text(cnt + 1);
            } else if (cnt === 4 && !addFive) {
                swal("5인 집합의 위험성이 있습니다.", "그래도 강행 하시겠습니까?", "warning", {
                    buttons: {
                        cancel: "아니오.",
                        yes: "네."
                    }
                }).then(v => {
                    if (v === "yes") {
                        addFive = true;
                        $(this).prev().text(cnt + 1);
                    }
                });
            } else if (cnt === 9) {
                swal("10명 이상은 불가능 합니다.", null, "error");
                return;
            } else if (addFive) {
                $(this).prev().text(cnt + 1);
            }
        } else {
            let menuCode = list.find('input').val();
            $('span[menu-num=' + menu[menuCode][0] + ']').text(++menu[menuCode][2]);
            changeTotalPrice();
        }
    });

    $(document).on('click', '.btn-minus', function () {
        let list = $(this).parents('.list-group-item');
        if (list.length === 0) {
            let cnt = +$(this).next().text();
            if (cnt > 1) {
                $(this).next().text(cnt - 1);
            }
        } else {
            let menuCode = list.find('input').val();
            if (menu[menuCode][2] > 1) {
                $('span[menu-num=' + menu[menuCode][0] + ']').text(--menu[menuCode][2]);
                changeTotalPrice();
            }
        }
    });

    $(document).on('click', '.btn-delete', function () {
        let list = $(this).parents('.list-group-item');
        let val = list.find('input').val();
        menu.splice(menu.indexOf(val), 1);
        menu[val] = null;
        list.remove();
        if (menu.length === 0) $('#empty-cart').show();
    });

    $('#reservation-btn').click(function () {
        let sum = 0;
        menu.forEach(i => sum += menu[i][1] * menu[i][2]);

        $('#reservation-confirm').html('');
        $('#reservation-total-price').text('총합 : ' + sum.toLocaleString('ko-KR') + "원");
        menu.forEach(i => {
            let div =
                '<div class="d-flex">' +
                '<h6 class="col">' + menu[i][0] + '</h6>' +
                '<h6 class="col">' + menu[i][1].toLocaleString('ko-KR') + '</h6>' +
                '<h6 class="col">' + menu[i][2] + '개</h6>' +
                '</div>'
            $('#reservation-confirm').append(div);
        });
    });

    $('#submit_reservation').click(function () {
        let popup = window.open("/payments", "_blank",
            "toolbar=no,scrollbars=no,resizable=no,top=" +
            (screen.availHeight - 500) / 2 +
            ",left=" +
            (screen.availWidth - 400) / 2 +
            ",width=400,height=500");
        let firMenu = menu[menu[0]][0];
        payTitle = menu.length > 1 ? firMenu + "외 " + (menu.length - 1) + "개" : firMenu;
        payPrice = 0;
        menu.forEach(i => payPrice += menu[i][1] * menu[i][2]);
    });

    $('.course').click(function () {
        let csCode = $(this).next().val();
        swal("해당 코스에 핫플을 추가하시겠습니까?",
            {
                buttons: {
                    cancel: "아니요!",
                    add: "네!"
                },
            }).then((v) => {
            if (v === "add") {
                requestServlet({
                        csCode: csCode,
                        htId: $('#htId').val()
                    }, '/add-in-course',
                    function (data) {
                        swal("추가 성공!", data, "success", {
                            buttons: {
                                cancel: "닫기",
                                go: "코스 보기"
                            }
                        }).then((v) => {
                            console.log(v);
                            if (v === "go") location.href = "/courseDetail/" + csCode.replaceAll('/', '');
                            if (v === null) {
                                let str = "";
                                courses[csCode].forEach((i, n) => {
                                    str += (n + 1) + " 번째 " + i.busnName + "\n---  " + i.htAddr + "  ---\n";
                                });
                                str += "\n현재 핫플 : " + $('.rd-header__rst-name-main').text() + "\n"
                                swal({
                                    title: "코스 상황",
                                    text: str
                                });
                            }
                        })
                    }, basicErrorFunc)
            }
        });
    });

    $('#name').on('properties change paste input', function () {
        payName = $(this).val();
        if (payName === "" || $('#roadAddr').length === 0 || $('#zipNo').length === 0) {
            $('#submit_reservation').prop('disabled', 'true');
        } else {
            $('#submit_reservation').prop('disabled', '');
        }
    });

    $('#pickBtn').click(function () {
        let htId = $('#htId').val();
        requestServlet({
            htId: htId
        }, "/pick-hotple", function (data) {
            swal({
                title: "찜 완료!",
                text: data,
                icon: "success",
                button: "확인"
            })
        }, basicErrorFunc);
    });
});

function addMenu(menuCode, menuTitle, menuPrice) {
    let price = +menuPrice.replace('원', '').replaceAll(',', '');
    if (!menu[menuCode]) {
        menu.push(menuCode);
        list_append(menuCode, menuTitle, menuPrice);
        menu[menuCode] = [menuTitle, price, 1];
    } else {
        $('span[menu-num=' + menu[menuCode][0] + ']').text(++menu[menuCode][2]);
    }
}

function list_append(menuCode, menuTitle, menuPrice) {
    let div =
        '<li class="list-group-item">' +
        '<input type="hidden" value="' + menuCode + '"/>' +
        '<div class="cart-item">' +
        '<div class="menu-name">' + menuTitle + '</div>' +
        '<div class="d-flex item-detail">' +
        '<div class="col-xs-6 pull-left flex-center">' +
        '<i class="btn btn-delete far fa-trash-alt flex-center"></i>' +
        '<span class="order-price" menu-type="' + menuTitle + '">' + menuPrice + '</span>' +
        '</div>' +
        '<div class="col-xs-6 pull-right flex-center">' +
        '<i class="btn btn-minus far fa-minus-square flex-center"></i>' +
        '<span class="order-num" menu-num="' + menuTitle + '" name="num">1</span>' +
        '<i class="btn btn-plus far fa-plus-square flex-center"></i>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</li>';
    $('.list-container').append(div);
}

var test = function test() {
    let riTime = $('#reservation-time').val();
    let riPerson = +$('#riPerson').text();
    if (menu.length === 0 || riPerson < 1
        || riTime === "" || odNum === undefined) {
        return;
    }

    let meCodes = [];
    let meNums = [];
    menu.forEach(l => {
        meCodes.push(l);
        meNums.push(menu[l][2]);
    })
    requestParams({
            meCode: meCodes,
            rsMeNum: meNums,
            riTime: riTime + ":00",
            riPerson: riPerson,
            riOdNum: odNum,
            riCont: $('#reservation-request').val()
        }, "/reservation-complete",
        function (data) {
            swal("예약 성공!", data, "success", {
                buttons: {
                    course: "코스 조회",
                    reservation: "예약 조회",
                    cancel: "닫기"
                }
            }).then(v => {
                if (v === null) $('#reservation-modal').modal('hide');
                else if (v === "course") location.href = "/myCourse";
                else if (v === "reservation") location.href = "/reservation";
            });
        }, basicErrorFunc);
}

function changeTotalPrice() {
    let sum = 0;
    menu.forEach(i => sum += menu[i][1] * menu[i][2]);
    $('#cart-total-price').text(sum.toLocaleString('ko-KR') + "원");
}

function goPopup(num) {
    // 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
    var pop = window.open("/popup/jusoPopup", "pop", "width=570,height=420, scrollbars=yes, resizable=yes");

    //var pop = window.open("/popup/jusoPopup.jsp","pop","width=570,height=420, scrollbars=yes, resizable=yes");
    // 모바일 웹인 경우, 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrMobileLinkUrl.do)를 호출하게 됩니다.
    //var pop = window.open("/popup/jusoPopup.jsp","pop","scrollbars=yes, resizable=yes");
}

/** API 서비스 제공항목 확대 (2017.02) **/

function jusoCallBack(roadFullAddr, roadAddrPart1, addrDetail, roadAddrPart2, engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn
    , detBdNmList, bdNm, bdKdcd, siNm, sggNm, emdNm, liNm, rn, udrtYn, buldMnnm, buldSlno, mtYn, lnbrMnnm, lnbrSlno, emdNo, entX, entY, lng, lat) {
    // 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
    let addr = $('#addr_btn');
    payAddr = roadAddrPart1;
    payZipNo = zipNo;
    addr.after("<input type='text' id='roadAddr' class='form-control editing' readonly value='" + zipNo + "'>");
    addr.after("<input type='text' id='zipNo' class='form-control editing' readonly value='" + roadAddrPart1 + "'>");
    if (payName !== "") $('#submit_reservation').prop('disabled', '');
}