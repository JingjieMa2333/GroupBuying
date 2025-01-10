package com.atguigu.ssyx.common.result;

import lombok.Data;

@Data
public class Result<T> {
    //状态码
    private int code;
    //信息
    private String msg;
    //数据
    private T data;

    //构造私有化
    private Result() {}

    //设置数据，返回结果的方法
    public static <T> Result<T> build(T data,ResultCodeEnum resultCodeEnum) {
        //创建result对象，设置值并返回
        Result<T> result = new Result<T>();
        //判断是否需要添加数据
        if(data != null) {
            //添加数据
            result.setData(data);
        }
        //设置状态码和信息
        result.setCode(resultCodeEnum.getCode());
        result.setMsg(resultCodeEnum.getMessage());
        //返回最后的对象
        return result;
    }

    public static <T> Result<T> build(T data,Integer code,String msg) {
        //创建result对象，设置值并返回
        Result<T> result = new Result<T>();
        //判断是否需要添加数据
        if(data != null) {
            //添加数据
            result.setData(data);
        }
        //设置状态码和信息
        result.setCode(code);
        result.setMsg(msg);
        //返回最后的对象
        return result;
    }

    //成功的方法
    public static <T> Result<T> ok(T data) {
        Result<T> result = build(data, ResultCodeEnum.SUCCESS);
        return result;
    }
    //失败的方法
    public static <T> Result<T> fail(T data) {
        Result<T> result = build(data, ResultCodeEnum.FAIL);
        return result;
    }

    //其他方法可以调用build建造
}
