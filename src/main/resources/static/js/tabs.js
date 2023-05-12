let currentTab = 0;
showTab(currentTab);
function showTab(n) {
    let x = document.getElementsByClassName("tab");
    x[n].style.display = "block";
    if (n === 0) {
        document.getElementById("prevBtn").disabled = true;
    } else {
        document.getElementById("prevBtn").disabled = false;
    }
    if (n == (x.length - 1)) {
        document.getElementById("nextBtn").innerHTML = "Submit";
    } else {
        document.getElementById("nextBtn").innerHTML = "Next";
    }
    fixStepIndicator(n)
}

function nextPrev(n) {
    console.log(n)
    let x = document.getElementsByClassName("tab");
    x[currentTab].style.display = "none";
    currentTab = currentTab + n;
    if (currentTab >= x.length) {
        document.getElementById("register-form").submit();
        return false;
    }
    showTab(currentTab);
}
function fixStepIndicator(n) {
    let x = document.getElementById("progressBar");
    x.style.width = (n + 1) * 100 / 2 + "%";
    x.ariaValueNow = (n + 1) * 100 / 2;
}