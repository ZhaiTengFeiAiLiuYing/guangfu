package com.pb.vo;
import com.pb.pojo.Station;
import lombok.Data;
import java.util.Set;
/**
 * Created by Administrator on 2019/5/20.
 */
@Data
public class UserVo {
    private String id;
    private String username;
    private Set<Station> stations;

}
