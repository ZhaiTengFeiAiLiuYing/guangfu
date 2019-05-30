package com.pb.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019/5/17.
 */
@NoArgsConstructor
@Getter
@AllArgsConstructor
public enum SuccessEnum {
    SUCCESS_SAVE(201,"添加成功"),
    SUCCESS_UPDATE(201,"修改成功"),
    SUCCESS_DELETE(204,"删除数据成功"),
    SUCCESS_SELECT(200,"查询成功")
    ;
    private int code;
    private String msg;
}
