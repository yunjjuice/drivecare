(function () {
    const btn = document.getElementById("btnCheckId");
    if (!btn) return;

    const input = document.getElementById("userId");
    const result = document.getElementById("idCheckResult");
    const checkUrl = btn.dataset.checkUrl;

    btn.addEventListener("click", function () {
        const userId = (input.value || "").trim();
        if (!userId) {
            result.textContent = "아이디를 입력하세요.";
            result.style.color = "red";
            return;
        }

        fetch(checkUrl + "?userId=" + encodeURIComponent(userId), {
            headers: { "X-Requested-With": "XMLHttpRequest" },
        })
            .then((r) => {
                if (!r.ok) throw new Error("HTTP " + r.status);
                return r.json();
            })
            .then((data) => {
                if (data.exists) {
                    result.textContent = "이미 사용 중인 아이디입니다.";
                    result.style.color = "red";
                } else {
                    result.textContent = "사용 가능한 아이디입니다.";
                    result.style.color = "green";
                }
            })
            .catch((e) => {
                console.error(e);
                result.textContent = "확인 중 오류가 발생했습니다.";
                result.style.color = "red";
            });
    });
})();
