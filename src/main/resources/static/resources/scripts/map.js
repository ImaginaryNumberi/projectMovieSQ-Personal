const mapElement = document.getElementById('map');

mapElement.geocoder = new kakao.maps.services.Geocoder();
mapElement.init = (params) => {
    mapElement.object = new kakao.maps.Map(mapElement, {
        center: new kakao.maps.LatLng(params.latitude, params.longitude),
        level: params.level
    });
    ['dragend', 'zoom_changed'].forEach(event => kakao.maps.event.addListener(mapElement.object, event, () => {
        loadPlaces();

        const center = mapElement.object.getCenter();
        mapElement.savePosition({
            latitude: center.Ma,
            longitude: center.La,
            level: mapElement.object.getLevel()
        });
    }));
};

mapElement.savePosition = (params) => {
    localStorage.setItem('latitude', params.latitude);
    localStorage.setItem('longitude', params.longitude);
    localStorage.setItem('level', params.level);
};

if (localStorage.getItem('latitude') &&
    localStorage.getItem('longitude') &&
    localStorage.getItem('level')) {
    mapElement.init({
        latitude: parseFloat(localStorage.getItem('latitude')),
        longitude: parseFloat(localStorage.getItem('longitude')),
        level: parseInt(localStorage.getItem('level'))
    });
} else {
    navigator.geolocation.getCurrentPosition(e => {
        mapElement.init({
            latitude: e.coords.latitude,
            longitude: e.coords.longitude,
            level: 3
        });
    }, () => {
        mapElement.init({
            latitude: 35.89154,
            longitude: 128.611505,
            level: 3
        });
    });
}

//자동
// fetch('/theater/theaterList')
//     .then(response => response.json())
//     .then(data => {
//         data.forEach(theater => {
//             const mapContainer = document.getElementById('map');
//             const mapOption = {
//                 center: new kakao.maps.LatLng(theater.latitude, theater.longitude),
//                 level: 8
//             };
//             const map = new kakao.maps.Map(mapContainer, mapOption);
//             const markerPosition  = new kakao.maps.LatLng(theater.latitude, theater.longitude);
//             const marker = new kakao.maps.Marker({
//                 position: markerPosition,
//                 clickable: true
//             });
//             marker.setMap(map);
//
//             // 영화관 이름과 주소 등을 DB에서 가져온 정보로 변경
//             const content = '<div class="wrap">' +
//                 '    <div class="info">' +
//                 `        <div class="title">${theater.name}` +
//                 '            <div class="close" onclick="closeOverlay()" title="닫기"></div>' +
//                 '        </div>' +
//                 '        <div class="body">' +
//                 '            <div class="img">' +
//                 '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
//                 '           </div>' +
//                 '            <div class="desc">' +
//                 `                <div class="ellipsis">${theater.address}</div>` +
//                 `                <a href="https://map.kakao.com/link/map/${theater.name},${theater.latitude},${theater.longitude}" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/${theater.name},${theater.latitude},${theater.longitude}" style="color:blue" target="_blank">길찾기</a></div>` +
//                 '            </div>' +
//                 '        </div>' +
//                 '    </div>' +
//                 '</div>';
//
//             const customOverlay = new kakao.maps.CustomOverlay({
//                 position: markerPosition,
//                 clickable: true,
//                 content: content,
//                 xAnchor: 0.3,
//                 yAnchor: 0.98,
//             });
//
//             kakao.maps.event.addListener(marker, 'click', function() {
//                 customOverlay.setMap(map);
//             });
//         });
//     })
//     .catch(error => console.error('영화관 데이터를 가져오는 중 오류가 발생하였습니다.', error));
//
// function closeOverlay() {
//     customOverlay.setMap(null);
// }


//수동
const mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(35.8715411, 128.601505), // 지도의 중심좌표
        level: 8 // 지도의 확대 레벨
    };
const map = new kakao.maps.Map(mapContainer, mapOption);

// 마커가 표시될 위치
const markerPosition = new kakao.maps.LatLng(35.8915411, 128.601505);
const markerPosition1 = new kakao.maps.LatLng(35.8699230, 128.594222);
const markerPosition2 = new kakao.maps.LatLng(37.5297625, 126.963955);
const markerPosition3 = new kakao.maps.LatLng(35.1691171, 129.130345);
const markerPosition4 = new kakao.maps.LatLng(36.3753691, 127.382516);
const markerPosition5 = new kakao.maps.LatLng(35.1542239, 126.854362);
const markerPosition6 = new kakao.maps.LatLng(37.3791504, 126.662405);
const markerPosition7 = new kakao.maps.LatLng(35.7199864, 128.261028);
const markerPosition8 = new kakao.maps.LatLng(33.4087710, 126.268062);
const markerPosition9 = new kakao.maps.LatLng(38.3790850, 128.474033);

