document.addEventListener("DOMContentLoaded", async () => {
    // CSRF 정보 읽기
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    loadEmployeeList(csrfToken, csrfHeader);

    // Save 버튼 이벤트 등록
    document.querySelector(".saveBtn").addEventListener("click", async () => {
        // 시작일, 종료일 값 가져오기 (yyyy-MM-dd 형식)
        const shiftStartDate = document.getElementById("startDateInput").value;
        const shiftEndDate = document.getElementById("endDateInput").value;

        if (!shiftStartDate || !shiftEndDate) {
            alert("시작일과 종료일을 모두 선택해주세요.");
            return;
        }

        // 직원 스케줄 데이터를 담을 배열
        const schedulePayload = [];

        // 테이블의 행들을 처리하는 함수
        const processRows = (rows) => {
            // 요일 배열 (인덱스 0~6에 대응)
            const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
            rows.forEach(row => {
                const employeeId = row.dataset.employeeId;
                const memberRole = row.dataset.memberRole;
                if (!employeeId) return;

                // 해당 행 내의 모든 select 요소(월~일 순서대로)를 가져옴
                const selects = row.querySelectorAll("select.form-select");
                const schedules = [];
                selects.forEach((select, index) => {
                    schedules.push({
                        day: days[index],
                        shift: select.value
                    });
                });
                // 각 직원 객체에 shiftStartDate, shiftEndDate 추가
                schedulePayload.push({
                    id: employeeId,
                    memberRole: memberRole,
                    schedules: schedules,
                    shiftStartDate: shiftStartDate,
                    shiftEndDate: shiftEndDate
                });
            });
        };

        // 각 테이블의 행 데이터를 처리
        const kitchenRows = document.querySelectorAll("table.kitchen-schedule tbody tr");
        processRows(kitchenRows);
        const serverRows = document.querySelectorAll("table.server-schedule tbody tr");
        processRows(serverRows);

        // URL에서 restaurantId 추출
        const urlParams = new URLSearchParams(window.location.search);
        const restaurantId = urlParams.get("restaurantId");
        if (!restaurantId) {
            alert("restaurantId가 URL에 없습니다.");
            return;
        }

        console.log("전송할 데이터:", schedulePayload);

        try {
            // 컨트롤러가 List<EmployeeDTO>를 받으므로 배열을 그대로 전송합니다.
            const res = await fetch(`/api/employee/schedule/save?restaurantId=${restaurantId}`, {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(schedulePayload)
            });

            if (res.ok) {
                alert("✅ 스케줄이 성공적으로 저장되었습니다!");
            } else {
                const err = await res.text();
                alert("❌ 저장 실패: " + err);
            }
        } catch (e) {
            alert("❌ 오류 발생: " + e.message);
        }
    });
});

