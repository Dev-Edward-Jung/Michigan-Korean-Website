



// getting member login information
document.addEventListener("DOMContentLoaded", async () => {
    let memberId = 0;
    // Get Now User Info
    try {
        const userRes = await fetch("/api/member/me", {
            method: "GET",
            credentials: "include" //
        });
        if (!userRes.ok) throw new Error("Not logged in");

        const user = await userRes.json();
        memberId = user.id;
        console.log("현재 로그인한 사용자:", user);


        // 이 다음부터 user.id 를 이용해 레스토랑/인벤토리 API 호출 가능
    } catch (err) {
        console.error("fail to login:", err);
        alert("fil to login.");
        window.location.href = "/page/member/login";
    }








    // Save Restaurant
    const nameInput = document.querySelector(".inputName");
    const cityInput = document.querySelector(".inputCity");
    const saveBtn = document.querySelector(".saveBtn");

    // ✅ CSRF 정보 가져오기
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // 버튼 처음엔 비활성화
    saveBtn.disabled = true;

    // 실시간 유효성 검사
    [nameInput, cityInput].forEach(input => {
        input.addEventListener("input", () => {
            const name = nameInput.value.trim();
            const city = cityInput.value.trim();
            saveBtn.disabled = !(name && city);
        });
    });


    // Save 버튼 클릭 시 POST 요청
    saveBtn.addEventListener("click", async () => {
        const name = nameInput.value.trim();
        const city = cityInput.value.trim();

        try {
            const res = await fetch("/api/restaurant/save", {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken, // ✅ CSRF 토큰 추가!
                },
                body: JSON.stringify({ name, city, memberId }),
            });

            if (res.ok) {
                alert("Restaurant saved!");
                window.location.reload();
            } else {
                alert("Failed to save restaurant");
            }
        } catch (err) {
            console.error("Error saving restaurant:", err);
            alert("Error occurred");
        }
    });






    // get list of restaurant
    try {
        const res = await fetch("/api/restaurant/list", {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ memberId }) // ✅ JSON body에 memberId 포함
        });

        if (!res.ok) throw new Error("getting list error");

        const restaurants = await res.json();
        const tbody = document.querySelector("tbody");

        restaurants.forEach((restaurant) => {
            const tr = document.createElement("tr");
            tr.classList.add("restaurant-row");
            tr.dataset.id = restaurant.id;

            tr.innerHTML = `
                <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${restaurant.name}</strong></td>
                <td>${restaurant.city}</td>
            `;

            tbody.appendChild(tr);
        });

    } catch (err) {
        console.error("Error fetching restaurants:", err);
        alert("Fail to get your restaurant");
    }

});


