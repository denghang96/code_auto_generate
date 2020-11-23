package com.dengooo.codegenerate.common.resp;

import lombok.Data;

@Data
public class RespData<T> {
    private Integer code;
    private String message;
    private T data;

    private RespData(){}

    public static <T> RespData success(Integer code, T data) {
        RespData respData = new RespData();
        respData.setCode(code);
        respData.setData(data);
        return respData;
    }

    public static <T> RespData success() {
        RespData respData = new RespData();
        respData.setCode(200);
        return respData;
    }

    public static <T> RespData success(T data) {
        RespData respData = new RespData();
        respData.setCode(200);
        respData.setData(data);
        return respData;
    }

    public static RespData fail(Integer code, String message) {
        RespData respData = new RespData();
        respData.setCode(code);
        respData.setMessage(message);
        return respData;
    }

    public static RespData fail(String message) {
        RespData respData = new RespData();
        respData.setCode(500);
        respData.setMessage(message);
        return respData;
    }
}
