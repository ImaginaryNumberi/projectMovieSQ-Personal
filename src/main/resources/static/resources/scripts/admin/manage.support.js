const noticeList = document.getElementById('adminNoticeList');
const notices = noticeList.querySelectorAll('[rel="notice"]');
const deleteButtons = noticeList.querySelectorAll('[rel="delete"]');

const noticeViewer = document.getElementById('noticeViewer');
noticeViewer.subject = noticeViewer.querySelector('[rel="subject"]');
noticeViewer.info = noticeViewer.querySelector('[rel="info"]');
noticeViewer.content = noticeViewer.querySelector('[rel="content"]');

// notice 항목 클릭할 경우 noticeViewer 띄움
notices.forEach(notice => {
    notice.addEventListener('click', e => {
        noticeViewer.subject.innerHTML = notice.dataset.title;
        noticeViewer.info.innerHTML = notice.dataset.info;
        noticeViewer.content.innerHTML = notice.dataset.content;

        cover.show();
        noticeViewer.classList.add('visible');
    });
});

// noticeViewer 에서 cover 클릭할 경우
cover.addEventListener('click', () => {
    cover.hide();
    noticeViewer.classList.remove('visible');
});

// 삭제
deleteButtons.forEach(deleteButton => {
    deleteButton.addEventListener('click', e => {
        e.preventDefault();
        cover.show();
        popUpBtns.AllHide();
        popUp.show('삭제하면 복구는 불가합니다', '삭제하겠습니까? ');
        popUpBtns.show('close', 'deleteOk');

        deleteOkBtn.onclick = () => {
            const index = deleteButton.dataset.index;
            const xhr = new XMLHttpRequest();
            xhr.open('DELETE', `./support?index=${index}`);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseText = xhr.responseText;
                        if (responseText === 'true') {
                            location.href = '';
                        } else {
                            alert('알 수 없는 이유로 삭제하지 못하였습니다.\n\n이미 삭제된 항목일 수도 있습니다');
                        }
                    } else {
                        alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                }
            };
            xhr.send();
        }
    });
});