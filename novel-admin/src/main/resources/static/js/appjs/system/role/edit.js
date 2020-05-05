var menuIds;
var permIds;
$(function() {
	getMenuTreeData();
	getPermTreeData();
	validateRule();
});
$.validator.setDefaults({
	submitHandler : function() {
		getAllSelectNodes();
		update();
	}
});
function loadMenuTree(menuTree) {
	$('#menuTree').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
		'core' : {
			'data' : menuTree
		},
		"checkbox" : {
			//"keep_selected_style" : false,
			//"undetermined" : true
			//"three_state" : false,
			//"cascade" : ' up'
		}
	});
	$('#menuTree').jstree('open_all');
}
function getAllSelectNodes() {
	var ref = $('#menuTree').jstree(true); // 获得整个树
	menuIds = ref.get_selected(); // 获得所有选中节点的，返回值为数组
	$("#menuTree").find(".jstree-undetermined").each(function(i, element) {
		menuIds.push($(element).closest('.jstree-node').attr("id"));
	});
	ref = $('#dataPermTree').jstree(true); // 获得整个树

	permIds = ref.get_selected(); // 获得所有选中节点的，返回值为数组

	$("#dataPermTree").find(".jstree-undetermined").each(function(i, element) {
		permIds.push($(element).closest('.jstree-node').attr("id"));
	});
}
function getPermTreeData() {
	var roleId = $('#roleId').val();
	$.ajax({
		type : "GET",
		url : "/system/dataPerm/tree/"+roleId,
		success : function(permTree) {
			loadPermTree(permTree);
		}
	});
}
function loadPermTree(permTree) {
	$('#dataPermTree').jstree({
		'core' : {
			'data' : permTree
		},
		"checkbox" : {
			"three_state" : true,
		},
		"plugins" : [ "wholerow", "checkbox" ]
	});
	//$('#menuTree').jstree("open_all");

}
function getMenuTreeData() {
	var roleId = $('#roleId').val();
	$.ajax({
		type : "GET",
		url : "/sys/menu/tree/" + roleId,
		success : function(data) {
			loadMenuTree(data);
		}
	});
}
function update() {
	$('#menuIds').val(menuIds);
	$('#permIds').val(permIds);
	var role = $('#signupForm').serialize();
	$.ajax({
		cache : true,
		type : "POST",
		url : "/sys/role/update",
		data : role, // 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(r) {
			if (r.code == 0) {
				parent.layer.msg(r.msg);
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.msg(r.msg);
			}

		}
	});
}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			roleName : {
				required : true
			}
		},
		messages : {
			roleName : {
				required : icon + "请输入角色名"
			}
		}
	});
}