/*! editTable v0.2.0 by Alessandro Benoit */
(function ($, window, i) {

    'use strict';

    $.fn.editTable = function (options) {

        // Settings
        var s = $.extend({
                data: [['']],
                tableClass: 'inputtable',
                jsonData: false,
                headerCols: false,
                maxRows: 999,
                first_row: true,
                row_template: false,
                field_templates: false,
                validate_field: function (col_id, value, col_type, $element) {
                    return true;
                }
            }, options),
            $el = $(this),
            defaultTableContent = '<thead><tr></tr></thead><tbody></tbody>',
            $table = $('<table/>', {
                class: s.tableClass + ((s.first_row) ? ' wh' : ''),
                html: defaultTableContent
            }),
            defaultth = '',//'<th><a class="addcol icon-button" href="#">+</a> <a class="delcol icon-button" href="#">-</a></th>',
            colnumber,
            rownumber,
            reset,
            is_validated = true;

        // Increment for IDs
        i = i + 1;

        // Build cell
        function buildCell(content, type) {
            content = (content === 0) ? "0" : (content || '');
            // Custom type
            if (type && 'text' !== type){
                var field = s.field_templates[type];
                return '<td>' + field.setValue(field.html, content)[0].outerHTML + '</td>';
            }
            // Default
            return '<td><input type="text" value="' + content.toString().replace(/"/g, "&quot;") + '" /></td>';
        }

        // Build row
        function buildRow(data, len) {

            var rowcontent = '', b;

            data = data || '';

            if (!s.row_template) {
                // Without row template
                for (b = 0; b < (len || data.length); b += 1) {
                    rowcontent += buildCell(data[b]);
                }
            } else {
                // With row template
                for (b = 0; b < s.row_template.length; b += 1) {
                    // For each field in the row
                    rowcontent += buildCell(data[b], s.row_template[b]);
                }
            }

            return $('<tr/>', {
                html: rowcontent //+ '<td><a class="addrow icon-button" href="#">+</a> <a class="delrow icon-button" href="#">-</a></td>'
            });

        }

        // Check button status (enable/disabled)
        function checkButtons() {
            if (colnumber < 2) {
                $table.find('.delcol').addClass('disabled');
            }
            if (rownumber < 2) {
                $table.find('.delrow').addClass('disabled');
            }
            if (s.maxRows && rownumber === s.maxRows) {
                $table.find('.addrow').addClass('disabled');
            }
        }

        // Fill table with data
        function fillTableData(data) {

            var a, crow = Math.min(s.maxRows, data.length);

            // Clear table
            $table.html(defaultTableContent);

            // If headers or row_template are set
            if (s.headerCols || s.row_template) {

                // Fixed columns
                var col = s.headerCols || s.row_template;

                // Table headers
                for (a = 0; a < col.length; a += 1) {
                    var col_title = s.headerCols[a] || '';
                    $table.find('thead tr').append('<th>' + col_title + '</th>');
                }

                // Table content
                for (a = 0; a < crow; a += 1) {
                    // For each row in data
                    buildRow(data[a], col.length).appendTo($table.find('tbody'));
                }

            } else if ( data[0] ) {

                // Variable columns
                for (a = 0; a < data[0].length; a += 1) {
                    $table.find('thead tr').append(defaultth);
                }

                for (a = 0; a < crow; a += 1) {
                    buildRow(data[a]).appendTo($table.find('tbody'));
                }

            }

            // Append missing th
            $table.find('thead tr').append('<th></th>');

            // Count rows and columns
            colnumber = $table.find('thead th').length - 1;
            rownumber = $table.find('tbody tr').length;

            checkButtons();
        }

        // Export data
        function exportData() {
            var row = 0, data = [], value;

            is_validated = true;

            $table.find('tbody tr').each(function () {

                row += 1;
                data[row] = [];

                $(this).find('td:not(:last-child)').each(function (i, v) {
                    if ( s.row_template && 'text' !== s.row_template[i] ){
                        var field = s.field_templates[s.row_template[i]],
                            el = $(this).find($(field.html).prop('tagName'));
                        
                        value = field.getValue(el);
                        if ( !s.validate_field(i, value, s.row_template[i], el) ){
                            is_validated = false;
                        }
                        data[row].push(value);
                    } else {
                        value = $(this).find('input[type="text"]').val();
                        if ( !s.validate_field(i, value, 'text', v) ){
                            is_validated = false;
                        }
                        data[row].push(value);
                    }
                });
                
            });

            // Remove undefined
            data.splice(0, 1);

            return data;
        }

        // Fill the table with data from textarea or given properties
        if ($el.is('textarea')) {

            try {
                reset = JSON.parse($el.val());
            } catch (e) {
                reset = s.data;
            }

            $el.after($table);

            // If inside a form set the textarea content on submit
            if ($table.parents('form').length > 0) {
                $table.parents('form').submit(function () {
                    $el.val(JSON.stringify(exportData()));
                });
            }

        } else {
            reset = (JSON.parse(s.jsonData) || s.data);
            $el.append($table);
        }

        fillTableData(reset);

        // Add column
        $table.on('click', '.addcol', function () {

            var colid = parseInt($(this).closest('tr').children().index($(this).parent('th')), 10);

            colnumber += 1;

            $table.find('thead tr').find('th:eq(' + colid + ')').after(defaultth);

            $table.find('tbody tr').each(function () {
                $(this).find('td:eq(' + colid + ')').after(buildCell());
            });

            $table.find('.delcol').removeClass('disabled');

            return false;
        });

        // Remove column
        $table.on('click', '.delcol', function () {

            if ($(this).hasClass('disabled')) {
                return false;
            }

            var colid = parseInt($(this).closest('tr').children().index($(this).parent('th')), 10);

            colnumber -= 1;

            checkButtons();

            $(this).parent('th').remove();

            $table.find('tbody tr').each(function () {
                $(this).find('td:eq(' + colid + ')').remove();
            });

            return false;
        });

        // Add row
        $table.on('click', '.addrow', function () {

            if ($(this).hasClass('disabled')) {
                return false;
            }

            rownumber += 1;

            $(this).closest('tr').after(buildRow(0, colnumber));

            $table.find('.delrow').removeClass('disabled');

            checkButtons();

            return false;
        });

        // Delete row
        $table.on('click', '.delrow', function () {

            if ($(this).hasClass('disabled')) {
                return false;
            }

            rownumber -= 1;

            checkButtons();

            $(this).closest('tr').remove();

            $table.find('.addrow').removeClass('disabled');

            return false;
        });

        // Select all content on click
        $table.on('click', 'input', function () {
            $(this).select();
        });

        // Return functions
        return {
            // Get an array of data
            getData: function () {
                return exportData();
            },
            // Get the JSON rappresentation of data
            getJsonData: function () {
                return JSON.stringify(exportData());
            },
            // Load an array of data
            loadData: function (data) {
                fillTableData(data);
            },
            // Load a JSON rappresentation of data
            loadJsonData: function (data) {
                fillTableData(JSON.parse(data));
            },
            // Reset data to the first instance
            reset: function () {
                fillTableData(reset);
            },
            isValidated: function () {
                return is_validated;
            }
        };
    };

})(jQuery, this, 0);