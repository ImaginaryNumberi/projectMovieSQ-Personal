HTMLElement.prototype.show = function () {
    this.classList.add('visible');
};

HTMLElement.prototype.hide = function () {
    this.classList.remove('visible');
};

// 현재시간 구하기(yyyymmdd)
function formatDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = ('0' + (today.getMonth() + 1)).slice(-2);
    const day = ('0' + today.getDate()).slice(-2);
    return year + month + day;
}

// HEADER
// 메인 메뉴 클릭 이벤트
const menus = document.querySelectorAll('.menu');


menus.forEach(menu =>
    menu.addEventListener('click', () => {
        setActiveMenu(menu);
    })
);


function setActiveMenu(activeElement) {
    const menus = document.querySelectorAll('.menu');
    menus.forEach(menu =>
        menu.classList.toggle('active', menu === activeElement));
}


// popUp
const cover = document.getElementById('cover');
const popUp = document.getElementById('popUp');
const popUpBtns = document.querySelectorAll('[data-method]');
const login = document.querySelector('[data-method="login"]');
const loginColor = document.querySelector('[data-method="loginColor"]')
const okayBtn = document.querySelector('[data-method="okay"]');
const closeBtn = document.querySelector('[data-method="close"]')
const pwdBtn = document.querySelector('[data-method="recoverPassword"]');
const deleteOkBtn = document.querySelector('[data-method="deleteOk"]');
const mainBtn = document.querySelector('[data-method="main"]');

popUp.show = (param1, param2) => {
    cover.classList.add('visible');
    popUp.classList.add('visible');
    popUpBtns.AllHide();
    popUp.querySelector('[rel="paramOne"]').innerHTML = param1;
    popUp.querySelector('[rel="paramTwo"]').innerHTML = param2;
}

popUp.hide = () => {
    cover.classList.remove('visible');
    popUp.classList.remove('visible');
}

// 모든 버튼 숨기기
popUpBtns.AllHide = () => {
    popUpBtns.forEach(Btn => {
        Btn.classList.add('hidden');
    })
}

// 해당 버튼만 보이기 ex)popUpBtns.show('');
popUpBtns.show = (...buttons) => {
    buttons.forEach((button) => {
        const element = document.querySelector(`[data-method="${button}"]`);
        if (element) {
            element.classList.remove('hidden');
        }
    });
};

// 닫기 버튼 효과
closeBtn.onclick = () => {
    cover.hide();
    popUp.hide();
}

// 확인 버튼 효과
okayBtn.onclick = e => {
    e.preventDefault();
    cover.hide();
    popUp.hide();
    location.reload();
}

// 비밀번호 재설정 버튼 효과
pwdBtn.onclick = e => {
    e.preventDefault();
    cover.hide();
    popUp.hide();

    radios.value = 'password';
}

mainBtn.onclick = e => {
    e.preventDefault();
    cover.hide();
    popUp.hide();
    location.href = '.';
}

function delay() {
    setTimeout(() => {
        mainBtn.click();
        location.href = './';
    }, 10000);
}

function kakaoLoginDelay() {
    setTimeout(() => {
        login.click();
        location.href = './login';
    }, 10000);
}