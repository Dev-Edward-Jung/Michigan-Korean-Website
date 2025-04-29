document.addEventListener("DOMContentLoaded", async () => {
    const pathParts = window.location.pathname.split('/');
    const announcementId = pathParts[pathParts.length - 1]; // ex: /page/announcement/detail/3
    const params = new URLSearchParams(window.location.search);
    const restaurantId = params.get("restaurantId");

    const loginUserId = document.querySelector('meta[name="user-id"]').content;
    const loginUserName = document.querySelector('meta[name="user-name"]').content;

    if (!announcementId || !restaurantId) {
        alert("Wrong request");
        window.location.href = `/page/announcement/list`;
        return;
    }

    try {
        const response = await fetch(`/api/announcement/detail/${announcementId}?restaurantId=${restaurantId}`);

        if (response.ok) {
            const data = await response.json();
            console.log(data);

            // 기본 글 정보 출력
            document.getElementById("title").textContent = data.title;
            document.getElementById("content").innerHTML = data.content;
            document.getElementById("writer").textContent = data.writerName;

            // ✅ 수정 버튼 조건 확인
            const writerId = data.writerId;
            const writerName = data.writerName;

            const isSameUser = loginUserId === String(writerId) && loginUserName === writerName;

            if (isSameUser) {
                const updateButtonWrapper = document.getElementById("btn-wrapper");
                if (updateButtonWrapper) {
                    // 버튼 HTML 동적으로 생성
                    updateButtonWrapper.innerHTML = `
            <button class="btn btn-primary" id="update-btn">Update</button>
            <button class="btn btn-danger" id="delete-btn">Delete</button>
        `;
                    document.getElementById("update-btn").addEventListener("click", () => {
                        // 수정 페이지로 이동
                        window.location.href = `/page/announcement/update/${announcementId}?restaurantId=${restaurantId}`;
                    });

                    document.getElementById("delete-btn").addEventListener("click", async () => {
                        const confirmed = confirm("정말 삭제하시겠습니까?");
                        if (!confirmed) return;

                        try {
                            const res = await fetch(`/api/announcement/delete/${announcementId}?restaurantId=${restaurantId}`, {
                                method: "DELETE"
                            });

                            if (res.ok) {
                                alert("삭제되었습니다.");
                                window.location.href = `/page/announcement/list?restaurantId=${restaurantId}`;
                            } else {
                                alert("삭제 실패");
                            }
                        } catch (err) {
                            console.error(err);
                            alert("서버 오류로 삭제에 실패했습니다.");
                        }
                    });
                }
            }

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