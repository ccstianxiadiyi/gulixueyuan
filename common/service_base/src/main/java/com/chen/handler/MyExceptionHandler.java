package com.chen.handler;



import com.chen.commonutils.GuliException;
import com.chen.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R mostException(Exception e){
        e.printStackTrace();
        return R.error();
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R privateException(GuliException e){
        return R.error().code(e.getCode()).message(e.getMessage());
    }
}
