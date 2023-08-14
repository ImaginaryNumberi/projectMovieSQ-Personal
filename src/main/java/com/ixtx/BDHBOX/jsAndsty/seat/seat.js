const seatCover = document.querySelector('.cover');

const seats1 = document.querySelector('.seats1');

// 일반, 청소년 개수
// 좌석 이름 개수
let headNameArray = [];
let seatNameArray = [];


// xhr 리턴값 받기
let seatStatus = '';


// 영화 선택창 돌아가기
const backBtn = document.getElementsByClassName('btnRefresh')[0];
backBtn.addEventListener('click', () => {
    sessionStorage.setItem('back', 'back');
    location.replace('/reserve');
})


// 좌석 정보 불러오기 함수
function selectSeats() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/seat/selectSeats');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                // console.log(response[0]['seatName']);
                // console.log(response[0]['reservedSeatName']);
                if (response.length === 0) {
                    popUp.show('', '해당 시간대는 이미 예매가 끝난 시간입니다. 예매 페이지에서 다시 시작합니다.');
                    popUpBtns.show('close');
                    closeBtn.addEventListener('click', () => {
                        location.replace('/reserve');
                    })
                } else {
                    createSeats(response);
                    activateHeadCountSeatCount();
                }
            } else {
                popUp.show('', '서버에 예기치 못한 오류가 발생하여 좌석을 불러오지 못했습니다. 예매 페이지에서 다시 시작합니다.(좌석 부르기 실패)');
                popUpBtns.show('close');
                closeBtn.addEventListener('click', () => {
                    sessionStorage.setItem('back', 'back');
                    location.replace('/reserve');
                })
            }
        }
    }
    xhr.send();
}


// 좌석 태그 만들기 함수
function createSeats(response) {
    let alphabet = ''; // A,B,C 행 판별 변수
    let temporary = ''; // <div class="row"> 임시 저장 변수

    // 예약된 좌석 판별 함수
    function checkReservedSeat(item, input) {
        if (item['seatName'] === item['reservedSeatName']) {
            input.classList.add('soldOut');
            input.setAttribute('style', 'background: #2d2d2d url(../resources/images/soldOutX.png) no-repeat 50% 50%;');
        }
    }

    // 좌석 생성
    response.forEach(item => {
        if (alphabet !== item['seatName'].substring(0, 1)) {

            const row = document.createElement('div');
            row.classList.add('row');
            temporary = row;

            const position = document.createElement('div');
            position.classList.add('position');
            position.innerText = item['seatName'].substring(0, 1);

            const input = document.createElement('input');
            checkReservedSeat(item, input);
            input.classList.add('seat');
            input.setAttribute('type', 'button');
            input.setAttribute('value', item['seatName'])

            alphabet = item['seatName'].substring(0, 1);

            temporary.append(position, input);
            seats1.append(temporary);

        } else {
            const input = document.createElement('input');
            checkReservedSeat(item, input);
            input.classList.add('seat');
            input.setAttribute('type', 'button');
            input.setAttribute('value', item['seatName'])

            temporary.append(input);
        }
    })
}


