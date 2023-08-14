// 간편결제 클릭 시 네이버페이 초기화
const payForm = document.getElementById('payForm');
const speedEl = payForm.querySelector('.speed-container');
const payEl = speedEl.querySelectorAll('input[name="speed"]');

function radioChange() {
    const value = payForm.querySelector('input[name="pay"]:checked').value;
    if (value === 'speed') {
        speedEl.classList.add('visible');
        payEl[0].checked = true; // 기본값 초기화
    } else {
        speedEl.classList.remove('visible');
    }
}


// 번호 입력 시 카드 번호 보이기
// 번호 입력 시 다음칸으로 자동넘김 및  expire month/year 보이기
const numberEl = document.querySelectorAll('.number-input');
const number = document.querySelectorAll('.number');
const cancelBtn = document.querySelector('[rel="cancel"]');
const expireEl = document.querySelectorAll('.expire-input');
const month = document.querySelector('.month');
const year = document.querySelector('.year');


for (let i = 0; i < numberEl.length; i++) {
    numberEl[i].addEventListener('input', () => {
        number[i].innerText = numberEl[i].value;

        if (numberEl[i].value.length === numberEl[i].maxLength && i < numberEl.length - 1) {
            numberEl[i + 1].focus();
        }
    });
}


expireEl[0].oninput = () => {
    month.innerText = expireEl[0].value;
    if (expireEl[0].value.length >= expireEl[0].maxLength) {
        expireEl[1].focus();
    }
}
expireEl[1].oninput = () => year.innerText = expireEl[1].value;


// region payForm
// 선택한 정보 보이기
const cardContainer = document.getElementById('cardContainer');
const cardInputForm = document.getElementById('cardInputForm');
const finishContainer = document.getElementById('finishContainer');
const paymentContainer = document.getElementById('paymentContainer');
const timeList = document.getElementById('timeList');

cardInputForm.checkWarning = cardInputForm.querySelector('[rel="payWarning"]');

cardInputForm.checkWarning.show = (text) => {
    cardInputForm.checkWarning.innerHTML = text;
    cardInputForm.checkWarning.classList.add('visible');
}

cardInputForm.checkWarning.hide = () => {
    cardInputForm.checkWarning.innerHTML = '';
    cardInputForm.checkWarning.classList.add('visible');
}

// 예매 다시하기 눌렀을 때
const resetBtn = payForm.querySelector('[rel="reset"]');
resetBtn.onclick = e => {
    e.preventDefault();
    location.href = './reserve';
}


