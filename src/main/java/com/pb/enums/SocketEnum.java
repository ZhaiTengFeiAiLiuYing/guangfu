package com.pb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019/5/20.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SocketEnum {
    CONNET(1, "第一次(或重连)初始化连接"),
    STATION(2,"站点信息"),
    SHEBEI(3,"设备信息"),
    SHE_BEI_XIANG_QING(4,"设备详情"),
    ;
    private int type;
    private String content;
}
