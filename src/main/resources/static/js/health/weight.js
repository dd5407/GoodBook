$(function () {

    refreshList();

    $('#datetimer').datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd hh:ii:ss',
        minView: 0,//最精确的时间（分钟）
        initialDate: new Date(),
        autoclose: true, //选中自动关闭
        todayBtn: true,
        clearBtn: true,
        locale: moment.locale('zh-cn')
    });

    initCharts("weight", 'weightChart', '体重变化图', '时间', '体重（kg）');

    // 添加时间范围选择器的change事件监听器
    $('#timeRangeSelectInModal').on('change', function () {
        var timeRange = $(this).val();
        methods.getWeightForChart(timeRange);
    });

    //模态框确认：新增、修改
    $('#confirm_btn').on('click', function () {
        var id = $('#weightModal input[name=id]').val();
        var weight = $('#weightModal input[name=weight]').val();
        var recordTime = $('#recordTime').val();
        if (weight === undefined || weight.trim() === '') {
            toastr.error("大佬，没有体重？");
            return;
        }
        if (recordTime === undefined || recordTime.trim() === '') {
            recordTime = moment().format('YYYY-MM-DD HH:mm:ss');
            console.log("未填写时间，使用当前时间：" + recordTime);
        }
        var item = {
            id: id,
            weight: weight.trim(),
            recordTime: recordTime.trim()
        };
        if (id == undefined || id == '') {
            methods.addItem(item);
        } else {
            methods.editItem(item);
        }
    });

    //全选复选框监听
    $('#batchCheckBox').on('click', function () {
        if ($('#batchCheckBox').prop('checked')) {
            $('#weight_tbody input[type=checkbox]').prop('checked', true);
        } else {
            $('#weight_tbody input[type=checkbox]').prop('checked', false);
        }
    });

    //重置按钮监听
    $('#reset_btn').on('click', function () {
        //清空查询框
        $('#Ktext').val("");
        //复原所有复选框
        $('input[type=checkbox]').prop('checked', false);
        //刷新列表
        refreshList();
    });

    //批量删除按钮监听
    $('#batch_del').on('click', function () {
        var ids = new Array();
        var delItemList = new Array();
        $('#weight_tbody input[type=checkbox]:checked').each(function () {
            var id = $(this).val();
            var weight = $(this).parent().parent().find('[name=weight]').text();
            ids.push(id);
            delItemList.push(weight);
        });
        if (ids.length <= 0) {
            $.alert({
                title: "大佬",
                content: "至少选一个呗！",
                buttons: {
                    好: {}
                }
            });
            return;
        }
        $.confirm({
            theme: "modern",
            title: "批量删除记录",
            content: "确认删除[" + delItemList + "]吗？",
            buttons: {
                确定: function () {
                    toastr.success("Let them go...");
                    methods.batchDelete(ids);
                },
                取消: {}
            }
        })
    });
});

var current = 1;
var pageSize = 10;
var total = 0;

function initCharts(chart, chartId, chartName, xName, yName) {
    charts[chart] = echarts.init(document.getElementById(chartId));
    //模态框不显示时，charts初始化width默认为100px。需要监听模态框显示事件，模态框显示时需要重绘图表，按模态框实际大小显示。
    $('#chartModal').on('shown.bs.modal', function () {
        charts[chart].resize();
    });

    var option = {
        title: {
            text: chartName,
            x: 'center'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            name: xName,
            data: []
        },
        yAxis: {
            boundaryGap: [0, '50%'],
            name: yName,
            type: 'value',
            min: function (value) {
                var ma = value.max;
                var mi = value.min;
                var val = Math.ceil(mi - (ma - mi) / 2);
                return val;
            },
            max: function (value) {
                var ma = value.max;
                var mi = value.min;
                var val = Math.ceil(ma + (ma - mi) / 2);
                return val;
            }
        },
        series: [
            {
                name: chartName,
                type: 'line',
                smooth: false,
                symbol: 'emptyCircle',
                areaStyle: {
                    normal: {}
                },
                data: []
            }
        ]
    };
    charts[chart].setOption(option);
}

function getFormatTime(time) {
    return time < 10 ? "0" + time : time;
}

