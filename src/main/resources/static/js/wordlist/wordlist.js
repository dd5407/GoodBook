$(function () {
    //查询
    $('#search_btn').on('click', function () {
        //按钮显示正在查询
        var btn = $(this).button('loading');
        var query = $('#Ktext').val();
        $.ajax({
            type: "GET",
            url: "/gu/word/wordList",
            data: {
                query: query,
                current: 1,
                pageSize: 10
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                total = jsonResult.data.total;
                var wordList = jsonResult.data.data;
                methods.showWordList(total, wordList);
                //todo 刷新分页
            },
            error: function () {
                toastr.error(textStatus);
                toastr.error(errorThrown);
            }
        });
        //恢复按钮状态
        btn.button('reset');
        //复原批量复选框
        $("#batchCheckBox").prop("checked", false);
    });

    //模态框确认：新增、修改
    $('#confirm_btn').on('click', function () {
        var id = $('#word input[name=id]').val();
        var english = $('#word input[name=english]').val();
        var chinese = $('#word input[name=chinese]').val();
        if (english == undefined || english.trim() == '') {
            toastr.error("大佬，英文不能为空！");
            return;
        }
        if (chinese == undefined || chinese.trim() == '') {
            toastr.error("大佬，中文不能为空！");
            return;
        }
        var word = {
            id: id,
            english: english.trim(),
            chinese: chinese.trim()
        };
        if (id == undefined || id == '') {
            methods.addWord(word);
        } else {
            methods.editWord(word);
        }
    });

    //全选复选框监听
    $('#batchCheckBox').on('click', function () {
        if ($('#batchCheckBox').prop('checked')) {
            $('#show_tbody input[type=checkbox]').prop('checked', true);
        } else {
            $('#show_tbody input[type=checkbox]').prop('checked', false);
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
        var delEnglishList = new Array();
        $('#show_tbody input[type=checkbox]:checked').each(function () {
            var id = $(this).val();
            var english = $(this).parent().parent().find('[name=english]').text();
            ids.push(id);
            delEnglishList.push(english);
        });
        if (ids.length <= 0) {
            $.alert({
                title: "大佬",
                content: "至少选一个呗！",
                buttons: {
                    好: function () {

                    }
                }
            });
            return;
        }
        $.confirm({
            theme: "modern",
            title: "批量删除单词",
            content: "确认删除[" + delEnglishList + "]吗？",
            buttons: {
                确定: function () {
                    toastr.success("Let them go...");
                    methods.batchDelete(ids);
                },
                取消: function () {
                    return;
                }
            }
        })
    });
})

var current = 1;
var pageSize = 10;
var total = 0;

function changePage(pageNum) {
    console.log(pageNum);
}

function nextPage() {

}

function prePage() {

}

function addModal() {
    $('#word input[name=id]').val("")
    $('#word input[name=english]').val("");
    $('#word input[name=chinese]').val("");
    $('#word .modal-header label').html("添加");
    $('#word').modal();
}

function editModal(ele) {
    var tr = $(ele).parent().parent();
    var word = {
        id: tr.find("[name=id]").text(),
        english: tr.find("[name=english]").text(),
        chinese: tr.find("[name=chinese]").text()
    };
    console.log(word);
    $('#word input[name=id]').val(word.id);
    $('#word input[name=english]').val(word.english);
    $('#word input[name=chinese]').val(word.chinese);
    $('#word .modal-header label').html("修改");
    //显示模态框
    $('#word').modal();
}

function deleteModal(ele) {
    var tr = $(ele).parent().parent();
    var word = {
        id: tr.find("[name=id]").text(),
        english: tr.find("[name=english]").text(),
        chinese: tr.find("[name=chinese]").text()
    };
    console.log(word);
    $.confirm({
        theme: 'light',
        title: "删除单词",
        content: "单词删除后无法恢复，确认删除[" + word.english + "][" + word.chinese + "]吗？",
        buttons: {
            确定: function () {
                methods.deleteWord(word);
            },
            取消: function () {
                return;
            }
        }
    });
}

function refreshList() {
    methods.getWordList();
    //复原批量复选框
    $("#batchCheckBox").prop("checked", false);
}

var methods = {
    //展示列表
    showWordList: function (total, wordList) {
        //清空列表
        $('#show_tbody tr').remove();
        var html = "";
        var index = 1;
        wordList.forEach(function (word) {
            html = html + "<tr>" +
                "<td><input type='checkbox' value='" + word.id + "' /></td>" +
                "<td name='id' hidden='hidden'>" + word.id + "</td>" +
                "<td name='index' class='hidden-xs'>" + (index++) + "</td>" +
                "<td name='english'>" + word.english + "</td>" +
                "<td name='chinese'>" + word.chinese + "</td>" +
                "<td name='createTime'>" + word.createTime + "</td>" +
                "<td name='creator' class='hidden-xs'>" + word.creator + "</td>" +
                "<td>" +
                "<a href='javascript:void(0)' class='edit' onclick='editModal(this)'>编辑</a>" +
                "<a href='javascript:void(0)' class='del' onclick='deleteModal(this)'>删除</a> " +
                "</td>"
        });
        $('#show_tbody').append(html);
    },
    getWordList: function () {
        $.ajax({
            type: "GET",
            url: "/gu/word/wordList",
            data: {
                current: 1,
                pageSize: 10
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                total = jsonResult.data.total;
                var wordList = jsonResult.data.data;
                methods.showWordList(total, wordList);
                //todo 刷新分页
            },
            error: function () {
                toastr.error(textStatus);
                toastr.error(errorThrown);
            }
        });
    },
    //添加记录
    addWord: function (word) {
        console.log(word);
        $.ajax({
            type: "POST",
            url: "/gu/word/addWord",
            data: JSON.stringify(word),
            contentType: "application/json",
            success: function (jsonResult) {
                if (jsonResult.errorCode == 0) {
                    toastr.success("添加成功！");
                    //刷新列表
                    refreshList();
                } else {
                    toastr.error("添加失败！" + jsonResult.msg);
                }
            },
            error: function (jqXHR) {
                var error = jqXHR.responseJSON.error;
                var errmsg = jqXHR.responseJSON.message;
                console.log(error);
                console.log(errmsg);
                toastr.error("系统错误！" + error + " " + errmsg);
            },
        });
        //隐藏模态框
        $('#word').modal('hide');
    },
    //修改记录
    editWord: function (word) {
        $.ajax({
            type: "POST",
            url: "/gu/word/updateWord",
            data: JSON.stringify(word),
            contentType: "application/json",
            success: function (jsonResult) {
                if (jsonResult.errorCode == 0) {
                    toastr.success("修改成功！");
                    //刷新列表
                    refreshList();
                } else {
                    toastr.error("修改失败！" + jsonResult.msg);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                var error = jqXHR.responseJSON.error;
                var errmsg = jqXHR.responseJSON.message;
                console.log(error);
                console.log(errmsg);
                toastr.error("系统错误！" + error + " " + errmsg);
            },
        });
        //隐藏模态框
        $('#word').modal('hide');
    },
    deleteWord: function (word) {

        $.ajax({
            type: "POST",
            url: "/gu/word/deleteWord",
            data: {
                id: word.id
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
                toastr.error("系统错误！" + textStatus + " " + errorThrown);
            }
        })
    },
    batchDelete: function (ids) {
        console.log(ids);
        $.ajax({
            type: "POST",
            url: "/gu/word/batchDelete",
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
                toastr.error("系统错误！" + textStatus + " " + errorThrown);
            }
        })
    }
};
