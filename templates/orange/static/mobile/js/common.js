Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};

Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

var token = localStorage.getItem("token");
if (token) {
    $.get("/user/isLogin", {"token": token}, function (data) {
        if (data.code != 1) {//未登录
            localStorage.removeItem("token");
        }
    })
}


function readHistory() {

    var books = localStorage.getItem("historyBooks");
    var bookIds = "-1929";
    if (books) {
        bookIds = JSON.parse(localStorage.getItem("historyBooks")).join(",");
    }
    window.location.href = "/book/search?historyBookIds=" + bookIds;
};


function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return (false);
}



