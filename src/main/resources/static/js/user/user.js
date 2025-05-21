$(function () {
    $('#regist').on('click', function () {
        regist();
    });

    $('#login').on('click', function () {
        login();
    });
});

// 修改键盘事件监听器，确保按下回车键时调用 login() 函数
document.addEventListener('keydown', function(event) {
    // 检查是否按下了回车键
    if (event.key === 'Enter') {
        login();
    }
});

function hiddenErrMsg() {
    var errMsgEle = $('#errorMsg');
    errMsgEle.prop('hidden', true);
    errMsgEle.text('');
}

function clearUserInfo() {
    $('#username').val('');
    $('#password').val('');
}

function showErrMsg(msg) {
    var errMsgEle = $('#errorMsg');
    errMsgEle.prop('hidden', false);
    errMsgEle.text(msg);
}

function regist() {
    //按钮显示正在查询
    var btn = $('#regist').button('loading');
    var username = $('#username').val();
    var password = $('#password').val();
    $.ajax({
        type: "POST",
        url: "/gu/user/regist",
        data: {
            username: username,
            password: password
        },
        success: function (jsonResult) {
            if (jsonResult.errorCode != 0) {
                var errMsgEle = $('#errorMsg');
                errMsgEle.prop('hidden', false);
                errMsgEle.text(jsonResult.msg);
                return;
            }
            showErrMsg('注册成功！');
            clearUserInfo();
            setTimeout(hiddenErrMsg, 3000);
        },
        error: function () {
            showErrMsg(textStatus);
        }
    });
    //恢复按钮状态
    btn.button('reset');
}

function login() {
    var username = $('#username').val();
    var password = $('#password').val();
    $.ajax({
        type: "POST",
        url: "/gu/user/login",
        data: {
            username: username,
            password: password
        },
        success: function (jsonResult) {
            if (jsonResult.errorCode != 0) {
                var errMsgEle = $('#errorMsg');
                errMsgEle.prop('hidden', false);
                errMsgEle.text(jsonResult.msg);
                return;
            }
            console.log('登陆成功！');
            window.location.href = "/head";
        },
        error: function () {
            showErrMsg(textStatus);
        }
    });
}