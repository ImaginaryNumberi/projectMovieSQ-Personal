// 태그에 속성 값 넣는 방법
// test.setAttribute('class', 'class이름')
// test.setAttribute('data-이름', '이름')
// test.setAttribute('name', 값);
// test.setAttribute('name', 값);

// test.classList.add('class이름')
// test.data.이름 = 값
// test['어떤 name'].value = 값



// 지역 클릭 버튼 리스트 넣을 태그 요소
const regionListStep1 = document.getElementsByClassName('step1')[0];
// 영화관 클릭 버튼 리스트 넣을 태그 요소
const theaterListStep2 = document.getElementById('cinemaList');
// 영화 클릭 버튼 리스트 넣을 태그 요소
const movieList = document.getElementById('movieList');
// 요일 클릭 버튼 리스트 넣을 태그 요소
const dayList = document.getElementById('timeList');
// 세개 요소(영화관, 영화, 요일) 다 채웠을 때 또는 안 채웠을때 쓰이는 태그 요소
const lackOfElement = document.getElementById('lackOfElement');
const sufficientOfElement = document.getElementById('sufficientOfElement');
// 예매 포스터
const infoPoster = document.querySelector('.reservePoster');
// 선택한 것 정보
const infoTextBox = document.querySelector('.text-box');
// 최종 인덱스 정보
// const screenIndex = document.getElementById('screeningTimeList'); // 이거 왜 안되냐?
const screenIndex = document.getElementById('index');








// 새로고침 버튼
const refresh = document.getElementsByClassName('btnRefresh')[0];
refresh.addEventListener('click', () => {
    sessionStorage.removeItem('back');
    sessionStorage.removeItem('region');
    sessionStorage.removeItem('theater');
    sessionStorage.removeItem('movie');
    sessionStorage.removeItem('time');
    location.reload();
})


