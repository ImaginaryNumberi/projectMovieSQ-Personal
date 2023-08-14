const movieLog = document.getElementById('movieLog-container');
// 관람 내역의 '영수증 출력하기' 버튼
const receiptButtons = movieLog.querySelectorAll('[rel="receiptButton"]');

const receiptContainer = document.getElementById('receipt-container');
const receiptClose = document.getElementById('receiptClose');
const receiptCloseButton = document.getElementById('closeButton');
// 출력 버튼
const receiptPrintButton = document.getElementById('printButton');

receiptButtons.forEach(receipt => {
    receipt.addEventListener('click', e => {
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        xhr.open('GET', `/mypage/movieLog/payment?index=${receipt.dataset.index}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject.result) {
                        case'success':
                            document.querySelector('[name="name"]').innerText = responseObject['payment'];
                            document.querySelector('[name="date"]').innerText = responseObject['paidAt'];
                            document.querySelector('[name="number"]').innerText = responseObject['cardNumber'];
                            const amount = parseInt(responseObject['paymentAmount']);
                            const tax = Math.trunc(amount / 11);
                            const supply = amount - tax;
                            document.querySelector('[name="amount"]').innerText = Math.trunc(amount).toLocaleString() + '원';
                            document.querySelector('[name="supply"]').innerText = Math.trunc(supply).toLocaleString() + '원';
                            document.querySelector('[name="tax"]').innerText = Math.trunc(tax).toLocaleString() + '원';
                            cover.show();
                            receiptContainer.show();
                            break;
                        case'failure':
                            popUp.show('', '잠시 후 다시 시도해 주세요.');
                            popUpBtns.show('okay');
                            break;
                        default:
                            popUp.show('실패', '잠시 후 다시 시도해 주세요.');
                            popUpBtns.show('okay');
                            break;
                    }
                } else {
                    alert('서버와 통신하지 못했습니다. 잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    })
});

function closeReceipt() {
    cover.hide();
    receiptContainer.hide();
}

receiptClose.onclick = function () {
    closeReceipt();
}

receiptCloseButton.onclick = function () {
    closeReceipt();
}

// 출력 버튼
receiptPrintButton.onclick = function () {
    popUp.show('출력', '출력 연결');
    popUpBtns.show('okay');
}
