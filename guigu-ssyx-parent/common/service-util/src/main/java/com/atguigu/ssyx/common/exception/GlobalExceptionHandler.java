package com.atguigu.ssyx.common.exception;


import com.atguigu.ssyx.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    //把对象转为json数据，放入响应体中
    @ResponseBody
    //全局异常处理器
    @ExceptionHandler(value = Exception.class/*只要是Exception类的异常都会执行这个方法*/)
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(null);
    }


    //自定义异常处理
    @ResponseBody
    @ExceptionHandler(value = SsyxException.class)
    public Result error(SsyxException e){
        return Result.build(null,e.getCode(),e.getMessage());
    }
}
