const markerList = [];
const markers = ['/images/icons/mark/redMark.png',
    '/images/icons/mark/blueMark.png',
    '/images/icons/mark/greenMark.png',
    '/images/icons/mark/yellowMark.png',
    '/images/icons/mark/pinkMark.png',
    '/images/icons/mark/grayMark.png']

let map;
let marker;
let setLat;
let setLng;
let infoWindow;

$(function() {
    map = new Tmapv2.Map("map_div", {
        center: new Tmapv2.LatLng(35.8992601, 128.6215081),
        width: "100%",
        height: "400px"
    });

    marker = new Tmapv2.Marker({
        position : new Tmapv2.LatLng(35.8992601, 128.6215081),
        map : map
    });
    map.setZoom(15);

    map.addListener("click", function() {
        if (infoWindow !== undefined) {
            infoWindow.setMap(null);
            infoWindow = undefined;
        }
    });

    map.addListener("drag", onDrag); // 지도 드래그시, 이벤트 리스너 등록.
    ht_map.forEach((h, n) => {
        addMarker(h.category, h.htLng, h.htLat, null);
        markerList[n].addListener("click", function() {
            addInfoWindow(h);
        });
    });

});

function addMarker(category, lon, lat, index) {
    let marker = new Tmapv2.Marker({
        position: new Tmapv2.LatLng(lat,lon),
        icon: markers[Math.floor(category / 10)],
        map: map
    });
    markerList.push(marker);
}

function addInfoWindow(h) {
    let content=
        "<div class='d-flex flex-column rounded infoWindow' style='background: seashell; position: absolute; box-shadow: 5px 5px 5px #00000040;width : 250px;height:250px;top: -300px;left: -125px;align-items: center;'>";
    (h.htImg === null && h.goImg === null) ? content += '<img src="/images/logo.jpg" style="width: 200px; height: 100px" class="shadow-sm bg-white rounded mt-1" alt="이미지가 없습니다.">'
        : (h.htImg != null ? (content += '<img src="/hotpleImage/'+ h.uploadPath.replaceAll('\\', '/') + '/' + h.htImg + '_' + h.fileName + '"' +
        ' style="width: 200px; height: 100px" class="shadow-sm bg-white rounded mt-1" alt="이미지가 없습니다.">')
        : (content += '<img src="' + h.goImg + '" style="width: 200px; height: 100px" class="shadow-sm bg-white rounded mt-1" alt="이미지가 없습니다.">'));
    content +=
        "<div class='mt-2'>"+
        "<p class='m-1'>"+
        "<span style=' font-size: 16px; font-weight: bold;'>" + h.busnName + "</span>"+
        "</p>" +
        "<p class='m-1'>"+
        "<span>"+ h.htAddr + "</span>"+
        "</p>"+
        "<p class='m-1'>"+
        "구글 별점 : <span class='text-warning'>" + (h.goGrd === null ? "없음" : h.goGrd) + "</span>"+
        "<a class='btn btn-sm btn-info ml-3' href='/hotple/" + h.htId + "'>자세히 보기</a>" +
        "</p>";

    if (h.ucode !== null) content += "<p class='m-1'>핫플GO 등록 업체</p>";
    content +=
        "</div>" +
        "<div style='position: absolute;border-style: solid;border-width: 15px 15px 0px;bottom: -15px;border-color: seashell transparent;'></div>" +
        "</div>";

    if (infoWindow !== undefined) {
        infoWindow.setMap(null);
        infoWindow = undefined;
    }



    infoWindow = new Tmapv2.InfoWindow({
        position: new Tmapv2.LatLng(h.htLat, h.htLng), //Popup 이 표출될 맵 좌표
        content: content, //Popup 표시될 text
        border :'0px solid #FF0000', //Popup의 테두리 border 설정.
        type: 2, //Popup의 type 설정.
        map: map //Popup이 표시될 맵 객체
    });
    $('.infoWindow').parent().parent().css('z-index', 999);
    map.setCenter(new Tmapv2.LatLng(h.htLat + 0.005, h.htLng));
    map.setZoom(15);
}

