$(function () {
    $('#searchFrogBtn').click(function () {
        var startNum = $('#startNum').val();
        var size = $('#size').val();
        var crazyMode = $('#crazyMode').is(':checked');
        var reg = /^[1-9]\d*$/;
        if (!reg.exec(startNum)) {
            toastr.error("大佬，起始数请输入正整数！");
            return;
        }
        if (!reg.exec(size)) {
            toastr.error("大佬，数量请输入正整数！");
            return
        }

        if (crazyMode) {
            console.log("crazyMode=" + crazyMode);
            toastr.info("进入疯狂模式！只要你够虎，青蛙随你数！");
        } else if ((startNum > 1000 || size > 1000) && parseInt(startNum) + parseInt(size) <= 10000) {
            toastr.warning("大大大大佬。。。");
        }

        var params = {
            startNum: startNum,
            size: size,
            crazyMode: crazyMode
        }
        queryFrog(params);
    });
    $('#resetFrogBtn').click(function () {
        $('#startNum').val("");
        $('#size').val("");
        var params = {
            startNum: 1,
            size: 100,
            crazyMode: false
        }
        queryFrog(params);
    });
});

function queryFrog(params) {
    console.log(params);
    //移除现有列表
    $('#frogList tr').remove();
    //请求后台
    $.ajax({
        type: "GET",
        url: "/gu/game/frogCount",
        data: {
            startNum: params.startNum,
            size: params.size,
            crazyMode: params.crazyMode
        },
        success: function (jsonResult) {
            if (jsonResult.errorCode != 0) {
                toastr.error(jsonResult.msg);
                return;
            }
            var frogList = jsonResult.data;
            if (frogList.size <= 0) {
                return;
            }
            var html = "";
            frogList.forEach(function (frog) {
                html = html + "<tr>" +
                    "<td>" + frog.num + "</td>" +
                    "<td>" + frog.month + "</td>" +
                    "<td>" + frog.eye + "</td>" +
                    "<td>" + frog.leg + "</td>" +
                    "</tr>"
            });
            $('#frogList').append(html);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            toastr.error(textStatus);
            toastr.error(errorThrown);
        }
    })
}