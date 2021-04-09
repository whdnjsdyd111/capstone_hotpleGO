const markerList = [];

let map;
let marker;
let setLat;
let setLng;
$(function() {
    map = new Tmapv2.Map("map_div", {
        center: new Tmapv2.LatLng(37.52084364186228,127.058908811749),
        width: "100%",
        height: "400px"
    });

    map.addListener("drag", onDrag); // 지도 드래그시, 이벤트 리스너 등록.
    myGeo();
    ht_map.forEach(i => {
        addMarker(null, i.htLng, i.htLat, null);
    });


});

function addMarker(status, lon, lat, index) {
    let marker = new Tmapv2.Marker({
        position: new Tmapv2.LatLng(lat,lon),
        icon: 'https://icons.iconarchive.com/icons/paomedia/small-n-flat/24/map-marker-icon.png',
        map: map
    });
    markerList.push(marker);
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

    h.htImg === null ? str += '<img src="/images/logo.jpg" class="ht-img m-3" alt="이미지가 없습니다.">'
        : str += '<img src="/hotpleImage/'+ h.uploadPath.replaceAll('\\', '/') + '/' + h.htImg + '_' + h.fileName + '" class="ht-img m-3">';

    str +=
        '<div class="card-body">' +
        '<h5 class="card-title">' + h.busnName +'</h5>' +
        '<div class="text-left">' +
        '<span class="font-weight-bold">구글 평점 : </span>' +
        '<span class="text-warning h4">' + (h.goGrd === null ? '' : h.goGrd) +'</span>' +
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
            markerList.forEach(i => i.setMap(null));
            let cards = "";
            data.forEach((h, n) => {
                console.log(h, n)
                addMarker(null, h.htLng, h.htLat, null)
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