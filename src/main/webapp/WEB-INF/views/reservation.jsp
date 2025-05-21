<%@ page contentType="text/html;charset=UTF-8" %>
    <html>

    <head>
        <title>EV 예약</title>
        <style>
            .time-button {
                display: inline-block;
                padding: 10px 20px;
                margin: 5px 5px;
                border: 1px solid #ccc;
                background-color: #f4f4f4;
                cursor: pointer;
                border-radius: 5px;
            }

            .time-button.selected {
                background-color: #0FAB77;
                color: white;
            }

            .time-button.disabled {
                background-color: #ddd;
                cursor: not-allowed;
            }

            #timeSlots {
                margin-top: 20px;
                line-height: 2.5;
            }
        </style>
    </head>
    <body>
        <h2>충전소 예약</h2>

        <form id="reservationForm" action="reserveTime.do" method="post">
            <input type="hidden" name="stat_id" value="#"> <%-- 충전소 ID --%>
            <input type="hidden" name="chger_id" value="#"> <%-- 충전기 ID --%>
                <label>예약 날짜: <input type="date" id="reservation_date" name="reservation_date" required></label>
                <div id="timeSlots"></div>
                <div id="selectedTimesContainer"></div>

                <br>
                <input type="submit" value="예약하기">
        </form>

        <script>
            const MAX_SELECT = 4; // 최대 선택 개수
            const timeSlots = [];
            for (let hour = 9; hour <= 22; hour++) {
                timeSlots.push((hour < 10 ? "0" + hour : hour) + ":00");
                if (hour < 22) {
                    timeSlots.push((hour < 10 ? "0" + hour : hour) + ":30");
                }
            }

            const timeSlotsDiv = document.getElementById("timeSlots");
            const selectedTimesContainer = document.getElementById("selectedTimesContainer");

            function renderTimeSlots(disabledTimes = []) {
                timeSlotsDiv.innerHTML = "";
                timeSlots.forEach((time, idx) => {
                    const btn = document.createElement("div");
                    btn.className = "time-button";
                    btn.textContent = time;
                    btn.dataset.time = time;

                    if (disabledTimes.includes(time)) {
                        btn.classList.add("disabled");
                    }

                    btn.onclick = function () {
                        if (btn.classList.contains("disabled")) return;

                        const isSelected = btn.classList.contains("selected");
                        const selectedButtons = document.querySelectorAll(".time-button.selected");
                        const time = btn.dataset.time;

                        if (!isSelected && selectedButtons.length >= MAX_SELECT) {
                            alert(`최대 ${MAX_SELECT}개(약 2시간)까지만 예약할 수 있습니다.`);
                            return;
                        }

                        // 버튼 상태 토글
                        btn.classList.toggle("selected");

                        // 숨겨진 input 관리
                        const inputs = selectedTimesContainer.querySelectorAll("input[name='reservation_time[]']");
                        let existingInput = null;
                        inputs.forEach(input => {
                            if (input.getAttribute("data-time") === time) {
                                existingInput = input;
                            }
                        });

                        if (btn.classList.contains("selected")) {
                            // 추가
                            if (!existingInput) {
                                const input = document.createElement("input");
                                input.type = "hidden";
                                input.name = "reservation_time[]";
                                input.value = time;
                                input.setAttribute("data-time", time);
                                selectedTimesContainer.appendChild(input);
                                console.log("@#추가")
                                console.log(selectedTimesContainer);
                            }
                        } else {
                            // 제거
                            if (existingInput) existingInput.remove();
                            console.log("@#제거")
                            console.log(existingInput);
                        }
                    };

                    timeSlotsDiv.appendChild(btn);

                    // 4개마다 줄바꿈 추가
                    if ((idx + 1) % 4 === 0) {
                        timeSlotsDiv.appendChild(document.createElement("br"));
                    }
                });
            }

            renderTimeSlots();

            // 날짜 변경 시 예약된 시간 가져오기
            document.getElementById("reservation_date").addEventListener("change", function () {
                const selectedDate = this.value;
                const statId = document.querySelector("input[name='stat_id']").value;

                fetch(`getReservedTimes?date=${selectedDate}&statId=${statId}`)
                    .then(response => response.json())
                    .then(data => {
                        renderTimeSlots(data);
                        selectedTimesContainer.innerHTML = ''; // ⬅️ 반드시 hidden input 초기화!
                    });
            });

            // 유효성 검사
            document.getElementById("reservationForm").addEventListener("submit", function (e) {
                const selectedTimes = Array.from(document.querySelectorAll("input[name='reservation_time']"))
                    .map(input => input.value)
                    .sort();

                if (selectedTimes.length < 1) {
                    alert("예약 시간을 선택해주세요.");
                    e.preventDefault();
                    return;
                }

                const toMinutes = time => {
                    const [h, m] = time.split(":").map(Number);
                    return h * 60 + m;
                };

                const minutesArray = selectedTimes.map(toMinutes);
                for (let i = 1; i < minutesArray.length; i++) {
                    if (minutesArray[i] - minutesArray[i - 1] !== 30) {
                        alert("선택한 시간이 30분 간격으로 연속되지 않았습니다.\n예: 10:00 → 10:30 → 11:00");
                        e.preventDefault();
                        return;
                    }
                }
            });
        </script>
    </body>

    </html>