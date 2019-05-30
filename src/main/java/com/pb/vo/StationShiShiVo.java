package com.pb.vo;

import lombok.*;

/**
 * Created by Administrator on 2019/5/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 封装的webSocket返回的电站数据类
 */
public class StationShiShiVo {
    private double rfdl;
    private double leiJiFdl;
    private double gongLv;

}