// 직원+스케줄 로드 함수 (두 개의 리스트: kitchenList, serverList)
async function loadEmployeeList(csrfToken, csrfHeader) {
    try {
        // URL에서 restaurantId 추출
        const urlParams = new URLSearchParams(window.location.search);
        const restaurantId = urlParams.get('restaurantId');
        if (!restaurantId) {
            alert('restaurantId가 URL에 없습니다');
            return;
        }

        // API 호출 : Controller에서 { kitchenList, serverList } 객체를 반환한다고 가정
        const res = await fetch(`/api/employee/schedule/list?restaurantId=${restaurantId}`, {
            method: "GET",
            credentials: "include",
            headers: {
                [csrfHeader]: csrfToken
            }
        });
        if (!res.ok) throw new Error("데이터 가져오기 실패");

        // 응답 데이터 (예: { kitchenList: [employeeDTO,...], serverList: [employeeDTO,...] })
        const data = await res.json();
        console.log("응답 데이터:", data);
        const kitchenList = data.kitchenList || [];
        const serverList = data.serverList || [];

        // 두 테이블의 tbody를 각각 선택하고 기존 내용을 초기화
        const kitchenTbody = document.querySelector("table.kitchen-schedule tbody");
        const serverTbody = document.querySelector("table.server-schedule tbody");
        kitchenTbody.innerHTML = "";
        serverTbody.innerHTML = "";

        // kitchenList 렌더링
        kitchenList.forEach(employee => {
            const tr = document.createElement("tr");
            // 직원 아이디와 memberRole 저장 (나중에 save 시 사용)
            tr.dataset.employeeId = employee.id;
            tr.dataset.memberRole = employee.memberRole;

            // 첫 번째 셀: 직원 이름 표시
            const tdName = document.createElement("td");
            tdName.innerHTML = `<strong>${employee.name}</strong>`;
            tr.appendChild(tdName);

            // 스케줄이 있으면 해당 스케줄 정보대로, 없으면 7개 셀 생성(기본값 "FULL_TIME")
            if (employee.schedules && employee.schedules.length > 0) {
                employee.schedules.forEach(schedule => {
                    const tdSelect = document.createElement("td");
                    const selectedValue = schedule.shift || "FULL_TIME";
                    tdSelect.innerHTML = `
                        <select class="form-select form-select-sm form-no-border">
                            <option class="badge bg-label-primary me-1" value="FULL_TIME" ${selectedValue === "FULL_TIME" ? "selected" : ""}>Full Time</option>
                            <option class="badge bg-label-info me-1" value="DINNER" ${selectedValue === "DINNER" ? "selected" : ""}>Dinner</option>
                            <option class="badge bg-label-success me-1" value="LUNCH" ${selectedValue === "LUNCH" ? "selected" : ""}>Lunch</option>
                            <option class="badge bg-label-warning me-1" value="OFF" ${selectedValue === "OFF" ? "selected" : ""}>Off</option>
                        </select>
                    `;
                    tr.appendChild(tdSelect);
                });
            } else {
                for (let i = 0; i < 7; i++) {
                    const tdSelect = document.createElement("td");
                    const selectedValue = "FULL_TIME";
                    tdSelect.innerHTML = `
                        <select class="form-select form-select-sm form-no-border">
                            <option class="badge bg-label-primary me-1" value="FULL_TIME" ${selectedValue === "FULL_TIME" ? "selected" : ""}>Full Time</option>
                            <option class="badge bg-label-info me-1" value="DINNER" ${selectedValue === "DINNER" ? "selected" : ""}>Dinner</option>
                            <option class="badge bg-label-success me-1" value="LUNCH" ${selectedValue === "LUNCH" ? "selected" : ""}>Lunch</option>
                            <option class="badge bg-label-warning me-1" value="OFF" ${selectedValue === "OFF" ? "selected" : ""}>Off</option>
                        </select>
                    `;
                    tr.appendChild(tdSelect);
                }
            }

            kitchenTbody.appendChild(tr);
        });

        // serverList 렌더링
        serverList.forEach(employee => {
            const tr = document.createElement("tr");
            tr.dataset.employeeId = employee.id;
            tr.dataset.memberRole = employee.memberRole;

            // 첫 번째 셀: 직원 이름 표시
            const tdName = document.createElement("td");
            tdName.innerHTML = `<strong>${employee.name}</strong>`;
            tr.appendChild(tdName);

            if (employee.schedules && employee.schedules.length > 0) {
                employee.schedules.forEach(schedule => {
                    const tdSelect = document.createElement("td");
                    const selectedValue = schedule.shift || "FULL_TIME";
                    tdSelect.innerHTML = `
                        <select class="form-select form-select-sm form-no-border">
                            <option class="badge bg-label-primary me-1" value="FULL_TIME" ${selectedValue === "FULL_TIME" ? "selected" : ""}>Full Time</option>
                            <option class="badge bg-label-info me-1" value="DINNER" ${selectedValue === "DINNER" ? "selected" : ""}>Dinner</option>
                            <option class="badge bg-label-success me-1" value="LUNCH" ${selectedValue === "LUNCH" ? "selected" : ""}>Lunch</option>
                            <option class="badge bg-label-warning me-1" value="OFF" ${selectedValue === "OFF" ? "selected" : ""}>Off</option>
                        </select>
                    `;
                    tr.appendChild(tdSelect);
                });
            } else {
                for (let i = 0; i < 7; i++) {
                    const tdSelect = document.createElement("td");
                    const selectedValue = "FULL_TIME";
                    tdSelect.innerHTML = `
                        <select class="form-select form-select-sm form-no-border">
                            <option class="badge bg-label-primary me-1" value="FULL_TIME" ${selectedValue === "FULL_TIME" ? "selected" : ""}>Full Time</option>
                            <option class="badge bg-label-info me-1" value="DINNER" ${selectedValue === "DINNER" ? "selected" : ""}>Dinner</option>
                            <option class="badge bg-label-success me-1" value="LUNCH" ${selectedValue === "LUNCH" ? "selected" : ""}>Lunch</option>
                            <option class="badge bg-label-warning me-1" value="OFF" ${selectedValue === "OFF" ? "selected" : ""}>Off</option>
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

// helper: select cell 생성 함수 (필요 시 사용)
function createSelectCell(employeeId, date, value) {
    const scheduleOptions = ["Full Time", "Dinner", "Lunch", "Off"];
    const colorMap = {
        "Full Time": "bg-label-primary",
        "Dinner": "bg-label-info",
        "Lunch": "bg-label-success",
        "Off": "bg-label-warning"
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