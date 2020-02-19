$(function () {
    //抽奖
    $('#randomBegin_btn').on('click', function () {
        $('#randomModal .modal-body p').html("<h1 style='text-align: center;'>正在抽奖...</h1>");
        //按钮显示正在抽奖
        $.ajax({
            type: "GET",
            url: "/gu/game/lottery/getRandomItem",
            success: function (jsonResult) {
                if (jsonResult.errorCode != 0) {
                    toastr.error(jsonResult.msg);
                    $('#randomModal .modal-body p').html("<h1 style='text-align: center;color: gray'>空气突然宁静。。。</h1>");
                    return;
                }
                var item = jsonResult.data;
                console.log(item);
                $('#randomModal .modal-body p').html("<h1 style='text-align: center;color: red'>" + item.name + "</h1>");
            },
            error: function () {
                toastr.error(textStatus);
                toastr.error(errorThrown);
                $('#randomModal .modal-body p').html("<h1 style='text-align: center;color: gray'>空气突然宁静。。。</h1>");
            }
        });
    });

    //模态框确认：新增、修改
    $('#confirm_btn').on('click', function () {
        var id = $('#lotteryModal input[name=id]').val();
        var name = $('#lotteryModal input[name=lotteryName]').val();
        if (name == undefined || name.trim() == '') {
            toastr.error("大佬，选项不能为空！");
            return;
        }
        var item = {
            id: id,
            name: name.trim()
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
        var delItemList = new Array();
        $('#show_tbody input[type=checkbox]:checked').each(function () {
            var id = $(this).val();
            var name = $(this).parent().parent().find('[name=itemName]').text();
            ids.push(id);
            delItemList.push(name);
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

function addModal() {
    $('#lotteryModal input[name=id]').val("")
    $('#lotteryModal input[name=lotteryName]').val("");
    $('#lotteryModal .modal-header label').html("添加选项");
    $('#notice').show();
    $('#lotteryModal').modal();
}

function editModal(ele) {
    $('#notice').hide();
    var tr = $(ele).parent().parent();
    var item = {
        id: tr.find("[name=id]").text(),
        name: tr.find("[name=itemName]").text()
    };
    console.log(item);
    $('#lotteryModal input[name=id]').val(item.id);
    $('#lotteryModal input[name=lotteryName]').val(item.name);
    $('#lotteryModal .modal-header label').html("修改");
    //显示模态框
    $('#lotteryModal').modal();
}

function deleteModal(ele) {
    var tr = $(ele).parent().parent();
    var item = {
        id: tr.find("[name=id]").text(),
        name: tr.find("[name=itemName]").text()
    };
    console.log(item);
    $.confirm({
        theme: 'light',
        title: "删除选项",
        content: "选项删除后无法恢复，确认删除[" + item.name + "]吗？",
        buttons: {
            确定: function () {
                methods.deleteItem(item);
            },
            取消: {}
        }
    });
}

function notice() {
    $.alert({
        title: "tips",
        content: "添加选项时，多个选项用英文逗号隔开，可以进行批量添加，如：一等奖,二等奖,三等奖",
        buttons: {
            明白了: function () {
                toastr.success("聪明！");
            },
            不明白: function () {
                toastr.warning("问问管理员，或者一个个加~");
            }
        }
    });
}

function randomModal() {
    //把body置为空
    $('#randomModal .modal-body p').html("");
    //显示模态框
    $("#randomModal").modal();
}

function refreshList() {
    methods.getItemList();
    //复原批量复选框
    $("#batchCheckBox").prop("checked", false);
}

var methods = {
    //展示列表
    showItemList: function (total, itemList) {
        //清空列表
        $('#show_tbody tr').remove();
        var html = "";
        var index = 1;
        itemList.forEach(function (item) {
            html = html + "<tr>" +
                "<td><input type='checkbox' value='" + item.id + "' /></td>" +
                "<td name='id' hidden='hidden'>" + item.id + "</td>" +
                "<td name='index' class='hidden-xs'>" + (index++) + "</td>" +
                "<td name='itemName'>" + item.name + "</td>" +
                "<td name='createTime'>" + item.createTime + "</td>" +
                "<td name='creator' class='hidden-xs'>" + item.creator + "</td>" +
                "<td>" +
                "<a href='javascript:void(0)' class='edit' onclick='editModal(this)'>编辑</a>" +
                "<a href='javascript:void(0)' class='del' onclick='deleteModal(this)'>删除</a> " +
                "</td>"
        });
        $('#show_tbody').append(html);
    },
    getItemList: function () {
        $.ajax({
            type: "GET",
            url: "/gu/game/lottery/itemList",
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
                var itemList = jsonResult.data.data;
                methods.showItemList(total, itemList);
                //todo 刷新分页
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
            url: "/gu/game/lottery/addItem",
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
        $('#lotteryModal').modal('hide');
    },
    //修改记录
    editItem: function (item) {
        $.ajax({
            type: "POST",
            url: "/gu/game/lottery/updateItem",
            data: JSON.stringify(item),
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
        $('#lotteryModal').modal('hide');
    },
    deleteItem: function (item) {

        $.ajax({
            type: "POST",
            url: "/gu/game/lottery/deleteItem",
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
            url: "/gu/game/lottery/batchDelete",
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
