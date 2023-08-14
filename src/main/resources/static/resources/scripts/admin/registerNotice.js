const noticeForm = document.getElementById('noticeForm');
ClassicEditor.create(noticeForm['content'], {
    simpleUpload: {
        uploadUrl: '/article/uploadImage'
    }
});