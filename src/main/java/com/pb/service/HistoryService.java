package com.pb.service;
import com.pb.mapper.HistoryFdlMapper;
import com.pb.mapper.HistoryGongLvMapper;
import com.pb.pojo.History;
import com.pb.pojo.YearFdl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * Created by Administrator on 2019/5/28.
 */
@Service
@Transactional
public class HistoryService {
    @Autowired
    private HistoryFdlMapper historyFdlMapper;
    @Autowired
    private HistoryGongLvMapper historyGongLvMapper;

    /**
     * 查询指定月中的每日发电量
     * @param month
     * @param xuLieHao
     * @return
     */
    public List<History> selectYfdl(String month, String xuLieHao){
        String tableName= month.split("-")[0]+"_fdl";
        List<History> historyFdls=historyFdlMapper.selectYfdl(month,xuLieHao,tableName);
        return historyFdls;
    }

    /**
     * 查询功率
     * @param date
     * @param xuLieHao
     * @return
     */
    public List<History> selectGongLv(String date, String xuLieHao){
        String tableName= date.split("-")[0]+"_gl";
        List<History> historyGongLvs=historyGongLvMapper.selectGongLv(date,xuLieHao,tableName);
        return historyGongLvs;
    }

    public List<YearFdl> selectZongFdl(String xuLieHao){
        return  historyFdlMapper.selectZongFdl(xuLieHao);
    }
}
