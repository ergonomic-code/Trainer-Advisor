function mountSidebar() {

    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
        });
    }

}

window.addEventListener('DOMContentLoaded', event => {
    mountSidebar();
});


window.addEventListener("load", () => {
    document.body.addEventListener('htmx:afterSwap', function (evt) {
        if (evt.detail.xhr.status === 200) {
            mountSidebar();
        }
    })
})