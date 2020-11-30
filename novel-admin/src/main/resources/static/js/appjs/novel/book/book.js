var prefix = "/novel/book"
$(function () {
    load();
});

function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/list", // 服务器数据的加载地址
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize: 'outline',
                toolbar: '#exampleToolbar',
                striped: true, // 设置为true会有隔行变色效果
                dataType: "json", // 服务器返回的数据类型
                pagination: true, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect: false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize: 10, // 如果设置了分页，每页数据条数
                pageNumber: 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParams: function (params) {
                    //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                    var queryParams = getFormJson("searchForm");
                    queryParams.limit = params.limit;
                    queryParams.offset = params.offset;
                    return queryParams;
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                responseHandler: function (rs) {

                    if (rs.code == 0) {
                        return rs.data;
                    } else {
                        parent.layer.alert(rs.msg)
                        return {total: 0, rows: []};
                    }
                },
                columns: [
                    {
                        checkbox: true
                    },
                    {
                        title: '序号',
                        formatter: function () {
                            return arguments[2] + 1;
                        }
                    },
                                                                        {
                                field: 'id',
                                title: '主键'
                            },

                        
                                                                        {
                                field: 'workDirection',
                                title: '作品方向，0：男频，1：女频'
                            },

                        
                                                                        {
                                field: 'catId',
                                title: '分类ID'
                            },

                        
                                                                        {
                                field: 'catName',
                                title: '分类名'
                            },

                        
                                                                        {
                                field: 'catChildId',
                                title: '子分类ID'
                            },

                        
                                                                        {
                                field: 'catChildName',
                                title: '子分类名'
                            },

                        
                                                                        {
                                field: 'picUrl',
                                title: '小说封面'
                            },

                        
                                                                        {
                                field: 'bookName',
                                title: '小说名'
                            },

                        
                                                                        {
                                field: 'heroName',
                                title: '男主角姓名'
                            },

                        
                                                                        {
                                field: 'ladyName',
                                title: '女主角姓名'
                            },

                        
                                                                        {
                                field: 'bookStyle',
                                title: '作品风格，0：甜宠，1：虐恋，2：其他'
                            },

                        
                                                                        {
                                field: 'bookLabel',
                                title: '作品标签'
                            },

                        
                                                                        {
                                field: 'authorId',
                                title: '作者id'
                            },

                        
                                                                        {
                                field: 'authorName',
                                title: '作者名'
                            },

                        
                                                                        {
                                field: 'bookDesc',
                                title: '书籍描述'
                            },

                        
                                                                        {
                                field: 'score',
                                title: '评分，预留字段'
                            },

                        
                                                                        {
                                field: 'bookStatus',
                                title: '书籍状态，0：连载中，1：已完结'
                            },

                        
                                                                        {
                                field: 'visitCount',
                                title: '点击量'
                            },

                        
                                                                        {
                                field: 'wordCount',
                                title: '总字数'
                            },

                        
                                                                        {
                                field: 'commentCount',
                                title: '评论数'
                            },

                        
                                                                        {
                                field: 'yesterdayBuy',
                                title: '昨日订阅数'
                            },

                        
                                                                        {
                                field: 'lastIndexId',
                                title: '最新目录ID'
                            },

                        
                                                                        {
                                field: 'lastIndexName',
                                title: '最新目录名'
                            },

                        
                                                                        {
                                field: 'lastIndexUpdateTime',
                                title: '最新目录更新时间'
                            },

                        
                                                                        {
                                field: 'isVip',
                                title: '是否收费，1：收费，0：免费'
                            },

                        
                                                                        {
                                field: 'status',
                                title: '状态，0：入库，1：上架'
                            },

                        
                                                                        {
                                field: 'updateTime',
                                title: '更新时间'
                            },

                        
                                                                        {
                                field: 'createTime',
                                title: '创建时间'
                            },

                        
                                                                        {
                                field: 'crawlSourceId',
                                title: '爬虫源站ID'
                            },

                        
                                                                        {
                                field: 'crawlBookId',
                                title: '抓取的源站小说ID'
                            },

                        
                                                                        {
                                field: 'crawlLastTime',
                                title: '最后一次的抓取时间'
                            },

                        
                                                                        {
                                field: 'crawlIsStop',
                                title: '是否已停止更新，0：未停止，1：已停止'
                            },

                        
                                        {
                        title: '操作',
                        field: 'id',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var d = '<a class="btn btn-primary btn-sm ' + s_detail_h + '" href="#" mce_href="#" title="详情" onclick="detail(\''
                                + row.id
                                + '\')"><i class="fa fa-file"></i></a> ';
                            var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id
                                + '\')"><i class="fa fa-edit"></i></a> ';
                            var r = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + row.id
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            return d + e + r;
                        }
                    }]
            });
}
function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}
function add() {
    layer.open({
        type: 2,
        title: '增加',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/add' // iframe的url
    });
}
function detail(id) {
    layer.open({
        type: 2,
        title: '详情',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/detail/' + id // iframe的url
    });
}
function edit(id) {
    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id // iframe的url
    });
}
function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'id': id
            },
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function resetPwd(id) {
}
function batchRemove() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {

    });
}