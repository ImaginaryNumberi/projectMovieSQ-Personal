// 파일 이미지만 선택하도록
function fileCheck(el) {
    // 파일 확장자가 이미지 파일인지 확인
    if (!/\.(jpeg|jpg|png|gif|bmp)$/i.test(el.value)) {
        popUp.show('업로드 실패', '이미지 파일만 업로드 가능합니다.');
        popUpBtns.show('close');
        el.value = ''; // 파일 선택 초기화
    } else {
        popUp.show('업로드 성공', '이미지 파일이 업로드되었습니다.');
        popUpBtns.show('close');
    }
}

// 이미지 보이기
const image = document.getElementById('image');
const input = document.getElementById('input');


input.onchange = () => {
    fileCheck(input);
    if (input.files[0])
        image.src = URL.createObjectURL(input.files[0]);
};