// 지역 HTML 생성, 영화관 HTML 생성, 요일 HTML 생성 함수
// document.getElementsByClassName('step1')[0] 에 넣어줄 거임
function createListTheaterMovieTime(response) {

    // 지역 HTML 생성
    let all = response['regionTheaterList'].length;
    let seoul = 0;
    let gyeonggiIncheon = 0;
    let chungcheongDaejeon = 0;
    let gyeongbukDaegu = 0;
    let gyeongNamBusanUlsan = 0;
    let gangwon = 0;
    let jeju = 0;
    for (const region of response['regionTheaterList']) {
        switch (region['region']) {
            case '서울':
                seoul += 1;
                break;
            case '경기/인천':
                gyeonggiIncheon += 1;
                break;
            case '충청/대전':
                chungcheongDaejeon += 1;
                break;
            case '경북/대구':
                gyeongbukDaegu += 1;
                break;
            case '경남/부산/울산':
                gyeongNamBusanUlsan += 1;
                break;
            case '강원':
                gangwon += 1;
                break;
            case '제주':
                jeju += 1;
                break;
        }
    }
    const htmlText = `<ul id="regionList">
                        <li>
                            <input title="전체" class="btnRegion" value="전체 (${all})" data-region="전체" type="button">
                        </li>
                        <li>
                            <input title="서울" class="btnRegion" value="서울 (${seoul})" data-region="서울" type="button">
                        </li>
                        <li>
                            <input title="경기/인천" class="btnRegion" value="경기/인천 (${gyeonggiIncheon})" data-region="경기/인천" type="button">
                        </li>
                        <li>
                            <input title="충청/대전" class="btnRegion" value="충청/대전 (${chungcheongDaejeon})" data-region="충청/대전" type="button">
                        </li>
                        <li>
                            <input title="경북/대구" class="btnRegion" value="경북/대구 (${gyeongbukDaegu})" data-region="경북/대구" type="button">
                        </li>
                        <li>
                            <input title="경남/부산/울산" class="btnRegion" value="경남/부산/울산 (${gyeongNamBusanUlsan})" data-region="경남/부산/울산" type="button">
                        </li>
                        <li>
                            <input title="강원" class="btnRegion" value="강원 (${gangwon})" data-region="강원" type="button">
                        </li>
                        <li>
                            <input title="제주" class="btnRegion" value="제주 (${jeju})" data-region="제주" type="button">
                        </li>
                    </ul>`;
    regionListStep1.append(new DOMParser().parseFromString(htmlText, 'text/html').querySelector('#regionList'));


    //영화관 HTML 생성
    for (const theater of response['regionTheaterList']) {
        const htmlText = `<li data-theater="${theater['theater']}" data-region="${theater['region']}">
                            <input title="${theater['theater']}" type="button" value="${theater['theater']}" class="btnTheater" rel="item">
                          </li>`;
        theaterListStep2.append(new DOMParser().parseFromString(htmlText, 'text/html').querySelector('li'));
    }

    // 영화 HTML 생성
    for (const movie of response['movieList']) {
        const htmlText = `<li data-movie="${movie['movie']}">
                            <input title="${movie['movie']}" type="button" value="${movie['movie']}" data-rating="${movie['rating']}" data-image="${movie['image']}" class="btnMovie" rel="item">
                          </li>`;
        movieList.append(new DOMParser().parseFromString(htmlText, 'text/html').querySelector('li'));
    }


    // 요일 HTML 생성

    // 요일 년,월 생성 변수 저장값
    let createYearMonth = '';

    //토,일 판별 함수
    function identifyWeekend (day, input) {
        if(day['day'].substring(0, 1) === '토') {
            input.classList.add('saturday');
        } else if(day['day'].substring(0, 1) === '일') {
            input.classList.add('sunday');
        }
    }

    for (let time of response['timeList']) {

        const splitYearMonth = time['originTime'].split('-');
        const translateYearMonth = splitYearMonth[0] + '.' + splitYearMonth[1];

        if(translateYearMonth !== createYearMonth) {

            // 년, 월 요소 생성
            const yearMonthli = document.createElement('li');
            yearMonthli.classList.add('yearMonth');
            yearMonthli.dataset.yearAndMonth = translateYearMonth;
            yearMonthli.innerHTML = translateYearMonth;

            // 요일 생성
            const dayli = document.createElement('li');
            const dayInput = document.createElement('input');
            dayli.classList.add('day');
            dayli.dataset.originTime = time['originTime'];
            dayInput.setAttribute('type', 'button');
            dayInput.value = time['day'];
            dayInput.classList.add('btnTime');
            dayInput.dataset.originTime = time['originTime']; // data-originTime 이 아니라 data-origin-time 으로 변환된다.
            dayInput.setAttribute('rel', 'item');

            // 주말(토,일) 색 입히기
            identifyWeekend(time, dayInput);

            dayli.append(dayInput);
            // id: timeList 요소 안에 넣기
            dayList.append(yearMonthli, dayli);
            // 다른 년월값 판별하기 위한 변수
            createYearMonth = translateYearMonth;
        } else {
            //뒤따라오는 년월요소가 똑같을 시
            const dayli = document.createElement('li');
            const dayInput = document.createElement('input');
            dayli.classList.add('day');
            dayli.dataset.originTime = time['originTime'];
            dayInput.setAttribute('type', 'button');
            dayInput.value = time['day'];
            dayInput.classList.add('btnTime');
            dayInput.dataset.originTime = time['originTime']; // data-originTime 이 아니라 data-origin-time 으로 변환된다.
            dayInput.setAttribute('rel', 'item');

            // 주말(토,일) 색 입히기
            identifyWeekend(time, dayInput);

            dayli.append(dayInput);

            // id: timeList 요소 안에 넣기
            dayList.append(dayli);
        }
    }
    // 지역 버튼 클릭 관련 함수 생성
    selectRegionButton();
    // 영화관, 영화, 요일 요소 버튼 선택 판별 함수 생성
    selectTheaterAndMovieAndTime();
}


