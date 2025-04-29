document.addEventListener("DOMContentLoaded", async () => {
    const pathParts = window.location.pathname.split('/');
    const announcementId = pathParts[pathParts.length - 1]; // 마지막 /{id} 추출
    const params = new URLSearchParams(window.location.search);
    const restaurantId = params.get("restaurantId");

    if (!announcementId || !restaurantId) {
        alert("Wrong request");
        window.location.href = `/page/announcement/list`;
        return;
    }

    try {
        const response = await fetch(`/api/announcement/detail/${announcementId}?restaurantId=${restaurantId}`);

        if (response.ok) {
            const data = await response.json();
            console.log(data)
            document.getElementById("title").textContent = data.title;
            document.getElementById("content").innerHTML = data.content;  // 내용은 HTML 허용
            document.getElementById("writer").textContent = data.writerName;
        } else {
            alert('Server Error');
            window.location.href = `/page/announcement/list?restaurantId=${restaurantId}`;
        }
    } catch (error) {
        console.error(error);
        alert('Server Error');
        window.location.href = `/page/announcement/list?restaurantId=${restaurantId}`;
    }
});