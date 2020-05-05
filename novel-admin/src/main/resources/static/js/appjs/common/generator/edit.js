// 以下为官方示例
$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		console.log('提交修改');
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/common/generator/update",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("网络连接超时");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg(data.msg);

			} else {
				parent.layer.msg(data.msg);
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			author : {
				required : true
			},
			email : {
				required : true,
			},
			package : {
				required : true,
			},
			
		},
		messages : {

			author : {
				required : icon + "请输入作者"
			},
			email : {
				required : icon + "请输入email",
			},
			package : {
				required : icon + "请输入包名",
			},
		}
	})
}