// 지역 버튼 클릭 관련 함수
function selectRegionButton() {

    // 지역 버튼 마지막 클릭 변수 저장값
    let selectRegionLast = '전체';
    // 요소, 지역 id 요소
    const regionList = document.getElementById('regionList');
    // 요소들, 지역 모든 요소
    const regionClick = regionList.querySelectorAll('input');
    // 요소들, 영화관 모든 요소
    const theaterElement = theaterListStep2.querySelectorAll('li');

    // 리스트 요소들 생성시 지역 버튼 '전체'로 선택 되어지게 하는 것
    // 이리 코드를 쓰지 않고 html 만들 때 '전체'를 미리 써두는 것도 하나의 방법
    regionClick.forEach(region => {
        if(region.dataset.region === selectRegionLast) {
            region.classList.add('btnSelected');
        }
    })


    // seat 페이지에서 '상영관 다시고르기' 눌렀을 경우
    // 그전에 골랐던 목록 값들을 재할당
    if(sessionStorage.getItem('back') && sessionStorage.getItem('region') !== null) {
        regionList.querySelector('.btnSelected').classList.remove('btnSelected'); // 기존 버튼 초기화
        if(sessionStorage.getItem('region') === null) {
            sessionStorage.setItem('region', '전체');
        }
        selectRegionLast = sessionStorage.getItem('region');
        regionClick.forEach(region => {
            if(region.dataset.region === selectRegionLast) {
                region.classList.add('btnSelected');

                // 해당 지역 영화관 표시
                if(region.dataset.region === '전체') {
                    for (const theater of theaterElement) {
                        theater.classList.remove('regionHidden');
                    }
                } else {
                    for (const theater of theaterElement) {
                        if(region.dataset.region !== theater.dataset.region){
                            theater.classList.add('regionHidden');
                        } else {
                            theater.classList.remove('regionHidden');
                        }
                    }
                }
            }
        })
    }

    //이벤트, 지역 버튼 클릭시 해당클릭 활성화 및 비활성화
    // regionClick[0].classList.add('btnSelected');
    regionClick.forEach(region => {
        region.addEventListener('click', () => {
            // 클릭 된 버튼 표시 및 전 클릭버튼 초기화
            regionList.querySelector('.btnSelected').classList.remove('btnSelected'); // 기존 버튼 초기화
            region.classList.add('btnSelected'); // 마지막으로 누른(새로운) 버튼

            // 마지막으로 누른(새로운) 버튼 변수 값 저장
            selectRegionLast = region.dataset.region;
            sessionStorage.setItem('region', selectRegionLast);

            // 해당 지역 영화관 표시
            if(region.dataset.region === '전체') {
                for (const theater of theaterElement) {
                    theater.classList.remove('regionHidden');
                }
            } else {
                for (const theater of theaterElement) {
                    if(region.dataset.region !== theater.dataset.region){
                        theater.classList.add('regionHidden');
                    } else {
                        theater.classList.remove('regionHidden');
                    }
                }
            }
        })
    });
}


