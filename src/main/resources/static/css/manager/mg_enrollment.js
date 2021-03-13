const swiper = new Swiper('.swiper-container', {
    loop: false,
    pagination: {
        el: '.swiper-pagination',
    },
    touchRatio: 0
});

function next_slide() {
    swiper.slideNext();
}

function prev_slide() {
    swiper.slidePrev();
}

function check_first() {
    if (($('#business-name').val().trim() != "") && ($('#business-id').val().trim() != "")) {
        next_slide();
    }
}

function check_second() {
    if ($('#kindU').val() != "선택해주세요") {
        next_slide();
    }
}

function check_third() {
    next_slide();
}

/////////////////스와이퍼 관련 끝
function kindChange(e) {
    const food = ["한식", "일식", "중식", "양식", "분식", "패스트푸드", "뷔페"];
    const dessert = ["커피", "차/음료", "베이커리", "빙수"];
    const play = ["게임", "방탈출", "VR", "노래방", "스포츠", "쇼핑", "만들기", "놀이공원"];
    const drink = ["맥주", "소주", "막걸리", "칵테일/와인"];
    const watch = ["영화", "전시회", "책", "공연", "스포츠"];
    const walk = ["테마", "문화재", "풍경", "시장", "공원"];
    let i = 0;
    const target = document.getElementById("kindD");

    if (e.value == "food") {
        var d = food;
    } else if (e.value == "dessert") {
        var d = dessert;
        i += 10;
    } else if (e.value == "play") {
        var d = play;
        i += 20;
    } else if (e.value == "drink") {
        var d = drink;
        i += 30;
    } else if (e.value == "watch") {
        var d = watch;
        i += 40;
    } else if (e.value == "walk") {
        var d = walk;
        i += 50;
    }

    target.options.length = 0;

    let num = 0;
    for (x in d) {
        let opt = document.createElement("option");
        opt.value = i + num++;
        opt.innerHTML = d[x];
        target.appendChild(opt);
    }
}

/////////////// select 관련 끝


// opener관련 오류가 발생하는 경우 아래 주석을 해지하고, 사용자의 도메인정보를 입력합니다. ("팝업API 호출 소스"도 동일하게 적용시켜야 합니다.)
//document.domain = "abc.go.kr";

function goPopup() {
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
    $('#roadAddrPart1').val(roadAddrPart1);
    $('#roadAddrPart2').val(roadAddrPart2);
    $('#addrDetail').val(addrDetail);
    $('#zipNo').val(zipNo);
    $('#lat').val(lat);
    $('#lng').val(lng);
}

$(function () {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $('#register').click(function () {
        let formData = new FormData();
        formData.append("busnNum", $('#business-id').val());
        formData.append("busnName", $('#business-name').val());
        formData.append("htAddr", $('#roadAddrPart1').val());
        formData.append("htAddrDet", $('#addrDetail').val());
        formData.append("htZip", $('#zipNo').val());
        formData.append("htCont", $('#intro').val());
        formData.append("htTel", $('#tel').val());
        formData.append("upload", $('#pic')[0].files[0]);
        formData.append("htLat", $('#lat').val());
        formData.append("htLng", $('#lng').val());
        formData.append("category", $('#kindD').val());
        $.ajax({
            type: "post",
            beforeSend: function (xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            processData: false,
            contentType: false,
            dataType: 'json',
            data: formData,
            url: "/manager/rest/comp-erm",
            success: function (data, status, xhr) {
                alert(data);
                self.location.reload();
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    })
});