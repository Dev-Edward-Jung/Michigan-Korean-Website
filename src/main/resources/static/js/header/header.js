let restaurantId = null;

document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    restaurantId = params.get("restaurantId");

    if (!restaurantId) {
        alert("레스토랑 정보가 없습니다. 다시 로그인해주세요.");
        window.location.href = "/page/owner/login";
        return;
    }

    document.querySelectorAll("a.menu-link").forEach(link => {
        const href = link.getAttribute("href");
        if (href && href.startsWith("/") && !href.startsWith("#") && !href.includes("restaurantId=")) {
            const separator = href.includes("?") ? "&" : "?";
            const newHref = `${href}${separator}restaurantId=${restaurantId}`;
            link.setAttribute("href", newHref);
        }
    });
});