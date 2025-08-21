$(function () {
    refreshList();
    //查询
    $('#search_btn').on('click', function () {
        //按钮显示正在查询
        var btn = $(this).button('loading');
        var query = $('#Ktext').val();
        var page = 1;
        var pageSize = 10;
        $.ajax({
            type: "GET",
            url: "/goodbook/word/wordList",
            data: {
                query: query,
                current: page,
                pageSize: pageSize
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var total = jsonResult.data.total;
                var wordList = jsonResult.data.data;
                methods.showWordList(total, wordList);
                // 刷新分页
                $('#pagination').twbsPagination('destroy'); // 先销毁现有的分页组件
                buildPagination(page, pageSize, total); // 重新构建分页组件
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
        var phonetic = $('#word input[name=phonetic]').val();
        var example = $('#word input[name=example]').val();

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
            chinese: chinese.trim(),
            phonetic: phonetic,
            example: example
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
                    好: {}
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
                取消: {}
            }
        })
    });
})

function addModal() {
    $('#word input[name=id]').val("")
    $('#word input[name=english]').val("");
    $('#word input[name=chinese]').val("");
    $('#word input[name=phonetic]').val("");
    $('#word input[name=example]').val("");
    $('#word .modal-header label').html("添加");
    $('#word').modal();
}

function editModal(ele) {
    var tr = $(ele).parent().parent();
    var word = {
        id: tr.find("[name=id]").text(),
        english: tr.find("[name=english]").text(),
        chinese: tr.find("[name=chinese]").text(),
        phonetic: tr.find("[name=phonetic]").text(),
        example: tr.find("[name=example]").text()
    };
    console.log(word);
    $('#word input[name=id]').val(word.id);
    $('#word input[name=english]').val(word.english);
    $('#word input[name=chinese]').val(word.chinese);
    $('#word input[name=phonetic]').val(word.phonetic);
    $('#word input[name=example]').val(word.example);
    $('#word .modal-header label').html("修改");
    //显示模态框
    $('#word').modal();
}

function deleteModal(ele) {
    var tr = $(ele).parent().parent();
    var word = {
        id: tr.find("[name=id]").text(),
        english: tr.find("[name=english]").text(),
        chinese: tr.find("[name=chinese]").text(),
        phonetic: tr.find("[name=phonetic]").text(),
        example: tr.find("[name=example]").text()
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
            取消: {}
        }
    });
}

function refreshList() {
    // 重置分页
    $('#pagination').twbsPagination('destroy');
    methods.getWordList(1, 10);
    //复原批量复选框
    $("#batchCheckBox").prop("checked", false);
}

function buildPagination(page, pageSize, total) {
    var pageSum = Math.ceil(total / pageSize);
    if (pageSum === 0) {
        pageSum = 1;
    }

    $('#pagination').twbsPagination({
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
            methods.getWordList(currentPage, pageSize);
        }
    });
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
                "<td name='phonetic'>" + (word.phonetic || '') + "</td>" +
                "<td name='example'>" + (word.example || '') + "</td>" +
                "<td name='createTime'>" + word.createTime + "</td>" +
                "<td name='creator' class='hidden-xs'>" + word.creator + "</td>" +
                "<td>" +
                "<a href='javascript:void(0)' class='edit' onclick='editModal(this)'>编辑</a>" +
                "<a href='javascript:void(0)' class='del' onclick='deleteModal(this)'>删除</a> " +
                "</td>"
        });
        $('#show_tbody').append(html);
    },
    getWordList: function (page, pageSize) {
        var query = $('#Ktext').val();
        $.ajax({
            type: "GET",
            url: "/goodbook/word/wordList",
            data: {
                query: query,
                current: page,
                pageSize: pageSize
            },
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    return;
                }
                var total = jsonResult.data.total;
                var wordList = jsonResult.data.data;
                methods.showWordList(total, wordList);
                // 刷新分页
                buildPagination(page, pageSize, total);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
    },
    //添加记录
    addWord: function (word) {
        console.log(word);
        $.ajax({
            type: "POST",
            url: "/goodbook/word/addWord",
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
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
        //隐藏模态框
        $('#word').modal('hide');
    },
    //修改记录
    editWord: function (word) {
        $.ajax({
            type: "POST",
            url: "/goodbook/word/updateWord",
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
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        });
        //隐藏模态框
        $('#word').modal('hide');
    },
    deleteWord: function (word) {

        $.ajax({
            type: "POST",
            url: "/goodbook/word/deleteWord",
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
                toastr.error(jqXHR.status + ":" + jqXHR.statusText);
                toastr.error(textStatus);
            }
        })
    },
    batchDelete: function (ids) {
        console.log(ids);
        $.ajax({
            type: "POST",
            url: "/goodbook/word/batchDelete",
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
