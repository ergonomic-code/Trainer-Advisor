function closeModal() {
    const el = document.getElementById("modal")
    el.classList.add('closing')
    el.addEventListener('animationend', function() {
        el.remove()
    })
}