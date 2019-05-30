package com.pb.pojo;
import lombok.Data;
import javax.persistence.Table;

/**
 * Created by Administrator on 2019/5/17.
 */
@Data
@Table(name="项目信息")
public class Station {
    private String zhanDianXuLieHao;
    private String xiangMuMingCheng;
    private String xiangMuRongLiang;
    private double dianYaDengJi;
    private String niBianFangShi;
    private String xiangMuDiZhi;
}
