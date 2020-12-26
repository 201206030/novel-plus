var SCYC = {
}

$.extend($.fn.validatebox.defaults.rules, {
    checkPenName: {
        validator: function (value, param) {
            var url = "/author/checkPenName";
            var data = { penName: value};
            var bool = false;
            $.ajax({
                type: "get",
                dataType: 'json',
                async: false,
                url: url,
                data: data,
                cache: false,
                success: function (result) {
                    if (result.data) {
                        $.fn.validatebox.defaults.rules.checkPenName.message = '笔名已存在,请重新输入';
                        bool = false;
                    } else {
                        $.fn.validatebox.defaults.rules.checkPenName.message = '';
                        bool = true;
                    }
                }
            });
            return bool;
            message: '';
        }
    }

});