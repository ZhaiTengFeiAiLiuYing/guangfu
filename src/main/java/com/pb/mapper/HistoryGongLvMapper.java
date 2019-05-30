package com.pb.mapper;

import com.pb.pojo.History;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2019/5/28.
 */
@Repository
public interface HistoryGongLvMapper {
    public void createGongLvTable(@Param("tableName")String tableName);
    public void insertGongLv(@Param("historyFdls")List<History> historyFdls, @Param("tableName")String tableName);
    public List<History> selectGongLv(@Param("date") String date,@Param("xuLieHao") String xuLieHao,
    @Param("tableName")String tableName);
}
