package com.pb.mapper;
import com.pb.pojo.History;
import com.pb.pojo.YearFdl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2019/5/27.
 */
@Repository
public interface HistoryFdlMapper {
    public void createHistoryFdlTable(@Param("tableName")String tableName);
    public void insertFdl(@Param("historyFdls")List<History> historyFdls, @Param("tableName")String tableName);
    public void updateFdl(@Param("historyFdls")List<History> historyFdls, @Param("tableName")
            String tableName, @Param("time")String time, @Param("selectTime")String selectTime);
    public List<History> selectYfdl(@Param("month")String month, @Param("xuLieHao") String xuLieHao,
                                    @Param("tableName")String tableName);

    public void insertYearFdl(@Param("yearFdls") List<YearFdl> yearFdls);
    public void updateYearFdl(@Param("yearFdls") List<YearFdl> yearFdls,@Param("year") String year);
    public List<YearFdl> selectZongFdl(@Param("xuLieHao")String xuLieHao);
}