// 영화관, 영화, 요일 요소 버튼 선택 판별 함수
function selectTheaterAndMovieAndTime() {

    // 조회할 때 보낼 변수값들
    let theaterValue = '';
    let movieValue = '';
    let timeValue = '';

    // 영화관, 영화, 요일의 모든 요소들
    const listOfItem = document.querySelectorAll('[rel="item"]');


    // seat 페이지에서 '예매 다시하기' 눌렀을 경우
    // 그전에 골랐던 목록 값들을 재할당
    if(sessionStorage.getItem('back') &&
        sessionStorage.getItem('theater') !== null &&
        sessionStorage.getItem('movie') !== null &&
        sessionStorage.getItem('time') !== null) {


        theaterValue = sessionStorage.getItem('theater');
        movieValue = sessionStorage.getItem('movie');
        timeValue = sessionStorage.getItem('time');


        showTheaterAndMovieAndTime(theaterValue, movieValue, timeValue);

        listOfItem.forEach(item => {
            if(item.value === theaterValue && item.classList.contains('btnTheater')) {
                item.classList.toggle('btnSelected');
                infoTextBox.querySelector('.thNm').innerHTML = theaterValue;
            } else if(item.value === movieValue && item.classList.contains('btnMovie')) {
                item.classList.toggle('btnSelected');
                infoTextBox.querySelector('.mvNm').innerHTML = movieValue;
                infoTextBox.querySelector('.rating').innerHTML = item.dataset.rating;
                infoPoster.src = '../movie/image?index=' + item.dataset.image;
            } else if (item.dataset.originTime === timeValue && item.classList.contains('btnTime')) {
                item.classList.toggle('btnSelected');
                infoTextBox.querySelector('.tiNm').innerHTML = timeValue;
            }
        })

        sessionStorage.removeItem('back');
    }


    // 이모든 요소들에 대해 클릭시 이벤트
    listOfItem.forEach(item => {
        item.addEventListener('click', () => {

            // 영화관 요소 클릭시
            if(item.value === theaterValue && item.classList.contains('btnTheater')) {

                // 해당 if가 실행 되었다는 것은 '이미 선택 되어진' 요소라는 것
                // item.classList.remove('btnSelected');
                item.classList.toggle('btnSelected');
                theaterValue = '';

                // 선택 정보 초기화
                //영화관
                infoTextBox.querySelector('.thNm').innerHTML = theaterValue;
                //상영관
                infoTextBox.querySelector('.scNm').innerHTML = '';
                infoTextBox.querySelector('.ruNm').innerHTML = '';

            } else if(item.value !== theaterValue && item.classList.contains('btnTheater')) {

                // 해당 if가 실행 되었다는 것은 '선택 되어진 것 외 요소를 클릭했다는 것'
                listOfItem.forEach(one => {
                    // '선택 되어있던 기존 요소' 초기화
                    if(one.classList.contains('btnTheater') && one.classList.contains('btnSelected')) {
                        // one.classList.remove('btnSelected');
                        one.classList.toggle('btnSelected');
                    }
                })

                // 선택 되어진 요소로 만듬
                // item.classList.add('btnSelected');
                item.classList.toggle('btnSelected');
                theaterValue = item.value;

                sessionStorage.setItem('theater', theaterValue);

                // 영화관
                infoTextBox.querySelector('.thNm').innerHTML = theaterValue;
                //상영관
                infoTextBox.querySelector('.scNm').innerHTML = '';
                infoTextBox.querySelector('.ruNm').innerHTML = '';

            }

            // 영화 요소 클릭시
            if(item.value === movieValue && item.classList.contains('btnMovie')) {

                // 기존 영화 요소 클릭시 if문 실행
                // item.classList.remove('btnSelected');
                item.classList.toggle('btnSelected');
                movieValue = '';

                // 영화
                // &nbsp;은 HTML에서 쓰이며 공백이어도 줄과 칸에 변화가 없게 해준다.
                infoTextBox.querySelector('.mvNm').innerHTML = '&nbsp;';
                infoTextBox.querySelector('.rating').innerHTML = '';
                //상영관
                infoTextBox.querySelector('.scNm').innerHTML = '';
                infoTextBox.querySelector('.ruNm').innerHTML = '';
                //포스터
                infoPoster.src = '../resources/images/no_movie.png';

            } else if(item.value !== movieValue && item.classList.contains('btnMovie')) {

                listOfItem.forEach(one => {
                    if(one.classList.contains('btnMovie') && one.classList.contains('btnSelected')) {
                        // one.classList.remove('btnSelected');
                        one.classList.toggle('btnSelected');
                    }
                })

                // item.classList.add('btnSelected');
                item.classList.toggle('btnSelected');
                movieValue = item.value;

                sessionStorage.setItem('movie', movieValue);

                //영화
                infoTextBox.querySelector('.mvNm').innerHTML = movieValue;
                infoTextBox.querySelector('.rating').innerHTML = item.dataset.rating;
                //상영관
                infoTextBox.querySelector('.scNm').innerHTML = '';
                infoTextBox.querySelector('.ruNm').innerHTML = '';
                //포스터
                <!-- movieController를 사용 -->
                infoPoster.src = '../movie/image?index=' + item.dataset.image;

            }

            // 요일 요소 클릭시
            if(item.dataset.originTime === timeValue && item.classList.contains('btnTime')) {

                // item.classList.remove('btnSelected');
                item.classList.toggle('btnSelected');
                timeValue = '';

                // 요일
                infoTextBox.querySelector('.tiNm').innerHTML = timeValue;
                //상영관
                infoTextBox.querySelector('.scNm').innerHTML = '';
                infoTextBox.querySelector('.ruNm').innerHTML = '';

            } else if(item.dataset.originTime !== timeValue && item.classList.contains('btnTime')) {

                listOfItem.forEach(one => {
                    if(one.classList.contains('btnTime') && one.classList.contains('btnSelected')) {
                        // one.classList.remove('btnSelected');
                        one.classList.toggle('btnSelected');
                    }
                })

                // item.classList.add('btnSelected');
                item.classList.toggle('btnSelected');
                timeValue = item.dataset.originTime;

                sessionStorage.setItem('time', timeValue);

                //요일
                infoTextBox.querySelector('.tiNm').innerHTML = timeValue;
                //상영관
                infoTextBox.querySelector('.scNm').innerHTML = '';
                infoTextBox.querySelector('.ruNm').innerHTML = '';

            }

            // DB 조회 함수
            showTheaterAndMovieAndTime(theaterValue, movieValue, timeValue);
        })
    })
}


