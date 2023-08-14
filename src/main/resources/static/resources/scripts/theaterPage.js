window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const theaterName = urlParams.get('theaterName');
    const latitude = urlParams.get('latitude');
    const longitude = urlParams.get('longitude');
    document.getElementById('theaterName').innerText = decodeURIComponent(theaterName);

    const directionsLink = document.querySelector('.find-load a');

    //길찾기 하면 해당 영화관 이름과 위도 경도를 가지고 카카오맵 페이지로 이동
    directionsLink.href = `https://map.kakao.com/link/map/${decodeURIComponent(theaterName)},${latitude},${longitude}`;

    // 해당 영화관 정보를 가진 theaterPage 정보로 지도의 중심을 이동
    mapElement.savePosition = (params) => {
        localStorage.setItem('latitude', params.latitude);
        localStorage.setItem('longitude', params.longitude);
        localStorage.setItem('level', params.level);
    };

    mapElement.init({
        latitude: latitude,
        longitude: longitude,
        level: 3
    });
}

// 이미지 정보를 불러오는 함수
async function fetchTheaterImage(theaterName) {
    try {
        const response = await fetch(`/theater/theaterImage?theaterName=${encodeURIComponent(theaterName)}`);
        const imageUrl = await response.text();
        const imgElement = document.getElementById('theaterImage');
        imgElement.src = imageUrl;
    } catch (error) {
        console.error('이미지 정보를 가져오는 중 에러 발생:', error);
    }
}

// 동적으로 이미지 정보를 업데이트하는 함수
async function updateTheaterImageOnPageLoad() {
    const urlParams = new URLSearchParams(window.location.search);
    const theaterName = urlParams.get('theaterName');
    if (theaterName) {
        await fetchTheaterImage(theaterName);
    }
}

// 페이지가 로드될 때 이미지 정보를 업데이트
window.addEventListener('load', updateTheaterImageOnPageLoad);