// 마커 생성
const marker = new kakao.maps.Marker({
    position: markerPosition,
    clickable: true
});
const marker1 = new kakao.maps.Marker({
    position: markerPosition1
});
const marker2 = new kakao.maps.Marker({
    position: markerPosition2
});
const marker3 = new kakao.maps.Marker({
    position: markerPosition3
});
const marker4 = new kakao.maps.Marker({
    position: markerPosition4
});
const marker5 = new kakao.maps.Marker({
    position: markerPosition5
});
const marker6 = new kakao.maps.Marker({
    position: markerPosition6
});
const marker7 = new kakao.maps.Marker({
    position: markerPosition7
});
const marker8 = new kakao.maps.Marker({
    position: markerPosition8
});
const marker9 = new kakao.maps.Marker({
    position: markerPosition9
});

// 마커 지도 위에 표시 설정
marker.setMap(map);
marker1.setMap(map);
marker2.setMap(map);
marker3.setMap(map);
marker4.setMap(map);
marker5.setMap(map);
marker6.setMap(map);
marker7.setMap(map);
marker8.setMap(map);
marker9.setMap(map);

// 커스텀 오버레이
const content = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            CGV대구' +
    '            <div class="close" onclick="closeOverlay()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">대구광역시 북구 침산로 93</div>' +
    '                <div class="jibun ellipsis">스펙트럼시티 4층</div>' +
    '                <a href="https://map.kakao.com/link/map/CGV대구,35.8915411, 128.601505" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/CGV대구,35.8915411, 128.601505" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content1 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            CGV아카데미' +
    '            <div class="close" onclick="closeOverlay1()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">대구광역시 중구 남일동 65-1</div>' +
    '                <div class="jibun ellipsis">3층</div>' +
    '                <a href="https://map.kakao.com/link/map/CGV아카데미,35.8699230, 128.594222" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/CGV아카데미,35.8699230, 128.594222" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content2 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            CGV용산아이파크몰' +
    '            <div class="close" onclick="closeOverlay2()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">서울특별시 용산구 한강대로23길 55</div>' +
    '                <div class="jibun ellipsis">6층(한강로동)</div>' +
    '                <a href="https://map.kakao.com/link/map/CGV용산아이파크몰,37.5297625, 126.963955" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/CGV용산아이파크몰,37.5297625, 126.963955" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content3 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            CGV센텀시티' +
    '            <div class="close" onclick="closeOverlay3()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">부산광역시 해운대구 센텀남대로 35</div>' +
    '                <div class="jibun ellipsis">7층(우동)</div>' +
    '                <a href="https://map.kakao.com/link/map/CGV센텀시티,35.1691171, 129.130345" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/CGV센텀시티,35.1691171, 129.130345" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content4 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            메가박스 대전신세계 아트앤사이언스' +
    '            <div class="close" onclick="closeOverlay4()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">대전광역시 유성구 엑스포로1</div>' +
    '                <div class="jibun ellipsis">(도룡동) 대전신세계아트앤사이언스 6층</div>' +
    '                <a href="https://map.kakao.com/link/map/메가박스 대전신세계 아트앤사이언스,36.3753691, 127.382516" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/메가박스 대전신세계 아트앤사이언스,36.3753691, 127.382516" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content5 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            메가박스 광주상무' +
    '            <div class="close" onclick="closeOverlay5()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">광주광역시 서구 치평동 시청로60번길 21 </div>' +
    '                <div class="jibun ellipsis">콜롬버스시네마</div>' +
    '                <a href="https://map.kakao.com/link/map/메가박스 광주상무,35.1542239, 126.854362" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/메가박스 광주상무,35.1542239, 126.854362" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content6 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            메가박스 송도' +
    '            <div class="close" onclick="closeOverlay6()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">인천광역시 연수구 송도과학로 16번길 33-4</div>' +
    '                <div class="jibun ellipsis">(송도동) 트리플 스트리트 D동 2층</div>' +
    '                <a href="https://map.kakao.com/link/map/메가박스 송도,37.3791504, 126.662405" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/메가박스 송도,37.3791504, 126.662405" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content7 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            고령 대가야시네마' +
    '            <div class="close" onclick="closeOverlay7()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">경상북도 고령군 대가야읍 대가야로 1216</div>' +
    '                <div class="jibun ellipsis">대가야시네마</div>' +
    '                <a href="https://map.kakao.com/link/map/고령 대가야시네마,35.7199864, 128.261028" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/고령 대가야시네마,35.7199864, 128.261028" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content8 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            제주 한림작은영화관' +
    '            <div class="close" onclick="closeOverlay8()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">제주특별자치도 제주시 신산로 82</div>' +
    '                <div class="jibun ellipsis">3층</div>' +
    '                <a href="https://map.kakao.com/link/map/제주 한림작은영화관,33.408771, 126.268062" style="color:blue" target="_blank">(일도이동)</a> <a href="https://map.kakao.com/link/to/제주 한림작은영화관,33.408771, 126.268062" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

