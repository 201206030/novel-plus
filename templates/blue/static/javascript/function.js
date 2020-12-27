//把对象调整到中心位置
;(function($){
    $.fn.setmiddle = function() {
        var dl = $(document).scrollLeft(),
            dt = $(document).scrollTop(),
            ww = $(window).width(),
            wh = $(window).height(),
            ow = $(this).width(),
            oh = $(this).height(),
            left = (ww - ow) / 2 + dl,
            top = (wh - oh) / 2 + dt;
        $(this).css({left:Math.max(left, dl) + "px",top:Math.max(top, dt) + "px"}); 
        return this;
    }
})(jQuery);

//提示信息
;(function($){
    $.tips = function(options) {
        var settings = {
            content: "",
            icon: "success",
            time: 1500,
            close: false,
            zindex: 2999
        };
        if (options) {
            $.extend(settings, options);
        }
        if (settings.close) {
            $(".tips").hide();
            return;
        }
        if (!$(".tips")[0]) {
            $("body").append('<div class="tips"><i></i><span></span></div>');
            $(".tips").css("z-index", parseInt(settings.zindex));
        }
        $(".tips span").html(settings.content);
        $(".tips").attr("class", "tips tips-" + settings.icon);
        $(".tips").css("z-index", parseInt($(".tips").css("z-index"))+1).setmiddle().show();
         
        if (settings.time > 0) {
            setTimeout(function() {
                $(".tips").fadeOut()
            }, settings.time);
        }
    }
})(jQuery);

//设置cookie
var setCookie = function(cname, cvalue, exdays) {
    var day = new Date();
    day.setTime(day.getTime() + (exdays*24*60*60*1000));
    document.cookie = cname + "=" + encodeURI(cvalue) + "; " + "expires=" + day.toUTCString() +"; path=/";
};

