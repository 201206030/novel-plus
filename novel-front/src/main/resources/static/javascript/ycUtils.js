var SCYC = {
}

$.extend($.fn.validatebox.defaults.rules, {
    checkBookName: {
        validator: function (value, param) {
            var url = "/aspx/book/booklist.aspx";
            var data = { bid: param, bname: value, act: "getbooknamerepeat" };
            var bool = false;
            $.ajax({
                type: "post",
                dataType: 'html',
                async: false,
                url: url,
                data: data,
                cache: false,
                success: function (result) {
                    if (result == "1") {
                        $.fn.validatebox.defaults.rules.checkBookName.message = '该书名已存在,请重新输入';
                        bool = false;
                    } else {
                        $.fn.validatebox.defaults.rules.checkBookName.message = '';
                        bool = true;
                    }
                }
            });
            return bool;
            message: '';
        }
    },
    checkNiceName: {
        validator: function (value, param) {
            var url = "/author/checkPenName";
            var data = { penName: value};
            var bool = false;
            $.ajax({
                type: "post",
                dataType: 'json',
                async: false,
                url: url,
                data: data,
                cache: false,
                success: function (result) {
                    if (result.data) {
                        $.fn.validatebox.defaults.rules.checkNiceName.message = '笔名已存在,请重新输入';
                        bool = false;
                    } else {
                        $.fn.validatebox.defaults.rules.checkNiceName.message = '';
                        bool = true;
                    }
                }
            });
            return bool;
            message: '';
        }
    }

});