const content9 = '<div class="wrap">' +
    '    <div class="info">' +
    '        <div class="title">' +
    '            고성 달흘영화관' +
    '            <div class="close" onclick="closeOverlay9()" title="닫기"></div>' +
    '        </div>' +
    '        <div class="body">' +
    '            <div class="img">' +
    '                <img src="https://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
    '           </div>' +
    '            <div class="desc">' +
    '                <div class="ellipsis">강원도 고성군 간성읍 수성로 3</div>' +
    '                <div class="jibun ellipsis">달홀문화센터</div>' +
    '                <a href="https://map.kakao.com/link/map/고성 달흘영화관,38.379085, 128.474033" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/고성 달흘영화관,38.379085, 128.474033" style="color:blue" target="_blank">길찾기</a></div>' +
    '            </div>' +
    '        </div>' +
    '    </div>' +
    '</div>';

// //커스텀 오버레이를 생성합니다
const customOverlay = new kakao.maps.CustomOverlay({
    position: markerPosition,
    clickable: true,
    content: content,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay1 = new kakao.maps.CustomOverlay({
    position: markerPosition1,
    clickable: true,
    content: content1,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay2 = new kakao.maps.CustomOverlay({
    position: markerPosition2,
    clickable: true,
    content: content2,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay3 = new kakao.maps.CustomOverlay({
    position: markerPosition3,
    clickable: true,
    content: content3,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay4 = new kakao.maps.CustomOverlay({
    position: markerPosition4,
    clickable: true,
    content: content4,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay5 = new kakao.maps.CustomOverlay({
    position: markerPosition5,
    clickable: true,
    content: content5,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay6 = new kakao.maps.CustomOverlay({
    position: markerPosition6,
    clickable: true,
    content: content6,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay7 = new kakao.maps.CustomOverlay({
    position: markerPosition7,
    clickable: true,
    content: content7,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay8 = new kakao.maps.CustomOverlay({
    position: markerPosition8,
    clickable: true,
    content: content8,
    xAnchor: 0.3,
    yAnchor: 0.98,
});
const customOverlay9 = new kakao.maps.CustomOverlay({
    position: markerPosition9,
    clickable: true,
    content: content9,
    xAnchor: 0.3,
    yAnchor: 0.98,
});

// // 커스텀 오버레이를 지도에 표시합니다
kakao.maps.event.addListener(marker, 'click', function () {
    customOverlay.setMap(map);
});
kakao.maps.event.addListener(marker1, 'click', function () {
    customOverlay1.setMap(map);
});
kakao.maps.event.addListener(marker2, 'click', function () {
    customOverlay2.setMap(map);
});
kakao.maps.event.addListener(marker3, 'click', function () {
    customOverlay3.setMap(map);
});
kakao.maps.event.addListener(marker4, 'click', function () {
    customOverlay4.setMap(map);
});
kakao.maps.event.addListener(marker5, 'click', function () {
    customOverlay5.setMap(map);
});
kakao.maps.event.addListener(marker6, 'click', function () {
    customOverlay6.setMap(map);
});
kakao.maps.event.addListener(marker7, 'click', function () {
    customOverlay7.setMap(map);
});
kakao.maps.event.addListener(marker8, 'click', function () {
    customOverlay8.setMap(map);
});
kakao.maps.event.addListener(marker9, 'click', function () {
    customOverlay9.setMap(map);
});

//커스텀 오버레이를 닫기 위해 호출되는 함수
function closeOverlay() {
    customOverlay.setMap(null);
}

function closeOverlay1() {
    customOverlay1.setMap(null);
}

function closeOverlay2() {
    customOverlay2.setMap(null);
}

function closeOverlay3() {
    customOverlay3.setMap(null);
}

function closeOverlay4() {
    customOverlay4.setMap(null);
}

function closeOverlay5() {
    customOverlay5.setMap(null);
}

function closeOverlay6() {
    customOverlay6.setMap(null);
}

function closeOverlay7() {
    customOverlay7.setMap(null);
}

function closeOverlay8() {
    customOverlay8.setMap(null);
}

function closeOverlay9() {
    customOverlay9.setMap(null);
}