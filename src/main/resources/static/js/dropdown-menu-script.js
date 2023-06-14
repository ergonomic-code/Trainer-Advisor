function myFunction(id) {
    document.getElementById("myDropdown"+id).classList.add("show");
}

document.onmouseover=function(event) {
    const target = event.target;
    /*console.log(target.className)*/
    if (target.className!=='triangle-left' && target.className!=='dropdown-row'){
        const dropdowns = document.getElementsByClassName("dropdown-content");
        let i;
        for (i = 0; i < dropdowns.length; i++) {
            const openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}