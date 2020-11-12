var BookDetail = {
    wepDomain: 'java2nb.com',
    msgStyle: 'background-color:#333; color:#fff; text-align:center; border:none; font-size:20px; padding:10px;',
    reShowCover: function () {
        //$(".cCover").height($(".cDetail").height());
        //$(".cCover").width($(".cDetail").width());
    },
    DescriptionMore: function (sClass) {
        if (sClass == "") {
            if ($("#pDesMore").html().length > 150) {
                $("#divDescription").html($("#pDesMore").html().substring(0, 150) + "<a href=\"javascript:void(0);\" class=\"info_txt_more\" onclick=\"javascript:BookDetail.DescriptionMore('down');\">展开<img src=\"../images/arrow_d.png\" /></a>");
            } else {
                $("#divDescription").html($("#pDesMore").html());
            }
        } else {
            $("#divDescription").html($("#pDesMore").html() + "<a href=\"javascript:void(0);\"  class=\"info_txt_more\"  onclick=\"javascript:BookDetail.DescriptionMore('');\">收起<img src=\"../images/arrow_t.png\" /></a>");
        }
    },
    AddFavorites: function (BId, CId, layerStatus) {
        $.ajax({
            type: "POST",
            url: "/user/addToBookShelf",
            data: {'bookId': $("#bookId").val(), 'preContentId': $("#preContentId").val()},
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    if (layerStatus == 1) {
                        $("#cFavs").html("<a class=\"ico_shelf\" href=\"javascript:void(0);\"><b>已收藏</b></a>");
                        jQuery.cookie("u-faorites", "1");
                    }
                    else {
                        $("#cFavs").html("<a class=\"btn_ora_white btn_addsj\" href=\"javascript:void(0);\">已在书架</a>");
                    }


                } else if (data.code == 1001) {
                    //未登录
                    location.href = '/user/login.html?originUrl=' + decodeURIComponent(location.href);

                } else {
                    layer.alert(data.msg);
                }

            },
            error: function () {
                layer.alert('网络异常');
            }
        })
    },
    GetFavorites: function (BId) {
        if (jQuery.cookie("u-faorites") == null) {
        } else {
            if (jQuery.cookie("u-faorites") == "1") {
                $("#cFavs").html("<a class=\"ico_shelf\" href=\"javascript:void(0);\"><b>已加书架</b></a>");
            }
        }
        /*BookDetail.SetWholeTip();*/
    },
    GetUserBookLevel: function (idList, bId) {
        if (idList.length > 2) {
        }
    },
    GetUserLevel: function (mTotal) {
        if (mTotal != undefined) {
            var iTotal = parseInt(mTotal);
            if (iTotal < 500) {
                return ["user_level1", "见习"];
            } else if (iTotal < 2000) {
                return ["user_level2", "学徒"];
            } else if (iTotal < 5000) {
                return ["user_level3", "弟子"];
            } else if (iTotal < 10000) {
                return ["user_level4", "执事"];
            } else if (iTotal < 20000) {
                return ["user_level5", "舵主"];
            } else if (iTotal < 30000) {
                return ["user_level6", "堂主"];
            } else if (iTotal < 40000) {
                return ["user_level7", "护法"];
            } else if (iTotal < 50000) {
                return ["user_level8", "长老"];
            } else if (iTotal < 70000) {
                return ["user_level9", "掌门"];
            } else if (iTotal < 100000) {
                return ["user_level10", "宗师"];
            } else {
                return ["user_level11", "盟主"];
            }
        } else {
            return ["user_level1", "见习"];
        }
    },
    SaveComment: function (cmtBId, cmtCId, cmtDetail) {
        if(!isLogin){
            layer.alert('请先登陆');
            return;
        }
        var cmtDetailTemp = cmtDetail.replace(/(^\s*)/g, "");
        if (cmtDetailTemp == '') {
            layer.alert('评论内容必须填写');
            return;
        }
        if (cmtDetailTemp.length < 5) {
            layer.alert('评论内容必须大于5个字');
            return;
        }
        if (cmtDetail.length < 5) {
            layer.alert('评论内容必须大于5个字');
            return;
        }
        $.ajax({
            type: "POST",
            url: "/book/addBookComment",
            data: {'bookId': $("#bookId").val(), 'commentContent': cmtDetail},
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    $('#txtComment').val("")
                    layer.alert('评价成功！');
                    loadCommentList();

                } else if (data.code == 1001) {
                    //未登录
                    location.href = '/user/login.html?originUrl=' + decodeURIComponent(location.href);

                } else {
                    layer.alert(data.msg);
                }

            },
            error: function () {
                layer.alert('网络异常');
            }
        })


    },
    GetFavoritesBook: function (BId) {
    },
    GetMoneyFlower: function () {
    },
    AddAgreeTotal: function (AId, objs) {
    },
    RelationBook: function () {
        relationStep++;
        var bListCount = 0;
        var bList = $("#relationBookList div");
        bListCount = bList.length;
        var sStep = 0, eStep = 2;
        if (bListCount > relationStep * 3) {
            sStep = relationStep * 3;
            eStep = sStep + 2;
            if (sStep > bListCount - 1) {
                eStep = bListCount;
            }
        } else {
            relationStep = -1;
        }
        for (var i = 0; i < bListCount; i++) {
            if (i >= sStep && i <= eStep) {
                bList[i].style.display = "";
            } else {
                bList[i].style.display = "none";
            }
        }
    },
    Click: function (bId) {
        //BookDetail.DescriptionMore("");
    },
    ClickChapter: function (bId, cId, isVip) {
        if (isVip == 1) {
            var m = Math.floor(6 * Math.random());
            if (m == 3) {
            }
        } else {
        }
    },
    SetReadFont: function (fonts) {
        var cFont = parseInt($("#cFonts").html());
        fonts = cFont + fonts;
        if (fonts < 8) {
            fonts = 8;
        }
        if (fonts > 48) {
            fonts = 48;
        }
        localStorage.setItem("fonts", fonts);
        $(".readBox").css("font-size", fonts + "px");
        $("#cFonts").html(fonts);
        $("#ChapterBody").attr("class","article-content font"+fonts);
        BookDetail.reShowCover();
    },
    SetBackUpColor: function (colorNum) {
        localStorage.setItem("colorNum", colorNum);
        document.body.className = 'read_style_' + colorNum;

    },
    SetReadFontFamily: function (fontNum) {
        localStorage.setItem("fontNum", fontNum);
        BookDetail.SetReadFontFamilyClear(fontNum);
    },
    SetReadFontFamilyClear: function (fontNum) {
        $("#setup_font_yahei").removeClass("current");
        $("#setup_font_simsun").removeClass("current");
        $("#setup_font_ks").removeClass("current");
        if (fontNum == 1) {
            $("#setup_font_simsun").addClass("current");
            $(".readBox").css("font-family", "Simsun");
        } else if (fontNum == 2) {
            $("#setup_font_ks").addClass("current");
            $(".readBox").css("font-family", "kaiti");
        } else {
            $("#setup_font_yahei").addClass("current");
            $(".readBox").css("font-family", "microsoft yahei");
        }
    },
    GetReadSet: function (bid, cid, preId, nextId, crank) {
        /*$(".nextPageBox .prev,.ico_pagePrev").click(function () {
            if (preId > 0) {
                location.href = '/book/' + bid + '/' + preId + '.html';
            }
            else {
                location.href = '/book/chapterlist-' + bid + '.html';
            }
        });

        $(".nextPageBox .next,.ico_pageNext").click(function () {
            if (nextId > 0) {
                location.href = '/book/' + bid + '/' + nextId + '.html';
            }
            else {
                location.href = '/book/chapterlist-' + bid + '.html';
            }
        });*/
        $(window).bind('keydown',
            function (e) {
                if (e.keyCode == 37) {
                    if (preId > 0) {
                        location.href = '/book/' + bid + '/' + preId + '.html';
                    } else {
                        location.href = '/book/indexList-' + bid + '.html';
                    }
                } else if (e.keyCode == 39) {
                    if (nextId > 0) {
                        location.href = '/book/' + bid + '/' + nextId + '.html';
                    } else {
                        location.href = '/book/indexList-' + bid + '.html';
                    }
                }
            });
        //BookDetail.SetReadHistory(bid, cid, crank);
    },
    SetReadHistory: function (bid, cid, crank) {
        var strHistory = jQuery.cookie("wapviewhistory");
        if (strHistory != null) {
            var r = new RegExp('b' + bid + '\\\|(.*?),', 'g');
            strHistory = strHistory.replace(r, '')
            strHistory = 'b' + bid + '|' + cid + '|' + crank + ',' + strHistory;
            if (strHistory.length > 500) {
                strHistory = strHistory.substring(0, 500);
                strHistory = strHistory.substring(0, strHistory.lastIndexOf(','));
                strHistory = strHistory + ',';
            }
        } else {
            strHistory = 'b' + bid + '|' + cid + '|' + crank + ',';
        }
        jQuery.cookie("wapviewhistory", strHistory, {path: '/', domain: BookDetail.wepDomain, expires: 365});
    },
    formatDate: function (now, types) {
        if (now != null && now != "") {
            var dateN = new Date(+/\d+/.exec(now)[0]);
            var year = dateN.getFullYear();
            var month = dateN.getMonth() + 1;
            var date = dateN.getDate();
            var hour = dateN.getHours();
            var minute = dateN.getMinutes();
            var second = dateN.getSeconds();
            if (typeof (types) != "undefined" && types != null) {
                return year + "-" + month + "-" + date;
            } else if (hour == 0 && minute == 0 && second == 0) {
                return year + "-" + month + "-" + date;
            } else {
                return year + "-" + month + "-" + date + "   " + hour + ":" + minute + ":" + second;
            }
        } else {
            return "";
        }
    },
    SetWholeTip: function () {
        var str = '<li>1、此书为全网优质作品，按照整本定价折扣销售，购买之后可以阅读该书全部章节。</li>';
        str += '<li>2、支付屋币即可阅读收费章节，没有屋币的需要先充值。</li>';
        str += '<li>3、QQ、微信、微博3种账号之间的数据不互通，如果你发现充值成功但没有屋币到账，请切换账号查看是否充到了别的账号中。</li>';
        if ($("#HidIsWholeBook").val() == "1") {
            $(".tip_list").html(str);
        }
    },
    SetDZChapter: function (bId, cId, isDianZan) {
        if (isDianZan == 1) {
            /*是点赞，设置点赞数+1和不可点状态*/
            var dzData = parseInt($("#read_dz_bar a").text()) + 1;
            $("#read_dz_bar").html('<a class="read_dz on" href="javascript:void(0)"><i></i>' + dzData + '</a>');
        }
    }
}
var rand = {};
rand.get = function (begin, end) {
    return Math.floor(Math.random() * (end - begin)) + begin;
}
