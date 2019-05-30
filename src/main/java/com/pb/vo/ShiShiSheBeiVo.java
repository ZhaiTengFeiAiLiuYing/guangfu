package com.pb.vo;
import com.pb.pojo.Shishi;
import lombok.Data;
import java.util.List;
/**
 * Created by Administrator on 2019/5/21.
 */
@Data
public class ShiShiSheBeiVo {
    private String sheBeiXuLieHao;

    private List<Shishi> shishis;
}
