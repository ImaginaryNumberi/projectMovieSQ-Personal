// 각 각 movie > detail 의 poster 이미지와 스틸컷 이미지
const poster = document.getElementById('poster');
const motion = document.getElementById('motion');
// 이미지를 클릭했을때 보여질 cover 및 viewer
const viewerCover = document.getElementById('viewerCover');
const viewer = document.getElementById('viewer');


viewerCover.show = function () {
    this.classList.add('visible');
};
viewerCover.hide = function () {
    viewer.classList.remove('visible');
    this.classList.remove('visible');
};

function showViewer(isPoster) {
    viewerCover.show();
    viewer.setAttribute('src', `/movie/image?index=${motion.dataset.index}&poster=${isPoster}`);
    viewer.classList.add('visible');
}

poster.onclick = function () {
    showViewer(true); // 매개변수에 true 전달
};

motion.onclick = function () {
    showViewer(false); // 매개변수에 false 전달
};

function hideViewer() {
    viewer.removeAttribute('src');
    viewer.classList.remove('visible');
    viewerCover.hide();
}

viewerCover.onclick = hideViewer;
viewer.onclick = hideViewer;