payForm.onsubmit = (e) => {
    e.preventDefault();

    const checkedPayWay = payForm.querySelector('input[name="pay"]:checked');
    const movieImage = finishContainer.querySelector('[rel="movieImage"]');
    const ticketNumber = finishContainer.querySelector('[rel="ticketNumber"]');
    const theater = finishContainer.querySelector('[rel="theater"]');
    const screen = finishContainer.querySelector('[rel="screen"]');
    const time = finishContainer.querySelector('[rel="time"]');
    const date = finishContainer.querySelector('[rel="date"]');
    const ticketTotal = finishContainer.querySelector('[rel="ticketTotal"]');
    const seatName = finishContainer.querySelector('[rel="seatName"]');
    const cancelTime = finishContainer.querySelector('[rel="cancelTime"]');
    const cardCompany = document.getElementById('cardCompany');

    if (checkedPayWay == null) {
        popUp.show('', '결제수단을 선택해 주세요.');
        popUpBtns.show('close');
        return;
    }

    //신용카드 선택시에만 카드 입력 보이기
    if (checkedPayWay.value === 'credit_card') {
        cover.show();
        cardContainer.classList.add('visible');
        cardInputForm['cMonth'].value = '';
        cardInputForm['cYear'].value = '';
        cardInputForm['cPassword'].value = '';

        const numberInputs = cardInputForm.querySelectorAll('.number-input');
        for (let numberInput of numberInputs) {
            numberInput.value = '';
        }

        cardInputForm['cBirth'].value = '';
        cardCompany.value = 'choice';
    }

    // TODO : 이전으로... 될까 ? ...
    // const before = paymentContainer.querySelector('[rel="befor"]');
    // before.onclick = () => {
    //
    // }


// 결제 확인 버튼 -> 예매 및 결제 완료 진행
    cardInputForm.onsubmit = e => {
        e.preventDefault();
        cardInputForm.checkWarning.hide();


        const selectedCompany = cardCompany.options[cardCompany.selectedIndex].value;

        if (selectedCompany === 'choice') {
            cardInputForm.checkWarning.show('결제하실 카드를 선택해 주세요.');
            return;
        }

        const numberInputs = cardInputForm.querySelectorAll('.number-input');


        for (let numberInput of numberInputs) {
            if (numberInput.value === '') {
                cardInputForm.checkWarning.show('카드번호를 입력해주세요.');
                cardInputForm[numberInput.name].focus();
                return;
            }

            if (!new RegExp(/^[0-9]{4}$/).test(numberInput.value)) {
                cardInputForm.checkWarning.show('카드번호를 잘못입력하셨습니다.');
                cardInputForm[numberInput.name].select();
                return;
            }
        }

        if (cardInputForm['cPassword'].value === '') {
            cardInputForm.checkWarning.show('잘못 입력하셨습니다.');
            cardInputForm['cPassword'].focus();
            return;
        }

        if (!new RegExp(/^[0-9]{2}$/).test(cardInputForm['cPassword'].value)) {
            cardInputForm.checkWarning.show('정확한 비밀번호를 입력해 주세요.');
            cardInputForm['cPassword'].focus();
            return;
        }

        if (cardInputForm['cMonth'].value === '') {
            cardInputForm.checkWarning.show('카드 유효기간(월)을 입력해 주세요.');
            cardInputForm['cMonth'].focus();
            return;
        }

        if (!RegExp(/(0[1-9]|1[012])/).test(cardInputForm['cMonth'].value)) {
            cardInputForm.checkWarning.show('카드 유효기간(월)을 잘못 입력하셨습니다.');
            cardInputForm['cMonth'].select();
            return;
        }

        if (cardInputForm['cYear'].value === '') {
            cardInputForm.checkWarning.show('카드 유효기간(년)을 입력해 주세요.');
            cardInputForm['cYear'].focus();
            return;
        }


        if (!RegExp(/(0[1-9]|[12][0-9]|3[01])$/).test(cardInputForm['cYear'].value)) {
            cardInputForm.checkWarning.show('카드 유효기간(년)을 잘못 입력하셨습니다.');
            cardInputForm['cYear'].select();
            return;
        }

        const now = new Date();

        if (cardInputForm['cYear'].value + cardInputForm['cMonth'].value < now.getFullYear() + String(now.getMonth() + 1).padStart(2, '0')) {
            cardInputForm.checkWarning.show('사용할 수 없는 카드 입니다.');

            return;
        }

        if (cardInputForm['cYear'].value > now.getFullYear() + 3) {
            cardInputForm.checkWarning.show('사용할 수 없는 카드 입니다.');
            cardInputForm['cYear'].select();
            return;
        }


        if (cardInputForm['cBirth'].value === '') {
            cardInputForm.checkWarning.show('주민등록번호를 입력해 주세요.');
            cardInputForm['cBirth'].focus();
            return;
        }

        if (!new RegExp(/([0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[1,2][0-9]|3[0,1]))/).test(cardInputForm['cBirth'].value)) {
            cardInputForm.checkWarning.show('형식에 맞는 주민등록번호를 입력해 주세요');
            cardInputForm['cBirth'].select();
            return;
        }


        let cardNumber = "";
        for (let number of numberInputs) {
            cardNumber += number.value;
        }


        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        // 예매 정보

        formData.append("category", checkedPayWay.value);
        formData.append("paymentCompany", selectedCompany);
        formData.append("cardNumber", cardNumber);
        formData.append("paymentPassword", cardInputForm['cPassword'].value);
        xhr.open('POST', 'pay');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject.result) {
                        case 'failure':
                            cardInputForm.checkWarning.show('결제 실패 <br> 잠시 후 다시 시도해 주세요.');
                            break;
                        case 'success':
                            console.log('성공');
                            let index = responseObject['movieIndex'];
                            movieImage.src = './movie/image?index='+index;
                            let ticket = responseObject['ticketNumber'];
                            ticketNumber.innerText = ticket.replace(/(.{4})/g, "$1-").slice(0, -1);
                            theater.innerText = responseObject['theater'];
                            date.innerText = responseObject['date'];
                            screen.innerText = responseObject['screen'];
                            time.innerText = responseObject['time'];
                            ticketTotal.innerText = "일반: "+responseObject["adult"]+" , 청소년: "+responseObject["child"];
                            seatName.innerText = responseObject['seatName'];
                            cancelTime.innerText = responseObject['cancelTime'];
                            cardContainer.remove();
                            paymentContainer.remove();
                            cover.hide();
                            finishContainer.classList.add('visible');
                            break;
                        case 'not_login':
                            location.replace('/login');
                        case 'invalid_index':
                            location.replace('/login');
                        case `invalid_screen_index`:
                            location.replace('/login');
                        case 'invalid_array':
                            location.replace('/login');
                    }
                } else {
                    cardInputForm.checkWarning.show('서버 연결 실패 <br> 잠시후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    }
}


// 결제 취소 누르기
cancelBtn.onclick = e => {
    e.preventDefault();

    cardInputForm.checkWarning.hide();
    cover.hide();
    cardContainer.classList.remove('visible');
}

// endregion

