const newsForm = document.getElementById('registerNewsForm');
ClassicEditor.create(newsForm['content'], {
    simpleUpload: {
        uploadUrl: '/article/uploadImage'
    }
});