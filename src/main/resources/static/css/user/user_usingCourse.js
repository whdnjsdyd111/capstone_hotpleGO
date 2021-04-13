// 0. 인덱스 색, 총 거리, 위도 경고 배열
const colors = [
    '#e12717',
    '#ffa400',
    '#00dd1c',
    '#1E90FF',
    '#003399',
    '#6B66FF',
    '#FFB2F5',
];
let total_distance = 0;
const latLng = [];

// 1. 지도 띄우기
let map;
let userMarker;

const new_polyLine = [];

function drawData(data){
    // 지도위에 선은 다 지우기
    routeData = data;
    let resultStr = "";
    let distance = 0;
    let idx = 1;
    let newData = [];
    let equalData = [];
    let pointId1 = "-1234567";
    let ar_line = [];

    for (let i = 0; i < data.features.length; i++) {
        let feature = data.features[i];
        //배열에 경로 좌표 저잘
        if(feature.geometry.type == "LineString"){
            ar_line = [];
            for (let j = 0; j < feature.geometry.coordinates.length; j++) {
                let startPt = new Tmapv2.LatLng(feature.geometry.coordinates[j][1],feature.geometry.coordinates[j][0]);
                ar_line.push(startPt);
                pointArray.push(feature.geometry.coordinates[j]);
            }
            let polyline = new Tmapv2.Polyline({
                path: ar_line,
                strokeColor: "#ff0000",
                strokeWeight: 6,
                map: map
            });
            new_polyLine.push(polyline);
        }
        let pointId2 = feature.properties.viaPointId;
        if (pointId1 != pointId2) {
            equalData = [];
            equalData.push(feature);
            newData.push(equalData);
            pointId1 = pointId2;
        } else {
            equalData.push(feature);
        }
    }
    geoData = newData;
    let markerCnt = 1;
    for (let i = 0; i < newData.length; i++) {
        let mData = newData[i];
        let type = mData[0].geometry.type;
        let pointType = mData[0].properties.pointType;
        let pointTypeCheck = false; // 경유지 일때만 true
        if (mData[0].properties.pointType == "S") {
            let img = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_s.png';
            let lon = mData[0].geometry.coordinates[0];
            let lat = mData[0].geometry.coordinates[1];
        } else if (mData[0].properties.pointType == "E") {
            let img = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_e.png';
            let lon = mData[0].geometry.coordinates[0];
            let lat = mData[0].geometry.coordinates[1];
        } else {
            markerCnt=i;
            let lon = mData[0].geometry.coordinates[0];
            let lat = mData[0].geometry.coordinates[1];
        }
    }
}

// 2. 시작, 도착 심볼찍기

const markerList = [];
const pointArray = [];

function addMarker(status, lon, lat, index) {
    let marker = new Tmapv2.Marker({
        position: new Tmapv2.LatLng(lat,lon),
        iconHTML: '<p class="m-index font-weight-bold" style="background-color: ' + colors[index - 1] + '">' + index + '</p>',
        map: map
    });
    //Marker에 클릭이벤트 등록.
    marker.addListener("click", function(evt) {

    });
    return marker;
}

let passList = "";  // 경로탐색 리스트

// 4. 경로탐색 API 사용요청
let prtcl;
let headers = {};
headers["appKey"]='l7xxcd9bdd0942b542fd8c7be931fc11a3b4';


let startMyGeo = null;

