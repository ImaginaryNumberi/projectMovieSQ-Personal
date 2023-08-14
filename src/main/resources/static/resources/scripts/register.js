const registerForm = document.getElementById('registerForm');
const user = document.getElementById('user');

// 회원가입 보여주기
// registerForm.show = () => {
//     registerForm.classList.remove('step-1', 'step-2', 'step-3', 'step-4');
//     registerForm.classList.add('step-1', 'visible');
// }

// 경고 보이게 하기
function showWarning(element, text) {
    element.innerHTML = text;
    element.classList.add('visible');
}

function hideWarning(element) {
    element.classList.remove('visible');
}

// 1단계 경고 (연락처 인증)
registerForm.contactWarning = document.querySelector('[rel ="contactWarning"]');
registerForm.contactWarning.show = (text) => showWarning(registerForm.contactWarning, text);
registerForm.contactWarning.hide = () => hideWarning(registerForm.contactWarning);

registerForm.contactCodeWarning = document.querySelector('[rel="contactCodeWarning"]');
registerForm.contactCodeWarning.show = (text) => showWarning(registerForm.contactCodeWarning, text);
registerForm.contactCodeWarning.hide = () => hideWarning(registerForm.contactCodeWarning);

//2단계 경고 (이용약관 체크)
registerForm.agreeWarning = document.querySelector('[rel="agreeWarning"]');
registerForm.agreeWarning.show = (text) => showWarning(registerForm.agreeWarning, text);
registerForm.agreeWarning.hide = () => hideWarning(registerForm.agreeWarning);

//3단계 경고 (개인정보 입력)
registerForm.nameWarning = document.querySelector('[rel="nameWarning"]');
registerForm.nameWarning.show = (text) => showWarning(registerForm.nameWarning, text);
registerForm.nameWarning.hide = () => hideWarning(registerForm.nameWarning);

registerForm.birthWarning = document.querySelector('[rel="birthWarning"]');
registerForm.birthWarning.show = (text) => showWarning(registerForm.birthWarning, text);
registerForm.birthWarning.hide = () => hideWarning(registerForm.birthWarning);

registerForm.emailWarning = document.querySelector('[rel="emailWarning"]');
registerForm.emailWarning.show = (text) => showWarning(registerForm.emailWarning, text);
registerForm.emailWarning.hide = () => hideWarning(registerForm.emailWarning);

registerForm.passwordWarning = document.querySelector('[rel="passwordWarning"]');
registerForm.passwordWarning.show = (text) => showWarning(registerForm.passwordWarning, text);
registerForm.passwordWarning.hide = () => hideWarning(registerForm.passwordWarning);

registerForm.passwordCheckWarning = document.querySelector('[rel="passwordCheckWarning"]');
registerForm.passwordCheckWarning.show = (text) => showWarning(registerForm.passwordCheckWarning, text);
registerForm.passwordCheckWarning.hide = () => hideWarning(registerForm.passwordCheckWarning);


