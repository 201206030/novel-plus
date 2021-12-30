var UserUtil = {
    msgStyle: 'background-color:#333; color:#fff; text-align:center; border:none; font-size:20px; padding:10px;',
    GetFavoritesNew: function () {
        var bIdList = "";
        $(".book_list").each(function () {
            bIdList += "," + $(this).attr("vals");
        });
        if (bIdList != "") {
        }
    },
    GetHistory: function () {
        var bIdList = "";
        $(".book_list").each(function () {
            bIdList += "," + $(this).attr("vals");
        });
        if (bIdList != "") {
        }
    },
    GetChapterInfo: function () {
        var cIdList = "";
        $(".showCName").each(function () {
            cIdList += "," + $(this).attr("vals");
        });
        if (cIdList != "") {
        }
    },
    SignDay: function () {
        if (!signed) {
            signed = true;
        }
    },
    SignDayStatus: function () {
    },
    RegSendSms: function () {
        var mob = $("#txtUName").val();
        var cCode = $("#TxtChkCode").val();
        if (mob != "" && cCode != "") {
            $("#btnSendSms").attr("disabled", "disabled");
            $("#txtUName").attr("readonly", "true");
        }
        else {
            layer.open({
                content: '手机号码和验证码必须填写',
                style: UserUtil.msgStyle,
                time: 2
            });
        }
    },
    GetPassSendSms: function () {
        var mob = $("#txtMobile").val();
        var cCode = $("#TxtChkCode").val();
        if (mob != "" && cCode != "") {
            $("#btnSendSms").attr("disabled", "disabled");
            $("#txtMobile").attr("readonly", "true");
        }
        else {
            layer.open({
                content: '手机号码和验证码必须填写',
                style: UserUtil.msgStyle,
                time: 2
            });
        }
    },
    RegSmsWait: function () {
        if (secondStep > 0) {
            $("#btnSendSms").val("重新发送(" + secondStep + ")");
            secondStep--;
            setTimeout("UserUtil.RegSmsWait()", 1000);
        }
        else {
            secondStep = 180;
            $("#btnSendSms").val("重新获取验证码");
            $("#btnSendSms").removeAttr("disabled");
            $("#txtUName").removeAttr("readonly");
        }
    }
}