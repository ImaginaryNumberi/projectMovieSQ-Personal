const noticeList = document.getElementById('noticeList');
const notices = noticeList.querySelectorAll('[rel="notice"]');

notices.forEach(notice => {
    notice.addEventListener('click', e => {
        location.href = `support/detail?index=${notice.dataset.index}`;
    })
});