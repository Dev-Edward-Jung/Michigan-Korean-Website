document.addEventListener("DOMContentLoaded", async() => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");


    let currentInventoryId = null;
    const urlParams = new URLSearchParams(window.location.search);
    const restaurantId = urlParams.get("restaurantId");// 현재 수정 중인 ID 저장
    const modalNameInput = document.querySelector(".nameInput");
    const modalQtyInput = document.querySelector(".numberInput");
    const modalUnitSelect = document.querySelector(".unitSelect");
    const modalUpdateBtn = document.querySelector(".updateBtn");
    

    try {
        const res = await fetch(`/api/inventory/list?restaurantId=${restaurantId}`, {
            method: "GET",
            credentials: "include",
            headers: {
                [csrfHeader]: csrfToken,
            },
        });

        if (!res.ok) throw new Error("Failed to load inventory");

        const inventoryList = await res.json();
        console.log("Inventory List:", inventoryList);

        const groupedInventory = inventoryList.reduce((acc, inv) => {
            // inv.category가 없는 경우 "Uncategorized"로 분류
            const category = inv.category || "Uncategorized";
            if (!acc[category]) {
              acc[category] = [];
            }
            acc[category].push(inv);
            return acc;
          }, {});

        const cardBody = document.querySelector(".card-body");

        inventoryList.forEach((inv) => {
            cardBody.insertAdjacentHTML("beforeend", `
<!--        one inventory         -->
                    <div class="mt-2 list-view">
                      <input
                              class="form-control form-control-lg large-input name-input"
                              placeholder="Inventory Name"
                              name="name"
                              type="text"
                              disabled=""
                              value="${inv.name}"
                      />
                      <input
                              class="form-control form-control-lg second-input numberInput"
                              type="number"
                              name="quantity"
                              disabled=""
                              pattern="\d*"
                              value="${inv.quantity}"

                      />
                      <select
                              class="form-select form-select-lg large-select unitSelect"
                              disabled=""
                              
                      >
                        <option>${inv.unit}</option>
                      </select>
                      <button
                              class="btn btn-primary editBtn"
                              data-bs-target="#modalCenter"
                              data-bs-toggle="modal"
                              data-id="${inv.id}" 
                              data-name="${inv.name}" 
                              data-quantity="${inv.quantity}" 
                              data-unit="${inv.unit}"
                              type="button"
                      >
                        EDIT
                      </button>

                    </div>
            <!--       one inventory          -->
      `)

        });


        // 버튼 클릭 시 모달에 데이터 세팅
        document.querySelectorAll(".editBtn").forEach((btn) => {
            btn.addEventListener("click", () => {
                currentInventoryId = btn.dataset.id;
                modalNameInput.value = btn.dataset.name;
                modalQtyInput.value = btn.dataset.quantity;
                modalUnitSelect.value = btn.dataset.unit;
            });
        });

        // 업데이트 버튼 클릭 시 서버에 전송
        modalUpdateBtn.addEventListener("click", async () => {
            const data = {
                id: currentInventoryId,
                name: modalNameInput.value.trim(),
                quantity: parseInt(modalQtyInput.value),
                unit: modalUnitSelect.value,
            };

            const res = await fetch("/api/inventory/update", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken,
                },
                body: JSON.stringify(data),
            });

            if (res.ok) {
                alert("Updated!");
                window.location.reload();
            } else {
                alert("Update failed");
            }
        });
    } catch (err) {
        console.error("Error fetching inventory:", err);
        alert("Failed to load inventory list.");
    }

    // ================================
    // 🔹 1. Edit 기능 처리
    // ================================


    document.querySelectorAll(".updateBtn").forEach((editBtn) => {
        editBtn.addEventListener("click", (e) => {
            const modal = document.getElementById(`modalCenter`);

            const editNameInput = modal.querySelector("input[name='name']");
            const editQuantityInput = modal.querySelector("input[name='quantity']");
            const editUnitSelect = modal.querySelector("select.unitSelect");
            const updateBtn = modal.querySelector(".updateBtn");

            // 🔥 Save Changes 버튼 클릭 시 동작 바인딩
            updateBtn.onclick = async () => {
                const data = {
                    id: currentInventoryId,
                    name: editNameInput.value.trim(),
                    quantity: parseInt(editQuantityInput.value),
                    unit: editUnitSelect.value,
                };

                try {
                    const res = await fetch("/api/inventory/save", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            [csrfHeader]: csrfToken,
                        },
                        body: JSON.stringify(data),
                    });

                    if (res.ok) {
                        alert("Updated!");
                        window.location.reload();
                    } else {
                        alert("Update failed");
                    }
                } catch (err) {
                    console.error("Error updating inventory:", err);
                }
            };
        });
    });

    // ================================
    // 🔹 2. Add 기능 처리
    // ================================

    const addModal = document.getElementById("modalCenterAdd");
    const addNameInput = addModal.querySelector("input[name='name']");
    const addQuantityInput = addModal.querySelector("input[name='quantity']");
    const addUnitSelect = addModal.querySelector("select.unitSelect");
    const addBtn = addModal.querySelector(".addBtn");

    addBtn.addEventListener("click", async () => {
        const name = addNameInput.value.trim();
        const quantity = parseInt(addQuantityInput.value);
        const unit = addUnitSelect.value;

        if (!name || !quantity || !unit || unit === "unit") {
            alert("Please fill all fields correctly.");
            return;
        }

        const data = {
            name,
            quantity,
            unit,
            restaurantId: restaurantId, // 👉 여기에 로그인된 사용자의 레스토랑 ID 사용
        };

        try {
            const res = await fetch("/api/inventory/save", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken,
                },
                body: JSON.stringify(data),
            });

            if (res.ok) {
                alert("Inventory added!");
                window.location.reload();
            } else {
                alert("Failed to add inventory.");
            }
        } catch (err) {
            console.error("Error adding inventory:", err);
        }
    });

});



// document.addEventListener("click", (e) => {
//     if (e.target.classList.contains("removeBtn")) {
//         const containerIndex = e.target.getAttribute("data-container-index");
//         const allRows = document.querySelectorAll(".inventory-row");
//         allRows[containerIndex]?.remove();
//     }
//
//     if (e.target.classList.contains("saveBtn")) {
//         const modalId = e.target.getAttribute("data-modal");
//         const modal = document.getElementById(modalId);
//         const name = modal.querySelector(".edit-name").value;
//         const qty = modal.querySelector(".edit-qty").value;
//         const unit = modal.querySelector(".edit-unit").value;
//         const id = e.target.getAttribute("data-id");
//
//         console.log("✅ Save inventory:", { id, name, qty, unit });
//
//         // 🔥 여기에 fetch(`/api/inventory/update`, { PUT ... }) 요청 넣으면 됨!
//     }
// });