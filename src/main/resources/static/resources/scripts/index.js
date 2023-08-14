// document.cookie = "cookieName=cookieValue; SameSite=None; Secure";
const indexNoticeList = document.getElementById('indexNoticeList');
const indexNotices = indexNoticeList.querySelectorAll('[rel="notice"]');

// 영화 예고편 팜업 보기
const wrapper = document.getElementById('wrapper');
const urlPopup = document.querySelector('.urlPopup');
const openButtons = document.querySelectorAll('[rel="open"]');
const closeButton = document.querySelector('[rel="close"]');

openButtons.forEach((button) => {
    button.addEventListener('click', e => {
        e.preventDefault();
        const xhr = new XMLHttpRequest();
        xhr.open('GET', `/url?index=${button.dataset.index}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 400) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject.result) {
                        case 'failure':
                            popUp.show('예고편 가져오기 실패', '잠시후 다시 시도해 주세요.')
                            popUpBtns.show('okay');
                            break;
                        case 'success':
                            document.getElementById('url').src = responseObject.url;
                            cover.show();
                            urlPopup.show();
                            break;
                    }
                } else {
                    popUp.show('서버 연결 실패', '잠시후 다시 시도해 주세요.');
                    popUpBtns.show('okay');
                }

            }
        }
        xhr.send();
    })
});

// 팜업 닫기 누르기
closeButton.addEventListener('click', () => {
    cover.hide();
    urlPopup.hide();
    document.getElementById('url').src = '';
});

// 공지사항 보기
indexNotices.forEach(notice => {
    notice.addEventListener('click', e => {
        location.href += `support/detail?index=${notice.dataset.index}`;
    })
});


// 카카오 간편 로그인 반응
if (document.location.href.includes("disconnected")) {
    popUp.show('간편 로그인', '카카오 간편 로그인 연결을 해제했습니다.');
    popUpBtns.show('main');
    delay();
} else if (document.location.href.includes("connected")) {
    popUp.show('간편 로그인', '카카오 간편 로그인 연결했습니다.');
    popUpBtns.show('main');
    delay();
}
