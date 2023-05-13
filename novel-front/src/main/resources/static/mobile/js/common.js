var needLoginPath = ['/user/favorites.html','/user/comment.html','/user/feedback.html',
    '/user/feedback_list.html','/user/read_history.html','/user/set_name.html',
    '/user/set_password.html','/user/set_sex.html','/user/setup.html','/user/userinfo.html',
    "/pay/index.html," +
    "/author/register.html","/author/index.html"];
var isLogin = false;

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
if(!token){
    if(needLoginPath.indexOf(window.location.pathname) != -1){
        location.href = '/user/login.html?originUrl='+decodeURIComponent(location.href);
    }

    $(".user_link").html("<a href=\"/user/login.html\">登录</a>｜<a href=\"/user/register.html\">注册</a>");
}else{
    $.ajax({
        type: "POST",
        url: "/user/refreshToken",
        data: {},
        dataType: "json",
        success: function(data){
            if(data.code == 200){
                $(".user_link").html("<a href=\"/user/userinfo.html\">"+(data.data.nickName.substring(0,3))+"...</a>&nbsp;&nbsp;<a href=\"javascript:logout()\">退出</a>");
                ;
                if("/user/login.html" == window.location.pathname){
                    var orginUrl = getSearchString("originUrl");
                    window.location.href = orginUrl == undefined || orginUrl.isBlank() ? "/" : orginUrl;
                    return;
                }
                isLogin = true;
                if(localStorage.getItem("autoLogin") == 1){
                    $.cookie('Authorization', data.data.token, { expires: 7 ,path: '/'  });
                }else {
                    $.cookie('Authorization', data.data.token,{ path: '/'  });
                }
            }else{
                if(needLoginPath.indexOf(window.location.pathname) != -1){
                    location.href = '/user/login.html';
                }
                $(".user_link").html("<a href=\"/user/login.html\">登录</a>｜<a href=\"/user/register.html\">注册</a>");
            }
        },
        error: function () {
            layer.alert('网络异常');
        }

    });
}

function logout() {
    $.cookie('Authorization', null,{ path: '/'  });
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



