$(function () {
    initCharts("cpu", 'cpuChart', 'cpu使用率', '时间', '使用率（%）');
    initCharts("memory", 'memoryChart', '内存使用率', '时间', '使用率（%）');
    initCharts("disk", 'diskChart', '磁盘使用率', '时间', '使用率（%）');
    initPies('cpu', 'cpuPie', '当前cpu使用率（%）');
    initPies('memory', 'memoryPie', '当前内存使用率（%）');
    initPies('disk', 'diskPie', '当前磁盘使用率（%）');
    refresh();
    interval = setInterval(refresh, 30000);
});

var interval;

var charts = {
    cpu: {},
    memory: {},
    disk: {}
};

var pies = {
    cpu: {},
    memory: {},
    disk: {}
};

function stopInterval() {
    clearInterval(interval);
    console.log("clear interval.")
}

function refresh() {
    showPiesLoading();
    showChartsLoading();
    methods.infos();
    hideChartsLoading();
    methods.latestInfo();
    hidePiesLoading();
}

function initCharts(chart, chartId, chartName, xName, yName) {
    charts[chart] = echarts.init(document.getElementById(chartId));
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
            type: 'value'
        },
        series: [
            {
                name: chartName,
                type: 'line',
                smooth: true,
                symbol: 'none',
                areaStyle: {
                    normal: {}
                },
                data: []
            }
        ]
    };
    charts[chart].setOption(option);
}

function initPies(pie, pieId, pieName) {
    pies[pie] = echarts.init(document.getElementById(pieId));
    var option = {
        title: {
            text: pieName,
            x: 'center'
        },
        tooltip: {
            show: true
        },
        series: [{
            type: 'pie',
            radius: '55%',
            data: []
        }]
    };
    pies[pie].setOption(option);
}

function setPie(pie, dataArr) {
    pie.setOption({
        series: [{
            data: dataArr
        }]
    });
}

function setPiesData(record) {
    var cpuUsage = record.cpuUsage;
    var diskUsage = record.diskUsage;
    var memoryUsage = record.memoryUsage;
    setPie(pies["cpu"], [{
        value: cpuUsage,
        name: "已使用"
    }, {
        value: 100 - cpuUsage,
        name: "空闲"
    }]);
    setPie(pies["memory"], [{
        value: memoryUsage,
        name: "已使用"
    }, {
        value: 100 - memoryUsage,
        name: "空闲"
    }]);
    setPie(pies["disk"], [{
        value: diskUsage,
        name: "已使用"
    }, {
        value: 100 - diskUsage,
        name: "空闲"
    }]);
}

function showChartsLoading() {
    charts["cpu"].showLoading();
    charts["memory"].showLoading();
    charts["disk"].showLoading();
}

function hideChartsLoading() {
    charts["cpu"].hideLoading();
    charts["memory"].hideLoading();
    charts["disk"].hideLoading();
}

function showPiesLoading() {
    pies["cpu"].showLoading();
    pies["memory"].showLoading();
    pies["disk"].showLoading();
}

function hidePiesLoading() {
    pies["cpu"].hideLoading();
    pies["memory"].hideLoading();
    pies["disk"].hideLoading();
}

function getFormatTime(time) {
    return time < 10 ? "0" + time : time;
}

function setChartsData(records) {
    var revRecords = records.reverse();
    console.log("reverse array:", revRecords);
    var cpuArr = [];
    var memoryArr = [];
    var diskArr = [];
    var time = [];
    revRecords.forEach(function (record) {
        var createTime = new Date(record.createTime.replace(/-/g, "/"));
        var formatTime = [getFormatTime(createTime.getHours()), getFormatTime(createTime.getMinutes()), getFormatTime(createTime.getSeconds())].join(":");
        time.push(formatTime);
        cpuArr.push(record.cpuUsage);
        memoryArr.push(record.memoryUsage);
        diskArr.push(record.diskUsage);
    });
    setChart(charts.cpu, time, cpuArr);
    setChart(charts.memory, time, memoryArr);
    setChart(charts.disk, time, diskArr);
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

var methods = {
    infos: function () {
        $.ajax({
            type: "GET",
            data: {
                page: 1,
                pageSize: 20
            },
            url: "/gu/system/infos",
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    stopInterval();
                    return;
                }
                var records = jsonResult.data;
                console.log(records);
                setChartsData(records);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(textStatus);
                toastr.error(errorThrown);
                stopInterval();
            }
        });
    },
    latestInfo: function () {
        $.ajax({
            type: "GET",
            url: "/gu/system/latestInfo",
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    stopInterval();
                    return;
                }
                var record = jsonResult.data;
                setPiesData(record);
                console.log(record);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(textStatus);
                toastr.error(errorThrown);
                stopInterval();
            }
        });
    }
};