function setChartsData(records) {
    var revRecords = records.reverse();
    console.log("reverse array:", revRecords);
    var weightArr = [];
    var time = [];
    revRecords.forEach(function (record) {
        var createTime = new Date(record.recordTime.replace(/-/g, "/"));
        var formatTime = [getFormatTime(createTime.getFullYear()), getFormatTime(createTime.getMonth() + 1), getFormatTime(createTime.getDate())].join("-");
        time.push(formatTime);
        weightArr.push(record.weight);
    });
    setChart(charts.weight, time, weightArr);
}

function setChart(chart, xData, data) {
    chart.setOption({
        xAxis: {
            data: xData
        },
        series: [{
            data: data
        }]
    });
}

function addModal() {
    $('#weightModal input[name=id]').val("")
    $('#weightModal input[name=weight]').val("");
    $('#recordTime').val("");
    $('#weightModal .modal-header label').html("添加记录");
    $('#weightModal').modal();
}

function openChart() {
    methods.getWeightForChart();
    $('#chartModal').modal();
}

function editModal(ele) {
    $('#notice').hide();
    var tr = $(ele).parent().parent();
    var item = {
        id: tr.find("[name=id]").text(),
        weight: tr.find("[name=weight]").text(),
        recordTime: tr.find("[name=recordTime]").text()
    };
    console.log(item);
    $('#weightModal input[name=id]').val(item.id);
    $('#weightModal input[name=weight]').val(item.weight);
    $('#weightModal input[name=recordTime]').val(item.recordTime);
    $('#weightModal .modal-header label').html("修改");
    //显示模态框
    $('#weightModal').modal();
}

function deleteModal(ele) {
    var tr = $(ele).parent().parent();
    var item = {
        id: tr.find("[name=id]").text(),
        weight: tr.find("[name=weight]").text()
    };
    console.log(item);
    $.confirm({
        theme: 'light',
        title: "删除记录",
        content: "选项删除后无法恢复，确认删除[体重=" + item.weight + "]的记录吗？",
        buttons: {
            确定: function () {
                methods.deleteItem(item);
            },
            取消: {}
        }
    });
}

function showWeightList(weightList, loginUser, page, pageSize) {
    var html = "";
    var index = (page - 1) * pageSize + 1;
    $('#weight_tbody').empty();
    for (var i = 0; i < weightList.length; i++) {
        var weightItem = weightList[i];
        var id = weightItem.id;
        var weight = weightItem.weight;
        var recordTime = weightItem.recordTime;
        var createTime = weightItem.createTime;
        var userName = weightItem.userName;

        html = html + "<tr>"
            + "<td><input type='checkbox' value='" + id + "'></td>"
            + "<td name='id' hidden='hidden'>" + id + "</td>"
            + "<td name='index' class='hidden-xs'>" + index + "</td>"
            + "<td name='weight'>" + weight + "</td>"
            + "<td name='recordTime'>" + recordTime + "</td>"
            + "<td name='createTime'>" + createTime + "</td>"
            + "<td name='userName'>" + userName + "</td>"
            + "<td><a href='javascript:void(0);' class='del' onclick='deleteModal(this)'>删除</a> </td>"
            + "</tr>";
        index++;
    }
    $('#weight_tbody').append(html);
}

var charts = {
    weight: {}
};

//生成分页组件
function buildPagination(page, pageSize, total) {
    var pageSum = Math.ceil(total / pageSize);
    if (pageSum === 0) {
        pageSum = 1;
    }

    $('#weightPagination').twbsPagination({
        //设置版本号
        bootstrapMajorVersion: 5,
        // 显示第几页
        currentPage: page,
        // 总页数
        totalPages: pageSum,
        visiblePages: 5,
        first: '首页',
        prev: '上一页',
        next: '下一页',
        last: '尾页 [{{total_pages}}]',
        //当单击操作按钮的时候, 执行该函数, 调用ajax渲染页面
        onPageClick: function (event, page) {
            // 把当前点击的页码赋值给currentPage, 调用ajax,渲染页面
            currentPage = page;
            //调用获取列表函数
            refreshItemList(currentPage, pageSize);
        }
    });
}

function refreshItemList(page, pageSize) {
    var params = {
        current: page,
        pageSize: pageSize
    };
    methods.getItemList(params);
}

