


// 광고 여부 수정하기 누르면 바뀌는 값만 수정 및 날짜 수정 (보이기->내리기, 내리기 -> 보이기)
const commercialList = document.getElementById('commercialList');


// 기존의 radio:check.value
const formData = new FormData(commercialList);
const nowData = {};
formData.forEach(function (value, key) {
    nowData[key] = value;
});


// 광고 사앹  기존 값과 바뀐 값 비교함수 -> 바뀐값만 반환
function findDifferentValue(obj1, obj2) {
    const keys = Object.keys(obj1);  // {1,2,3,4,5,8,9}
    const differentValue = {}; // 다른 값을 넣을 배열
    for (let i = 0; i < keys.length; i++) {
        const key = keys[i];
        if (obj1[key] !== obj2[key]) {
            differentValue[key] = obj1[key];
        }
    }
    return differentValue;
}


commercialList.onsubmit = e => {
    e.preventDefault();
    // 바뀐 값 가져오기
    const formData = new FormData(commercialList);
    const changeData = {};
    formData.forEach(function (value, key) {
            changeData[key] = value;
        }
    );
    const xhr = new XMLHttpRequest();
    formData.append("result", JSON.stringify(findDifferentValue(nowData, changeData)));
    xhr.open('PATCH', '/admin/commercial');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseText = xhr.responseText;
                if (responseText === 'true') {
                    popUp.show('성공', '메인화면 광고를 업데이트 했습니다.');
                    popUpBtns.show('okay');
                    return;
                }
                if (responseText === 'false') {
                    popUp.show('실패', '메인화면 광고 업데이트 실패했습니다.');
                    popUpBtns.show('okay');
                    return;
                }
                if (responseText === 'none') {
                    popUp.show('수정사항 없음', '이전의 데이터와 동일합니다.');
                    popUpBtns.show('okay');
                }
            } else {
                popUp.show('서버 연결 실패', '잠시 후 다시 시도해 주세요.');
                popUpBtns.show('okay');

            }
        }
    };
    xhr.send(formData);
}

// 광고 삭제 (목록에서만, DB delete_flag 변경)
const deleteButtons = commercialList.querySelectorAll('[rel="delete"]');

deleteButtons.forEach(deleteButton => {
        deleteButton.addEventListener('click', e => {
            e.preventDefault();

            popUp.show('광고삭제', '정말로 해당 광고를 삭제하시겠습니까?');
            popUpBtns.show('close', 'deleteOk');

            deleteOkBtn.onclick = e => {
                popUp.hide();
                e.preventDefault();

                const index = deleteButton.dataset.index;
                const xhr = new XMLHttpRequest();
                xhr.open('PATCH', `/admin/commercialList?index=${index}`);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status >= 200 && xhr.status < 300) {
                            const responseText = xhr.responseText;
                            if (responseText === 'true') {
                                popUp.show('삭제 성공', '광고를 목록에서 삭제했습니다.');
                                popUpBtns.show('okay');
                                return;
                            }
                            if (responseText === 'false') {
                                popUp.show('삭제 실패', '잠시후 다시 시도해 주세요.');
                                popUpBtns.show('okay');
                            }
                        } else {
                            popUp.show('서버 연결 실패', '잠시후 다시 시도해 주세요.');
                            popUpBtns.show('okay');
                        }
                    }
                };
                xhr.send();
            }
        });
    }
)


// const commercialForm = document.getElementById('commercial');
// commercialForm.onsubmit = e => {
//     e.preventDefault();
//
//     const xhr = new XMLHttpRequest();
//     const formData = new FormData();
//     formData.append('commercialName',commercialForm['commercialName'].value);
//     formData.append('startDate',commercialForm['startDate'].value);
//     formData.append('finishDate',commercialForm['finishDate'].value);
//     if(commercialForm['file'].files.length !== 0){
//         for(const file of commercialForm['file'].files){
//             formData.append('file',file);
//         }
//     }
//     xhr.open('PATCH','');    xhr.onreadystatechange = () => {
//         if(xhr.readyState === XMLHttpRequest.DONE){
//             if(xhr.status >=200 && xhr.status <300){
//
//             }else{
//
//             }
//         }
//     };
//     xhr.send(formData);
//
// }