// 영화관, 영화, 요일 요소 공통값 변환 함수
function intersectTheaterAndMovieAndTime(response) {

    // 영화관 리스트 input 요소들
    const theaterLi = theaterListStep2.querySelectorAll('li');
    // console.log(theaterLi[0]);
    // 영화 리스트 요소들
    const movieLi = movieList.querySelectorAll('li');
    // console.log(movieLi[0]);
    // 요일 리스트 요소들
    const dayLi = dayList.querySelectorAll('li');



    // 영화관 hidden 클래스 리스트 초기화
    theaterLi.forEach(li => {
        if(li.classList.contains('theaterHidden')) {
            li.classList.remove('theaterHidden');
        }
    })
    // 영화 hidden 클래스 리스트 초기화
    movieLi.forEach(li => {
        if(li.classList.contains('movieHidden')) {
            li.classList.remove('movieHidden');
        }
    })
    // 요일 hidden 클래스 리스트 초기화
    dayLi.forEach(li => {
        if(li.classList.contains('dayHidden')) {
            li.classList.remove('dayHidden');
        }
    })

    // 영화관 공통값 보여주기
    if(!response['intersectionTheater'][0].hasOwnProperty('nothingElement')) {
        // 공통 리스트 값들
        for (const intersectionElement of response['intersectionTheater']) {
            // 원래 있던 리스트 값들
            for (const originElement of theaterLi) {
                if(intersectionElement['intersectionTheater'] === originElement.dataset.theater) {
                    originElement.classList.add('temporary');
                }
            }
        }

        theaterLi.forEach(li => {
            if(!li.classList.contains('temporary')) {
                li.classList.add('theaterHidden');
            } else {
                li.classList.remove('temporary');
            }
        })
    }

    // 영화 공통값 보여주기
    if(!response['intersectionMovie'][0].hasOwnProperty('nothingElement')) {
        // 공통 리스트 값들
        for (const intersectionElement of response['intersectionMovie']) {
            // 원래 있던 리스트 값들
            for (const originElement of movieLi) {
                if(intersectionElement['intersectionMovie'] === originElement.dataset.movie) {
                    originElement.classList.add('temporary');
                }
            }
        }
        movieLi.forEach(li => {
            if(!li.classList.contains('temporary')) {
                li.classList.add('movieHidden');
            } else {
                li.classList.remove('temporary');
            }
        })
    }

    // 요일 공통값 보여주기
    if(!response['intersectionTime'][0].hasOwnProperty('nothingElement')) {
        //공통 리스트 값들
        for(const intersectionElement of response['intersectionTime']) {
            const yearMonthArray = intersectionElement['intersectionTime'].split('-');
            const yearMonth = yearMonthArray[0] + '.' + yearMonthArray[1];
            // 원래 있던 리스트 값들
            for (const originElement of dayLi) {
                if(originElement.classList.contains('yearMonth') &&
                    originElement.dataset.yearAndMonth === yearMonth) {
                    originElement.classList.add('temporary');
                } else if(originElement.dataset.originTime === intersectionElement['intersectionTime']) {
                    originElement.classList.add('temporary');
                }

            }

        }

        dayLi.forEach(li => {
            if(!li.classList.contains('temporary')) {
                li.classList.add('dayHidden');
            } else {
                li.classList.remove('temporary');
            }
        })
    }
}