function refreshList() {
    var params = {
        current: 1,
        pageSize: 10
    }
    methods.getItemList(params);
    //复原批量复选框
    $("#batchCheckBox").prop("checked", false);
}

var methods = {
    //展示列表
    showItemList: function (total, itemList) {
        //清空列表
        $('#weight_tbody tr').remove();
        var html = "";
        var index = 1;
        itemList.forEach(function (item) {
            html = html + "<tr>" +
                "<td><input type='checkbox' value='" + item.id + "' /></td>" +
                "<td name='id' hidden='hidden'>" + item.id + "</td>" +
                "<td name='index' class='hidden-xs'>" + (index++) + "</td>" +
                "<td name='weight'>" + item.weight + "</td>" +
                "<td name='recordTime'>" + item.recordTime + "</td>" +
                "<td name='createTime'>" + item.createTime + "</td>" +
                // "<td name='updateTime'>" + item.updateTime + "</td>" +
                "<td name='userName' class='hidden-xs'>" + item.userName + "</td>" +
                "<td>" +
                // "<a href='javascript:void(0)' class='edit' onclick='editModal(this)'>编辑</a>" +
                "<a href='javascript:void(0)' class='del' onclick='deleteModal(this)'>删除</a> " +
                "</td>"
        });
        $('#weight_tbody').append(html);
    },
    getItemList: function (params) {
        $.ajax({
            type: "GET",
            url: "/goodbook/health/weight/getRecordByUser",
            data: {
                current: params.current,
                pageSize: params.pageSize
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var itemList = jsonResult.data.data;
                var current = params.current;
                var pageSize = params.pageSize;
                var total = jsonResult.data.total;
                methods.showItemList(total, itemList);
                buildPagination(current, pageSize, total);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    getWeightForChart: function (timeRange) {
        $.ajax({
            type: "GET",
            url: "/goodbook/health/weight/getRecordByUser",
            data: {
                timeRange: timeRange
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var itemList = jsonResult.data.data;
                setChartsData(itemList);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    //添加记录
    addItem: function (item) {
        console.log(item);
        $.ajax({
            type: "POST",
            url: "/goodbook/health/weight/add",
            data: JSON.stringify(item),
            contentType: "application/json",
            success: function (jsonResult) {
                if (jsonResult.errorCode == 0) {
                    toastr.success("添加成功！");
                } else {
                    toastr.error("添加失败！" + jsonResult.msg);
                }
                //刷新列表，批量添加失败时，可能会有部分成功，所以也要刷新列表
                refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
        //隐藏模态框
        $('#weightModal').modal('hide');
    },
    //修改记录
    // editItem: function (item) {
    //     $.ajax({
    //         type: "POST",
    //         url: "/goodbook/game/lottery/updateItem",
    //         data: JSON.stringify(item),
    //         contentType: "application/json",
    //         success: function (jsonResult) {
    //             if (jsonResult.errorCode == 0) {
    //                 toastr.success("修改成功！");
    //                 //刷新列表
    //                 refreshList();
    //             } else {
    //                 toastr.error("修改失败！" + jsonResult.msg);
    //             }
    //         },
    //         error: function (jqXHR, textStatus, errorThrown) {
    //             toastr.error(jqXHR.status + ":" + jqXHR.statusText);
    //             toastr.error(textStatus);
    //         }
    //     });
    //     //隐藏模态框
    //     $('#weightModal').modal('hide');
    // },
    deleteItem: function (item) {

        $.ajax({
            type: "POST",
            url: "/goodbook/health/weight/delete/",
            data: {
                id: item.id
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode == 0) {
                    toastr.success("删除成功！");
                    //刷新列表
                    refreshList();
                } else {
                    toastr.error("删除失败！" + jsonResult.msg);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        })
    },
    batchDelete: function (ids) {
        console.log(ids);
        $.ajax({
            type: "POST",
            url: "/goodbook/health/weight/batchDelete",
            //传数组需要加此参数，不然后台会获取不到值
            traditional: true,
            data: {
                ids: ids
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode == 0) {
                    toastr.success("批量删除成功！");
                    //刷新列表
                    refreshList();
                } else {
                    toastr.error("批量删除失败！" + jsonResult.msg);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        })
    }
};
