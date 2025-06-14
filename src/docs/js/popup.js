function ready(callbackFunc) {
    if (document.readyState !== "loading") {
        callbackFunc();
    } else if (document.addEventListener) {
        document.addEventListener("DOMContentLoaded", callbackFunc);
    } else {
        document?.attachEvent("onreadystatechange", function () {
            if (document.readyState === "complete") {
                callbackFunc();
            }
        })
    }
}

function openPopup(e) {
    const target = e.target;
    if (target.className !== "popup") {
        return;
    }

    e.preventDefault();
    const screenX = e.screenX;
    const screenY = e.screenY;
    window.open(target.href, target.text, `left=${screenX}, top=${screenY}, width=500, height=600, status=no, menubar=no, toolbar=no, resizable=no, popup=true`);
}

ready(function () {
    const el = document.getElementById("content");
    el.addEventListener("click", e => openPopup(e));
})