package com.ddpzp.goodbook.model;

import com.ddpzp.goodbook.common.ErrorCode;
import lombok.Data;

/**
 * 返回结果
 * Created by dd
 * Date 2019/7/25 0:39
 */
@Data
public class JsonResult<T> {
    private Integer errorCode;
    private T data;
    private String msg;

    public static <T> JsonResult<T> success(T data) {
        JsonResult<T> result = new JsonResult<>();
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setData(data);
        result.setMsg("调用成功！");
        return result;
    }

    public static JsonResult success() {
        return success(null);
    }

    public static JsonResult error(String errMsg) {
        return error(ErrorCode.ERROR, errMsg);
    }

    public static JsonResult error(Integer code, String errMsg) {
        JsonResult result = new JsonResult<>();
        result.setErrorCode(code);
        result.setMsg(errMsg);
        return result;
    }

    public static JsonResult error() {
        return error(null);
    }
}
