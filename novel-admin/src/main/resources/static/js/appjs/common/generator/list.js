var prefix = "/common/generator"
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
						showRefresh : false,
						showToggle : false,
						showColumns : true,
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						// queryParamsType : "limit",
						// //设置为limit则会发送符合RESTFull格式的参数
						singleSelect : false, // 设置为true将禁止多选
						// contentType : "application/x-www-form-urlencoded",
						// //发送到服务器的数据编码类型
						pageSize : 10, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						search : false, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "client", // 设置在哪里进行分页，可选值为"client" 或者
						// "server"
						// queryParams : queryParams,
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						queryParams : function(params) {
							return {
								// 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit : params.limit,
								offset : params.offset,
								tableName : $('#tableName').val(),
							};
						},
						columns : [
								{
									checkbox : true
								},
								{
									field : 'tableName', // 列字段名
									title : '表名称' // 列标题
								},
								{
									field : 'engine',
									title : 'engine'
								},
								{
									field : 'tableComment',
									title : '表描述'
								},
								{
									field : 'createTime',
									title : '创建时间'
								},
								{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										/*var d = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="在线下载代码" onclick="downloadCode(\''
												+ row.tableName
												+ '\')"><i class="fa fa-cloud-download"></i></a> ';*/
										var g = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="本地生成代码" onclick="columnEdit(\''
											+ row.tableName
											+ '\')"><i class="fa fa-bug"></i></a> ';

										return g;
									}
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
function downloadCode(tableName) {
	location.href = prefix + "/downLoadCode/" + tableName;
}
function genCode(tableName) {
	layer.confirm('确定要在本地项目根路径下生成选中记录的代码？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/genCode",
			type : "post",
			data : {
				'tableName' : tableName
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})
}
function batchDownload() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要生成代码的表");
		return;
	}
	var tables = new Array();
	// 遍历所有选择的行数据，取每条数据对应的ID
	$.each(rows, function(i, row) {
		tables[i] = row['tableName'];
	});
	location.href = prefix + "/batchDownload?tables=" + JSON.stringify(tables).replace('[','%5B').replace(']','%5D');
}

function batchCode() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要生成代码的表");
		return;
	}
	var tables = new Array();
	// 遍历所有选择的行数据，取每条数据对应的ID
	$.each(rows, function(i, row) {
		tables[i] = row['tableName'];
	});
	layer.confirm('确定要在本地项目根路径下批量生成选中记录的代码？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/batchCode",
			type : "post",
			data : {
				'tables' : JSON.stringify(tables)
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})

}

function edit(){
	console.log('打开配置页面');
	layer.open({
		type : 2,
		title : '生成配置',
		maxmin : true,
		shadeClose : false, 
		area : [ '800px', '520px' ],
		content : prefix + '/edit'
	});
}

function columnEdit(tableName){
	layer.open({
		type : 2,
		title : '列配置',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/genColumns?tableName='+tableName
	});
}