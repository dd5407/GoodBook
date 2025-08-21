$(function () {

    $('#randomPersonNameBtn').click(function () {
        methods.random("人名", "person");
    });
});

//展示面板
function showPanel(title) {
    $('#resultPanelTitle').text(title);
    $('#resultPanel').prop('hidden', false);
}

//隐藏面板
function hiddenPanel() {
    $('#resultPanel').prop('hidden', true);
}

//将结果置入面板
function setResultOnPanel(result) {
    //清空面板
    $('#resultPanelBody').empty();

    var html = "<p class='coreMsg' style='text-align: center;font-size: large'>"
        + result.name
        + "</p>";
    //将成语写入面板
    $('#resultPanelBody').append(html);
}

function changeHideStatus() {
    //切换显示、隐藏
    $('.coreMsg').fadeToggle();
}

var methods = {
    random: function (title, type) {
        console.log("random, title: " + title + " type: " + type);
        $.ajax({
            type: "GET",
            url: "/goodbook/game/random",
            data: {
                type: type
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var result = jsonResult.data;
                console.log("random result:" + result);
                setResultOnPanel(result);
                showPanel(title);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },

};