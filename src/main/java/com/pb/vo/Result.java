package com.pb.vo;
import com.pb.enums.SuccessEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019/5/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 封装的htpp返回数据类型
 */
public class Result {
    private int code;
    private String message;
    private Object data;
    public Result(SuccessEnum em){
        this.code=em.getCode();
        this.message=em.getMsg();
    }
    public Result(SuccessEnum em,Object data){
        this.code=em.getCode();
        this.message=em.getMsg();
        this.data=data;
    }
}
