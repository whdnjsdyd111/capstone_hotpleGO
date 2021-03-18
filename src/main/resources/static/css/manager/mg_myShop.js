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
    $('#addrDetail').val(addrDetail);
    $('#zipNo').val(zipNo);
}

function changeText (btn) {
    if($(btn).html() === "펼치기") {
        $(btn).html("접기");
    } else {
        $(btn).html("펼치기");
    }
}

function kindChange(obj) {
    const food = ["한식", "일식", "중식", "양식", "분식", "패스트푸드", "뷔페"];
    const dessert = ["커피", "차/음료", "베이커리", "빙수"];
    const play = ["게임", "방탈출", "VR", "노래방", "스포츠", "쇼핑", "만들기", "놀이공원"];
    const drink = ["맥주", "소주", "막걸리", "칵테일/와인"];
    const watch = ["영화", "전시회", "책", "공연", "스포츠"];
    const walk = ["테마", "문화재", "풍경", "시장", "공원"];
    let i = 0;
    let d;

    const target = $('#kindD');

    if ($(obj).val() === "food") {
        d = food;
    } else if ($(obj).val() === "dessert") {
        d = dessert;
        i += 10;
    } else if ($(obj).val() === "play") {
        d = play;
        i += 20;
    } else if ($(obj).val() === "drink") {
        d = drink;
        i += 30;
    } else if ($(obj).val() === "watch") {
        d = watch;
        i += 40;
    } else if (obj.val() === "walk") {
        d = walk;
        i += 50;
    }

    target.children('option').remove();
    let num = 0;
    for (x in d) {
        let opt = document.createElement("option");
        opt.value = i + num++;
        opt.innerHTML = d[x];
        target.append(opt);
    }
}

function initKind(num) {
    let n = Number.parseInt(num);
    alert(n / 10);
    if(n / 10 === 0) {
        $('#kindU').val("food");
    } else if (n / 10 === 1) {
        $('#kindU').val("dessert");
    } else if (n / 10 === 2) {
        $('#kindU').val("play");
    } else if (n / 10 === 3) {
        $('#kindU').val("drink");
    } else if (n / 10 === 4) {
        $('#kindU').val("watch");
    } else {
        $('#kindU').val("walk");
    }

    kindChange($('#kindU'));
    $('#kindD').val(n);
}