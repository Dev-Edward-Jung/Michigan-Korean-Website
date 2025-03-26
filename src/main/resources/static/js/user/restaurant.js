
// restaurant-list.html

document.querySelectorAll('restaurant-row').forEach(row => {
    row.addEventListener('click', () => {
        const id = row.getAttribute('data-id');
        window.location.href = `/inventory/detail/${id}`;
    });
});