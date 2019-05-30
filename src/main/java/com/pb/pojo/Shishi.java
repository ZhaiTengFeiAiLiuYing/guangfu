package com.pb.pojo;

import lombok.Data;

import javax.persistence.Table;

/**
 * Created by Administrator on 2019/5/17.
 */
@Data
@Table(name = "实时测值")
public class Shishi {
    private String ceDianXuLieHao;
    private String zhanDianXuLieHao;
    private String  ceDianMingCheng;
    private String sheBeiXuLieHao;
    private String  ceDianZhi;
    private String ceDianLeiXing;
    private String updatetime;
}
