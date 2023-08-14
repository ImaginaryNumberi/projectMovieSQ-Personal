const recoverForm = document.getElementById('recoverForm');

// 아이디 비밀번호 찾기 보여주기
recoverForm.show = () => {
    recoverForm.classList.add('visible');
}

// 아이디 찾기 경고 (이름,생일,연락처,인증번호)
recoverForm.nameWarning = recoverForm.querySelector('[rel="eNameWarning"]');
recoverForm.nameWarning.show = (text) => {
    recoverForm.nameWarning.innerHTML = text;
    recoverForm.nameWarning.classList.add('visible');
}
recoverForm.nameWarning.hide = () =>
    recoverForm.nameWarning.classList.remove('visible');

recoverForm.birthWarning = recoverForm.querySelector('[rel="eBirthWarning"]');
recoverForm.birthWarning.show = (text) => {
    recoverForm.birthWarning.innerHTML = text;
    recoverForm.birthWarning.classList.add('visible');
}
recoverForm.birthWarning.hide = () => {
    recoverForm.birthWarning.classList.remove('visible')
}

recoverForm.contactWarning = recoverForm.querySelector('[rel="eContactWarning"]');
recoverForm.contactWarning.show = (text) => {
    recoverForm.contactWarning.innerHTML = text;
    recoverForm.contactWarning.classList.add('visible');
}
recoverForm.contactWarning.hide = () => recoverForm.contactWarning.classList.remove('visible');

recoverForm.contactCodeWarning = recoverForm.querySelector('[rel="eContactCodeWarning"]');
recoverForm.contactCodeWarning.show = (text) => {
    recoverForm.contactCodeWarning.innerHTML = text;
    recoverForm.contactCodeWarning.classList.add('visible');
}
recoverForm.contactCodeWarning.hide = () => recoverForm.contactCodeWarning.classList.remove('visible');

//비밀번호 재설정 경고 (이메일,이름,생일)
recoverForm.emailWarning = recoverForm.querySelector('[rel="pMailWarning"]');
recoverForm.emailWarning.show = (text) => {
    recoverForm.emailWarning.innerHTML = text;
    recoverForm.emailWarning.classList.add('visible');
}
recoverForm.emailWarning.hide = () =>
    recoverForm.emailWarning.classList.remove('visible');

recoverForm.pNameWarning = recoverForm.querySelector('[rel="pNameWarning"]');
recoverForm.pNameWarning.show = (text) => {
    recoverForm.pNameWarning.innerHTML = text;
    recoverForm.pNameWarning.classList.add('visible');
}
recoverForm.pNameWarning.hide = () =>
    recoverForm.pNameWarning.classList.remove('visible');


// 이메일 확인 팜업 (비밀번호 재설정 이동)

const radios = recoverForm.elements.option;


// 화면 전환 시 초기화
radios.forEach((radio) => radio.onclick = () => {
    recoverForm['eName'].value = '';
    recoverForm['eBirth'].value = '';
    recoverForm['eContact'].value = '';
    recoverForm['eContactCode'].value = '';
    recoverForm['pMail'].value = '';
    recoverForm['pName'].value = '';
})


