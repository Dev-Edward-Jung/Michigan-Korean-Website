let restaurantId = null;

document.addEventListener("DOMContentLoaded", () => {
    // passing RestaurantId
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

    // Role Based Function
    const roleMeta = document.querySelector('meta[name="user-role"]');
    const userRole = roleMeta ? roleMeta.content : null;

    if (userRole !== "OWNER" && userRole !== "MANAGER") {
        const employeeMenu = document.querySelectorAll("a.menu-link.inventory_link, .menu-item > a.menu-link.menu-toggle > div[data-i18n='employee']");
        employeeMenu.forEach(el => {
            // 상위 li까지 삭제 (menu-sub도 포함)
            const menuItem = el.closest(".menu-item") || el.closest("li");
            if (menuItem) menuItem.remove();
        });
    }
});