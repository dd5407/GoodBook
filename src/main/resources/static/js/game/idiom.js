$(function () {
    $('#randomIdiomBtn').click(function () {
        methods.randomIdiom();
    });
});

//展示面板
function showPanel(title) {
    $('#idiomPanelTitle').text(title)
    $('#idiomPanel').prop('hidden', false);
}

//隐藏面板
function hiddenPanel() {
    $('#idiomPanel').prop('hidden', true);
}

//将成语置入面板
function setRandomIdiomOnPanel(idiom) {
    //清空面板
    $('#idiomPanelBody').empty();

    var html = "<p style='text-align: center;'>"
        + idiom.phoneticPinyin
        + "</p>"
        + "<p style='text-align: center;'>"
        + idiom.word
        + "</p>"
        + "<p style='text-align: center;'>"
        + "【释义】：" + idiom.means
        + "</p>";
    //将成语写入面板
    $('#idiomPanelBody').append(html);
    showPanel("转屏猜成语");
}

var methods = {
    randomIdiom: function () {
        $.ajax({
            type: "GET",
            url: "/gu/game/randomIdiom",
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var idiom = jsonResult.data;
                console.log(idiom);
                setRandomIdiomOnPanel(idiom);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(textStatus);
                toastr.error(errorThrown);
            }
        });
    }
}