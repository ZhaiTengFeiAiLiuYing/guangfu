package com.pb.pojo;
import lombok.Data;
import javax.persistence.Table;

/**
 * Created by Administrator on 2019/5/17.
 */
@Data
@Table(name = "设备信息")
public class SheBei {
    private String sheBeiXuLieHao;
    private String  mingCheng;
    private String leiXing;
    private String xingHao;
    private String ceDianZhi;
    private String ceDianMingCheng;
    private String updatetime;
}
