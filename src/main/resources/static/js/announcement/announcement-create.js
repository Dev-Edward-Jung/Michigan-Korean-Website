const quill = new Quill('#editor', {
    theme: 'snow',
    placeholder: 'Please Work Everyday...',
    modules: {
        toolbar: [
            [{ 'header': [1, 2, false] }],
            ['bold', 'italic', 'underline', 'strike'],
            [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        ]
    }
});

async function sendContent() {
    const title = document.getElementById("basic-default-title").value;
    const content = quill.root.innerHTML;
    const type = document.querySelector('input[name="btnradio"]:checked').value;

    const params = new URLSearchParams(window.location.search);
    const restaurantId = params.get("restaurantId");

    if (!restaurantId) {
        alert('레스토랑 ID가 없습니다. 다시 로그인해주세요.');
        return;
    }

    const response = await fetch(`/api/announcement/save?restaurantId=${restaurantId}`, { // 여기
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [document.querySelector('meta[name="_csrf_header"]').getAttribute('content')]: document.querySelector('meta[name="_csrf"]').getAttribute('content')
        },
        body: JSON.stringify({
            title: title,
            content: content,
            type: type
        })
    });

    if (response.ok) {
        alert('Successfully saved!');
        location.href = `/page/announcement/list?restaurantId=${restaurantId}`;
    } else {
        alert('Save Error');
    }
}


async function updateContent() {
    const content = quill.root.innerHTML;
    const type = document.querySelector('input[name="type"]:checked').value;

    const response = await fetch('/api/announcement/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            content: content,
            type: type
        })
    });

    if (response.ok) {
        alert('저장 완료!');
        location.reload(); // 저장 후 페이지 새로고침
    } else {
        alert('저장 실패 ㅠㅠ');
    }
}