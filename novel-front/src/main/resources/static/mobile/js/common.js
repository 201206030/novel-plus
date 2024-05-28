var needLoginPath = ['/user/favorites.html', '/user/comment.html', '/user/feedback.html',
    '/user/feedback_list.html', '/user/read_history.html', '/user/set_name.html',
    '/user/set_password.html', '/user/set_sex.html', '/user/setup.html', '/user/userinfo.html',
    "/pay/index.html," +
    "/author/register.html", "/author/index.html"];
var isLogin = false;
var url = window.location.search;

//key(需要检索的键）
function getSearchString(key) {
    var str = url;
    str = str.substring(1, str.length); // 获取URL中?之后的字符（去掉第一位的问号）
    // 以&分隔字符串，获得类似name=xiaoli这样的元素数组
    var arr = str.split("&");

    for (var i = 0; i < arr.length; i++) {
        var tmp_arr = arr[i].split("=");
        if (tmp_arr[0] == key) {
            return decodeURIComponent(tmp_arr[1]);
        }
    }
    return undefined;
}

var keyword = getSearchString("k");
if (keyword != undefined) {
    $("#searchKey").val(keyword);
    $("#workDirection").remove();
    $("#idGirl").remove();
}

function searchByK(k) {
    if (!k) {
        window.location.href = '/book/bookclass.html?k=' + encodeURIComponent(document.getElementById("searchKey").value)
    } else {
        window.location.href = '/book/bookclass.html?k=' + encodeURIComponent(k)
    }
}

$("#searchKey").keypress(function (even) {
    if (even.which == 13) {
        even.stopPropagation();
        //enter键按下
        searchByK();
    }
});
Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};

jQuery.cookie = function (name, value, options) {
    if (typeof value != 'undefined') {
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString();
        }
        var path = options.path ? '; path=' + options.path : '';
        var domain = options.domain ? '; domain=' + options.domain : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};

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

var token = $.cookie('Authorization');
if (!token) {
    if (needLoginPath.indexOf(window.location.pathname) != -1) {
        location.href = '/user/login.html?originUrl=' + encodeURIComponent(location.href);
    }

    // $(".user_link").html("<a href=\"/user/login.html\">登录</a>｜<a href=\"/user/register.html\">注册</a>");
} else {
    $.ajax({
        type: "POST",
        url: "/user/refreshToken",
        data: {},
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                // $(".user_link").html("<a href=\"/user/userinfo.html\"><i style=\"font-size: 20px;\" class=\"layui-icon \n" +
                //     "\">&#xe66f;" +
                //     "\n" +
                //     "</i></a>");
                if ("/user/login.html" == window.location.pathname) {
                    var orginUrl = getSearchString("originUrl");
                    window.location.href = orginUrl == undefined || orginUrl.isBlank() ? "/" : orginUrl;
                    return;
                }
                isLogin = true;
                if (localStorage.getItem("autoLogin") == 1) {
                    $.cookie('Authorization', data.data.token, {expires: 7, path: '/'});
                } else {
                    $.cookie('Authorization', data.data.token, {path: '/'});
                }
            } else {
                if (needLoginPath.indexOf(window.location.pathname) != -1) {
                    location.href = '/user/login.html';
                }
                // $(".user_link").html("<a href=\"/user/login.html\">登录</a>｜<a href=\"/user/register.html\">注册</a>");
            }
        },
        error: function () {
            layer.alert('网络异常');
        }

    });
}

function logout() {
    $.cookie('Authorization', null, {path: '/'});
    location.reload();
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

String.prototype.isPhone = function () {
    var strTemp = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
    if (strTemp.test(this)) {
        return true;
    }
    return false;
};

String.prototype.isBlank = function () {
    if (this == null || $.trim(this) == "") {
        return true;
    }
    return false;
};
String.prototype.isNickName = function () {
    var strTemp = /^[\u4E00-\u9FA5A-Za-z0-9_]+$/;
    if (strTemp.test(this)) {
        return true;
    }
    return false;
};