//获取cookie
var getCookie = function(cname) {
    var name = cname + "=";
    var obj = document.cookie.split(';');
    for (var i = 0; i < obj.length; i++) {
        var c = obj[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
    }
    return "";
};

//清除cookie 
var clearCookie = function(cname) { 
    setCookie(cname, "", -1); 
};

//检查cookie 
var checkCookie = function(cname) {
    var value = getCookie(cname);
    if (value != "") {
        return true;
    }
    else {
        return false;
    }
};

var chapterInit = function(){
    $(".left-btns li").click(function() {
        var index = $(this).index();
        $(this).addClass("on").siblings("li").removeClass("on");
        if (index < 3) {
            $(".show-panel").eq(index).addClass("on").siblings(".show-panel").removeClass("on");
        }
        else {
            $(".show-panel").removeClass("on");
        }
    });
    $(".hide-panel").click(function() {
        $(".left-btns li").removeClass("on");
        $(".show-panel").removeClass("on");
    });
    $(".hide-panel").click(function() {
        $(".left-btns li").removeClass("on");
        $(".show-panel").removeClass("on");
    });
    $(".chapter-wrap .porn .index").click(function() {
        $(".left-btns li.btn-chapter").addClass("on").siblings("li").removeClass("on");
        $(".chapter-panel").addClass("on").siblings(".show-panel").removeClass("on");
    });
    $(".set-skin dd span").click(function() {
        $(this).addClass("cur").siblings("span").removeClass("cur");
        var value = $(this).attr("data-value");
        switch (value) {
            case "0":
                $("body").removeClass().addClass("chapter-skin0");
                break;
            case "1":
                $("body").removeClass().addClass("chapter-skin1");
                break;
            case "2":
                $("body").removeClass().addClass("chapter-skin2");
                break;
        }
    });
    $(".set-font-family dd span").click(function() {
        $(this).addClass("cur").siblings("span").removeClass("cur");
        var value = $(this).attr("data-value");
        switch (value) {
            case "0":
                $(".chapter-wrap").removeClass("font-family0 font-family1 font-family2").addClass("font-family0");
                break;
            case "1":
                $(".chapter-wrap").removeClass("font-family0 font-family1 font-family2").addClass("font-family1");
                break;
            case "2":
                $(".chapter-wrap").removeClass("font-family0 font-family1 font-family2").addClass("font-family2");
                break;
        }
    });
    $(".set-font-size dd .prev").click(function() {
        var size = parseInt($(".set-font-size dd .size").text());
        if (size <= 12) {
            size = 12;
        }
        else {
            size = size-1;
        }
        $(".set-font-size dd .size").text(size);
        $(".chapter-wrap").css("font-size",size);

    });
    $(".set-font-size dd .next").click(function() {
        var size = parseInt($(".set-font-size dd .size").text());
        if (size >= 36) {
            size = 36;
        }
        else {
            size = size+1;
        }
        $(".set-font-size dd .size").text(size);
        $(".chapter-wrap").css("font-size",size);
    });
    $(".set-width dd .prev").click(function() {
        var size = parseInt($(".set-width dd .size").text());
        if (size <= 760) {
            size = 760;
        }
        else {
            size = size-100
        }
        $(".set-width dd .size").text(size);
        $(".chapter-container").removeClass("w760 w860 w960 w1060").addClass("w"+size);

    });
    $(".set-width dd .next").click(function() {
        var size = parseInt($(".set-width dd .size").text());
        if (size >= 1060) {
            size = 1060;
        }
        else {
            size = size+100
        }
        $(".set-width dd .size").text(size);
        $(".chapter-container").removeClass("w760 w860 w960 w1060").addClass("w"+size);
    });
    $(".set-btns .btn-save").click(function() {
        var a,b,c,d,e;
        $(".set-skin dd span").each(function(){
            if ($(this).hasClass("cur")) {
                a = $(this).attr("data-value");
            }
        });
        $(".set-font-family dd span").each(function(){
            if ($(this).hasClass("cur")) {
                b = $(this).attr("data-value");
            }
        });
        c = $(".set-font-size dd .size").text();
        d = $(".set-width dd .size").text();
        e = '{"skin":' + a +',"family":' + b +',"size":' + c +',"width":' + d +'}';
        clearCookie("hs13_set123");
        setCookie("hs13_set123", e, "365");
        $(".left-btns li").removeClass("on");
        $(".show-panel").removeClass("on");
    });
    $(".set-btns .btn-cancel").click(function() {
        $(".left-btns li").removeClass("on");
        $(".show-panel").removeClass("on");
    });
    if (checkCookie("hs13_set123") == true) {
        var data = JSON.parse(decodeURI(getCookie("hs13_set123")));
        $("body").removeClass().addClass("chapter-skin"+data.skin);
        $(".chapter-container").removeClass("w760 w860 w960 w1060").addClass("w"+data.width);
        $(".chapter-wrap").css("font-size",data.size);
        $(".chapter-wrap").removeClass("font-family0 font-family1 font-family2").addClass("font-family"+data.family);
        $(".set-skin dd span").each(function(){
            if ($(this).attr("data-value") == data.skin) {
                a = $(this).addClass("cur").siblings("span").removeClass("cur");
            }
        });
        $(".set-font-family dd span").each(function(){
            if ($(this).attr("data-value") == data.family) {
                a = $(this).addClass("cur").siblings("span").removeClass("cur");
            }
        });
        $(".set-font-size dd .size").text(data.size);
        $(".set-width dd .size").text(data.width);
    }
    $(window).scroll(function () {
        if ($(".left-bar,.right-bar").length > 0) {
            var st = $(window).scrollTop();
            var ct = $(".chapter-wrap .main").offset().top;
            var cb = $(".chapter-wrap .main").offset().bottom;
            var ch = $(".chapter-wrap .main").height();
            if (st > ct && st < ct + ch) {
                $(".left-bar").css({position:"fixed", top:0});
            }
            else {
                $(".left-bar").css({position:"absolute", top:ct});
            }
            $(".right-bar").css({position:"fixed", bottom:"20px"});
        }
    });
}

$(function(){

    //首页切换
    $(".new-book ol li").click(function() {
        var index = $(this).index();
        var _cid = $(this).data('cid');
        $(this).addClass("on").siblings("li").removeClass("on");
        // $(".new-book ul").eq(index).show().siblings("ul").hide();

        $.ajax({
           type: "GET",
           url: "/ajax/home_cid_list?cid="+_cid,
           async: false,
           success: function(info) {
               if (info.code == 0 && info.data != "") {
                   $(".home_cid_list").html(info.data);
               }
           }
       });

    });


	//小说页
	$(".book-info .intro .more").click(function() {
		$(this).toggleClass("show");
    	$(".book-info .intro").toggleClass("show");
	});
    $(".go-review").click(function() {
        $('body,html').animate({
            scrollTop: $(".review").offset().top
        }, 300);
        return false;
    });
	$(".j-textarea").live("focus", function() {
		if ($(this).val() == "说点什么吧，您的评论是对本书最大的支持！") {
			$(this).val("")
		}
	}).live("blur", function() {
		if ($.trim($(this).val()) == "") {
			$(this).val("说点什么吧，您的评论是对本书最大的支持！")
		}
	}).live("keydown", function(){  
        var len = $(".j-textarea").val().length;
        if (len >= 200) {
            var num = $(".j-textarea").val().substr(0,200);;
            $(".j-textarea").val(num);;
        }
        else {
            $(".j-count").text(200-len-1);
        }
		$(".form-msg").text("");
    });
    $(".j-send").click(function() {
    	var txt = $(".j-textarea").val();
    	var len = $(".j-textarea").val().length;
    	if (txt == "说点什么吧，您的评论是对本书最大的支持！" || len == 0) {
    		$(".form-msg").text("评论内容不能为空");
    		return false;
    	}
    	else {
    		alert("您的评论已提交，审核过后您的评论会出现在这里！");
    	}
    });
    var $review = $(".review-list ul");
    if ($review.children("li").length > 5) {
    	$review.children("li").slice(5).css("display","none");
    	$(".review .more").click(function() {
    		$(this).css("display","none");
    		$review.children("li").slice(5).css("display","block");
    	});
    }
    $(".guess ol li").click(function() {
        var index = $(this).index();
        $(this).addClass("on").siblings("li").removeClass("on");
        $(".guess ul").eq(index).show().siblings("ul").hide();
    });
    //搜索输入字符串合法性验证
    $("#search-btn").click(function(){
        var str = $("#q").val();
        var ret = /[^\w\u4e00-\u9fa5]/;
        if(ret.test(str)){
           alert("搜索内容不合法");
           return false;
        }else{
           return ture;
        }
    });

});
