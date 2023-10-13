var uFans = {
    startSupportRead: function () {
        var uname = jQuery.cookie("waplogname");
        if (uname != undefined && uname != "") {
            if (spmymoney == 0) {
            }
            else {
                uFans.startSupport();
            }
        }
        else {
            layer.open({
                content: '请先登录',
                style: BookDetail.msgStyle,
                time: 2
            });
        }
    },
    startSupport: function () {
        var rStr = '<a class="closePopup" href="javascript:void(0);" onclick="javascript:uFans.closeBox();"></a>';
        rStr += '<div class="popupTit">';
        rStr += '	<h3>我要捧场作品</h3>';
        rStr += '</div>';
        rStr += '<div class="propsList cf">';
        rStr += '	<ul>';
        rStr += '		<li vals="100">';
        rStr += '			<a class="propWrap" href="javascript:void(0);">';
        rStr += '				<i class="icon_check"></i>';
        rStr += '				<span class="propsBox">100屋币</span>';
        rStr += '			</a>';
        rStr += '		</li>';
        rStr += '		<li class="on"  vals="500">';
        rStr += '			<a class="propWrap" href="javascript:void(0);">';
        rStr += '				<i class="icon_check"></i>';
        rStr += '				<span class="propsBox">500屋币</span>';
        rStr += '			</a>';
        rStr += '		</li>';
        rStr += '		<li vals="2000">';
        rStr += '			<a class="propWrap" href="javascript:void(0);">';
        rStr += '				<i class="icon_check"></i>';
        rStr += '				<span class="propsBox">2000屋币</span>';
        rStr += '			</a>';
        rStr += '		</li>';
        rStr += '		<li vals="5000">';
        rStr += '			<a class="propWrap" href="javascript:void(0);">';
        rStr += '				<i class="icon_check"></i>';
        rStr += '				<span class="propsBox">5000屋币</span>';
        rStr += '			</a>';
        rStr += '		</li>';
        rStr += '		<li vals="10000">';
        rStr += '			<a class="propWrap" href="javascript:void(0);">';
        rStr += '				<i class="icon_check"></i>';
        rStr += '				<span class="propsBox">10000屋币</span>';
        rStr += '			</a>';
        rStr += '		</li>';
        rStr += '		<li vals="100000">';
        rStr += '			<a class="propWrap" href="javascript:void(0);">';
        rStr += '				<i class="icon_check"></i>';
        rStr += '				<span class="propsBox">100000屋币</span>';
        rStr += '			</a>';
        rStr += '		</li>';
        rStr += '	</ul>';
        rStr += '</div>';
        rStr += '<p class="have_num">当前剩余<span class="red">' + spmymoney + '</span>屋币&nbsp;&nbsp;本次捧场<span class="red" id="pcTotal">500</span>屋币<a class="red" href="../pay/" >[充值]</a></p>';
        rStr += '<p><textarea class="popup_text" id="sendSupportNote"   placeholder="感谢您的捧场，留句话鼓励作者吧！"></textarea></p>';
        rStr += '<p class="tc"><a class="btn_red btn_send_pc" href="javascript:void(0);" onclick="javascript:uFans.SendSupport();">立即捧场</a></p>';
        $("#showPC").html(rStr);
        $("#showPC").show();
        $(".maskBox").show();
        $(".pcBox .propsList li").click(function () {
            $(".pcBox .propsList li").removeClass("on");
            $(this).addClass("on");
            $("#pcTotal").html($(this).attr("vals"));
        })
    },
    closeBox: function () {
        $(".pcBox,.flowerBox,.newsTipBox,.maskBox").hide();
    },
    SendSupport: function () {
        var uname = jQuery.cookie("waplogname");
        if (uname != undefined && uname != "") {
            var moneyTotal = spmymoney;
            var moneySupport = parseInt($("#pcTotal").html());
            var sendNote = $("#sendSupportNote").val();
            var clearSendNote = sendNote.replace(/[\ |\~|\`|\!|\@|\#|\$|\%|\^|\&|\*|\(|\)|\-|\_|\+|\=|\||\\|\[|\]|\{|\}|\;|\:|\"|\'|\,|\<|\.|\>|\/|\?]/g, "");
            if (sendNote == "") {
                layer.open({
                    content: '感谢您的捧场，留句话鼓励作者吧！',
                    style: BookDetail.msgStyle,
                    time: 2
                });
                return;
            }
            if (clearSendNote.length<5)
            {
                layer.open({
                    content: '评论最少5个字符！',
                    style: BookDetail.msgStyle,
                    time: 2
                });
                return;
            }
            if (moneyTotal >= moneySupport) {
                var BId = currentBId;
            }
            else {
                layer.open({
                    content: '屋币余额不足',
                    style: BookDetail.msgStyle,
                    time: 2
                });
            }
        }
        else {
            layer.open({
                content: '请先登录',
                style: BookDetail.msgStyle,
                time: 2
            });
        }
    },
    GetSupport: function (BId) {
    },
    GetFlower: function (BId) {
    },
    showNote: function (noteClass) {
        uFans.closeBox();
        $(".maskBox").show();
        var rStr = '<a class="closePopup" href="javascript:void(0);" onclick="javascript:uFans.closeBox();"></a>';
        rStr += '<div class="popupTit">';
        rStr += '	<h3>消息提示</h3>';
        rStr += '</div>';
        if (noteClass == 'pc') {
            rStr += '<div class="tipWrap suc_txt_pc">捧场作品成功！</div>';
        }
        else {
            rStr += '<div class="tipWrap suc_txt_flw">点赞作品成功！</div>';
        }
        rStr += '<div class="tc">';
        rStr += '	<a href="javascript:void(0);" class="btn_red btn_sure"  onclick="javascript:uFans.closeBox();">确定</a>';
        rStr += '</div>';
        $("#showNote").html(rStr);
        $("#showNote").show();
    },
    formatDateTime: function (now) {
        if (now != null && now != "") {
            var dateN = new Date(+/\d+/.exec(now)[0]);
            var year = dateN.getFullYear();
            var month = dateN.getMonth() + 1;
            var date = dateN.getDate();
            var hour = dateN.getHours();
            var minute = dateN.getMinutes();
            var second = dateN.getSeconds();
            minute = parseInt(minute) < 10 ? "0" + minute : minute;

            if (hour == 0 && minute == 0 && second == 0) {
                return year + "-" + month + "-" + date;
            }
            else {
                return month + "-" + date + "   " + hour + ":" + minute;
            }
        }
        else {
            return "";
        }
    }
}