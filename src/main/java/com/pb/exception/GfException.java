package com.pb.exception;
import com.pb.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019/5/17.
 * 自定义异常类，抛出枚举中定义的异常类型
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GfException extends RuntimeException {
    private ExceptionEnum exceptionEnum;
}
