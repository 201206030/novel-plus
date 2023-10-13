var $C = function (objName) {
    if (typeof (document.getElementById(objName)) != "object")
    { return null; }
    else
    { return document.getElementById(objName); }
}
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
$(function () {


    $(".rightList li").mouseover(function () {
        //$($(this).parent()).children().each(function () {
        //    $(this).removeClass("on");
        //});
        //$(this).addClass("on");
    });
    $(".rightList_nobor li").mouseover(function () {
        $($(this).parent()).children().each(function () {
            $(this).addClass("on");
        });
    });

    $("#headerUserHistoryBtn").mouseover(function () {
        HeaderShowUtil.headerShowHistory();
    });
    $("#headerUserHistory").mouseleave(function () {
        HeaderShowUtil.headerHideHistory();
    });
});
function getNote() {
}
function goPage(cpage) {
    location.href = '?page=' + cpage;
}



function isWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase();
    if (ua.indexOf("micromessenger") > 0) {
        return true;
    } else {
        return false;
    }
}

var HeaderShowUtil = {
    headerShowHistory: function (obj) {
        if ($("#headerUserHistory").html().length < 10) {
            var rStr = '<div class="record_box">';
            rStr += '					<div class="record_title" id="hdShowTitle"><a href="javascript:void(0);" class="record_tit1 on" onclick="javascript:HeaderShowUtil.headerShowHistoryLog(this);">最近阅读</a><a href="javascript:void(0);" class="record_tit2" onclick="javascript:HeaderShowUtil.headerShowFavLog(this);">我的书架</a></div>';
            rStr += '					<div class="record_list record_list1" id="hdShowHistory">';
            rStr += '						<ul>';
            rStr += '						</ul>';
            rStr += '						<a class="all" href="/" >查看全部</a>';
            rStr += '					</div>';
            rStr += '					<div class="record_list record_list2" style="display:none" id="hsShowFav">';
            rStr += '						<ul>';
            rStr += '						</ul>';
            rStr += '						<a class="all" href="/" >查看全部</a>';
            rStr += '					</div>';
            rStr += '					<p class="sp"></p>';
            rStr += '				</div>';
            $("#headerUserHistory").html(rStr);
        }
        $("#headerUserHistory").show();
        $("#headerUserHistoryBtn").addClass("on");
        HeaderShowUtil.headerShowHistoryLog();
    },
    headerHideHistory: function () {
        $("#headerUserHistory").hide();
        $("#headerUserHistoryBtn").removeClass("on");
    },
    headerShowHistoryLog: function (obj) {
        if (obj != undefined) {
            $("#hdShowTitle a").removeClass("on");
            $(obj).addClass("on");
            $("#hdShowHistory").show();
            $("#hsShowFav").hide();
        }
        var cookieHistory = jQuery.cookie("wapviewhistory");
        if (cookieHistory != undefined && cookieHistory.length > 0) {
            var bList, bIdList;
            var bIdArray = new Array();
            var cookieList = cookieHistory.split(',');
            for (var i = 0; i < cookieList.length && i < 3; i++) {
                var str = cookieList[i];
                if (str.indexOf('|') > 0) {
                    bList = str.split('|');
                    if (bList.length == 3) {
                        bIdList += ',' + bList[0].replace("b", "");
                        bIdArray[bList[0].replace("b", "")] = bList[1];
                    }
                }
            }

        }
        else {
            $("#hdShowHistory ul").html("<li>暂无看书历史</li>");
        }
    },
    headerShowFavLog: function (obj) {
        $("#hdShowTitle a").removeClass("on");
        $(obj).addClass("on");
        $("#hsShowFav").show();
        $("#hdShowHistory").hide();
        var rStr = '';
        var uname = jQuery.cookie("waplogname");
        if (uname != undefined && uname != "") {
        }
        else {
            rStr = '<li><a href="/user/login.html">请先登录</a></li>';
            $("#hsShowFav ul").html(rStr);
        }

    }
}
