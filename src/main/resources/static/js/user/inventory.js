// ✅ CSRF 토큰 가져오기
function getCsrfInfo() {
    return {
        token: document.querySelector('meta[name="_csrf"]').getAttribute("content"),
        header: document.querySelector('meta[name="_csrf_header"]').getAttribute("content"),
    };
}

// ✅ 인벤토리 전용 컨테이너 가져오기 (없으면 생성)
function getInventoryContainer() {
    let container = document.querySelector(".inventory-list");
    if (!container) {
        // 기존 card-body 내부에 별도의 인벤토리 영역 생성 (모달 등은 card-body 내 다른 영역에 유지)
        const cardBody = document.querySelector(".card-body");
        container = document.createElement("div");
        container.className = "inventory-list";
        // 인벤토리 영역을 card-body 맨 위에 추가
        cardBody.prepend(container);
    }
    return container;
}

// ✅ 서버로부터 인벤토리 리스트 불러오기
async function fetchInventoryList(restaurantId, csrf) {
    try {
        const res = await fetch(`/api/inventory/list?restaurantId=${restaurantId}`, {
            method: "GET",
            credentials: "include",
            headers: {
                [csrf.header]: csrf.token,
            },
        });
        if (!res.ok) throw new Error("Failed to load inventory");
        return await res.json();
    } catch (err) {
        console.error("Fetch Inventory Error:", err);
        return [];
    }
}

// ✅ 인벤토리 하나 UI로 추가 + 이벤트 바인딩
function addInventoryToUI(inv) {
    const container = getInventoryContainer();
    const html = `
    <div class="mt-2 list-view" data-id="${inv.id}">
      <input class="form-control form-control-lg name-input" disabled value="${inv.name}" />
      <input class="form-control form-control-lg numberInput" disabled type="number" value="${inv.quantity}" />
      <select class="form-select form-select-lg unitSelect" disabled>
        <option>${inv.unit}</option>
      </select>
      <button class="btn btn-primary editBtn"
          data-id="${inv.id}"
          data-name="${inv.name}"
          data-quantity="${inv.quantity}"
          data-unit="${inv.unit}"
          data-category="${inv.category || ''}"
      >EDIT</button>
    </div>
  `;
    container.insertAdjacentHTML("beforeend", html);

    // 이벤트 바인딩: 마지막에 추가된 edit 버튼 선택
    const editBtns = container.querySelectorAll(".editBtn");
    const lastBtn = editBtns[editBtns.length - 1];
    lastBtn.addEventListener("click", () => {
        const modalEl = document.getElementById("modalCenter");
        const modalInstance = new bootstrap.Modal(modalEl);
        modalInstance.show();

        // 모달의 DOM 요소(modalEl)를 사용해 내부 요소 접근
        modalEl.querySelector("input.nameInput").value = lastBtn.dataset.name;
        modalEl.querySelector("input.numberInput").value = lastBtn.dataset.quantity;
        modalEl.querySelector("select.unitSelect").value = lastBtn.dataset.unit;
        modalEl.querySelector("select.categorySelect").value = lastBtn.dataset.category;
        currentInventoryId = lastBtn.dataset.id;
    });
}

// ✅ UI 렌더링: 인벤토리 목록을 카테고리별로 출력
function renderInventory(inventoryList) {
    const container = getInventoryContainer();
    // 인벤토리 전용 컨테이너만 초기화 (모달 등은 card-body 내에 그대로 유지)
    container.innerHTML = "";
    const groupedInventory = inventoryList.reduce((acc, inv) => {
        const category = inv.category || "Uncategorized" || "null";
        if (!acc[category]) acc[category] = [];
        acc[category].push(inv);
        return acc;
    }, {});


    for (const [category, items] of Object.entries(groupedInventory)) {
        console.log(category)
        container.insertAdjacentHTML("beforeend", `
      <div class="divider">
        <div class="divider-text">${category}</div>
      </div>
    `);
        items.forEach(addInventoryToUI);
    }
}

// ✅ 업데이트 API 호출
async function updateInventory(restaurantId, csrf) {
    const data = {
        id: currentInventoryId,
        restaurantId: restaurantId,
        name: document.querySelector("#modalCenter input.nameInput").value.trim(),
        quantity: parseInt(document.querySelector("#modalCenter input.numberInput").value),
        unit: document.querySelector("#modalCenter select.unitSelect").value,
        category: document.querySelector("#modalCenter select.categorySelect").value,
    };
    categoryListI = document.querySelector("#modalCenter select.categorySelect").value
    console.log(categoryListI)

    const res = await fetch("/api/inventory/update", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            [csrf.header]: csrf.token,
        },
        body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Update failed");
    return data;
}

// ✅ 삭제 API 호출
async function deleteInventory(restaurantId, csrf) {
    const res = await fetch(`/api/inventory/delete`, {
        method: "DELETE",
        headers: {
            "Content-Type" : "application/json",
            [csrf.header]: csrf.token,
        },
        body: JSON.stringify({
            id : currentInventoryId,
            restaurantId
        })
    });
    if (!res.ok) throw new Error("Delete failed");
}

// ✅ 추가 API 호출
async function addInventory(csrf, restaurantId) {
    const name = document.querySelector("#modalCenterAdd input[name='name']").value.trim();
    const quantity = parseInt(document.querySelector("#modalCenterAdd input[name='quantity']").value);
    const unit = document.querySelector("#modalCenterAdd select.unitSelect").value;
    const category = document.querySelector("#modalCenterAdd select.categorySelect").value;

    if (!name || isNaN(quantity) || !unit || !category || unit === "unit") {
        alert("Please fill all fields correctly.");
        return;
    }

    const data = { name, quantity, unit, category, restaurantId };

    const res = await fetch("/api/inventory/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [csrf.header]: csrf.token,
        },
        body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Add failed");
}

// ✅ 전체 로직 초기화
async function initInventoryPage() {
    const csrf = getCsrfInfo();
    const restaurantId = new URLSearchParams(window.location.search).get("restaurantId");
    if (!restaurantId) {
        alert("Restaurant ID is missing");
        return;
    }

    const inventoryList = await fetchInventoryList(restaurantId, csrf);
    renderInventory(inventoryList);

    // 모달 내 업데이트 버튼 이벤트 (Edit Modal)
    document.querySelector("#modalCenter .updateBtn").addEventListener("click", async () => {
        try {
            await updateInventory(restaurantId, csrf);
            alert("Updated!");
            window.location.reload();
        } catch (err) {
            alert("Update failed");
            console.error(err);
        }
    });

    // 모달 내 삭제 버튼 이벤트 (Edit Modal)
    document.querySelector("#modalCenter .removeBtn").addEventListener("click", async () => {
        try {
            await deleteInventory(restaurantId, csrf);
            alert("Deleted!");
            window.location.reload();
        } catch (err) {
            alert("Delete failed");
            console.error(err);
        }
    });

    // 추가 모달 내 추가 버튼 이벤트 (Add Modal)
    document.querySelector("#modalCenterAdd .addBtn").addEventListener("click", async () => {
        try {
            await addInventory(csrf, restaurantId);
            alert("Added!");
            window.location.reload();
        } catch (err) {
            alert("Add failed");
            console.error(err);
        }
    });
}

let currentInventoryId = null;
document.addEventListener("DOMContentLoaded", initInventoryPage);