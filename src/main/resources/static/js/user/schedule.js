document.addEventListener("DOMContentLoaded", async () => {
    // ✅ CSRF 정보
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // 이벤트: 날짜 입력 값 변경 시 스케줄 다시 로드 (입력 필드가 변경될 때마다)
    document.getElementById("startDateInput").addEventListener("change", () => loadEmployeeList(csrfToken, csrfHeader));
    document.getElementById("endDateInput").addEventListener("change", () => loadEmployeeList(csrfToken, csrfHeader));

    // 최초 페이지 로드시에도 로드
    loadEmployeeList(csrfToken, csrfHeader);
});

// 직원+스케줄 로드 함수 (두 개의 리스트: kitchenList, serverList)
async function loadEmployeeList(csrfToken, csrfHeader) {
    console.log("로드 함수안에? 들어왔나?")
    try {
        // URL에서 restaurantId 추출
        const urlParams = new URLSearchParams(window.location.search);
        const restaurantId = urlParams.get('restaurantId');
        console.log(restaurantId)
        if (!restaurantId) {
            alert('restaurantId가 URL에 없습니다');
            return;
        }

        // 날짜 범위를 입력 필드에서 가져옴
        // const startDateInput = document.getElementById("startDateInput").value;
        // const endDateInput = document.getElementById("endDateInput").value;
        // if (!startDateInput || !endDateInput) {
        //     // 날짜가 없으면 테이블을 비워두고 종료
        //     clearTableBodies();
        //     return;
        // }

        // 시작날짜부터 종료날짜까지의 날짜 목록 생성 (예: ["2025-04-13", "2025-04-14", ...])
        // const start = new Date(startDateInput);
        // const end = new Date(endDateInput);
        // const dates = [];
        // for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
        //     dates.push(d.toISOString().split("T")[0]);
        // }
        // console.log("hello")
        // API 호출 : Controller에서 kitchenList와 serverList를 담아 반환하도록 구성되어 있음.
        const res = await fetch(`/api/employee/schedule/list?restaurantId=${restaurantId}`, {
            method: "GET",
            credentials: "include",
            headers: {
                [csrfHeader]: csrfToken
            }
        });
        console.log("hellow")
        if (!res.ok) throw new Error("get list error");

        // 응답 데이터 (예: { kitchenList: [employeeDTO,...], serverList: [employeeDTO,...] })
        const data = await res.json();
        console.log("응답 데이터:", data);
        const kitchenList = data.kitchenList || [];
        const serverList = data.serverList || [];

        // 두 테이블의 tbody를 각각 선택하고 기존 내용을 초기화합니다.
        const kitchenTbody = document.querySelector("table.kitchen-schedule tbody");
        const serverTbody = document.querySelector("table.server-schedule tbody");
        kitchenTbody.innerHTML = "";
        serverTbody.innerHTML = "";

        kitchenList.forEach(employee => {
            const tr = document.createElement("tr");

            // 첫 번째 셀: 직원 이름 표시
            const tdName = document.createElement("td");
            tdName.innerHTML = `<strong>${employee.name}</strong>`;
            tr.appendChild(tdName);

            // 스케줄이 있으면 해당 스케줄 정보대로, 없으면 7개 셀 생성(기본값 "FULL_TIME")
            if (employee.schedules && employee.schedules.length > 0) {
                // employee.schedules 배열에 있는 스케줄 정보대로 셀 생성 (예: 스케줄이 7개인 경우)
                employee.schedules.forEach(schedule => {
                    const tdSelect = document.createElement("td");
                    // 해당 스케줄이 있으면 shift 값을 사용, 없으면 기본 "FULL_TIME"
                    const selectedValue = schedule.shift || "FULL_TIME";
                    tdSelect.innerHTML = `
                <select class="form-select form-select-sm form-no-border">
                    <option class="badge bg-label-primary me-1" value="FULL_TIME" ${selectedValue === "FULL_TIME" ? "selected" : ""}>Full Time</option>
                    <option class="badge bg-label-info me-1" value="DINNER" ${selectedValue === "DINNER" ? "selected" : ""}>Dinner</option>
                    <option class="badge bg-label-success me-1" value="LUNCH" ${selectedValue === "LUNCH" ? "selected" : ""}>Lunch</option>
                    <option class="badge bg-label-warning me-1" value="NO_WORK" ${selectedValue === "NO_WORK" ? "selected" : ""}>Not Work</option>
                </select>
            `;
                    tr.appendChild(tdSelect);
                });
            } else {
                // 스케줄 정보가 없으면 7일(월~일)에 대한 선택 셀을 기본값 "FULL_TIME"으로 생성
                for (let i = 0; i < 7; i++) {
                    const tdSelect = document.createElement("td");
                    const selectedValue = "FULL_TIME";
                    tdSelect.innerHTML = `
                <select class="form-select form-select-sm form-no-border">
                    <option class="badge bg-label-primary me-1" value="FULL_TIME" ${selectedValue === "FULL_TIME" ? "selected" : ""}>Full Time</option>
                    <option class="badge bg-label-info me-1" value="DINNER" ${selectedValue === "DINNER" ? "selected" : ""}>Dinner</option>
                    <option class="badge bg-label-success me-1" value="LUNCH" ${selectedValue === "LUNCH" ? "selected" : ""}>Lunch</option>
                    <option class="badge bg-label-warning me-1" value="NO_WORK" ${selectedValue === "NO_WORK" ? "selected" : ""}>Not Work</option>
                </select>
            `;
                    tr.appendChild(tdSelect);
                }
            }

            kitchenTbody.appendChild(tr);
        });

        serverList.forEach(employee => {
            const tr = document.createElement("tr");

            // 첫 번째 셀: 직원 이름 표시
            const tdName = document.createElement("td");
            tdName.innerHTML = `<strong>${employee.name}</strong>`;
            tr.appendChild(tdName);

            // 스케줄이 있으면 해당 스케줄 정보대로, 없으면 7개 셀 생성(기본값 "FULL_TIME")
            if (employee.schedules && employee.schedules.length > 0) {
                // employee.schedules 배열에 있는 스케줄 정보대로 셀 생성 (예: 스케줄이 7개인 경우)
                employee.schedules.forEach(schedule => {
                    const tdSelect = document.createElement("td");
                    // 해당 스케줄이 있으면 shift 값을 사용, 없으면 기본 "FULL_TIME"
                    const selectedValue = schedule.shift || "FULL_TIME";
                    tdSelect.innerHTML = `
                <select class="form-select form-select-sm form-no-border">
                    <option class="badge bg-label-primary me-1" value="FULL_TIME" ${selectedValue === "FULL_TIME" ? "selected" : ""}>Full Time</option>
                    <option class="badge bg-label-info me-1" value="DINNER" ${selectedValue === "DINNER" ? "selected" : ""}>Dinner</option>
                    <option class="badge bg-label-success me-1" value="LUNCH" ${selectedValue === "LUNCH" ? "selected" : ""}>Lunch</option>
                    <option class="badge bg-label-warning me-1" value="NO_WORK" ${selectedValue === "NO_WORK" ? "selected" : ""}>Not Work</option>
                </select>
            `;
                    tr.appendChild(tdSelect);
                });
            } else {
                // 스케줄 정보가 없으면 7일(월~일)에 대한 선택 셀을 기본값 "FULL_TIME"으로 생성
                for (let i = 0; i < 7; i++) {
                    const tdSelect = document.createElement("td");
                    const selectedValue = "FULL_TIME";
                    tdSelect.innerHTML = `
                <select class="form-select form-select-sm form-no-border">
                    <option class="badge bg-label-primary me-1" value="FULL_TIME" ${selectedValue === "FULL_TIME" ? "selected" : ""}>Full Time</option>
                    <option class="badge bg-label-info me-1" value="DINNER" ${selectedValue === "DINNER" ? "selected" : ""}>Dinner</option>
                    <option class="badge bg-label-success me-1" value="LUNCH" ${selectedValue === "LUNCH" ? "selected" : ""}>Lunch</option>
                    <option class="badge bg-label-warning me-1" value="NO_WORK" ${selectedValue === "NO_WORK" ? "selected" : ""}>Not Work</option>
                </select>
            `;
                    tr.appendChild(tdSelect);
                }
            }

            serverTbody.appendChild(tr);
        });
    } catch (err) {
        console.error("Error fetching employee schedules:", err);
        alert("Failed to load employee schedules");
    }
}