function onDrag() {
    marker.setMap(null);
    marker = new Tmapv2.Marker({
        position : new Tmapv2.LatLng(map.getCenter().lat(), map.getCenter().lng()),
        map : map
    });
}

function myGeo() {
    setLat = map.getCenter().lat();
    setLng = map.getCenter().lng();
    if (marker !== undefined) marker.setMap(null);
    // HTML5의 geolocation으로 사용할 수 있는지 확인합니다
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function(position) {
                let lat = position.coords.latitude;
                let lon = position.coords.longitude;

                marker = new Tmapv2.Marker({
                    position : new Tmapv2.LatLng(lat,lon),
                    map : map
                });
                map.setCenter(new Tmapv2.LatLng(lat,lon));
                map.setZoom(15);
            });
    }
}

function setGeo() {
    setLat = map.getCenter().lat();
    setLng = map.getCenter().lng();
    map.setZoom(15);
    send();
}

function setMyGeo() {
    myGeo();
    send();
}

function buildCards(h, n) {
    let str = "";
    if (n % 4 === 0)
        str += '<div class="row row-cols-1 row-cols-md-2 row-cols-xl-4 m-2">';

    str += // 4 번씩 넣기
        '<div class="col mb-4">' +
        '<div class="card h-100">';

    (h.htImg === null && h.goImg === null) ? str += '<img src="/images/logo.jpg" class="ht-img m-3" alt="이미지가 없습니다.">'
        : (h.htImg != null ? (str += '<img src="/hotpleImage/'+ h.uploadPath.replaceAll('\\', '/') + '/' + h.htImg + '_' + h.fileName + '"' +
        ' class="ht-img m-3" alt="이미지가 없습니다.">')
        : (str += '<img src="' + h.goImg + '" class="ht-img m-3" alt="이미지가 없습니다.">'));
    str +=
        '<div class="card-body">' +
        '<h5 class="card-title">' + h.busnName +'</h5>' +
        '<div class="text-left">' +
        '<span class="font-weight-bold">구글 평점 : </span>' +
        '<span class="text-warning h4">' + (h.goGrd === null ? '없음' : h.goGrd) +'</span>' +
        '</div>' +
        '</div>' +
        '<a href="/hotple/' + h.htId + '" class="stretched-link"></a>' +
        '</div>' +
        '</div>'
    if (n % 4 === 3) str += '</div>';
    return str;
}

var getParameters = function (paramName) {
    // 리턴값을 위한 변수 선언
    let returnValue;

    // 현재 URL 가져오기
    let url = location.href;

    // get 파라미터 값을 가져올 수 있는 ? 를 기점으로 slice 한 후 split 으로 나눔
    let parameters = (url.slice(url.indexOf('?') + 1, url.length)).split('&');

    // 나누어진 값의 비교를 통해 paramName 으로 요청된 데이터의 값만 return
    for (let i = 0; i < parameters.length; i++) {
        let varName = parameters[i].split('=')[0];
        if (varName.toUpperCase() === paramName.toUpperCase()) {
            returnValue = parameters[i].split('=')[1];
            return decodeURIComponent(returnValue);
        }
    }
}

function send() {
    $.ajax({
        type: "POST",
        url: "/setGeo",
        data: {
            lat : setLat,
            lng : setLng,
            keyword: getParameters('keyword')
        },
        success: function(data) {
            let div = $("#hotple_div");
            // div.fadeTo(500, 0.2);
            initMarkerWindow();
            let cards = "";
            data.forEach((h, n) => {
                console.log(h, n);
                addMarker(h.category, h.htLng, h.htLat, null);
                markerList[n].addListener("click", function() {
                    addInfoWindow(h);
                });
                cards += buildCards(h, n);
            });
            div.html(cards);
            // div.fadeTo(500, 1);
        },
        error: function (xhr, status, err) {
            alert(xhr.responseText);
        }
    });
}

function initMarkerWindow() {
    markerList.forEach(i => i.setMap(null));
    markerList.length = 0;
    if (infoWindow !== undefined) {
        infoWindow.setMap(null);
        infoWindow = undefined;
    }
}