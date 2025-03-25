



// for only register page



document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registerForm");
    const username = document.getElementById("username");
    const password = document.getElementById("password");
    const passwordConfirm = document.getElementById("passwordConfirm");
    const submitBtn = document.getElementById("submitBtn");
    const errorMessage = document.getElementById("errorMsg");
    function validateForm() {
        const usernameVal = username.value.trim();
        const passwordVal = password.value;
        const passwordConfirmVal = passwordConfirm.value;

        // 모든 필드 채워졌는지
        if (!usernameVal || !passwordVal || !passwordConfirmVal) {
            errorMessage.textContent = "Please input everyting";
            submitBtn.disabled = true;
            return;
        }

        // 비밀번호 일치 여부
        if (passwordVal !== passwordConfirmVal) {
            errorMessage.textContent = "Password is not same";
            submitBtn.disabled = true;
            return;
        }

        // 모두 통과
        errorMessage.textContent = "";
        submitBtn.disabled = false;
    }

    // 입력 이벤트에 실시간 반응
    [username, password, passwordConfirm].forEach((input) => {
        input.addEventListener("input", validateForm);
    });

    // 제출 시 기본 이벤트 방지 (백엔드 연동 전용)
    form.addEventListener("submit", (e) => {
        if (submitBtn.disabled) {
            e.preventDefault();
        }
    });
});




// email check with DB
async function checkEmail() {
    const emailInput = document.getElementById('email');
    const message = document.getElementById('emailMessage');
    const email = emailInput.value.trim();

    //  email regular expression
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    // if email is empty, reset value
    if (!email) {
        message.textContent = '';
        return;
    }

    // if email expression is invalid
    if (!emailRegex.test(email)) {
        message.textContent = "invalid email";
        message.style.color = "orange";
        return;
    }

    // if it is email, then request checking duplication
    try {
        const res = await fetch("/api/member/checkEmail", {
            method: "POST",
            headers: {
                "Content-Type": "text/plain",
            },
            body: email,
        });

        if (res) {
            message.textContent = "You can use this email";
            message.style.color = "green";
        } else {
            message.textContent = "You can't use this email";
            message.style.color = "red";
        }

    } catch (err) {
        console.error(err);
        message.textContent = "There is an error while checking email";
        message.style.color = "gray";
    }
}