const updateForm = document.getElementById('updateForm');
const contactChange = document.getElementById('changeContact');
const contactEl = document.querySelectorAll('label.contact');
const content = document.getElementById('content');
const contactSend = document.querySelector('[name="contactSend"]');
const codeVerify = document.querySelector('[name="contactVerify"]');
const contactCode = document.querySelector('[name="contactCode"]');
const userDeleteBtn = updateForm.querySelector('[rel="userDelete"]');


// 연락처 변경
contactChange.onclick = () => {

    if (contactChange.checked) {
        content.style.display = 'flex';

        contactSend.onclick = () => {
            if (updateForm['contact'].value === '') {
                popUp.show('입력오류', '변경할 전화번호를 입력해주세요.');
                popUpBtns.show('close');
                return;
            }

            if (!new RegExp(/^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/).test(updateForm['contact'].value)) {
                popUp.show('입력오류', '올바른 전화번호를 입력해주세요.');
                popUpBtns.show('close');
                return;
            }

            if (updateForm['currentContact'].value === updateForm['contact'].value) {
                popUp.show('입력오류', '이전의 전화번호와 동일합니다.');
                popUpBtns.show('close');
                return;
            }

            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            xhr.open('GET', `/contactCode?contact=${updateForm['contact'].value}`);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject.result) {
                            case 'failure':
                                popUp.show('전송실패', '잠시후 다시 시도해 주세요.');
                                popUpBtns.show('close');
                                break;
                            case 'success':
                                updateForm['contact'].setAttribute('disabled', 'disabled');
                                updateForm['contactSend'].setAttribute('disabled', 'disabled');
                                updateForm['contactCode'].removeAttribute('disabled');
                                updateForm['contactVerify'].removeAttribute('disabled');
                                updateForm['contactSalt'].value = responseObject.salt;
                                popUp.show('입력하신 연락처로 인증번호로 전송했습니다.', '아래 인증번호 확인란에 5분 이내로 입력해 주세요.');
                                popUpBtns.show('close');
                                break;
                            case 'failure_duplicate':
                                popUp.show('사용할 수 없는 번호입니다.', '고객센터로 문의해 주세요.');
                                popUpBtns.show('close');
                                break;
                            default:
                                popUp.show('서버 연결 실패', '잠시후 다시 시도해 주세요.');
                                popUpBtns.show('close');
                        }
                    } else {
                        popUp.show('서버 연결 실패', '잠시후 다시 시도해 주세요.');
                        popUpBtns.show('close');
                    }
                }
            };
            xhr.send(formData);
        }

        codeVerify.onclick = () => {
            if (updateForm['contactCode'].value === '') {
                popUp.show('인증번호 누락', '인증번호를 입력해 주세요.');
                popUpBtns.show('close');
                return;
            }
            if (!new RegExp(/^[0-9]{6}$/).test(updateForm['contactCode'].value)) {
                popUp.show('잘못된 인증번호', '올바른 인증번호를 입력해 주세요.');
                updateForm['contactCode'].focus();
                updateForm['contactCode'].select();
                popUpBtns.show('close');
                return;
            }

            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('contact', updateForm['contact'].value);
            formData.append('salt', updateForm['contactSalt'].value);
            formData.append('code', updateForm['contactCode'].value);
            xhr.open('PATCH', '/contactCode');
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject.result) {
                            case 'failure':
                                popUp.show('인증 실패', '인증번호를 잘못 입력하셨습니다. ');
                                popUpBtns.show('close');
                                break;
                            case 'success':
                                updateForm['contactCode'].setAttribute('disabled', 'disabled');
                                updateForm['contactVerify'].setAttribute('disabled', 'disabled');
                                popUp.show('인증 성공', '하단의 변경을 누르시면 연락처 변경이 완료됩니다.');
                                popUpBtns.show('close');
                                break;
                            case 'failure_expired':
                                popUp.show('인증번호 만료', '해당 인증번호는 유효시간이 만료되었습니다. 처음부터 다시 진행해 주세요');
                                popUpBtns.show('close');
                                break;
                            default:
                                popUp.show('인증 실패', '잠시후 다시 시도해 주세요.');
                                popUpBtns.show('close');
                        }

                    } else {
                        popUp.show('서버 연결 실패', '잠시후 다시 시도해 주세요.');
                        popUpBtns.show('close');
                    }
                }
            };
            xhr.send(formData);
        }
    } else {
        content.style.display = 'none';
    }

}

