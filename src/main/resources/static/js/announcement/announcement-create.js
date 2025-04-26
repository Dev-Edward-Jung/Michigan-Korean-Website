const quill = new Quill('#editor', {
    theme: 'snow',
    placeholder: 'Please Work Everyday...',
    modules: {
        toolbar: [
            [{ 'header': [1, 2, false] }],
            ['bold', 'italic', 'underline', 'strike'],
            [{ 'list': 'ordered'}, { 'list': 'bullet' }],
            ['image'],
            ['clean']
        ]
    }
});

async function sendContent() {
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