// 회원가입 휴대폰 인증
registerForm['contactSend'].onclick = () => {
    registerForm.contactWarning.hide();

    if (registerForm['contact'].value === '') {
        registerForm.contactWarning.show('휴대전화 번호를 입력해주세요.');
        registerForm['contact'].focus();
        return;
    }
    if (!new RegExp(/^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/).test(registerForm['contact'].value)) {
        registerForm.contactWarning.show('올바른 전화번호를 입력해주세요.');
        registerForm['contact'].focus();
        registerForm['contact'].select();
        return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/contactCode?contact=${registerForm['contact'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 400) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'success' :
                        registerForm['contact'].setAttribute('disabled', 'disabled');
                        registerForm['contactSend'].setAttribute('disabled', 'disabled');
                        registerForm['contactCode'].removeAttribute('disabled');
                        registerForm['contactVerify'].removeAttribute('disabled');
                        registerForm['contactSalt'].value = responseObject.salt;
                        registerForm['contactCode'].focus();
                        registerForm.contactWarning.show('입력하신 연락처로 인증번호로 전송했습니다. <br> 아래 인증번호 확인란에 5분 이내로 입력해 주세요.');
                        break;
                    case 'failure_duplicate':
                        registerForm.contactWarning.show('사용할 수 없는 번호입니다.');
                        registerForm['contact'].focus();
                        registerForm['contact'].select();
                        break;
                    default :
                        registerForm.contactWarning.show('인증번호 전송에 실패했습니다. 잠시 후 다시 시도해 주세요.');
                        registerForm['contact'].focus();
                }
            } else {
                registerForm.contactWarning.show('서버와 통신하지 못했습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
}

// 회원가입 휴대폰 인증번호 확인
registerForm['contactVerify'].onclick = () => {
    registerForm.contactWarning.hide();

    if (registerForm['contactCode'].value === '') {
        registerForm.contactCodeWarning.show('인증번호를 입력해 주세요.');
        registerForm['contactCode'].focus();
        return;
    }

    if (!new RegExp(/^[0-9]{6}$/).test(registerForm['contactCode'].value)) {
        registerForm.contactCodeWarning.show('올바른 인증번호를 입력해 주세요.');
        registerForm['contactCode'].focus();
        registerForm['contactCode'].select();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('contact', registerForm['contact'].value);
    formData.append('code', registerForm['contactCode'].value);
    formData.append('salt', registerForm['contactSalt'].value);

    xhr.open('PATCH', '/contactCode');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'failure_expired':
                        registerForm.contactCodeWarning.show('해당 인증번호는 유효시간이 만료되었습니다. <br> 처음부터 다시 진행해 주세요');
                        return;
                    case 'success':
                        registerForm.contactCodeWarning.show('인증번호 인증이 완료되었습니다. <br> 다음 버튼을 눌러 회원가입을 진행해 주세요.');
                        registerForm['next'].removeAttribute('disabled');
                        return;
                    case 'failure':
                        registerForm.contactCodeWarning.show('인증번호를 잘못 입력하셨습니다. <br> 다시한번 확인해 주세요.');
                        return;
                    default:
                        registerForm.contactCodeWarning.show('인증번호 확인에 실패했습니다. 잠시 후 다시 시도해 주세요.');
                        registerForm['contactCode'].focus();
                }
            } else {
                registerForm.contactCodeWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}

// 회원가입 다음버튼 누르기
registerForm.onsubmit = e => {
    e.preventDefault();
    registerForm.contactWarning.hide();
    registerForm.contactCodeWarning.hide();
    registerForm.nameWarning.hide();
    registerForm.emailWarning.hide();
    registerForm.passwordWarning.hide();
    registerForm.passwordCheckWarning.hide();

    if (registerForm.classList.contains('step-1')) {

        if (registerForm['contact'].value === '') {
            registerForm.contactWarning.show('휴대전화 번호를 입력해 주세요.');
            registerForm['contact'].focus();
            return;
        }
        if (!new RegExp(/^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/).test(registerForm['contact'].value)) {
            registerForm.contactWarning.show('올바른 전화번호를 입력해주세요.');
            return;
        }

        if (registerForm['contactCode'].value === '') {
            registerForm.contactCodeWarning.show('인증번호 전송 및 6자리 숫자를 입력해 주세요.');
            registerForm['contactCode'].focus();
            return;
        }

        if (!new RegExp(/^[0-9]{6}$/).test(registerForm['contactCode'].value)) {
            registerForm.contactCodeWarning.show('올바른 인증번호를 입력해 주세요.');
            registerForm['contactCode'].focus();
            registerForm['contactCode'].select();
            return;
        }

        if (registerForm['contactSalt'].value === '') {
            registerForm.contactCodeWarning.show('인증번호 확인을 진행해 주세요.');
            registerForm['contactCode'].focus();
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('contact', registerForm['contact'].value);
        xhr.open('POST', 'checkContactCode');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 400) {
                    const responseText = xhr.responseText;
                    if (responseText === 'false') {
                        registerForm.contactCodeWarning.show('인증번호 확인을 완료해 주세요.');
                    }
                    if (responseText === 'true') {
                        registerForm.classList.remove('step-1');
                        registerForm.classList.add('step-2');
                    }
                } else {
                    registerForm.contactCodeWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');

                }
            }
        };
        xhr.send(formData);

    } else if (registerForm.classList.contains('step-2')) {
        registerForm.agreeWarning.hide();
        if (!registerForm['agreeService'].checked) {
            registerForm.agreeWarning.show('서비스 이용약관 동의해 주세요.');
            registerForm['agreeService'].select();
            return;
        }
        if (!registerForm['agreePrivacy'].checked) {
            registerForm.agreeWarning.show('개인정보 수집 및 이용 동의해 주세요.');
            return;
        }
        registerForm.classList.remove('step-2');
        registerForm.classList.add('step-3');
    } else if (registerForm.classList.contains('step-3')) {
        registerForm.nameWarning.hide();
        registerForm.birthWarning.hide();
        registerForm.emailWarning.hide();
        registerForm.passwordWarning.hide();
        registerForm.passwordCheckWarning.hide();

        if (registerForm['name'].value === '') {
            registerForm.nameWarning.show('이름을 입력해 주세요.');
            registerForm['name'].focus();
            return;
        }
        if (!new RegExp(/^[가-힣]{2,4}|[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/).test(registerForm['name'].value)) {
            registerForm.nameWarning.show('이름을 정확하게 입력해 주세요.');
            registerForm['name'].focus();
            registerForm['name'].select();
            return;
        }

        if (registerForm['birth'].value === '') {
            registerForm.birthWarning.show('생일을 입력해 주세요.');
            registerForm['birth'].focus();
            return;
        }

        if (registerForm['birth'].value > formatDate(new Date())) {
            registerForm.birthWarning.show('정확한 생년월일을 입력해주세요.');
            registerForm['birth'].focus();
            registerForm['birth'].select();
            return;
        }

        if (registerForm['email'].value === '') {
            registerForm.emailWarning.show('이메일을 입력해 주세요.')
            registerForm['email'].focus();
            return;
        }

        if (!new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/).test(registerForm['email'].value)) {
            registerForm.emailWarning.show('잘못된 이메일 형식입니다. 다시 한번 확인해 주세요.');
            registerForm['email'].focus();
            registerForm['email'].select();
            return;
        }
        if (registerForm['password'].value === '') {
            registerForm.passwordWarning.show('비밀번호를 입력해 주세요.');
            registerForm['password'].focus();
            return;
        }

        if (!new RegExp(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/).test(registerForm['password'].value)) {

            registerForm.passwordWarning.show('형식에 맞게 비밀번호를 입력해 주세요. <br> 대문자+소문자+숫자+특수문자 1개이상포함 8~20자리');
            registerForm['password'].focus();
            registerForm['password'].select();
            return;
        }

        if (registerForm['passwordCheck'].value === '') {
            registerForm.passwordCheckWarning.show('비밀번호를 다시 한번 입력해 주세요.');
            registerForm['passwordCheck'].focus();
            return;
        }

        if (registerForm['passwordCheck'].value !== registerForm['password'].value) {
            registerForm.passwordCheckWarning.show('비밀번호가 동일하지 않습니다. 다시 한번 확인해 주세요.');
            registerForm['passwordCheck'].focus();
            registerForm['passwordCheck'].select();
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('name', registerForm['name'].value);
        formData.append('birthStr', registerForm['birth'].value);
        formData.append('email', registerForm['email'].value);
        formData.append('password', registerForm['password'].value);
        formData.append('contact', registerForm['contact'].value);
        xhr.open('POST', './register');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject.result) {
                        case 'failure':
                            registerForm.passwordCheckWarning.show('회원가입에 실패했습니다. 잠시 후 다시 시도해 주세요.');
                            break;
                        case 'failure_email_duplicate':
                            registerForm.emailWarning.show('사용할 수 없는 이메일입니다.');
                            break;
                        default:
                        case 'success':
                            registerForm.classList.remove('step-3');
                            registerForm.classList.add('step-4');
                            user.innerText = registerForm['name'].value;
                            break;
                    }
                } else {
                    registerForm.passwordCheckWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    }
}