$(function() {
    $('.delete_shop').click(function() {
        if(confirm("정말 삭제하시겠습니까?")) {
            let htId = $(this).next().val();
            $.ajax({
                type: "post",
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({
                    htId: htId,
                    htImg: $('#htImg' + htId).val()
                }),
                async: true, //동기, 비동기 여부
                cache : false, // 캐시 여부
                url: "/manager/rest/comp-delete",
                success: function(data, status, xhr) {
                    alert(data);
                    self.location.reload();
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('.update_shop').click(function() {
        let htId = $(this).prev().val();
        let formData = new FormData();
        formData.append("htId", $(this).prev().val());
        formData.append("busnNum", $('#busn-num').val());
        formData.append("busnName", $('#busn-name').val());
        formData.append("htAddr", $('#roadAddrPart1' + htId).val());
        formData.append("htAddrDet", $('#addrDetail' + htId).val());
        formData.append("htZip", $('#zipNo' + htId).val());
        formData.append("htCont", $('#introduction').val());
        formData.append("htTel", $('#phone' + htId).val());
        formData.append("htImg", $('#htImg' + htId).val());
        formData.append("uploadPath", $('#uploadPath' + htId).val());
        formData.append("fileName", $('#fileName' + htId).val());
        formData.append("upload", $('#upload' + htId)[0].files[0]);
        formData.append("category", $('#kindD' + htId).val());
        formData.append("htLng", $('#lng').val());
        formData.append("htLat", $('#lat').val());
        $.ajax({
            type: "post",
            processData: false,
            contentType: false,
            data: formData,
            url: "/manager/rest/comp-update",
            success: function (data, status, xhr) {
                alert(data);
                self.location.reload();
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    });

    $("input[type='file']").change(function() {
        $(this).next().html($(this)[0].files[0].name);
    });
});

let cur_btn = null;

function goPopup(num) {
    // 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
    var pop = window.open("/popup/jusoPopup", "pop", "width=570,height=420, scrollbars=yes, resizable=yes");

    //var pop = window.open("/popup/jusoPopup.jsp","pop","width=570,height=420, scrollbars=yes, resizable=yes");
    // 모바일 웹인 경우, 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrMobileLinkUrl.do)를 호출하게 됩니다.
    //var pop = window.open("/popup/jusoPopup.jsp","pop","scrollbars=yes, resizable=yes");

    cur_btn = num;
}

/** API 서비스 제공항목 확대 (2017.02) **/

function jusoCallBack(roadFullAddr, roadAddrPart1, addrDetail, roadAddrPart2, engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn
    , detBdNmList, bdNm, bdKdcd, siNm, sggNm, emdNm, liNm, rn, udrtYn, buldMnnm, buldSlno, mtYn, lnbrMnnm, lnbrSlno, emdNo, entX, entY, lng, lat) {
    // 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
    $('#roadAddrPart1' + cur_btn).val(roadAddrPart1);
    $('#addrDetail' + cur_btn).val(addrDetail);
    $('#zipNo' + cur_btn).val(zipNo);
    $('#lng').val(lng)
    $('#lat').val(lat)
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

    const target = $(obj).parent().next().children()[0];

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
    } else if ($(obj).val() === "walk") {
        d = walk;
        i += 50;
    }

    $(target).children('option').remove();
    let num = 0;
    for (x in d) {
        let opt = document.createElement("option");
        opt.value = i + num++;
        opt.innerHTML = d[x];
        target.append(opt);
    }
}

function initKind(num, id) {
    if(Math.floor(num / 10) === 0) {
        $('#kindU' + id).val("food");
    } else if (Math.floor(num / 10) === 1) {
        $('#kindU' + id).val("dessert");
    } else if (Math.floor(num / 10) === 2) {
        $('#kindU' + id).val("play");
    } else if (Math.floor(num / 10) === 3) {
        $('#kindU' + id).val("drink");
    } else if (Math.floor(num / 10) === 4) {
        $('#kindU' + id).val("watch");
    } else {
        $('#kindU' + id).val("walk");
    }

    kindChange($('#kindU' + id));
    $('#kindD' + id).val(num);
}