    document.addEventListener("DOMContentLoaded", () => {

    const params = new URLSearchParams(window.location.search);
    const restaurantId = params.get("restaurantId");

    if (!restaurantId) {
    console.log("No restaurantId found");
    return;
}

    document.querySelectorAll("a.menu-link").forEach(link => {
    const href = link.getAttribute("href");
    if (href && href.startsWith("/") && !href.includes("restaurantId=")) {
    const separator = href.includes("?") ? "&" : "?";
    const newHref = `${href}${separator}restaurantId=${restaurantId}`;
    link.setAttribute("href", newHref);
}
});
});
