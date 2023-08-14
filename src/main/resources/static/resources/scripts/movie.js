// 영화 목록 정렬
function changeSortSelect() {
    const sortSelect = document.getElementById("sortSelect");
    const selectValue = sortSelect.options[sortSelect.selectedIndex].value; // "abc", "dates"

    location.href = `movie?sort=${selectValue}`;
}