const cancelTickets = document.querySelectorAll('[rel="cancelTicket"]');
const movie = document.querySelectorAll('[rel="movieName"]');
const date = document.querySelectorAll('[rel="date"]');
const seat = document.querySelectorAll('[rel="seat"]');

const ticketInfos = document.querySelectorAll('[rel="reservedInformation"]');
const information = document.querySelector('.information');
information.closes = information.querySelectorAll('[rel="close"]');

// 예매 취소
cancelTickets.forEach((ticket, index) => {
    ticket.addEventListener("click", e => {
        e.preventDefault();

        popUp.show(`${movie[index].innerText} <br> ${date[index].innerText} <br> ${seat[index].innerText} <br> 예매를 취소하시겠습니까?`, '예매취소는 상영 시작 15분 전까지 가능합니다.');
        popUpBtns.show('close', 'deleteOk');

        deleteOkBtn.onclick = e => {
            e.preventDefault();
            popUp.hide();
            const reservationIndex = ticket.dataset.index;
            //let ticketNumber = document.getElementById('ticketIndex').textContent;
            //ticketNumber = ticketNumber.replace(/-/g,'');

            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            xhr.open('PATCH', `/mypage/cancelTicket?reservationIndex=${reservationIndex}`);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 400) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject.result) {
                            case'success':
                                popUp.show('예매 취소', '해당 영화 예매를 취소했습니다.');
                                popUpBtns.show('okay');
                                break;
                            case'failure':
                                popUp.show('취소 실패', '잠시 후 다시 시도해 주세요.');
                                popUpBtns.show('okay');
                                break;
                            case 'failure_time_expired':
                                popUp.show('취소 실패', '예매 취소 가능한 시간이 지났습니다.');
                                popUpBtns.show('okay');
                                break;
                            case 'not_login':
                                popUp.show('취소 실패', '로그인 되어있지 않습니다.');
                                popUpBtns.show('okay');
                                closeBtn.addEventListener('click', () => {
                                    location.replace('/login');
                                })
                            case 'failure_db':
                                popUp.show('취소 실패', '서버에 문제가 생겨 환불 되지 않았습니다. 잠시 후 다시 시도해주세요.');
                                popUpBtns.show('okay');
                                break;
                            default:
                                popUp.show('실패', '잠시 후 다시 시도해 주세요.');
                                popUpBtns.show('okay');
                                break;
                        }
                    } else {
                        popUp.show('서버 연결 실패', '잠시후 다시 시도해 주세요.');
                        popUpBtns.show('close');
                    }
                }
            };
            xhr.send(formData);

        }


    })
})

const main = information.querySelector('.main');

// 예매정보 스크롤 상단 유지
information.show = () => {
    information.classList.add('visible');
    main.scrollTo(0, 0);
}


// 예매 정보 닫기
information.closes.forEach(close => {
    close.addEventListener('click', () => {
        information.hide();
        cover.hide();
    })
});


// 예매정보출력 보기
const ticketNumber = information.querySelector('.ticketNumber');
const movieName = information.querySelector('.movieName');
const movieDate = information.querySelector('.movieDate');
const theater = information.querySelector('.theater');
const person = information.querySelector('.person');
const seatData = information.querySelector('.seat');

ticketInfos.forEach((ticket) => {
    ticket.addEventListener('click', e => {

        const index = ticket.dataset.index;
        console.log(index);
        const xhr = new XMLHttpRequest();

        xhr.open('GET', `ticket/information?index=${index}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject.result) {
                        case 'failure':
                            popUp.show('예매정보출력 조회 실패', '잠시후 다시 시도해 주세요.');
                            popUpBtns.show('close');
                            break;
                        case 'success':
                            cover.show();
                            information.show();
                            ticketNumber.innerText = responseObject['ticketNumber'].replace(/(.{4})/g, "$1-").slice(0, -1);
                            movieName.innerText = responseObject['movieName'];
                            movieDate.innerText = responseObject['movieDate'];
                            theater.innerText = responseObject['theater'];
                            screen.innerText = responseObject['screen'];
                            seatData.innerText = responseObject['seat'];
                            break;
                        default:
                            popUp.show('예매정보출력 조회 실패', '잠시후 다시 시도해 주세요.');
                            popUpBtns.show('close');

                    }
                } else {
                    popUp.show('서버 연결 실패', '잠시후 다시 시도해 주세요.');
                    popUpBtns.show('okay');
                }
            }
        };
        xhr.send();


    })

});

