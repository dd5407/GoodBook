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

    //成语查询
    $('#idiomQueryBtn').click(function () {
        var queryIdiom = $('input[name=idiomQueryInput]').val();
        if (queryIdiom == undefined || queryIdiom == '') {
            toastr.error("什么都不填是不行滴！");
            return
        }
        var chineseReg = /^[\u4e00-\u9fa5]{4}$/;
        if (!chineseReg.test(queryIdiom)) {
            toastr.error("默默说一声，只能填4个字的中文！");
            return;
        }
        methods.queryIdiom(queryIdiom);
    })
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

function addModal() {
    //如果面板中有成语，自动填充到modal
    if ($('#idiomPanelBody>p').length > 0) {
        var idiom = $('#idiomPanelBody>p:nth-child(2)').text();
        console.log("Put idiom " + idiom + " into modal");
        $('input[name=guessIdiom]').val(idiom);
    }
    $('#sendIdiomModal').modal();
}

//将成语发送给别人猜
function sendIdiom() {
    //校验参数
    if (!validateModal()) {
        return;
    }

    var params = {
        toUsername: $('input[name=toUsername]').val(),
        guessIdiom: $('input[name=guessIdiom]').val()
    };
    //发送请求
    methods.sendIdiom(params);
}

//校验模态框
function validateModal() {
    var toUsername = $('input[name=toUsername]').val();
    if (toUsername == undefined || toUsername == "") {
        toastr.error("用户名为空！");
        return false;
    }
    var guessIdiom = $('input[name=guessIdiom]').val();
    if (guessIdiom == undefined || guessIdiom == "") {
        toastr.error("成语为空！");
        return false;
    }
    return true;
}

//关闭模态框
function closeModal() {
    //隐藏模态框
    $('#sendIdiomModal').modal('hide');
    //清除模态框数据
    $('input[name=toUsername]').val("");
    $('input[name=guessIdiom]').val("");
}

//查看猜成语列表
function getGuessIdiomList(page, pageSize) {
    refreshGuessIdiomList(page, pageSize);
    $('#idiomListModal').modal();
}

function refreshGuessIdiomList(page, pageSize) {
    var params = {
        page: page,
        pageSize: pageSize
    };
    methods.getGuessIdiomList(params);
}

function refreshGuessIdiomListDefaultPage() {
    refreshGuessIdiomList(1, 5);
}

