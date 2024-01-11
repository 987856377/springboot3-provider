package com.springboot.provider.common;

import java.io.Serializable;

/**
 * @Description
 * @Project mybatisplus
 * @Package com.spring.development.utils
 * @Author xuzhenkui
 * @Date 2019/9/19 9:37
 */
public class ResultJson<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public ResultJson() {
    }

    public ResultJson(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultJson(ResultCode resultCode) {
        setResultCode(resultCode);
    }

    public ResultJson(ResultCode resultCode, T data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResultJson<T> success() {
        return success(null);
    }

    public static <T> ResultJson<T> success(T body) {
        return new ResultJson<>(ResultCode.SUCCESS, body);
    }

    public static <T> ResultJson<T> failure(ResultCode resultCode) {
        return new ResultJson<>(resultCode, null);
    }


    public static <T> ResultJson<T> failure(String message) {
        return new ResultJson<T>(500, message, null);
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }


}
