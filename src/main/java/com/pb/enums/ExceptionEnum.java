package com.pb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019/5/17.
 * 异常枚举，定义异常类型
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    USERNAME_OR_PASSWORD_BE_NULL(400,"用户名或密码为空"),
    USERNAME_OR_PASSWORD_BE_ERROR(400,"用户名或密码错误"),
    PASSWORD_LENGTH_ERROR(400,"密码长度必须在5-10位之间"),
    USER_BEREGSTRY(400,"用户名已存在"),
    INSERT_ERROR(500,"添加失败"),
    PARAMS_BE_NULL(400,"参数不能为空"),
    TIME_BE_ERROR(400,"日期错误"),
    SELECT_ERROR(500,"查询失败")
    ;
    private int code;
    private String msg;
}