//渲染猜成语列表
function showGuessIdiomList(guessIdiomList, loginUser) {
    var html = "";
    var index = 1;
    $('#idiomListModal tbody').empty();
    for (var i = 0; i < guessIdiomList.length; i++) {
        var guessItem = guessIdiomList[i];
        var id = guessItem.id;
        var means = guessItem.means;
        var word = guessItem.word;
        var fromUsername = guessItem.fromUsername;
        var toUsername = guessItem.toUsername;
        var createTime = guessItem.createTime;
        var idiomStatus;
        if (guessItem.idiomStatus == 0) {
            idiomStatus = "待猜";
        } else if (guessItem.idiomStatus == 1) {
            idiomStatus = "猜中";
        } else if (guessItem.idiomStatus == 2) {
            idiomStatus = "放弃";
        } else {
            idiomStatus = "其他";
        }
        //自己出的题，显示成语，并可以删除。
        if (loginUser == fromUsername) {
            html = html + "<tr>"
                + "<td hidden='hidden'>" + id + "</td>"
                + "<td class='hidden-xs'>" + index + "</td>"
                + "<td>" + word + "</td>"
                + "<td style='white-space: nowrap;text-overflow: ellipsis;overflow: hidden;' onclick='showFullMeans(this)'>" + means + "</td>"
                + "<td>" + fromUsername + "</td>"
                + "<td>" + toUsername + "</td>"
                + "<td>" + idiomStatus + "</td>"
                + "<td class='hidden-xs'>" + createTime + "</td>"
                + "<td><button class='btn btn-default' onclick='getGuessIdiomDetail(" + id + ")'>查看</button>"
                + "<button class='btn btn-danger' onclick='deleteGuessIdiom(" + id + ")'>删除</button> </td>"
                + "</tr>";
        } else if (loginUser == toUsername) {
            //别人出的题，答对或手动放弃的显示成语，待猜的不显示
            if (guessItem.idiomStatus == 0) {
                html = html + "<tr>"
                    + "<td hidden='hidden'>" + id + "</td>"
                    + "<td class='hidden-xs'>" + index + "</td>"
                    + "<td>【待猜】</td>"
                    + "<td style='white-space: nowrap;text-overflow: ellipsis;overflow: hidden;' onclick='showFullMeans(this)'>" + means + "</td>"
                    + "<td>" + fromUsername + "</td>"
                    + "<td>" + toUsername + "</td>"
                    + "<td>" + idiomStatus + "</td>"
                    + "<td class='hidden-xs'>" + createTime + "</td>"
                    + "<td><button class='btn btn-info' onclick='toGuessIdiom(" + JSON.stringify(guessItem).replace(/"/g, '&quot;') + ")'>答题</button></td>"
                    + "</tr>";
            } else {
                html = html + "<tr>"
                    + "<td hidden='hidden'>" + id + "</td>"
                    + "<td class='hidden-xs'>" + index + "</td>"
                    + "<td>" + word + "</td>"
                    + "<td style='white-space: nowrap;text-overflow: ellipsis;overflow: hidden;' onclick='showFullMeans(this)'>" + means + "</td>"
                    + "<td>" + fromUsername + "</td>"
                    + "<td>" + toUsername + "</td>"
                    + "<td>" + idiomStatus + "</td>"
                    + "<td class='hidden-xs'>" + createTime + "</td>"
                    + "<td><button class='btn btn-default' onclick='getGuessIdiomDetail(" + id + ")'>查看</button></td>"
                    + "</tr>";
            }
        } else {
            console.error("异常数据：该记录与当前用户无关！" + guessItem);
        }
        index++;
    }
    $('#idiomListModal tbody').append(html);
}

//猜成语列表，点击省略的成语，显示完整释义
function showFullMeans(ele) {
    var means = $(ele).text();
    $.alert({
        title: "【释义】",
        content: means,
        buttons: {
            "了解！": {}
        }
    });
}

//删除猜成语题目
function deleteGuessIdiom(id) {
    $.confirm({
        title: "删除",
        content: "确认删除该题目吗？",
        buttons: {
            删除: {
                btnClass: 'btn-red any-other-class',
                action: function () {
                    methods.deleteGuessIdiom(id);
                }
            },
            取消: {}
        },
        autoClose: "取消|10000"
    });
}

function getGuessIdiomDetail(id) {
    methods.getGuessIdiomDetail(id);
}

function showGuessDetail(guessIdiom) {
    var status;
    if (guessIdiom.idiomStatus == 0) {
        status = "待猜";
    } else if (guessIdiom.idiomStatus == 1) {
        status = "已猜中";
    } else if (guessIdiom.idiomStatus == 2) {
        status = "放弃";
    }
    $.alert({
        title: "详情",
        content: "<p>【成语】：" + guessIdiom.word + "</p>"
        + "<p>【释义】：" + guessIdiom.means + "</p>"
        + "<p>【出题人】：" + guessIdiom.fromUsername + "</p>"
        + "<p>【答题人】：" + guessIdiom.toUsername + "</p>"
        + "<p>【状态】：" + status + "</p>"
        + "<p>【创建时间】：" + guessIdiom.createTime + "</p>",
        buttons: {
            "已阅!": {}
        }
    });
}

