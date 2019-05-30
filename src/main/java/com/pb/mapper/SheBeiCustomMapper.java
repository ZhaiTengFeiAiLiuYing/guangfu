package com.pb.mapper;
import com.pb.pojo.SheBei;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2019/5/23.
 */
@Repository
public interface SheBeiCustomMapper {
    public List<SheBei> selectSheBeiByStation(@Param("stationXuLeiHao") String stationXuLeiHao);
}