// helper: 테이블 tbody 초기화
function clearTableBodies() {
    const kitchenTbody = document.querySelector("table.kitchen-schedule tbody");
    const serverTbody = document.querySelector("table.server-schedule tbody");
    if (kitchenTbody) kitchenTbody.innerHTML = "";
    if (serverTbody) serverTbody.innerHTML = "";
}

// helper: select cell 생성 함수
function createSelectCell(employeeId, date, value) {
    const scheduleOptions = ["Full Time", "Dinner", "Lunch", "Not Work"];
    const colorMap = {
        "Full Time": "bg-label-primary",
        "Dinner": "bg-label-info",
        "Lunch": "bg-label-success",
        "Not Work": "bg-label-warning"
    };

    const td = document.createElement("td");
    const select = document.createElement("select");
    select.className = "form-select form-select-sm form-no-border";
    select.dataset.employeeId = employeeId;
    select.dataset.date = date;

    scheduleOptions.forEach(option => {
        const opt = document.createElement("option");
        opt.value = option;
        opt.textContent = option;
        if (option === value) {
            opt.selected = true;
        }
        opt.className = `badge ${colorMap[option]} me-1`;
        select.appendChild(opt);
    });

    // 업데이트: 선택 변경 시 셀 색상 변경
    select.addEventListener("change", () => {
        updateSelectColor(select, colorMap);
    });
    updateSelectColor(select, colorMap);

    td.appendChild(select);
    return td;
}

function updateSelectColor(select, colorMap) {
    // 모든 색상 클래스 제거
    Object.values(colorMap).forEach(color => {
        select.classList.remove(color);
    });
    // 선택된 값에 해당하는 색상 클래스 추가
    const selectedValue = select.value;
    if (colorMap[selectedValue]) {
        select.classList.add(colorMap[selectedValue]);
    }
}