// 회원 정보 수정
updateForm.onsubmit = e => {
    e.preventDefault();

    if (contactChange.checked) {
        if (updateForm['contact'].value === '') {
            popUp.show('입력 누락', '전화번호를 입력해 주세요.');
            popUpBtns.show('close');
            return;
        }
        if (!new RegExp(/^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/).test(updateForm['contact'].value)) {
            popUp.show('입력 오류', '올바른 전화번호를 입력해주세요.');
            popUpBtns.show('close');
            updateForm.checkWarning.show('');
            return;
        }

        if (updateForm['contactCode'].value === '') {
            popUp.show('입력 누락', '인증번호를 입력해 주세요.');
            popUpBtns.show('close');
            return;
        }

        if (!new RegExp(/^[0-9]{6}$/).test(updateForm['contactCode'].value)) {
            popUp.show('입력 오류', '정확한 인증번호를 입력해 주세요.');
            popUpBtns.show('close');
            return;
        }
    }

    if (updateForm['uPassword'].value !== '' || updateForm['uPasswordCheck'].value !== '') {

        if (updateForm['uPassword'].value === '') {
            popUp.show('입력 누락', '비밀번호를 입력해 주세요.');
            popUpBtns.show('close');
            return;
        }

        if (!new RegExp(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/).test(updateForm['uPassword'].value)) {
            popUp.show('형식에 맞게 비밀번호를 입력해 주세요.', '대문자+소문자+숫자+특수문자 1개이상포함 8~20자리');
            popUpBtns.show('close');
            return;
        }

        if (updateForm['uPasswordCheck'].value === null) {
            popUp.show('입력 누락', '비밀번호를 한번 더 입력해 주세요.');
            popUpBtns.show('close');
            return;
        }

        if (updateForm['uPassword'].value !== updateForm['uPasswordCheck'].value) {
            popUp.show('입력 오류', '비밀번호가 동일하지 않습니다.<br> 다시 한번 확인해 주세요.');
            popUpBtns.show('close');
            return;
        }
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', updateForm['uEmail'].value);
    formData.append('contact', updateForm['currentContact'].value);

    if (contactChange.checked) {
        formData.append('newContact', updateForm['contact'].value);
        formData.append('salt', updateForm['contactSalt'].value);
        formData.append('code', updateForm['contactCode'].value);
    }

    if (updateForm['uPassword'].value !== '') {
        formData.append('uPassword', updateForm['uPassword'].value);
    }
    xhr.open('PATCH', '/mypage/update');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'success':
                        popUp.show('회원 정보 수정이 완료 되었습니다.', '개인정보 보호를 위해 다시 로그인 해주세요.');
                        popUpBtns.show('okay');
                        break;
                    case 'failure':
                        popUp.show('회원 정보 수정 실패', '잠시 후 다시 시도해 주세요.');
                        popUpBtns.show('close');
                        break;
                    case 'failure_not_verify':
                        popUp.show('인증번호 확인 누락', '연락처 변경시 인증을 완료해 주세요.');
                        popUpBtns.show('close');
                        break;
                    default :
                        popUp.show('변경 실패', '잠시 후 다시 시도해 주세요.');
                        popUpBtns.show('close');
                }
            } else {
                popUp.show('서버 연결 실패', '잠시후 다시 시도해 주세요.');
                popUpBtns.show('close');
            }
        }
    };
    xhr.send(formData);
}

// 회원탈퇴
userDeleteBtn.onclick = e => {
    e.preventDefault();
    cover.show();
    popUpBtns.AllHide();
    popUp.show('탈퇴 시 회원정보 복구는 불가능합니다', '계속 진행하시겠습니까? ');
    popUpBtns.show('close', 'deleteOk');

    deleteOkBtn.onclick = () => {
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('email', updateForm['uEmail'].value);
        formData.append('contact', updateForm['contact'].value);
        xhr.open('PATCH', '/mypage/user');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseText = xhr.responseText;
                    switch (responseText) {
                        case 'true':
                            popUp.show('회원탈퇴 완료', '그동안 이용해 주셔서 감사합니다.');
                            popUpBtns.show('okay');
                            break;
                        case 'false':
                            popUp.show('회원탈퇴 실패', '잠시 후 다시 시도해 주세요.');
                            popUpBtns.show('close');
                            break;
                        default:
                            popUp.show('회원탈퇴 실패', '잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    alert('서버 연결 실패, 잠시후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    }
}

