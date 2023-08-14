const loginForm = document.getElementById('loginForm');

// 로그인 경고창 show,hide 정의 (이메일, 비밀번호 미작성, 불일치)
loginForm.emailWarning = loginForm.querySelector('[rel="emailWarning"]');
loginForm.emailWarning.show = (text) => {
    loginForm.emailWarning.innerHTML = text;
    loginForm.emailWarning.classList.add('visible');
}
loginForm.emailWarning.hide = () => loginForm.emailWarning.classList.remove('visible');

loginForm.passwordWarning = loginForm.querySelector('[rel="passwordWarning"]');
loginForm.passwordWarning.show = (text) => {
    loginForm.passwordWarning.innerHTML = text;
    loginForm.passwordWarning.classList.add('visible');
}
loginForm.passwordWarning.hide = () => loginForm.passwordWarning.classList.remove('visible');

loginForm.loginWarning = loginForm.querySelector('[rel="loginWarning"]');
loginForm.loginWarning.show = (text) => {
    loginForm.loginWarning.innerHTML = text;
    loginForm.loginWarning.classList.add('visible');
}


if (localStorage.getItem("rememberId") !== null) {
    loginForm['email'].value = localStorage.getItem("rememberId");

}


//로그인 진행 및 경고
loginForm.onsubmit = e => {
    e.preventDefault();
    loginForm.emailWarning.hide();
    loginForm.passwordWarning.hide();
    loginForm.loginWarning.hide();

    if (loginForm['email'].value === '') {
        loginForm.emailWarning.show('아이디(이메일)를 입력해 주세요');
        loginForm['email'].focus();
        return;
    }

    if (!new RegExp('^(?=.{10,50}$)([\\da-zA-Z\\-_\\.]{5,25})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$').test(loginForm['email'].value)) {
        loginForm.emailWarning.show('잘못된 이메일 형식입니다. 다시 한번 확인해 주세요.');
        loginForm['email'].focus();
        loginForm['email'].select();
        return;
    }

    if (loginForm['password'].value === '') {
        loginForm.passwordWarning.show('비밀번호를 입력해 주세요');
        loginForm['password'].focus();
        return;
    }

    if (!new RegExp(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/).test(loginForm['password'].value)) {
        loginForm.passwordWarning.show('형식에 맞게 비밀번호를 적어주세요. <br> 대문자+소문자+숫자+특수문자 1개이상포함 8~20자리');
        loginForm['password'].focus();
        loginForm['password'].select();
        return;
    }


    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append("email", loginForm['email'].value);
    formData.append("password", loginForm['password'].value);
    xhr.open('POST', '/login');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 400) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'success':
                        const rememberIdCheck = document.getElementById('rememberId');
                        if (rememberIdCheck.checked) {
                            localStorage.setItem("rememberId", loginForm['email'].value);
                        } else {
                            localStorage.removeItem("rememberId");
                        }

                        if (responseObject.url == null) {
                            location.href = '/';
                        } else {
                            location.href = responseObject.url;
                        }
                        break;
                    case 'failure':
                        loginForm.loginWarning.show('로그인에 실패했습니다. <br>잠시 후 다시 시도해 주세요.');
                        break;
                    case 'failure_wrong_id':
                        loginForm.loginWarning.show('입력하신 정보와 일치하는 회원이 없습니다. <br> 다시 한번 확인해 주세요.');
                        break;
                    case 'failure_wrong_pwd':
                        loginForm.loginWarning.show('입력하신 정보와 일치하는 회원이 없습니다. <br> 다시 한번 확인해 주세요.');
                        break;
                    case 'failure_deleted':
                        loginForm.loginWarning.show('해당 계정은 삭제된 계정입니다.');
                        break;
                    case 'failure_suspended':
                        loginForm.loginWarning.show('해당 계정은 이용이 정지된 계정입니다. <br>관리자에게 문의해 주세요.')
                        break;
                    default :
                        loginForm.loginWarning.show('알 수 없는 이유로 로그인에 실패했습니다.<br> 잠시 후 다시 시도해 주세요.');
                }
            } else {
                loginForm.loginWarning.show('서버 통신에 실패했습니다. <br> 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}


// 카카오 로그인 반응
if (document.location.href.includes("notConnect")) {
    popUp.show('간편 로그인', '마이페이지에서 카카오 연동을 해주세요.');
    popUpBtns.show('login');
    kakaoLoginDelay();
}