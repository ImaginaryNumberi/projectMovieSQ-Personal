const faqList = document.getElementById('faqList');
const faqs = faqList.querySelectorAll('[rel="faq"]');

// faq 제목 click하면 내용 보였다가 사라졌다가 하는 동작ㄴ
faqs.forEach(faq => {
    faq.addEventListener('click', e => {
        const content = faq.querySelector('.content');
        if (content.classList.contains('visible')) {
            content.classList.remove('visible');
        } else {
            content.classList.add('visible');
        }
    })
});