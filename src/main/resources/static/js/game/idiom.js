$(function () {
    $('#randomIdiomBtn').click(function () {
        methods.randomIdiom();
    });

    //实时监听输入框的变化
    $('input[name=queryWord]').on("input propertychange", function () {
        var queryWord = $(this).val();
        var chineseReg = /^[\u4e00-\u9fa5]{4}$/;
        //不是拼音，且是4个字，显示下拉选项
        if (chineseReg.test(queryWord)) {
            $('#idiomIndex').show();
        } else {
            $('#idiomIndex').hide();
        }
    });

    $('#idiomLoongBtn').click(function () {
        var queryWord = $('input[name=queryWord]').val();
        var wordIndex = $('#idiomIndex').val();
        console.log(queryWord + "," + wordIndex);
        methods.idiomLoong(queryWord, wordIndex);
    });

    $('#notice').click(function () {
        $.alert({
            title: "提示",
            content: "【输入框】<br/>1.输入要接的字。<br/>2.输入要接的字的拼音。<br/>3.输入完整4字成语。<br/>" +
            "【下拉选项】<br/>1.输入4字成语时，可选择接哪个字，可接第2-4字。",
            buttons: {
                明白了: function () {
                    toastr.success("聪明！");
                },
                不明白: function () {
                    toastr.warning("你知道该问谁的~");
                }
            }
        });
    });
});

//展示面板
function showPanel(title) {
    $('#idiomPanelTitle').text(title);
    $('#idiomPanel').prop('hidden', false);
}

//隐藏面板
function hiddenPanel() {
    $('#idiomPanel').prop('hidden', true);
}

//将成语置入面板
function setIdiomOnPanel(idiom) {
    //清空面板
    $('#idiomPanelBody').empty();

    var html = "<p class='coreMsg' style='text-align: center;'>"
        + idiom.phoneticPinyin
        + "</p>"
        + "<p class='coreMsg' style='text-align: center;'>"
        + idiom.word
        + "</p>"
        + "<p class='coreMsg' style='text-align: center;'>"
        + "【释义】：" + idiom.means
        + "</p>";
    //将成语写入面板
    $('#idiomPanelBody').append(html);
}

function changeHideStatus() {
    //切换显示、隐藏
    $('.coreMsg').fadeToggle();
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
                setIdiomOnPanel(idiom);
                showPanel("转屏猜成语");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(textStatus);
                toastr.error(errorThrown);
            }
        });
    },

    idiomLoong: function (queryWord, wordIndex) {
        $.ajax({
            type: "GET",
            url: "/gu/game/idiomLoong",
            data: {
                queryWord: queryWord,
                wordIndex: wordIndex
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var idiom = jsonResult.data;
                console.log(idiom);
                setIdiomOnPanel(idiom);
                showPanel("成语接龙");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(textStatus);
                toastr.error(errorThrown);
            }
        });
    }
};