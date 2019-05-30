package com.pb.vo;

import com.pb.enums.ExceptionEnum;
import lombok.Data;

/**
 * Created by Administrator on 2019/5/17.
 */
@Data
public class ExceptionResult {
    private int code;
    private String msg;
    public ExceptionResult(ExceptionEnum em){
        this.code=em.getCode();
        this.msg=em.getMsg();
    }
}
