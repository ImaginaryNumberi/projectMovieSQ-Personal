const userCheck = document.getElementById('userCheckForm');


// 회원정보 경고
userCheck.checkWarning = userCheck.querySelector('[rel="checkWarning"]');

userCheck.checkWarning.show = (text) => {
    userCheck.checkWarning.innerHTML = text;
    userCheck.checkWarning.classList.add('visible');
}

// 본인 확인 (로그인 정보 비교, 일치 여부)
userCheck.onsubmit = e => {
    e.preventDefault();
    userCheck.checkWarning.hide();

    if (userCheck['password'].value === '') {
        userCheck.checkWarning.show('비밀번호를 입력해 주세요.');
        return;
    }

    if (!new RegExp(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/).test(userCheck['password'].value)) {
        userCheck.checkWarning.show('형식에 맞게 비밀번호를 입력해 주세요. <br> 대문자+소문자+숫자+특수문자 1개이상포함 8~20자리');
        userCheck['password'].focus();
        userCheck['password'].select();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', userCheck['email'].value);
    formData.append('password', userCheck['password'].value);
    xhr.open('POST', '/mypage/userCheck');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseText = xhr.responseText;
                switch (responseText) {
                    case 'true':
                        location.href = `./update`;
                        break;
                    case 'false':
                        userCheck.checkWarning.show('회원정보를 잘못 입력하셨습니다.');
                        break;
                }
            } else {
                alert('서버 연결실패');
            }
        }
    };
    xhr.send(formData);
}

