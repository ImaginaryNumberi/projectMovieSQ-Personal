const recoverForm = document.getElementById('recoverForm');


recoverForm.onsubmit = e => {
    e.preventDefault();

    if (recoverForm['password'].value === '') {
        alert('비밀번호를 입력해 주세요.');
        recoverForm['password'].focus();
        return;
    }

    if (!new RegExp(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/).test(recoverForm['password'].value)) {

        alert('형식에 맞게 비밀번호를 입력해 주세요.\n대문자+소문자+숫자+특수문자 1개이상포함 (8~20자리)');
        recoverForm['password'].focus();
        recoverForm['password'].select();
        return;
    }

    if (recoverForm['passwordCheck'].value === '') {
        alert('비밀번호를 다시 한번 입력해 주세요.');
        recoverForm['passwordCheck'].focus();
        return;
    }

    if (recoverForm['passwordCheck'].value !== recoverForm['password'].value) {
        alert('비밀번호가 동일하지 않습니다. 다시 한번 확인해 주세요.');
        recoverForm['passwordCheck'].focus();
        recoverForm['passwordCheck'].select();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', recoverForm['email'].value);
    formData.append('code', recoverForm['code'].value);
    formData.append('salt', recoverForm['salt'].value);
    formData.append('password', recoverForm['password'].value);
    xhr.open('PATCH', '/recoverPassword');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 400) {
                const responseText = xhr.responseText;
                switch (responseText) {
                    case 'failure':
                        alert('비밀번호 재설정 실패\n잠시후 다시 시도해 주세요.');
                        break;
                    case 'success':
                        alert('비밀번호 재설정이 완료되었습니다.\n변경하신 비밀번호로 로그인 해주세요.');
                        location.href = './login';
                        break;
                    default :

                }
            } else {
                alert('서버와 통신하지 못했습니다.\n잠시 후 다시 시도해 주세요');
            }
        }
    };
    xhr.send(formData);


}