// 페이지 로딩 시 모든 영화관 목록을 불러오도록
document.addEventListener('DOMContentLoaded', async function() {
    const theaters = await fetchTheater();
    displayTheaters(theaters);
});
//해당 지역에 있는 영화관만 보이게
document.getElementById('areaList').addEventListener('click', async function(e) {
    if (e.target && e.target.tagName === 'A') {
        const selectedRegion = e.target.getAttribute('data-region');
        const theaters = await fetchTheater();

        if (selectedRegion === 'all') {
            displayTheaters(theaters);
        } else {
            // 특정 지역이 선택된 경우
            const filteredTheaters = theaters.filter(theater => theater.regionCategory === selectedRegion);
            displayTheaters(filteredTheaters);
        }
    }
});
// 검색 기능 ( 대소문자 구분없이 검색을 하기 위해 알파벳을 모두 소문자로 변환하여 비교 하여 필터링함. )
document.getElementById('searchTheater').addEventListener('input', async function(e) {
    const searchTerm = e.target.value.toLowerCase();//입력값을 소문자로 변환

    const theaters = await fetchTheater();
    if (!searchTerm.trim()) {
        displayTheaters(theaters);
    } else {
        // 영화관 이름도 소문자로 변환하여 검색어와 비교
        const matchedTheaters = theaters.filter(theater => theater.name.toLowerCase().includes(searchTerm));
        displayTheaters(matchedTheaters);
    }
});

async function fetchTheater() {
    try {
        const response = await fetch('/theater/theaterList');
        return await response.json();
    } catch (error) {
        console.error('영화관 데이터를 가져오는 중 오류가 발생하였습니다.', error);
    }
}
//영화관 목록을 불러옴
function displayTheaters(theaters) {
    const theaterList = document.getElementById("theaterList");
    theaterList.innerHTML = '';

    theaters.forEach(theater => {
        const listItem = document.createElement("li");
        const link = document.createElement("a");
        link.href = `/theater/theaterPage?theaterName=${encodeURIComponent(theater.name)}&latitude=${theater.latitude}&longitude=${theater.longitude}`;
        link.textContent = theater.name;
        link.setAttribute('data-region', theater.regionCategory);
        link.setAttribute('data-latitude', theater.latitude);
        link.setAttribute('data-longitude', theater.longitude);

        listItem.appendChild(link);
        theaterList.appendChild(listItem);
    });
}



//지역을 받아옴
async function fetchRegions() {
    try {
        const response = await fetch('/theater/regionsList');
        const data = await response.json();

        //지역들 정렬 순서
        let order = ["서울", "경기/인천", "강원", "충청/대전", "경북/대구", "경남/부산/울산", "전라/광주", "제주"];
        data.sort((a, b) => {
            return order.indexOf(a.regionCategory) - order.indexOf(b.regionCategory);
        });

        const areaList = document.getElementById("areaList");

        areaList.addEventListener('click', function(e) {


            if (e.target && e.target.classList.contains('region')) {
                const clickedRegion = e.target;

                // 이전에 선택된 지역의 스타일 초기화
                const previouslySelected = document.querySelector('.selected-region');
                if (previouslySelected) {
                    previouslySelected.removeAttribute('style');
                    previouslySelected.classList.remove('selected-region');
                }

                // 클릭된 지역의 스타일 설정
                clickedRegion.style.color = '#40dfd0';
                clickedRegion.style.fontWeight = '700';// 검은색 텍스트
                clickedRegion.classList.add('selected-region');
            }
        });



        areaList.innerHTML = '';

        //'전체' 를 생성
        const allLinkItem = document.createElement("li");
        const allLink = document.createElement("a");
        allLink.href = "#";
        allLink.textContent = "전체";
        allLink.setAttribute('class', 'region');
        allLink.setAttribute('data-region', 'all');
        allLinkItem.appendChild(allLink);
        areaList.appendChild(allLinkItem);

        //DB에 있는 각 지역들을 가져옴
        data.forEach(region => {
            const listItem = document.createElement("li");
            const link = document.createElement("a");
            link.href = "#";
            link.textContent = region.regionCategory;
            link.setAttribute('class', 'region');
            link.setAttribute('data-region', region.regionCategory);
            listItem.appendChild(link);
            areaList.appendChild(listItem);
        });

        //전체 지도를 생성
        const fullMapItem = document.createElement("li");
        const link = document.createElement("a");
        link.href = "/map";
        link.textContent = "전체지도";
        fullMapItem.appendChild(link);
        areaList.appendChild(fullMapItem);

    } catch (error) {
        console.error('지역 데이터를 가져오는 중 오류가 발생하였습니다.', error);
    }
}

document.addEventListener("DOMContentLoaded", async function () {
    try {
        await fetchTheater();
        await fetchRegions();
    } catch (error) {
        console.error('Error:', error);
    }
});

// 지역을 눌렀을 때 그 지역의 영화관만 나타나게 함.
function filterTheatersByRegion(selectedRegion) {
    document.querySelectorAll('#theaterList a').forEach(theater => {
        theater.style.display = 'none';
    });
    if (selectedRegion === 'all') {
        document.querySelectorAll('#theaterList a').forEach(theater => {
            theater.style.display = 'block';
        });
    } else {
        document.querySelectorAll(`#theaterList a[data-region='${selectedRegion}']`).forEach(theater => {
            theater.style.display = 'block';
        });
    }
}

document.addEventListener('click', function (e) {
    if (e.target && e.target.classList.contains('region')) {
        e.preventDefault();
        const selectedRegion = e.target.getAttribute('data-region');
        filterTheatersByRegion(selectedRegion);
    }
});


// 'area' 내의 모든 앵커에 이벤트 리스너를 추가합니다.
document.querySelectorAll('.theater-find .body .area a').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        // 이전에 클릭된 앵커의 "clicked" 클래스 제거
        const previouslyClicked = document.querySelector('.theater-find .body .area a.clicked');
        if (previouslyClicked) {
            previouslyClicked.classList.remove('clicked');
        }

        // 현재 클릭된 앵커에 "clicked" 클래스 추가
        e.currentTarget.classList.add('clicked');
    });
});