function toGuessIdiom(guessItem) {
    //先隐藏模态框，不然会冲突，导致confirm里的input无法输入
    $('#idiomListModal').modal('hide');
    var id = guessItem.id;
    var means = guessItem.means;
    var createTime = guessItem.createTime;
    $.confirm({
        title: "猜成语",
        content: "<form class='form-horizontal'>"
        + "<div class='form-group'>"
        + "<label class='control-label col-xs-3'>成语</label>"
        + "<div class='col-xs-8'>"
        + "<input class='form-control' type='text' name='inputIdiom' placeholder='请输入成语' required>"
        + "</div> "
        + "</div> "
        + "</form>"
        + "<p>释义：" + means + "</p>"
        + "<p>创建时间：" + createTime + "</p>",
        buttons: {
            提交: {
                btnClass: "btn-green",
                action: function () {
                    var inputIdiom = $('input[name=inputIdiom]').val();
                    if (inputIdiom == undefined || inputIdiom == "") {
                        $.alert("成语不能为空！");
                        return false;
                    }
                    return methods.toGuessIdiom(id, inputIdiom);
                }
            },
            关闭: function () {
                toastr.info("下次再猜吧~");
            },
            放弃: {
                btnClass: "btn-red",
                action: function () {
                    methods.abandonGuessIdiom(id);
                }
            }
        }
    });
}

//生成分页组件
function buildPagination(page, pageSize, total) {
    var pageSum = Math.ceil(total / pageSize);
    $('.pagination').bootstrapPaginator({
        //设置版本号
        bootstrapMajorVersion: 3,
        // 显示第几页
        currentPage: page,
        // 总页数
        totalPages: pageSum,
        //当单击操作按钮的时候, 执行该函数, 调用ajax渲染页面
        onPageClicked: function (event, originalEvent, type, page) {
            // 把当前点击的页码赋值给currentPage, 调用ajax,渲染页面
            currentPage = page;
            //调用获取列表函数
            refreshGuessIdiomList(currentPage, pageSize);
        }
    });
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
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
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
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    sendIdiom: function (params) {
        $.ajax({
            type: "POST",
            url: "/gu/game/sendIdiom",
            data: {
                toUsername: params.toUsername,
                guessIdiom: params.guessIdiom
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                closeModal();
                toastr.success("发送成功！");

            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    getGuessIdiomList: function (params) {
        $.ajax({
            type: "GET",
            url: "/gu/game/getGuessIdiomList",
            data: params,
            success: function (jsonResult) {
                var total = jsonResult.data.total;
                var guessIdiomList = jsonResult.data.data;
                var loginUser = jsonResult.data.loginUser;
                var page = params.page;
                var pageSize = params.pageSize;
                showGuessIdiomList(guessIdiomList, loginUser);
                buildPagination(page, pageSize, total);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    deleteGuessIdiom: function (id) {
        $.ajax({
            type: "POST",
            url: "/gu/game/deleteGuessIdiom",
            data: {id: id},
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                toastr.success("删除成功！");
                //刷新列表
                refreshGuessIdiomListDefaultPage();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    getGuessIdiomDetail: function (id) {
        $.ajax({
            type: "GET",
            url: "/gu/game/getGuessIdiomDetail",
            data: {id: id},
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var guessIdiom = jsonResult.data;
                showGuessDetail(guessIdiom);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    toGuessIdiom: function (id, idiom) {
        var isPass = false;
        $.ajax({
            type: "POST",
            url: "/gu/game/guessIdiom",
            //等待ajax返回结果再往下执行，否则isPass永远为false
            async: false,
            data: {
                id: id,
                idiom: idiom
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                toastr.success("恭喜你，答对了！");
                isPass = true;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
        return isPass;
    },
    abandonGuessIdiom: function (id) {
        $.ajax({
            type: "POST",
            url: "/gu/game/abandonGuessIdiom",
            data: {id: id},
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                toastr.success("已放弃！");
                refreshGuessIdiomListDefaultPage();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    queryIdiom: function (queryIdiom) {
        $.ajax({
            type: "GET",
            url: "/gu/game/queryIdiom",
            data: {idiom: queryIdiom},
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var idiom = jsonResult.data;
                setIdiomOnPanel(idiom);
                showPanel("成语查询");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    }
};