// 상영시간 리스트 태그 생성 및 상영관 요소 클릭시 효과 나타내기 함수
function createListScreen(response) {
    // 생성 및 붙이기
    response.forEach(screen => {
        const htmlText = `<li class="btnScreen" data-schedule="${screen['scheduleIndex']}" data-screen="${screen['screenName']}" data-runningTime="${screen['runningTime']}"  rel="item">
                             <div class="screenName">${screen['screenName']}</div>
                             <div class="runningTimeAndSeat">
                             <span>${screen['runningTime']}</span><span>${screen['occupiedSeatTotal']}/<span>${screen['seatTotal']}석</span></span>
                             </div>
                          </li>`;

        document.querySelector('#sufficientOfElement').append(new DOMParser().parseFromString(htmlText, 'text/html').querySelector('li'));
    })

    // 상영관 선택시 효과 보여주기
    let screenValue = '';
    const listOfScreen = sufficientOfElement.querySelectorAll('li');
    listOfScreen.forEach(li => {
        li.addEventListener('click', () => {
            // 기존에 선택되어진것을 눌렀을 경우
            if(li.classList.contains('screenBtnSelected')) {

                li.classList.remove('screenBtnSelected')
                screenValue = '';

                infoTextBox.querySelector('.scNm').innerHTML = '';
                infoTextBox.querySelector('.ruNm').innerHTML = '';

            } else {
                // 다른 것을 눌렀을 경우
                // 기존 선택되어져있던 것 초기화
                listOfScreen.forEach(one => {
                    if(one.classList.contains('screenBtnSelected')) {
                        one.classList.remove('screenBtnSelected');
                    }
                })

                // 선택효과 클래스 추가 및 스케쥴 인덱스 값 저장
                li.classList.add('screenBtnSelected');
                screenValue = li.dataset.schedule;

                infoTextBox.querySelector('.scNm').innerHTML = li.dataset.screen;
                infoTextBox.querySelector('.ruNm').innerHTML = li.dataset.runningtime;
            }
        })
    })
}


// DB 조회 함수
function showTheaterAndMovieAndTime(theaterValue, movieValue, timeValue) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/reserve/intersectionTheaterMovieTimeScreen?theaterName=${theaterValue}&movieName=${movieValue}&timeName=${timeValue}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                // 시간이 지난 것 또는 삭제 되거나 어떠한 이유로 DB에서 관련된 값을 조회 못했을 경우 expiredElement 값이 들어가게 설정
                if('expiredElement' in response['intersectionTheater'][0] ||
                    'expiredElement' in response['intersectionMovie'][0] ||
                    'expiredElement' in response['intersectionTime'][0]){
                    sessionStorage.removeItem('back');
                    // alert('고른 요소중에 유효 시간이 지났거나 잘못된 값을 고르셨습니다.');
                    popUp.show('', '고른 요소중에 유효 시간이 지났거나 잘못된 값을 고르셨습니다.');
                    popUpBtns.show('close');
                    closeBtn.addEventListener('click', () => {
                        // location.replace('/reserve');
                        // location.href = '/reserve';
                        location.reload();
                    })
                } else {

                    // 공통값 요소 보여주는 함수
                    intersectTheaterAndMovieAndTime(response);

                }
                // 3개 요소 다 안골랐을 때와 다 골랐을 때, 상영시간 안보이기 또는 보여주기
                // 세개의 값이 다 들어가있을 시
                // response['상영관관련'] 존재할시
                if(response['screenList']) {
                    // 시간이 지난 것 또는 삭제 되거나 어떠한 이유로 DB에서 관련된 값을 조회 못했을 경우
                    if('expiredElement' in response['screenList']) {
                        sessionStorage.removeItem('back');
                        //alert('고른 요소중에 유효 시간이 지났거나 잘못된 값을 고르셨습니다.');
                        popUp.show('', '고른 요소중에 유효 시간이 지났거나 잘못된 값을 고르셨습니다.');
                        popUpBtns.show('close');
                        closeBtn.addEventListener('click', () => {
                            // location.replace('/reserve');
                            // location.href = '/reserve';
                            location.reload();
                        })
                    }
                    // 기존 상영관 요소 초기화
                    if(sufficientOfElement.querySelectorAll('.btnScreen')) {
                        sufficientOfElement.querySelectorAll('.btnScreen').forEach(item => {
                            item.remove();
                        })
                    }
                    // 상영관 리스트 생성 및 선택 효과 함수 호출
                    createListScreen(response['screenList']);
                    // 3개 요소 선택창 숨김 클래스 및 상영관 리스트 출력 클래스
                    lackOfElement.classList.add('screenHidden');
                    sufficientOfElement.classList.remove('screenHidden');
                } else { // 세개의 값 중 하나라도 빠져있을 시
                    // 기존 상영관 요소 초기화
                    if(sufficientOfElement.querySelectorAll('.btnScreen')) {
                        sufficientOfElement.querySelectorAll('.btnScreen').forEach(item => {
                            item.remove();
                        })
                    }

                    lackOfElement.classList.remove('screenHidden');
                    sufficientOfElement.classList.add('screenHidden');

                }
            } else {
                sessionStorage.removeItem('back');
                //alert('서버에 예기치 못한 오류가 발생하여 목록을 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.2 (공통값)')
                popUp.show('', '서버에 예기치 못한 오류가 발생하여 목록을 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.2 (공통값)');
                popUpBtns.show('close');
            }
        }
    };
    xhr.send();
}