$(function() {
    // 0. 위도 경도
    htList.forEach(i => {
        latLng.push({
            lat : i.htLat,
            lng : i.htLng
        });
    });

    $('.c-index').each(i => {
        $($('.c-index')[i]).css('background', colors[i]);
    });

    // 1. 지도 띄우기
    map = new Tmapv2.Map("map_div", {
        center: new Tmapv2.LatLng(latLng[0].htLat, latLng[0].htLng),
        width: "100%",
        height: "400px"
    });

    // 2. 시작, 도착 심볼찍기

    // 시작
    addMarker("llStart", latLng[0].lng, latLng[0].lat, 1);
    // 도착
    addMarker("llEnd", latLng[latLng.length - 1].lng, latLng[latLng.length - 1].lat, latLng.length);

    // 3. 경유지 심볼 찍기
    latLng.forEach((i, n) => {
        if (!(n === 0 || n === latLng.length - 1)) {
            addMarker("llPass", i.lng, i.lat, n + 1);
            passList += i.lng + "," + i.lat;
            if (n !== latLng.length - 2) passList += "_";
        }
    });

    // 4. 경로탐색 API 사용요청
    $.ajax({
        method:"POST",
        headers : headers,
        url:"https://apis.openapi.sk.com/tmap/routes?version=1&format=json",//
        async:false,
        data:{
            startX : latLng[0].lng,
            startY : latLng[0].lat,
            endX : latLng[latLng.length - 1].lng,
            endY : latLng[latLng.length - 1].lat,
            passList : passList,
            reqCoordType : "WGS84GEO",
            resCoordType : "WGS84GEO",
            angle : "172",
            searchOption : "0",
            trafficInfo : "Y"
        },
        success:function(response){
            prtcl = response;

            // 5. 경로탐색 결과 Line 그리기
            let trafficColors = {
                extractStyles:true,
                /* 실제 교통정보가 표출되면 아래와 같은 Color로 Line이 생성됩니다. */
                trafficDefaultColor:"#636f63", //Default
                trafficType1Color:"#19b95f", //원할
                trafficType2Color:"#f15426", //지체
                trafficType3Color:"#ff970e"  //정체
            };
            let style_red = {
                fillColor:"#FF0000",
                fillOpacity:0.2,
                strokeColor: "#FF0000",
                strokeWidth: 3,
                strokeDashstyle: "solid",
                pointRadius: 2,
                title: "this is a red line"
            };
            drawData(prtcl);


            // 6. 경로탐색 결과 반경만큼 지도 레벨 조정
            let newData = geoData[0];
            PTbounds = new Tmapv2.LatLngBounds();
            let latlon;
            for (let i = 0; i < newData.length; i++) {
                let mData = newData[i];
                let type = mData.geometry.type;
                let pointType = mData.properties.pointType;
                if(type == "Point"){
                    let linePt = new Tmapv2.LatLng(mData.geometry.coordinates[1],mData.geometry.coordinates[0]);
                    if (latlon === undefined) {
                        latlon = linePt;
                    } else {
                        total_distance += latlon.distanceTo(linePt);
                        latlon = linePt;
                    }
                    PTbounds.extend(linePt);
                } else{
                    let startPt,endPt;
                    for (let j = 0; j < mData.geometry.coordinates.length; j++) {
                        let linePt = new Tmapv2.LatLng(mData.geometry.coordinates[j][1],mData.geometry.coordinates[j][0]);
                        console.log(linePt);
                        if (latlon === undefined) {
                            latlon = linePt;
                        } else {
                            total_distance += latlon.distanceTo(linePt);
                            latlon = linePt;
                        }
                        PTbounds.extend(linePt);
                    }
                }
            }
            console.log("총 거리 : " + total_distance + " m");
            map.fitBounds(PTbounds);
        },
        error:function(request,status,error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });

    $('#total_distance').text(Math.ceil(total_distance) / 1000 + " km");

    $('.delete-hotple').click(function() {
        swal("핫플 삭제", "이 코스에서 삭제하시겠습니까?", "warning", {
            buttons: {
                cancel : "취소",
                yes : "삭제"
            },
        }).then(v => {
            if (v === "yes") requestServlet({
                csCode : htList[0].csCode,
                htId : $(this).next().val()
            }, "/delete-hotple-in-course", function(data) {
                swal("삭제하였습니다!", data, "success").then(() => location.reload());
            }, basicErrorFunc);
        });
    });

    $('#course_delete').click(function() {
        swal("코스 삭제", "추가된 핫플들과 코스를 삭제하시겠습니까?", "warning", {
            buttons: {
                cancel : "취소",
                yes : "삭제",
            },
        }).then(v => {
            if (v === "yes") requestServlet({
                csCode : htList[0].csCode,
            }, "/delete-course", function(data) {

            }, basicErrorFunc);
        })
    })

    userMarker = new Tmapv2.Marker({
        position : new Tmapv2.LatLng(userLat, userLong),
        iconHTML: '<p class="m-index font-weight-bold" style="background-color: #1a252f"></p>',
        map : map
    });

    setInterval(function() {
        userMarker.setMap(null);
        if (navigator.geolocation) { // GPS를 지원하면
            navigator.geolocation.getCurrentPosition(function(position) {
                userLat = position.coords.latitude;
                userLong = position.coords.longitude;
                console.log(userLat, userLong);

                userMarker = new Tmapv2.Marker({
                    position : new Tmapv2.LatLng(userLat, userLong),
                    iconHTML: '<p class="m-index font-weight-bold" style="background-color: #1a252f"></p>',
                    map : map
                });
            }, function(error) {
                console.error(error);
            }, {
                enableHighAccuracy: false,
                maximumAge: 0,
                timeout: Infinity
            });
        } else {
            alert('GPS를 지원하지 않습니다');
            clearInterval(startMyGeo);
        }
    }, 1000);
})