// 인원선택 버튼 활성화 함수
// 좌석버튼 활성화 함수
function activateHeadCountSeatCount() {

    // 인원수버튼들 활성화
    const radios = document.querySelectorAll('.radio');
    radios.forEach(radio => {
        radio.removeAttribute('onclick');
    })

    // checked 되어있는 태그 요소(일반)
    let majorInput = document.querySelector('input[name="adult"]:checked');
    // checked 되어있는 태그 요소(청소년)
    let minorInput;
    if (document.querySelector('.minor')) {
        minorInput = document.querySelector('input[name="kid"]:checked');
    }

    let majorCount = 0;
    let minorCount = 0;
    let total = 0;
    let total2 = [];

    // 사용 가능한 좌석들
    const emptySeats = seats1.querySelectorAll('input:not(.soldOut)');
    // 선택되어진 좌석 표시 하는 박스들
    const pickBoxes = document.querySelector('#choiceList').querySelectorAll('li');

    // 총 가격
    const sum = document.querySelector('.sum');
    let sumCount = 0;

    // 일반 input 요소들
    const majorRadios = document.querySelectorAll('input[name="adult"]');
    majorRadios.forEach(major => {
        major.addEventListener('change', () => {
            majorCount = major.value;
            total = Number(majorCount) + Number(minorCount);

            // 커버 생성 또는 없애기
            // 모든값이 0일 시 커버 생성
            // 아닐시 커버 해제
            if (total === 0) {
                seatCover.style.display = 'block';
            } else {
                seatCover.style.display = 'none';
            }

            // 선택한 좌석 및 선택되어진 좌석박스 초기화
            sumCount = 0;
            sum.innerText = '총' + ' ' + sumCount + '원';
            emptySeats.forEach(item => {
                item.classList.remove('selecting');
            })
            pickBoxes.forEach(item => {
                item.classList.remove('on');
                item.innerText = '-';
            })

            // 8명 인원 초과 또는 이상한 값 변조시
            if (total > 8 || total < 0) {
                popUp.show('', '인원선택은 총 8명까지 선택할 수 있습니다.');
                popUpBtns.show('close');
                majorInput.checked = true;
                majorCount = majorInput.value;
                total = Number(majorCount) + Number(minorCount);
            } else {
                majorInput = major;
                total2 = [];
                for (let i = 0; i < majorCount; i++) {
                    total2[i] = '일반';
                }
                for (let i = 0; i < minorCount; i++) {
                    total2[Number(majorCount) + i] = '청소년';
                }

                headNameArray = total2;
                console.log(headNameArray);
            }
        })
    })


    // 청불이아닐 경우
    if (document.querySelector('.minor')) {
        // 청소년 input 요소들
        const minorRadios = document.querySelectorAll('input[name="kid"]');

        // 인원수 값이 바뀔시
        minorRadios.forEach(minor => {
            minor.addEventListener('change', () => {
                minorCount = minor.value;
                total = Number(majorCount) + Number(minorCount);
                // 커버 생성 또는 없애기
                // 모든값이 0일 시 커버 생성
                // 아닐시 커버 해제
                if (total === 0) {
                    seatCover.style.display = 'block';
                } else {
                    seatCover.style.display = 'none';
                }

                // 선택한 좌석 및 선택되어진 좌석박스 초기화
                sumCount = 0;
                sum.innerText = '총' + ' ' + sumCount + '원';
                emptySeats.forEach(item => {
                    item.classList.remove('selecting');
                })
                pickBoxes.forEach(item => {
                    item.classList.remove('on');
                    item.innerText = '-';
                })

                // 8명 인원 초과 또는 이상한 값 변조시
                if (total > 8 || total < 0) {
                    popUp.show('', '인원선택은 총 8명까지 선택할 수 있습니다.');
                    popUpBtns.show('close');
                    minorInput.checked = true;
                    minorCount = minorInput.value;
                    total = Number(majorCount) + Number(minorCount);
                } else {
                    minorInput = minor;
                    total2 = [];
                    for (let i = 0; i < majorCount; i++) {
                        total2[i] = '일반';
                    }
                    for (let i = 0; i < minorCount; i++) {
                        total2[Number(majorCount) + i] = '청소년';
                    }

                    headNameArray = total2;
                    console.log(headNameArray);
                }
            })
        })
    }

    // 좌석버튼들 활성화
    // 빈좌석 클릭시
    emptySeats.forEach(empty => {
        empty.addEventListener('click', (e) => {
            if(empty.classList.contains('soldOut')) {
                e.preventDefault();
                return false;
            }

            // 선택되어진 좌석 표시 하는 박스들(on 클래스 만, 선택되어진 좌석만 표시)
            const pickSeats = document.querySelector('#choiceList').querySelectorAll('.on');

            // 이미 골랐던 좌석을 눌렀을 때
            if (empty.classList.contains('selecting')) {
                // 선택 표시 삭제
                empty.classList.remove('selecting');
                // 선택되어있는 좌석들
                const selectingSeats = seats1.querySelectorAll('.selecting');
                // const pickBoxes = document.querySelector('#choiceList').querySelectorAll('li');

                if (selectingSeats.length == 0) {
                    seatNameArray = [];
                    console.log(seatNameArray);
                    pickBoxes.forEach(item => {
                        // 선택박스 모두 초기화
                        item.classList.remove('on');
                        item.innerText = '-';

                        sumCount = 0;
                        // seatNameArray = [];
                        // console.log(seatNameArray);
                        sum.innerText = '총' + ' ' + sumCount + '원';
                    })
                } else {
                    // 선택박스 모두 초기화
                    pickBoxes.forEach(item => {
                        item.classList.remove('on');
                        item.innerText = '-';
                    })
                    sumCount = 0;
                    seatNameArray = [];
                    // 선택되어진 좌석 수대로 선택박스에 넣기
                    for (let i = 0; i < selectingSeats.length; i++) {
                        pickBoxes[i].classList.add('on');
                        pickBoxes[i].innerText = selectingSeats[i].value;
                        seatNameArray[i] = selectingSeats[i].value;
                        if (total2[i] === '일반') {
                            sumCount += 10000;
                        } else {
                            sumCount += 5000;
                        }
                    }
                    sum.innerText = '총' + ' ' + sumCount.toLocaleString() + '원';
                }


            } else if (pickSeats.length < total) {
                // 콜백 관련 콜백 관련
                // promise 함수 관련 : 보류
                checkStatusSeat(empty, function (result) {
                    if (result === 'reserved') {
                        return false;
                    } else if (result === 'available') {
                        empty.classList.add('selecting');
                        const selectingSeats = seats1.querySelectorAll('.selecting');
                        // const pickBoxes = document.querySelector('#choiceList').querySelectorAll('li');

                        sumCount = 0;
                        seatNameArray = [];
                        for (let i = 0; i < selectingSeats.length; i++) {
                            pickBoxes[i].classList.add('on');
                            pickBoxes[i].innerText = selectingSeats[i].value;
                            seatNameArray[i] = selectingSeats[i].value;
                            if (total2[i] === '일반') {
                                sumCount += 10000;
                            } else {
                                sumCount += 5000;
                            }
                        }

                        sum.innerText = '총' + ' ' + sumCount.toLocaleString() + '원';
                    }
                });
            } else {
                popUp.show('', '선택 좌석수를 초과하셨습니다.');
                popUpBtns.show('close');
            }
        })
    })
}


