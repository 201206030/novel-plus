/*
 * 后台管理通用CRUD框架
 * 依赖：jQuery 1.8 / layui（layer、laypage）
 * 对接统一端点约定：
 *   GET  {api}/list   参数 curr/limit + 搜索参数 + 固定参数，返回 RestResult<PageBean>
 *   GET  {api}/get    参数 id
 *   POST {api}/save   表单字段
 *   POST {api}/update 表单字段（含 id）
 *   POST {api}/remove 参数 id
 *   POST {api}/batchRemove 参数 ids（逗号分隔）
 */
var Admin = (function ($) {
    'use strict';

    var state = {curr: 1, limit: 10, cfg: null, keyword: '', filterVals: {}};

    /* ---------------- 基础工具 ---------------- */

    function escapeHtml(v) {
        if (v === null || v === undefined) {
            return '';
        }
        return String(v).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;').replace(/'/g, '&#39;');
    }

    // 后端 Date 序列化可能是 "2026-10-20T12:00:00.000+08:00" 或 "2026-10-20 12:00:00"
    function fmtDate(v) {
        if (!v) {
            return '';
        }
        v = String(v);
        if (v.indexOf('T') > -1) {
            // ISO 串带时区信息，先转成本地时间再截，避免直接截断显示成 UTC（差 8 小时）
            var d = new Date(v);
            if (!isNaN(d.getTime())) {
                function pad(n) {
                    return n < 10 ? '0' + n : '' + n;
                }
                return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' +
                    pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds());
            }
            v = v.replace('T', ' ').substring(0, 19);
        }
        return v;
    }

    function q(name) {
        var m = location.search.match(new RegExp('[?&]' + name + '=([^&]*)'));
        return m ? decodeURIComponent(m[1]) : '';
    }

    function handle(data, ok) {
        if (data.code == 200) {
            ok(data.data);
        } else if (data.code == 1001) {
            //未登录
            location.href = '/login.html';
        } else {
            layer.alert(data.msg || '操作失败');
        }
    }

    function get(url, params, ok) {
        $.ajax({type: 'GET', url: url, data: params, dataType: 'json',
            success: function (d) { handle(d, ok); },
            error: function () { layer.alert('网络异常'); }});
    }

    function post(url, params, ok) {
        $.ajax({type: 'POST', url: url, data: params, dataType: 'json',
            success: function (d) { handle(d, ok); },
            error: function () { layer.alert('网络异常'); }});
    }

    /* ---------------- 表单构建 ---------------- */

    function selectOptionsHtml(options, value) {
        var html = '';
        for (var i = 0; i < options.length; i++) {
            var ov = String(options[i][0]), ot = options[i][1];
            html += '<option value="' + escapeHtml(ov) + '"' + (String(value) === ov ? ' selected' : '') + '>' + escapeHtml(ot) + '</option>';
        }
        return html;
    }

    // 字段渲染，editData 为编辑时回填数据（新增时为 null）
    function fieldHtml(field, editData) {
        if (field.type === 'hidden') {
            var hv = editData && editData[field.name] !== undefined ? editData[field.name] : (field.defaultValue || '');
            return '<input type="hidden" name="' + field.name + '" value="' + escapeHtml(hv) + '"/>';
        }
        var v = editData && editData[field.name] !== undefined && editData[field.name] !== null ? editData[field.name] : '';
        if (editData === null && !field.requiredWhenAddOnly) {
            v = field.defaultValue !== undefined && field.defaultValue !== null ? field.defaultValue : '';
        }
        var label = escapeHtml(field.label || '');
        var req = field.required ? ' data-required="1"' : '';
        // lockOnEdit：编辑时该字段只读不可修改（提交时仍回传原值）
        var lock = !!editData && !!field.lockOnEdit;
        var dis = lock ? ' disabled' : '';
        var tip = field.tip ? '<p class="form_tip">' + escapeHtml(field.tip) + '</p>' : '';
        if (lock) {
            tip += '<p class="form_tip">编辑时不可修改</p>';
        }
        var inner = '';
        if (field.type === 'textarea') {
            inner = '<textarea class="form_input" name="' + field.name + '" placeholder="' + escapeHtml(field.placeholder || '') + '"' + req + dis + '>' + escapeHtml(v) + '</textarea>';
        } else if (field.type === 'rich') {
            // wangEditor 富文本：容器内渲染工具栏+编辑区，隐藏 textarea 在提交前同步 HTML（v 为原始HTML）
            inner = '<div class="rich_box" data-lock="' + (lock ? '1' : '0') + '" data-raw="' + escapeHtml(v) + '">' +
                '<div class="rich_toolbar"></div>' +
                '<div class="rich_editor"></div>' +
                '</div>' +
                '<textarea class="rich_store" name="' + field.name + '" style="display:none"' + req + '></textarea>';
        } else if (field.type === 'select') {
            var opts = field._opts || field.options || [];
            // 动态加载的下拉加一个占位选项，静态选项保持原样
            var pre = field._opts ? ('<option value="">' + escapeHtml(field.placeholder || '请选择') + '</option>') : '';
            inner = '<select class="form_input" name="' + field.name + '"' + req + dis + '>' + pre + selectOptionsHtml(opts, v) + '</select>';
        } else if (field.type === 'remote') {
            // 下拉选择远程数据（如选书籍/类别）；数据量少时可用 searchable:false 去掉检索输入框
            var kw = field.searchable === false ? '' :
                '<input type="text" class="form_input remote_kw" placeholder="输入' + escapeHtml(field.searchLabel || '关键字') + '回车检索下方列表"' + dis + '/>';
            inner = kw +
                '<select class="form_input remote_sel" name="' + field.name + '" data-url="' + field.url + '"' +
                ' data-value-field="' + (field.valueField || 'id') + '" data-label-fields="' + (field.labelFields || '') + '"' +
                ' data-current="' + escapeHtml(v) + '"' + req + dis + '></select>';
        } else if (field.type === 'date') {
            // 用 laydate 日期控件选择，禁止手动输入；dateType 可选 date/datetime（默认 datetime）
            inner = '<input type="text" class="form_input a_date" readonly name="' + field.name + '" value="' + escapeHtml(fmtDate(v)) + '"' +
                ' data-datatype="' + (field.dateType || 'datetime') + '"' +
                ' placeholder="' + escapeHtml(field.placeholder || '请选择') + '"' + req + dis + '/>';
        } else {
            var itype = field.type === 'number' ? 'number' : 'text';
            inner = '<input type="' + itype + '" class="form_input" name="' + field.name + '" value="' + escapeHtml(v) + '"' +
                ' placeholder="' + escapeHtml(field.placeholder || '') + '"' + req + dis + '/>';
        }
        return '<div class="form_row"><label class="form_label">' + label + (field.required ? ' *' : '') + '</label>' +
            '<div class="form_cell">' + inner + tip + '</div></div>';
    }

    // 页面初始化时一次性预取动态下拉选项（type:'select' + optionsUrl），存到 field._opts
    function loadOptions(cfg, done) {
        var fields = cfg.fields || [];
        var pending = 0;
        var scanDone = false;
        function finish() {
            if (scanDone && pending === 0) {
                done();
            }
        }
        for (var i = 0; i < fields.length; i++) {
            (function (field) {
                if (field.type === 'select' && field.optionsUrl) {
                    pending++;
                    get(field.optionsUrl, {}, function (data) {
                        var list = data && data.list ? data.list : data;
                        var opts = [];
                        for (var j = 0; j < list.length; j++) {
                            opts.push([String(list[j][field.optionValue || 'id']), list[j][field.optionLabel || 'name']]);
                        }
                        field._opts = opts;
                        pending--;
                        finish();
                    });
                }
            })(fields[i]);
        }
        scanDone = true;
        finish();
    }

    // 加载 remote 下拉选项，并保持当前选中值
    function loadRemote($sel, keyword, keepValue, cb) {
        var params = {};
        var keyName = $sel.attr('data-key-param') || 'keyword';
        if (keyword) {
            params[keyName] = keyword;
        }
        get($sel.attr('data-url'), params, function (data) {
            var list = data && data.list ? data.list : data;
            var valueField = $sel.attr('data-value-field');
            var labelFields = ($sel.attr('data-label-fields') || '').split(',');
            var html = '<option value="">请选择</option>';
            for (var i = 0; i < list.length; i++) {
                var text = '';
                for (var j = 0; j < labelFields.length; j++) {
                    if (labelFields[j]) {
                        text += (list[i][labelFields[j]] || '') + ' ';
                    }
                }
                html += '<option value="' + escapeHtml(list[i][valueField]) + '">' + escapeHtml($.trim(text)) + '</option>';
            }
            $sel.html(html);
            var cur = keepValue !== undefined && keepValue !== null ? String(keepValue) : $sel.val();
            if (cur && $sel.find('option[value="' + cur + '"]').length === 0) {
                $sel.prepend('<option value="' + escapeHtml(cur) + '" selected>ID:' + escapeHtml(cur) + '</option>');
            } else {
                $sel.val(cur);
            }
            if (cb) {
                cb();
            }
        });
    }

    function bindRemote($box) {
        $box.find('.remote_kw').keydown(function (e) {
            if (e.which === 13) {
                e.preventDefault();
                var $kw = $(this);
                loadRemote($kw.siblings('.remote_sel'), $kw.val(), null);
                return false;
            }
        });
        $box.find('.remote_sel').each(function () {
            var $sel = $(this);
            loadRemote($sel, '', $sel.attr('data-current') || null);
        });
    }

    // 给 $box 内的日期字段绑定 layui 日期控件（点击选择，禁止手动输入）
    function bindDate($box) {
        if (!window.layui || !layui.laydate) {
            return;
        }
        $box.find('.a_date').each(function () {
            var $el = $(this);
            if ($el.attr('data-inited') === '1') {
                return;
            }
            $el.attr('data-inited', '1');
            layui.laydate.render({
                elem: this,
                type: $el.attr('data-datatype') || 'datetime',
                trigger: 'click'
            });
        });
    }

    /* ---------------- 富文本（wangEditor） ---------------- */

    // 初始化 $box 内所有 .rich_box，返回编辑器实例数组（页面需持有以便提交前同步/关闭时销毁）
    function initRich($box) {
        var editors = [];
        if (!window.wangEditor) {
            // CDN 加载失败兜底：直接提交原始 HTML
            $box.find('.rich_store').each(function () {
                $(this).val($(this).prev('.rich_box').attr('data-raw') || '');
            });
            return editors;
        }
        var E = window.wangEditor;
        $box.find('.rich_box').each(function () {
            var $r = $(this);
            // v5 配置需在 createEditor 时传入，图片上传配置在 MENU_CONF.uploadImage
            var editor = E.createEditor({
                selector: $r.find('.rich_editor')[0],
                html: $r.attr('data-raw') || '',
                mode: 'default',
                config: {
                    placeholder: '请输入内容…',
                    MENU_CONF: {
                        uploadImage: {
                            server: '/file/upload',
                            fieldName: 'wangeditor-file',
                            maxFileSize: 5 * 1024 * 1024
                        }
                    }
                }
            });
            E.createToolbar({
                editor: editor,
                selector: $r.find('.rich_toolbar')[0],
                mode: 'default',
                excludeKeys: ['group-video', 'fullScreen']
            });
            if ($r.attr('data-lock') === '1') {
                editor.disable();
            }
            editor._store = $r.next('.rich_store');
            editors.push(editor);
        });
        return editors;
    }

    // 提交前把编辑器内容同步到隐藏 textarea
    function syncRich(editors) {
        for (var i = 0; i < editors.length; i++) {
            var ed = editors[i];
            ed._store.val(ed.isEmpty() ? '' : ed.getHtml());
        }
    }

    function destroyRich(editors) {
        for (var i = 0; i < editors.length; i++) {
            try {
                editors[i].destroy();
            } catch (e) { /* 弹层已移除时忽略 */ }
        }
    }

    // 收集表单数据并提交
    function submitForm($box, url, ok) {
        syncRich($box.data('richEditors') || []);
        var params = {};
        var valid = true;
        $box.find('[name]').each(function () {
            var $el = $(this);
            var val = $el.val() === undefined ? '' : String($el.val());
            if ($el.attr('data-required') === '1' && $.trim(val) === '') {
                layer.alert('「' + $el.closest('.form_row').find('.form_label').text().replace('*', '') + '」不能为空');
                valid = false;
                return false;
            }
            if ($el.hasClass('remote_kw')) {
                return;
            }
            params[$el.attr('name')] = val;
        });
        if (!valid) {
            return;
        }
        post(url, params, ok);
    }

    /* ---------------- CRUD 主框架 ---------------- */

    function listParams() {
        var cfg = state.cfg;
        var params = {curr: state.curr, limit: state.limit};
        if (cfg.searchKey !== false) {
            params[cfg.searchKey || 'keyword'] = state.keyword;
        }
        for (var i = 0; i < (cfg.filters || []).length; i++) {
            var f = cfg.filters[i];
            if (state.filterVals[f.param] !== undefined && state.filterVals[f.param] !== '') {
                params[f.param] = state.filterVals[f.param];
            }
        }
        if (cfg.params) {
            for (var k in cfg.params) {
                if (cfg.params.hasOwnProperty(k) && cfg.params[k] !== '' && cfg.params[k] !== null && cfg.params[k] !== undefined) {
                    params[k] = cfg.params[k];
                }
            }
        }
        return params;
    }

    function rowActionHtml(row, index) {
        var cfg = state.cfg;
        var html = '';
        if (cfg.hasEdit !== false && cfg.fields) {
            html += '<a href="javascript:;" class="a_edit" data-id="' + escapeHtml(row.id) + '" data-index="' + index + '">修改</a> ';
        }
        for (var i = 0; i < (cfg.rowActions || []).length; i++) {
            var action = cfg.rowActions[i];
            var show = action.show ? action.show(row) : true;
            if (show) {
                html += '<a href="javascript:;" class="a_action" data-act="' + i + '" data-index="' + index + '">' + escapeHtml(action.title) + '</a> ';
            }
        }
        if (cfg.hasDel !== false) {
            html += '<a href="javascript:;" class="a_del" data-id="' + escapeHtml(row.id) + '">删除</a>';
        }
        return html;
    }

    function renderTable(list) {
        var cfg = state.cfg;
        // 是否有操作列（修改/删除/自定义行操作）；全部关闭时整列隐藏（只读页）
        var hasActions = cfg.hasEdit !== false || cfg.hasDel !== false || !!cfg.rowActions;
        var headHtml = '';
        if (cfg.selectable) {
            headHtml += '<th style="width:36px;"><input type="checkbox" id="aChkAll"/></th>';
        }
        headHtml += '<th class="style">序号</th>';
        for (var i = 0; i < cfg.columns.length; i++) {
            headHtml += '<th class="' + (cfg.columns[i].thClass || 'name') + '">' + escapeHtml(cfg.columns[i].title) + '</th>';
        }
        if (hasActions) {
            headHtml += '<th class="goread">操作</th>';
        }

        var bodyHtml = '';
        for (var r = 0; r < list.length; r++) {
            var row = list[r];
            bodyHtml += '<tr class="book_list">';
            if (cfg.selectable) {
                bodyHtml += '<td><input type="checkbox" class="a_chk" value="' + escapeHtml(row.id) + '"/></td>';
            }
            bodyHtml += '<td class="style bookclass">[' + ((state.curr - 1) * state.limit + r + 1) + ']</td>';
            for (var c = 0; c < cfg.columns.length; c++) {
                var col = cfg.columns[c];
                var cell = col.render ? col.render(row, c) : escapeHtml(row[col.field]);
                if (col.ellipsis) {
                    // 单行显示，超出省略号，title 悬停显示全文
                    var tip = col.render ? String(cell).replace(/<[^>]*>/g, '') : String(row[col.field] === undefined || row[col.field] === null ? '' : row[col.field]);
                    cell = '<span class="cell-ell" title="' + escapeHtml(tip) + '">' + cell + '</span>';
                }
                bodyHtml += '<td class="' + (col.thClass || 'name') + '">' + cell + '</td>';
            }
            if (hasActions) {
                bodyHtml += '<td class="goread">' + rowActionHtml(row, r) + '</td>';
            }
            bodyHtml += '</tr>';
        }
        if (list.length === 0) {
            var colCount = cfg.columns.length + 1 + (cfg.selectable ? 1 : 0) + (hasActions ? 1 : 0);
            bodyHtml = '<tr><td colspan="' + colCount + '" style="text-align:center;color:#999;padding:20px;">暂无数据</td></tr>';
        }
        $('#aHeadRow').html(headHtml);
        $('#aBody').html(bodyHtml);
        bindTableEvents(list);
    }

    function bindTableEvents(list) {
        var cfg = state.cfg;
        $('#aChkAll').click(function () {
            $('.a_chk').prop('checked', $(this).prop('checked'));
        });
        $('#aBody .a_edit').click(function () {
            var id = $(this).attr('data-id');
            if (cfg.inlineEdit) {
                // 无 get 端点时直接用行数据回填表单
                showForm(list[parseInt($(this).attr('data-index'), 10)], true);
            } else {
                openForm(id);
            }
        });
        $('#aBody .a_del').click(function () {
            var id = $(this).attr('data-id');
            layer.confirm('确定删除该条记录吗？', {icon: 3, title: '提示'}, function (index) {
                layer.close(index);
                post(cfg.removeUrl || (cfg.api + '/remove'), {id: id}, function () {
                    layer.msg('删除成功');
                    search();
                });
            });
        });
        $('#aBody .a_action').click(function () {
            var action = cfg.rowActions[parseInt($(this).attr('data-act'), 10)];
            action.fn(list[parseInt($(this).attr('data-index'), 10)]);
        });
    }

    function doSearch() {
        var cfg = state.cfg;
        get(cfg.listUrl || (cfg.api + '/list'), listParams(), function (data) {
            var list = data.list || [];
            renderTable(list);
            layui.use('laypage', function () {
                var laypage = layui.laypage;
                laypage.render({
                    elem: 'shellPage',
                    count: data.total,
                    curr: data.pageNum,
                    limit: 10,
                    layout: ['count', 'prev', 'page', 'next'],
                    jump: function (obj, first) {
                        if (!first) {
                            state.curr = obj.curr;
                            state.limit = obj.limit;
                            doSearch();
                        }
                    }
                });
            });
        });
    }

    function search(resetPage) {
        if (resetPage) {
            state.curr = 1;
        }
        doSearch();
    }

    // 打开新增/编辑弹层，id 为空表示新增
    function openForm(id) {
        var cfg = state.cfg;
        if (id) {
            get(cfg.getUrl || (cfg.api + '/get'), {id: id}, function (data) {
                showForm(data, true);
            });
        } else {
            showForm(null, false);
        }
    }

    function showForm(editData, isEdit) {
        var cfg = state.cfg;
        var html = '<div class="a_form" id="aFormBox">';
        if (isEdit) {
            html += '<input type="hidden" name="id" value="' + escapeHtml(editData.id) + '"/>';
        }
        var fields = cfg.fields || [];
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            if (field.addOnly && isEdit) {
                continue;
            }
            if (field.type === 'hidden') {
                // hidden 字段：编辑用数据值，新增用固定参数/默认值
                var hv = isEdit ? editData[field.name] : (cfg.params && cfg.params[field.name] !== undefined ? cfg.params[field.name] : field.defaultValue);
                html += '<input type="hidden" name="' + field.name + '" value="' + escapeHtml(hv === undefined || hv === null ? '' : hv) + '"/>';
                continue;
            }
            html += fieldHtml(field, isEdit ? editData : null);
        }
        html += '<div class="form_btns"><a href="javascript:;" class="layui-btn" id="aFormSubmit">确 定</a>' +
            '<a href="javascript:;" class="layui-btn layui-btn-primary" id="aFormCancel">取 消</a></div></div>';

        var richEditors = [];
        var layerIndex = layer.open({
            type: 1,
            title: isEdit ? (cfg.editTitle || '编辑') : (cfg.addTitle || '新增'),
            area: [cfg.formWidth || '620px', cfg.formHeight || 'auto'],
            closeBtn: 1,
            content: html,
            success: function (layero) {
                var $box = layero.find('#aFormBox');
                bindRemote($box);
                bindDate($box);
                richEditors = initRich($box);
                $box.data('richEditors', richEditors);
                layero.find('#aFormCancel').click(function () {
                    layer.close(layerIndex);
                });
                layero.find('#aFormSubmit').click(function () {
                    var url = isEdit ? (cfg.updateUrl || (cfg.api + '/update')) : (cfg.saveUrl || (cfg.api + '/save'));
                    submitForm($box, url, function () {
                        layer.close(layerIndex);
                        layer.msg(cfg.saveTip || '保存成功');
                        search();
                    });
                });
            },
            end: function () {
                // 弹层关闭时销毁编辑器实例，避免内存泄漏
                destroyRich(richEditors);
            }
        });
    }

    function buildPage() {
        var cfg = state.cfg;
        var html = '';
        // 标题栏
        html += '<div class="title cf"><h2 class="fl">' + escapeHtml(cfg.title || '') + '</h2>';
        if (cfg.backUrl) {
            html += '<div class="fr"><a href="' + cfg.backUrl + '" class="btn_red">返 回</a></div>';
        }
        html += '</div>';
        html += '<div id="divData" class="updateTable">';
        // 工具条
        html += '<div class="a_toolbar">';
        if (cfg.hasAdd !== false && cfg.fields) {
            html += '<a href="javascript:;" class="layui-btn layui-btn-sm a_btn_add" id="aAddBtn">' + escapeHtml(cfg.addBtnText || '新 增') + '</a>';
        }
        if (cfg.searchKey !== false) {
            html += '<input type="text" class="layui-input" id="aKeyword" placeholder="' + escapeHtml(cfg.searchPlaceholder || '输入关键字搜索') + '"/>' +
                '<a href="javascript:;" class="layui-btn layui-btn-sm a_btn_search" id="aSearchBtn">搜 索</a>';
        }
        for (var i = 0; i < (cfg.filters || []).length; i++) {
            var f = cfg.filters[i];
            html += '<select class="a_filter" data-param="' + f.param + '"><option value="">' + escapeHtml(f.label) + '：全部</option>' +
                selectOptionsHtml(f.options, '') + '</select>';
        }
        if (cfg.selectable) {
            html += '<a href="javascript:;" class="layui-btn layui-btn-sm layui-btn-danger" id="aBatchDelBtn">批量删除</a>';
        }
        if (cfg.toolbar) {
            html += cfg.toolbar;
        }
        html += '</div>';
        // 表格
        html += '<table cellpadding="0" cellspacing="0"><thead><tr id="aHeadRow"></tr></thead><tbody id="aBody"></tbody></table>';
        html += '<div class="pageBox cf" id="shellPage"></div>';
        html += '</div>';
        $('#adminApp').html(html);

        $('#aSearchBtn').click(function () {
            state.keyword = $('#aKeyword').val();
            search(true);
        });
        $('#aKeyword').keydown(function (e) {
            if (e.which === 13) {
                $('#aSearchBtn').click();
                return false;
            }
        });
        $('.a_filter').change(function () {
            state.filterVals[$(this).attr('data-param')] = $(this).val();
            search(true);
        });
        if (cfg.hasAdd !== false && cfg.fields) {
            $('#aAddBtn').click(function () {
                openForm(null);
            });
        }
        if (cfg.selectable) {
            $('#aBatchDelBtn').click(function () {
                var ids = [];
                $('.a_chk:checked').each(function () {
                    ids.push($(this).val());
                });
                if (ids.length === 0) {
                    layer.alert('请先勾选要删除的记录');
                    return;
                }
                layer.confirm('确定删除选中的 ' + ids.length + ' 条记录吗？', {icon: 3, title: '提示'}, function (index) {
                    layer.close(index);
                    post(cfg.batchRemoveUrl || (cfg.api + '/batchRemove'), {ids: ids.join(',')}, function () {
                        layer.msg('删除成功');
                        search();
                    });
                });
            });
        }
        doSearch();
    }

    /* ---------------- 对外接口 ---------------- */

    return {
        // 通用列表+增删改查页面
        crud: function (userCfg) {
            state.cfg = userCfg;
            state.curr = 1;
            state.keyword = '';
            state.filterVals = {};
            $(function () {
                loadOptions(userCfg, buildPage);
                highlightMenu();
            });
        },
        // 单记录设置表单页（如网站信息）
        formPage: function (userCfg) {
            $(function () {
                highlightMenu();
                loadOptions(userCfg, function () {
                    get(userCfg.getUrl, {}, function (data) {
                        var html = '<div class="a_form" id="aFormBox" style="padding-left:0;">';
                        var fields = userCfg.fields || [];
                        for (var i = 0; i < fields.length; i++) {
                            html += fieldHtml(fields[i], data || {});
                        }
                        html += '<div class="form_btns" style="text-align:left;padding-left:105px;"><a href="javascript:;" class="layui-btn" id="aFormSubmit">保 存</a></div></div>';
                        $('#adminApp').html(html);
                        var $fbox = $('#aFormBox');
                        bindDate($fbox);
                        $fbox.data('richEditors', initRich($fbox));
                        $('#aFormSubmit').click(function () {
                            submitForm($('#aFormBox'), userCfg.saveUrl, function () {
                                layer.msg(userCfg.saveTip || '保存成功');
                            });
                        });
                    });
                });
            });
        },
        // 手动触发某配置下的刷新
        refresh: function () {
            search();
        },
        resetSearch: function () {
            search(true);
        },
        get: get,
        post: post,
        q: q,
        fmt: fmtDate,
        escape: escapeHtml,
        layerConfirmPost: function (url, params, confirmText, okText) {
            layer.confirm(confirmText || '确定执行该操作吗？', {icon: 3, title: '提示'}, function (index) {
                layer.close(index);
                post(url, params, function () {
                    layer.msg(okText || '操作成功');
                });
            });
        }
    };

    function highlightMenu() {
        // 当前页菜单高亮
        var path = location.pathname;
        $('.my_l a').each(function () {
            if ($(this).attr('href') === path) {
                $(this).addClass('on');
            }
        });
    }
})(jQuery);
