package com.pb.vo;
import com.pb.pojo.SheBei;
import com.pb.pojo.Shishi;
import lombok.Data;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by Administrator on 2019/5/23.
 */
@Data
@Table(name = "设备信息")
public class SheBeiVo {
    private String sheBeiXuLieHao;
    private String  mingCheng;
    private String leiXing;
    private String xingHao;
    private String rfdl;
    private String ljFdl;
    private String lv;
    private String kaiGuanZhuangTai;
    private String niBianQingZhuangTai;
    private String updatetime;
}