// 좌석 버튼 클릭시, 예약이 되어있는지 또 한번 조회하는 함수
// 콜백 관련 콜백 관련
// promise 함수 관련 : 보류
function checkStatusSeat(seatNameTag, callback) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/seat/checkStatusSeat?seatName=${seatNameTag.value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                switch (response['result']) {
                    case 'expired_sessionindex':
                        popUp.show('', '유효하지 않은 접근입니다.(세션인덱스 값 초기화, 15분 장기 체류시)');
                        popUpBtns.show('close');
                        closeBtn.addEventListener('click', () => {
                            sessionStorage.setItem('back', 'back');
                            location.replace('/reserve');
                        })
                        break;
                    case 'nonexistent_seat':
                        popUp.show('', '유효하지 않는 접근(좌석값 변조)');
                        popUpBtns.show('close');
                        closeBtn.addEventListener('click', () => {
                            location.replace('/login');
                        })
                        break;
                    case 'reserved_seat':
                        seatStatus = 'reserved';
                        seatNameTag.classList.add('soldOut');
                        seatNameTag.setAttribute('style', 'background: #2d2d2d url(../resources/images/soldOutX.png) no-repeat 50% 50%;');
                        popUp.show('', '해당 좌석은 이미 예약된 좌석입니다. 다른 좌석을 골라주세요.');
                        popUpBtns.show('close');
                        callback('reserved');
                        break;
                    case 'available_seat':
                        callback('available');
                        break;
                    default:
                        popUp.show('', '서버에서 알 수 없는 응답을 반환하였습니다. 잠시 후 시도 해주세요.');
                        popUpBtns.show('close');
                        closeBtn.addEventListener('click', () => {
                            sessionStorage.setItem('back', 'back');
                            location.replace('/reserve');
                        })
                }
            } else {
                popUp.show('', '서버에 예기치 못한 오류가 발생하여 좌석현황을 조회하지 못했습니다. 예약 페이지로 되돌아갑니다.(좌석 현황 조회 실패)');
                popUpBtns.show('close');
                closeBtn.addEventListener('click', () => {
                    sessionStorage.setItem('back', 'back');
                    location.replace('/reserve');
                })
            }
        }
    }
    xhr.send();
}


// 결제하기 버튼 클릭시
const payBtn = document.getElementById('sendPayment');
payBtn.onsubmit = e => {
    e.preventDefault();

    if (headNameArray.length === 0) {
        popUp.show('', '인원수를 선택 해주세요.');
        popUpBtns.show('close');
        e.preventDefault();
        return false
    }

    if (seatNameArray.length === 0) {
        popUp.show('', '좌석을 선택 해주세요.');
        popUpBtns.show('close');
        e.preventDefault();
        return false
    }

    if (headNameArray.length !== seatNameArray.length) {
        popUp.show('', '인원수와 좌석수가 일치하지 않습니다.');
        popUpBtns.show('close');
        e.preventDefault();
        return false
    }


    // 결제 페이지 이동
    location.href = `/payment.do?headNameArray=${headNameArray}&seatNameArray=${seatNameArray}`;
}


//처음 시작점, 페이지가 다 로드 되었을 때 해당 함수 실행
window.onload = function () {
    // 좌석 정보 불러오기
    // 좌석 생성
    // 인원 및 좌석 버튼 활성화
    selectSeats();
}