// 인원/좌석 선택 버튼 눌렀을 시
const nextForm = document.getElementById('selectMovieContainer');
nextForm.onsubmit = e => {
    e.preventDefault(); // 이론이 부족하여 사용법을 잘 몰랐음

    // 선택하지 않고 눌렀을시
    let noSelectWarning = '극장, 영화, 관람일, 관람시간(를)을 선택해주세요.';
    const items = nextForm.querySelectorAll('[rel="item"]');
    items.forEach(item => {
        if(item.classList.contains('btnTheater') && item.classList.contains('btnSelected')) {
            noSelectWarning = noSelectWarning.replace('극장, ','');
        }
        if(item.classList.contains('btnMovie') && item.classList.contains('btnSelected')){
            noSelectWarning = noSelectWarning.replace('영화, ','');
        }
        if(item.classList.contains('btnTime') && item.classList.contains('btnSelected')) {
            noSelectWarning = noSelectWarning.replace('관람일, ','');
        }
        if(item.classList.contains('btnScreen') && item.classList.contains('screenBtnSelected')) {
            noSelectWarning = noSelectWarning.replace('관람시간','');

            // 최종 인덱스 값 넣기
            screenIndex.value = Number(item.dataset.schedule);
        }
    })
    // 알림창 띄우기
    if(noSelectWarning !== '(를)을 선택해주세요.') {
        popUp.show('', noSelectWarning);
        popUpBtns.show('close');
        //e.preventDefault();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('index', String(screenIndex.value));
    xhr.open('POST', '/reserve');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                switch (response['result']) {
                    case 'failure_not_login':
                        location.href = '/login';
                        break;
                    case 'failure_invalid_index':
                        popUp.show('', '만료 되었거나 유효하지 않은 상영관 입니다.');
                        popUpBtns.show('close');
                        break;
                    case 'failure_full_seat':
                        popUp.show('', '해당 상영관은 전좌석 매진 되었습니다.');
                        popUpBtns.show('close');
                        break;
                    case 'success':
                        if(document.getElementById('test')) {
                            document.getElementById('test').remove();
                        }
                        // get 방식이 아닌 post 방식으로 seat페이지로 넘어가게 해준다.
                        const form = document.createElement('form');
                        form.id = 'test';
                        form.action = '/seat';
                        form.method = 'post';
                        form.appendChild(screenIndex);
                        document.getElementById('selectMovieContainer').appendChild(form);
                        form.submit();
                        break;
                    default:
                        popUp.show('', '서버에서 알 수 없는 응답을 반환하였습니다. 잠시 후 시도 해주세요.');
                        popUpBtns.show('close');
                }
            } else {
                popUp.show('', '서버에 예기치 못한 오류가 발생하여 목록을 불러오지 못했습니다.잠시 후 다시 시도해 주세요.');
                popUpBtns.show('close');
            }
        }
    };
    xhr.send(formData);
}


// 처음 시작점, 페이지가 다 로드 되었을 때 해당 함수 실행
window.onload = function () {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/reserve/theaterMovieTime');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                // 지역, 영화관, 영화, 요일 HTML 생성
                createListTheaterMovieTime(response);
            } else {
                //alert('서버에 예기치 못한 오류가 발생하여 목록을 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.1(처음 모든 세가지 요소 리스트)');
                popUp.show('', '서버에 예기치 못한 오류가 발생하여 목록을 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.1(처음 모든 세가지 요소 리스트)');
                popUpBtns.show('close');
            }
        }
    };
    xhr.send();
}












