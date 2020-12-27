layui.use(['layer', 'jquery'], function () {
    var layer = layui.layer;
    var $ = layui.jquery;

    function LastRead() {
        this.bookList = "bookcms_book_list"
    }

    LastRead.prototype = {
        set: function (title,article_id, chapter_title,chapter_id, author, category,source_id) {
           if (!(article_id && title && chapter_title && chapter_id && author && category && source_id)) return false;
            var value =  title + '#' + article_id + '#' + chapter_title + '#' + chapter_id + '#' + author + '#' + category + '#' + source_id;

            this.setItem(article_id, value);
            this.setBook(article_id);
            return true;
        },
        get: function (k) {
            return this.getItem(k) ? this.getItem(k).split("#") : "";
        },
        remove: function (k) {
            this.removeItem(k);
            this.removeBook(k)
        },
        setBook: function (v) { //保存书籍
            var reg = new RegExp("(^|#)" + v);
            var books = this.getItem(this.bookList);
            if (books == "") {
                books = v
            }
            else {
                if (books.search(reg) == -1) {
                    books += "#" + v
                }
                else {
                    books.replace(reg, "#" + v)
                }
            }
            this.setItem(this.bookList, books)
        },
        getBook: function () {
            var v = this.getItem(this.bookList) ? this.getItem(this.bookList).split("#") : Array();
            var books = Array();
            if (v.length) {
                for (var i = 0; i < v.length; i++) {
                    var tem = this.getItem(v[i]).split('#');
                    if (tem.length > 3) books.push(tem);
                }
            }
            return books
        },
        removeBook: function (v) {
            var reg = new RegExp("(^|#)" + v);
            var books = this.getItem(this.bookList);
            if (!books) {
                books = ""
            }
            else {
                if (books.search(reg) != -1) {
                    books = books.replace(reg, "")
                }
            }
            this.setItem(this.bookList, books)
        },
        setItem: function (k, v) { //保存章节
            if (!!window.localStorage) {
                localStorage.setItem(k, v);
            } else {
                var expireDate = new Date();
                var EXPIR_MONTH = 30 * 24 * 3600 * 1000;
                expireDate.setTime(expireDate.getTime() + 12 * EXPIR_MONTH)
                document.cookie = k + "=" + encodeURIComponent(v) + ";expires=" + expireDate.toGMTString() + "; path=/";
            }
        },
        getItem: function (k) {
            var value = ""
            var result = ""
            if (!!window.localStorage) {
                result = window.localStorage.getItem(k);
                value = result || "";
            }
            else {
                var reg = new RegExp("(^| )" + k + "=([^;]*)(;|\x24)");
                var result = reg.exec(document.cookie);
                if (result) {
                    value = decodeURIComponent(result[2]) || ""
                }
            }
            return value
        },
        removeItem: function (k) {
            if (!!window.localStorage) {
                window.localStorage.removeItem(k);
            } else {
                var expireDate = new Date();
                expireDate.setTime(expireDate.getTime() - 1000)
                document.cookie = k + "= " + ";expires=" + expireDate.toGMTString()
            }
        },
        removeAll: function () {
            if (!!window.localStorage) {
                window.localStorage.clear();
            }
            else {
                var v = this.getItem(this.bookList) ? this.getItem(this.bookList).split("#") : Array();
                var books = Array();
                if (v.length) {
                    for (i in v) {
                        var tem = this.removeItem(v[k])
                    }
                }
                this.removeItem(this.bookList)
            }
        }
    };

    function zzleft(mainStr, lngLen) {
        if (lngLen > 0) {
            return mainStr.substring(0, lngLen)
        }
        else {
            return null
        }
    }

    //载入历史记录
    // function loadbooker() {
    //     var bookhtml = '';
    //     var books = lastread.getBook();
    //     var books = books.reverse();
    //     if (books.length) {
    //         for (var i = 0; i < books.length; i++) {
    //             if (i <= 100) {
    //
    //                 var title = books[i][0];
    //                 var article_id = books[i][1];
    //                 var chapter_title = books[i][2];
    //                 var chapter_id = books[i][3];
    //                 var author = books[i][4];
    //                 var category = books[i][5];
    //
    //                 var article_url = article_rule.replace('{article_id}', article_id);
    //                 var chapter_url = chapter_rule.replace('{article_id}', article_id);
    //                 chapter_url = chapter_url.replace('{chapter_id}', chapter_id);
    //
    //                 bookhtml += '<li><span class="s1">' + category + '</span><span class="s2"><a href="' + article_url + '" target="_blank">' + title + '</a></span><span class="s3"><a href="' + chapter_url + '" target="_blank">' + chapter_title + '</a></span><span class="s4">' + author + '</span><span class="s5"><a href="javascript:void(0)" class="remove-book" data-id="' + article_id + '" title="删除“' + title + '”?">删除</a></span><span class="s6">&nbsp;</span><span class="s7">&nbsp;</span></li>';
    //             }
    //         }
    //     } else {
    //         bookhtml += '<div style="height:100px;line-height:100px; text-align:center">还木有任何书籍( ˙﹏˙ )</div>';
    //     }
    //
    //     // $(".read_book").html(bookhtml);
    // }

    window.lastread = new LastRead();

    $(function () {

        $(".link-bookshelf").on("click",function() {

            var books = lastread.getBook().reverse();

            if (($(".bookshelf-list").length - 1) == books.length)  {
                $(".bookshelf-mask").show();
                $(".bookshelf-panel").show();
            } else {

                var html = "";

                html += "<div class=\"bookshelf-mask\"></div>";
                html += "<div class=\"bookshelf-panel\">";
                html += "<div class=\"bookshelf-head\"><h4>我的书架(" + books.length +"本) </h4><a class=\"close\" target=\"_self\">关闭</a></div>";
                html += "<div class=\"bookshelf-list\">";
                html += "<div class=\"clearfix title\"><span class=\"label\"></span><em>用户浏览过的小说会自动保存到书架中（只限同一电脑）</em></div>";
                html += "</div>";
                html += "</div>";
                $("body").append(html);

                var book_html = '<li><span class="s1"><b>作品分类</b></span><span class="s2"><b>作品名称</b></span><span class="s3"><b>上次阅读章节</b></span><span class="s4"><b>作者</b></span><span class="s5"><b>操作</b></span><span class="s6"><b>&nbsp;</b></span><span class="s7"><b>&nbsp;</b></span></li>';

                if (books.length) {
                    for (var i = 0; i < books.length; i++) {
                        if (i <= 100) {

                            var title = books[i][0];
                            var article_id = books[i][1];
                            var chapter_title = books[i][2];
                            var chapter_id = books[i][3];
                            var author = books[i][4];
                            var category = books[i][5];
                            var sourceid = books[i][6];
                            var article_url = article_rule.replace('{article_id}', article_id);
                            var chapter_url = chapter_rule.replace('{article_id}', sourceid);
                            chapter_url = chapter_url.replace('{chapter_id}', chapter_id);

                            book_html += '<li><span class="s1">' + category + '</span><span class="s2"><a href="' + article_url + '" target="_blank">' + title + '</a></span><span class="s3"><a href="' + chapter_url + '" target="_blank">' + chapter_title + '</a></span><span class="s4">' + author + '</span><span class="s5"><a href="javascript:void(0)" class="remove-book" data-id="' + article_id + '" title="删除“' + title + '”?">删除</a></span><span class="s6">&nbsp;</span><span class="s7">&nbsp;</span></li>';

                        }
                    }
                } else {
                    book_html += '<div style="height:100px;line-height:100px; text-align:center">还木有任何书籍( ˙﹏˙ )</div>';
                }
                $(".bookshelf-list").append(book_html);

            }
        });
        $(".bookshelf-head .close").live("click", function() {
            $(".bookshelf-mask").hide();
            $(".bookshelf-panel").hide();
        });

        $(".bookshelf-list li a.remove-book").live("click", function() {
            var _this =  $(this);
            layer.confirm('确定从书架中移除？', {title:'提示'}, function(index){

                lastread.remove(_this.data('id'));
                _this.parent().parent().slideUp(300, function(){
                    $(this).remove();
                    var books = lastread.getBook().reverse();
                    $(".bookshelf-head h4").html("我的书架("+books.length+"本)");
                });

                layer.close(index);
            });
        });

    });
});