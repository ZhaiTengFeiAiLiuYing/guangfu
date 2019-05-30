package com.pb.advice;

import com.pb.enums.ExceptionEnum;
import com.pb.exception.GfException;
import com.pb.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Administrator on 2019/5/17.
 * 统一异常处理类
 * 拦截controller，获取Controller中的异常，并返回异常状态码和异常信息
 */
@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(GfException.class)
    public ResponseEntity<ExceptionResult> handleException(GfException e){
        ExceptionEnum em = e.getExceptionEnum();
        return ResponseEntity.status(em.getCode()).body(new ExceptionResult(em));
    }
}
