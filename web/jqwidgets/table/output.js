/**
 * Created by d on 2017/5/9.
 * 任务输出表单js
 */
//@ sourceURL=output.js
function getWidth() {
    return $('#data').outerWidth();
}

function getHeight() {
    return $(window).height() - $('.nav-tabs').outerHeight(true) - 100;
}

function strToJson(o) {
    var tempJson = '[' + o + ']';
    return tempJson;
}

function outputTableInit(path, taskId, projectId) {
    var cellClass = function (row, dataField, cellText, rowData) {
        var cellValue = rowData[dataField];
        if (Number(cellValue) < rowData.dataSenMin) {
            return "min";
        }
        if (Number(cellValue) > rowData.dataSenMax) {
            return "max";
        }
        if (Number(cellValue) == NaN) {
            return;
        }
    }
    var newRowID = null;
    //加载数据
    var source =
        {
            dataType: "json",
            dataFields: [
                {name: "dataId", type: "number"},
                {name: "dataName", type: "string"},
                {name: "filePath", type: "string"},
                {name: "parentId", type: "number"},
                {name: "taskId", type: "number"},
                {name: "isLeaf", type: "number"},
                {name: "dataType", type: "string"},
                {name: "dataDescription", type: "string"},
                {name: "publishState", type: "string"},
                {name: "orderState", type: "string"},
                {name: "submitState", type: "string"},
                {name: "taskName", type: "string"},
                {name: "creator", type: "string"},
                {name: "createTime", type: "string"},
                {name: "projectId", type: "number"},
                {name: "creatorId", type: "number"},
                {name: "dataUnit", type: "string"},
                {name: "dataValue", type: "string"},
                {name: "dataSenMax", type: "number"},
                {name: "dataSenMin", type: "number"},
                {name: "type", type: "number"}
            ],
            hierarchy: {
                keyDataField: {name: 'dataId'},
                parentDataField: {name: 'parentId'}
            },
            id: 'dataId',
            url: path,
            addRow: function (rowID, rowData, position, parentID, commit) {
                $.get("addPrivateData.ht", rowData, function (data, status) {
                    if (status == "success") {
                        $("#treeGridOut").jqxTreeGrid('updateBoundData');
                    }
                }, "json");
                commit(true);
            },
            updateRow: function (rowID, rowData, commit) {
                $.get("updatePrivateData.ht", rowData, function (data, status) {
                    if (status == "success") {
                        $("#treeGridOut").jqxTreeGrid('updateBoundData');
                    }
                }, "json");
                commit(true);
            },
            deleteRow: function (dataId, commit) {
                $.get("delPrivateData.ht?dataId=" + dataId, dataId, function (data, status) {
                    if (status == "success") {
                        $("#treeGridOut").jqxTreeGrid('updateBoundData');
                    }
                }, "json");
                commit(true);
            }
        };
    var dataAdapter = new $.jqx.dataAdapter(source);
    // create Tree Grid
    $("#treeGridOut").jqxTreeGrid(
        {
            width: getWidth(),
            height: getHeight(),
            source: dataAdapter,
            pageable: true,
            pagerMode: 'advanced',
            showHeader: true,
            editable: true,
            pageSize: 20,
            pageSizeOptions: ['10', '20', '50', '100', '500'],
            columnsResize: true,
            showToolbar: true,
            altRows: true,
            hierarchicalCheckboxes: false,
            checkboxes: false,
            filterable: true,
            filterMode: 'advanced',
            theme: "bootstrap",
            pagerButtonsCount: 8,
            toolbarHeight: 35,
            editSettings: {
                saveOnPageChange: true,
                saveOnBlur: true,
                saveOnSelectionChange: true,
                cancelOnEsc: true,
                saveOnEnter: true,
                editSingleCell: true,
                editOnDoubleClick: true,
                editOnF2: true
            },
            ready: function () {
                // called when the DatatreeGrid is loaded.
                $("#treeGridOut").jqxTreeGrid('expandRow', '0');
                $("#treeGridOut").jqxTreeGrid('selectRow', '0');
                $("#treeGridOut").jqxTreeGrid('clearSelection');
                // focus jqxTreeGrid.
                // $("#treeGridOut").jqxTreeGrid('focus');
            },
            renderToolbar: function (toolBar) {
                var toTheme = function (className) {
                    return className;
                };
                var container = $("<div style='margin: 5px;'></div>");
                var addRow = $('<button id="addRow" type="button" style="margin-left: 5px"><span class="glyphicon glyphicon-plus"></span> 添加数据</button>');
                var delRow = $('<button id="delRow" type="button" style="margin-left: 5px"><span class="glyphicon glyphicon-trash"></span> 删除数据</button>');
                var pubRow = $('<button id="pubRow" type="button" style="margin-left: 5px"><span class="glyphicon glyphicon-ok"></span> 发布数据</button>');
                var cancelRow = $('<button id="cancelRow" type="button" style="margin-left: 5px"><span class="glyphicon glyphicon-remove"></span> 撤销发布</button>');
                var refreshTable = $('<button id="refreshTable" type="button" style="margin-left: 5px"><span class="glyphicon glyphicon-refresh"></span> 刷新表单</button>');
                container.append(addRow);
                container.append(delRow);
                container.append(pubRow);
                container.append(cancelRow);
                container.append(refreshTable);
                toolBar.append(container);

                addRow.jqxButton({
                    cursor: "pointer",
                    disabled: false
                });
                addRow.jqxTooltip({position: 'bottom', content: "点击添加一行主数据"});

                delRow.jqxButton({
                    cursor: "pointer",
                    disabled: false
                });
                delRow.jqxTooltip({position: 'bottom', content: "点击删除所选数据"});
                pubRow.jqxButton({
                    cursor: "pointer",
                    disabled: false
                });
                pubRow.jqxTooltip({position: 'bottom', content: "点击发布所选数据"});
                cancelRow.jqxButton({
                    cursor: "pointer",
                    disabled: false
                });
                cancelRow.jqxTooltip({position: 'bottom', content: "点击撤销所选发布数据"});
                refreshTable.jqxButton({
                    cursor: "pointer",
                    disabled: false
                });
                refreshTable.jqxTooltip({position: 'bottom', content: "点击刷新数据列表"});

                var updateButtons = function (action) {
                    switch (action) {
                        case "Select":
                            addRow.jqxButton({disabled: false});
                            delRow.jqxButton({disabled: false});
                            pubRow.jqxButton({disabled: false});
                            cancelRow.jqxButton({disabled: true});
                            break;
                        case "Unselect":
                            addRow.jqxButton({disabled: false});
                            delRow.jqxButton({disabled: false});
                            pubRow.jqxButton({disabled: false});
                            cancelRow.jqxButton({disabled: true});
                            break;
                        case "Edit":
                            addRow.jqxButton({disabled: false});
                            delRow.jqxButton({disabled: false});
                            pubRow.jqxButton({disabled: false});
                            cancelRow.jqxButton({disabled: true});
                            break;
                        case "End Edit":
                            addRow.jqxButton({disabled: false});
                            delRow.jqxButton({disabled: false});
                            pubRow.jqxButton({disabled: false});
                            cancelRow.jqxButton({disabled: true});
                            break;
                    }
                };
                var rowKey = null;
                $("#treeGridOut").on('rowSelect', function (event) {
                    var args = event.args;
                    rowKey = args.key;
                    updateButtons('Select');
                });
                $("#treeGridOut").on('rowUnselect', function (event) {
                    updateButtons('Unselect');
                });

                $("#treeGridOut").on('rowEndEdit', function (event) {
                    updateButtons('End Edit');
                });
                $("#treeGridOut").on('rowBeginEdit', function (event) {
                    updateButtons('Edit');
                });
                addRow.click(function (event) {
                    if (!addRow.jqxButton('disabled')) {
                        // $("#treeGridOut").jqxTreeGrid('expandRow', rowKey);
                        $("#treeGridOut").jqxTreeGrid('addRow', null, {
                            type: 1,
                            dataName: "未定义数据名称",
                            taskId: taskId,
                            dataSenMax: 10000,
                            dataSenMin: 0,
                            isLeaf: 0,
                            dataType: "数值",
                            publishState: "未发布",
                            parentId: 0,
                            projectId: projectId,
                        }, 'first');
                        // add new empty row.
                        // $("#treeGridOut").jqxTreeGrid('addRow', null, {}, 'first', rowKey);
                        // select the first row and clear the selection.
                        $("#treeGridOut").jqxTreeGrid('clearSelection');
                        $("#treeGridOut").jqxTreeGrid('selectRow', newRowID);
                        // edit the new row.
                        $("#treeGridOut").jqxTreeGrid('beginRowEdit', newRowID);
                        updateButtons('add');
                    }
                });
                delRow.click(function () {
                    if (!delRow.jqxButton('disabled')) {
                        var selection = $("#treeGridOut").jqxTreeGrid('getSelection');
                        if (selection.length > 1) {
                            var keys = new Array();
                            for (var i = 0; i < selection.length; i++) {
                                keys.push($("#treeGridOut").jqxTreeGrid('getKey', selection[i]));
                            }
                            $("#treeGridOut").jqxTreeGrid('deleteRow', keys);
                        }
                        else {
                            $("#treeGridOut").jqxTreeGrid('deleteRow', rowKey);
                        }
                        updateButtons('delete');
                    }
                });
            },
            columns: [
                {text: '名称', dataField: "dataName", align: 'left', width: '25%', pinned: true, editable: true},
                {text: '类型', dataField: "dataType", align: 'left', width: '10%', columnType: "template", value: "数值"},
                {text: '最新值', dataField: "dataValue", align: 'left', width: '25%'},
                {text: '单位', dataField: "dataUnit", align: 'left', width: '10%', columnType: "template"},
                {text: '最小值', dataField: "dataSenMin", align: 'left', width: '12%'},
                {text: '最大值', dataField: "dataSenMax", align: 'left', width: '12%'},
                {text: '发布状态', dataField: "publishState", align: ' center', width: '6%', editable: false}
            ]
        });
    // create context menu
    var contextMenu = $("#Menu").jqxMenu({width: 200, height: 58, autoOpenPopup: false, mode: 'popup'});
    $("#treeGridOut").on('contextmenu', function () {
        return false;
    });
    $("#treeGridOut").on('rowClick', function (event) {
        var args = event.args;
        if (args.originalEvent.button == 2) {
            var scrollTop = $(window).scrollTop();
            var scrollLeft = $(window).scrollLeft();
            contextMenu.jqxMenu('open', parseInt(event.args.originalEvent.clientX) + 5 + scrollLeft, parseInt(event.args.originalEvent.clientY) + 5 + scrollTop);
            return false;
        }
    });
    $("#Menu").on('itemclick', function (event) {
        var args = event.args;
        var selection = $("#treeGridOut").jqxTreeGrid('getSelection');
        var rowid = selection[0].uid
        if ($.trim($(args).text()) == "添加子数据") {
            $("#treeGridOut").jqxTreeGrid('beginRowEdit', rowid);
        } else {
            $("#treeGridOut").jqxTreeGrid('deleteRow', rowid);
        }
    });
}