// 이메일 찾기 인증번호 전송
recoverForm['eContactSend'].onclick = () => {
    recoverForm.emailWarning.hide();
    recoverForm.birthWarning.hide();
    recoverForm.contactWarning.hide();
    recoverForm.contactCodeWarning.hide();

    if (recoverForm['eContact'].value === '') {
        recoverForm.contactWarning.show('연락처를 입력해주세요.');
        return;
    }

    if (!RegExp(/^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/).test(recoverForm['eContact'].value)) {
        recoverForm.contactWarning.show('정확한 휴대전화번호를 입력해 주세요.');
        return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/contactCodeRec?contact=${recoverForm['eContact'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'success':
                        recoverForm['eContact'].setAttribute('disabled', 'disabled');
                        recoverForm['eContactSend'].setAttribute('disabled', 'disabled');
                        recoverForm['eContactCode'].removeAttribute('disabled');
                        recoverForm['eContactVerify'].removeAttribute('disabled');
                        recoverForm['eContactSalt'].value = responseObject.salt;
                        recoverForm.contactWarning.show('입력하신 연락처로 인증번호를 전송했습니다.<br> 5분 이내로 입력해 주세요. ');
                        recoverForm['eContactCode'].focus();
                        break;
                    case 'failure_not_user':
                        recoverForm.contactWarning.show('입력하신 정보로 가입된 회원을 찾을 수 없습니다 <br> 다시 한번 확인해 주세요.');
                        break;
                    case 'failure':
                        recoverForm.contactWarning.show('인증번호 전송에 실패했습니다. 잠시 후 다시 시도해 주세요.')
                        break;
                    default:
                        recoverForm.contactWarning.show('알 수 없는 이유로 인증번호 전송에 실패했습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                recoverForm.contactWarning.show('서버와 통신하지 못했습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
}

// 이메일 찾기 인증번호 확인
recoverForm['eContactVerify'].onclick = () => {
    recoverForm.emailWarning.hide();
    recoverForm.nameWarning.hide();
    recoverForm.birthWarning.hide();
    recoverForm.contactWarning.hide();
    recoverForm.contactCodeWarning.hide();

    if (recoverForm['eContactCode'].value === '') {
        recoverForm.contactCodeWarning.show('인증번호를 입력해 주세요');
        recoverForm['eContactCode'].focus();
        return;
    }

    if (!new RegExp(/^[0-9]{6}$/).test(recoverForm['eContactCode'].value)) {
        recoverForm.contactCodeWarning.show('올바른 인증번호를 입력해 주세요.');
        recoverForm['eContactCode'].focus();
        recoverForm['eContactCode'].select();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append("contact", recoverForm['eContact'].value);
    formData.append("code", recoverForm['eContactCode'].value);
    formData.append("salt", recoverForm['eContactSalt'].value);
    xhr.open('PATCH', 'contactCodeRec');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject.result) {
                    case 'failure':
                        recoverForm.contactCodeWarning.show('올바른 인증번호를 입력해 주세요.');
                        recoverForm.contactCodeWarning.show('인증번호를 잘못 입력 하셨습니다.<br> 다시 한번 확인해 주세요.');
                        break;
                    case'failure_expired':
                        recoverForm.contactCodeWarning.show('해당 번호는 유효기간이 만료되었습니다. 다시 처음부터 진행해 주세요.');
                        break;
                    case'success':
                        recoverForm.contactCodeWarning.show('인증번호 인증이 완료되었습니다. <br> 아이디 찾기 버튼을 눌러 주세요.');
                        recoverForm['eContactCode'].setAttribute('disabled', 'disabled');
                        recoverForm['eContactVerify'].setAttribute('disabled', 'disabled');
                        break;
                    default :
                        recoverForm.contactCodeWarning.show('인증번호 인증에 실패했습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                recoverForm.contactCodeWarning.show('서버와 통신하지 못했습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}

// 아이디 찾기 , 비밀번호 재설정 확인
recoverForm.onsubmit = e => {
    e.preventDefault();
    recoverForm.emailWarning.hide();
    recoverForm.nameWarning.hide();
    recoverForm.birthWarning.hide();
    recoverForm.contactWarning.hide();
    recoverForm.contactCodeWarning.hide();
    recoverForm.pNameWarning.hide();

    // 아이디 찾기
    if (recoverForm['option'].value === 'email') {
        if (recoverForm['eName'].value === '') {
            recoverForm.nameWarning.show('이름을 입력해 주세요');
            recoverForm['eName'].focus();
            return;
        }

        if (!new RegExp(/^[가-힣]{2,4}|[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/).test(recoverForm['eName'].value)) {
            recoverForm.nameWarning.show('이름을 정확하게 입력해 주세요.');
            recoverForm['eName'].focus();
            recoverForm['eName'].select();
            return;
        }

        if (recoverForm['eBirth'].value === '') {
            recoverForm.birthWarning.show('생년월일을 입력해 주세요');
            recoverForm['eBirth'].focus();
            return;
        }

        if (recoverForm['eBirth'].value > formatDate(new Date())) {
            recoverForm.birthWarning.show('정확한 생년월일을 입력해주세요.');
            recoverForm['eBirth'].focus();
            recoverForm['eBirth'].select();
            return;
        }

        if (!new RegExp(/^(19[0-9][0-9]|20\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/).test(recoverForm['eBirth'].value)) {
            recoverForm.birthWarning.show('형식에 맞는 생년월일을 입력해 주세요.');
            recoverForm['eBirth'].focus();
            recoverForm['eBirth'].select();
            return;
        }

        if (recoverForm['eContact'].value === '') {
            recoverForm.contactWarning.show('연락처를 입력해 주세요');
            recoverForm['eContact'].focus();
            return;
        }

        if (!new RegExp(/^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/).test(recoverForm['eContact'].value)) {
            recoverForm.contactWarning.show('올바른 전화번호를 입력해주세요.');
            recoverForm['eContact'].focus();
            recoverForm['eContact'].select();
            return;
        }
        if (recoverForm['eContactCode'].value === '') {
            recoverForm.contactCodeWarning.show('인증번호를 입력해 주세요');
            recoverForm['eContactCode'].focus();
            return;
        }

        if (!new RegExp(/^[0-9]{6}$/).test(recoverForm['eContactCode'].value)) {
            recoverForm.contactCodeWarning.show('올바른 인증번호를 입력해 주세요.');
            recoverForm['eContactCode'].focus();
            recoverForm['eContactCode'].select();
            return;
        }

        if (recoverForm['eContactSalt'].value === '') {
            recoverForm.contactCodeWarning.show('인증번호 확인을 진행해 주세요.');
            recoverForm['eContactCode'].focus();
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('name', recoverForm['eName'].value);
        formData.append('birthStr', recoverForm['eBirth'].value);
        formData.append('contact', recoverForm['eContact'].value);
        formData.append('code', recoverForm['eContactCode'].value);
        formData.append('salt', recoverForm['eContactSalt'].value);
        xhr.open('POST', 'searchId');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 400) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject.result) {
                        case'failure':
                            popUp.show('입력하신 정보로 가입된 회원을 찾을 수 없습니다',
                                '다시 한번 확인해 주세요'
                            );
                            popUpBtns.show('close');
                            break;
                        case 'failure_not_verify':
                            popUp.show('<b>인증번호 확인 미완료</b>',
                                '인증번호 확인 버튼을 눌러 연락처 인증을 완료해주세요'
                            );
                            popUpBtns.show('close');

                            break;
                        case 'success':
                            popUp.show(`${responseObject.name} 님`,
                                `ID는 ${responseObject.email} 입니다`);
                            popUpBtns.show('login', 'recoverPassword');
                            break;
                    }
                } else {
                    popUp.show();
                }
            }
        };
        xhr.send(formData);
    }

    // 비밀번호 재설정
    if (recoverForm['option'].value === 'password') {
        if (recoverForm['pMail'].value === '') {
            recoverForm.emailWarning.show('이메일을 입력해 주세요');
            return;
        }
        if (!new RegExp('^(?=.{10,50}$)([\\da-zA-Z\\-_\\.]{5,25})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$').test(recoverForm['pMail'].value)) {
            recoverForm.emailWarning.show('잘못된 이메일 형식입니다. 다시 한번 확인해 주세요.');
            recoverForm['pMail'].focus();
            recoverForm['pMail'].select();
            return;
        }

        if (recoverForm['pName'].value === '') {
            recoverForm.pNameWarning.show('이름을 입력해 주세요');
            return;
        }
        if (!new RegExp(/^[가-힣]{2,4}|[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/).test(recoverForm['pName'].value)) {
            recoverForm.pNameWarning.show('이름을 정확하게 입력해 주세요.');
            recoverForm['pName'].focus();
            recoverForm['pName'].select();
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('email', recoverForm['pMail'].value);
        formData.append('name', recoverForm['pName'].value);
        xhr.open('POST', 'recoverPassword');
        popUp.show('이메일 전송중입니다.', '약 5분 정도 소요되므로 잠시만 기다려 주세요.')
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    popUp.hide();
                    switch (responseObject.result) {
                        case 'failure':
                            popUp.show('비밀번호 재설정에 실패했습니다.',
                                '잠시후 다시 시도해 주세요.'
                            )
                            popUpBtns.show('close');
                            return;
                        case 'failure_not_user':
                            popUp.show('입력하신 정보로 가입된 회원을 찾을 수 없습니다.',
                                '다시 한번 확인해 주세요.'
                            );
                            popUpBtns.show('close');
                            return;
                        case 'success':
                            popUp.show(
                                '입력하신 이메일로 회원 인증을 위한 링크가 담긴 메일을 전송 했습니다.',
                                '10분 이내로 확인후 비밀번호를 변경해 주세요'
                            )
                            popUpBtns.show('okay', 'loginColor');
                            return;
                        default:
                            popUp.show('알 수 없는 이유로 비밀번호 재설정에 실패했습니다.',
                                '잠시후 다시 시도해 주세요.'
                            );
                    }
                } else {
                    popUp.show('서버와 통신하지 못했습니다.', '잠시 후 다시 시도해 주세요');
                }
            }
        };
        xhr.send(formData);
    }
}