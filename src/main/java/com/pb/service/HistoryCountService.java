package com.pb.service;
import com.pb.mapper.HistoryFdlMapper;
import com.pb.mapper.HistoryGongLvMapper;
import com.pb.pojo.History;
import com.pb.pojo.YearFdl;
import com.pb.utils.IdWorker;
import com.pb.utils.MapCacheUtils;
import com.pb.vo.SheBeiVo;
import com.pb.vo.StationShiShiVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * Created by Administrator on 2019/5/27.
 */
@Service
@Transactional
public class HistoryCountService {
    @Autowired
    private HistoryFdlMapper historyFdlMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private HistoryGongLvMapper historyGongLvMapper;
    /**
     * 初始化电站和设备序列号
     */
    @Scheduled(cron="0 0/4 5-20 * * ?" )
    public void initXuLieHao(){
        Map<String,List<SheBeiVo>> stationSheBeiMap=MapCacheUtils.stationSheBeiMap;
        MapCacheUtils.sheBeiXuLieHaoSet.clear();
        for(Map.Entry<String,List<SheBeiVo>> entry:stationSheBeiMap.entrySet()){
            MapCacheUtils.stationXuLieHaoSet.add(entry.getKey());
            List<SheBeiVo> sheBeiVos=entry.getValue();
            for(SheBeiVo sheBeiVo:sheBeiVos){
                MapCacheUtils.sheBeiXuLieHaoSet.add(sheBeiVo);
            }
        }
    }

    /**
     * 每年5月27日17点18分执行
     * 创建下年的历史表
     */
    @Scheduled(cron = "0 20 17 28 5 ? ")
    public void createHistoryFdlTable(){
        DateTime dateTime=new DateTime();
        int year = dateTime.getYear()+1;
        String tableName=year+"_"+"fdl";
        String tableNameGl=year+"_"+"gl";
        historyFdlMapper.createHistoryFdlTable(tableName);
        historyGongLvMapper.createGongLvTable(tableNameGl);
    }

    @Scheduled(cron = "0 22 1 * * ?")
    public void insertRfdl(){
        DateTime dateTime=new DateTime();
        List<History> historyFdls=getHistoryFdl(dateTime);
        if(historyFdls!=null&&historyFdls.size()>0){
            int year = dateTime.getYear();
            String tableName=year+"_"+"fdl";
            try {
                historyFdlMapper.insertFdl(historyFdls,tableName);
            }catch (Exception e){
                //如果表不存在则创建表
                String exceptionMessage=e.getCause().getMessage();
                if(StringUtils.equals(exceptionMessage,"Table 'guangfu."+tableName+ "' doesn't exist")){
                    historyFdlMapper.createHistoryFdlTable(tableName);
                    historyFdlMapper.insertFdl(historyFdls,tableName);
                }
            }
        }

    }
    @Scheduled(cron = "0 0/10 5-20 * * ?")
    public void updateRfdl(){
        DateTime dateTime=new DateTime();
        List<History> historyFdls=getHistoryFdl(dateTime);
        if(historyFdls!=null&&historyFdls.size()>0){
            int year = dateTime.getYear();
            String tableName=year+"_"+"fdl";
            String time=dateTime.toString("d");
            String selectTime=dateTime.toString("yyyy-MM");
            historyFdlMapper.updateFdl(historyFdls,tableName,time,selectTime);
        }
    }

    private List<History> getHistoryFdl(DateTime dateTime){
        String time=dateTime.toString("d");
        String selectTime=dateTime.toString("yyyy-MM");
        List<History> historyFdls=new ArrayList<>();
        for(String stationXuLieHao:MapCacheUtils.stationXuLieHaoSet){
            StationShiShiVo stationShiShiVo=(StationShiShiVo) MapCacheUtils.shiShiMap.get(stationXuLieHao);
            if(stationShiShiVo!=null){
                History historyFdl=new History();
                historyFdl.setId(idWorker.nextId()+"");
                historyFdl.setCount(stationShiShiVo.getRfdl());
                historyFdl.setLeiXing("日发电量");
                historyFdl.setTime(time);
                historyFdl.setSelectTime(selectTime);
                historyFdl.setXuLieHao(stationXuLieHao);
                historyFdls.add(historyFdl);
            }
        }
        /*for(SheBeiVo sheBeiVo:MapCacheUtils.sheBeiXuLieHaoSet){
            History historyFdl=new History();
            historyFdl.setId(idWorker.nextId()+"");
            historyFdl.setCount(Double.parseDouble(sheBeiVo.getRfdl()));
            historyFdl.setLeiXing("日发电量");
            historyFdl.setTime(time);
            historyFdl.setSelectTime(selectTime);
            historyFdl.setXuLieHao(sheBeiVo.getSheBeiXuLieHao());
            historyFdls.add(historyFdl);
        }*/
        return historyFdls;
    }

    private List<History> getHistoryGongLv(DateTime dateTime){
        String time=dateTime.toString("HH:mm");
        String selectTime=dateTime.toString("yyyy-MM-dd");
        List<History> historyGls=new ArrayList<>();
        for(String stationXuLieHao:MapCacheUtils.stationXuLieHaoSet){
            StationShiShiVo stationShiShiVo=(StationShiShiVo) MapCacheUtils.shiShiMap.get(stationXuLieHao);
            if(stationShiShiVo!=null){
                History historyGl=new History();
                historyGl.setId(idWorker.nextId()+"");
                historyGl.setCount(stationShiShiVo.getGongLv());
                historyGl.setTime(time);
                historyGl.setSelectTime(selectTime);
                historyGl.setXuLieHao(stationXuLieHao);
                historyGls.add(historyGl);
            }
        }
        for(SheBeiVo sheBeiVo:MapCacheUtils.sheBeiXuLieHaoSet){
            History historyGl=new History();
            historyGl.setId(idWorker.nextId()+"");
            historyGl.setCount(Double.parseDouble(sheBeiVo.getLv()));
            historyGl.setTime(time);
            historyGl.setSelectTime(selectTime);
            historyGl.setXuLieHao(sheBeiVo.getSheBeiXuLieHao());
            historyGls.add(historyGl);
        }
        return historyGls;
    }
    /**
     * 添加功率
     */
    @Scheduled(cron = "0 0/5 5-20 * * ?")
    public void insertGongLv(){
        DateTime dateTime=new DateTime();
        List<History> historyGls=getHistoryGongLv(dateTime);
        if(historyGls!=null&&historyGls.size()>0){
            int year = dateTime.getYear();
            String tableName=year+"_"+"gl";
            try {
                historyGongLvMapper.insertGongLv(historyGls,tableName);
            }catch (Exception e){
                //如果表不存在则创建表
                String exceptionMessage=e.getCause().getMessage();
                if(StringUtils.equals(exceptionMessage,"Table 'guangfu."+tableName+ "' doesn't exist")){
                    historyGongLvMapper.createGongLvTable(tableName);
                    historyGongLvMapper.insertGongLv(historyGls,tableName);
                }
            }
        }
    }

    /**
     * 插入一年中每月的发电量
     * 每月1号2点执行
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void insertYearFdl(){
        List<History> histories=new ArrayList<>();
        DateTime dateTime=new DateTime();
        String time=dateTime.toString("M");
        String selectTime=dateTime.toString("yyyy");
        for(String stationXuLieHao:MapCacheUtils.stationXuLieHaoSet){
            StationShiShiVo stationShiShiVo=(StationShiShiVo) MapCacheUtils.shiShiMap.get(stationXuLieHao);
            if(stationShiShiVo!=null){
                History history=new History();
                history.setCount(stationShiShiVo.getRfdl());
                history.setId(idWorker.nextId()+"");
                history.setLeiXing("月发电量");
                history.setTime(time);
                history.setSelectTime(selectTime);
                history.setXuLieHao(stationXuLieHao);
                histories.add(history);
            }
        }
        int year = dateTime.getYear();
        String tableName=year+"_"+"fdl";
        historyFdlMapper.insertFdl(histories,tableName);
    }

    /**
     * 更新一年中每月的发电量
     */
    @Scheduled(cron = "0 0/5 5-20 * * ?")
    public void updateYearFdl(){
        DateTime dateTime=new DateTime();
        String selectTime=dateTime.toString("yyyy");
        String selectTime2=dateTime.toString("yyyy-MM");
        String tableName=dateTime.getYear()+"_fdl";
        String month=dateTime.getMonthOfYear()+"";
        Double yfdl=0.0;
        List<History> historiesYfdls=new LinkedList<>();
        for(String stationXuLieHao:MapCacheUtils.stationXuLieHaoSet){
            //查询当月的所有天数的发电量
            List<History> histories= historyFdlMapper.selectYfdl(selectTime2,stationXuLieHao,tableName);
            if(histories!=null&&histories.size()>0){
                if(histories.size()==1){
                    yfdl=histories.get(0).getCount();
                }else {
                    for(int i=0;i<histories.size()-1;i++){
                        yfdl+=histories.get(i).getCount();
                    }
                    StationShiShiVo stationShiShiVo=(StationShiShiVo) MapCacheUtils.shiShiMap.get(stationXuLieHao);
                    yfdl+=stationShiShiVo.getRfdl();
                }
                History history=new History();
                history.setCount(yfdl);
                history.setXuLieHao(stationXuLieHao);
                historiesYfdls.add(history);
            }
            yfdl=0.0;
        }
        if(historiesYfdls!=null&&historiesYfdls.size()>0){
            historyFdlMapper.updateFdl(historiesYfdls,tableName,month,selectTime);
        }
    }

    /**
     * 插入年发电量
     * 每年的1月1日3点插入年发电量
     */
    @Scheduled(cron = "0 0 3 1 1 ? ")
    //@Scheduled(cron = "0 39 17 29 5 ? ")
    public void insertYear(){
        DateTime dateTime=new DateTime();
        String time=dateTime.toString("yyyy-MM-dd HH:mm:ss");
        int year=dateTime.getYear();
        List<YearFdl> yearFdls=new ArrayList<>();
        for(String stationXuLieHao:MapCacheUtils.stationXuLieHaoSet){
            YearFdl yearFdll=new YearFdl();
            yearFdll.setId(idWorker.nextId()+"");
            yearFdll.setCount(0.0);
            yearFdll.setYear(year);
            yearFdll.setXuLieHao(stationXuLieHao);
            yearFdll.setUpdateTime(time);
            yearFdls.add(yearFdll);
        }
        historyFdlMapper.insertYearFdl(yearFdls);
    }

    /**
     * 更新年发电量
     */
    //@Scheduled(cron = "0 0/1 5-20 * * ?")
    @Scheduled(initialDelay = 5000,fixedDelay = 5000)
    public void updateEveryYearFdl(){
        DateTime dateTime=new DateTime();
        String time=dateTime.toString("yyyy-MM-dd HH:mm:ss");
        int year=dateTime.getYear();
        String selectTime=year+"";
        String tableName=year+"_fdl";
        Double yearFdl=0.0;
        List<YearFdl> yearFdls=new ArrayList<>();
        for(String stationXuLieHao:MapCacheUtils.stationXuLieHaoSet){
            List<History> histories= historyFdlMapper.selectYfdl(selectTime,stationXuLieHao,tableName);
            if(histories!=null&&histories.size()>0){
                for(History history:histories){
                    yearFdl+=history.getCount();
                }
                YearFdl yearFdl1=new YearFdl();
                yearFdl1.setCount(yearFdl);
                yearFdl1.setUpdateTime(time);
                yearFdl1.setXuLieHao(stationXuLieHao);
                yearFdls.add(yearFdl1);
                yearFdl=0.0;
            }
        }
        if(yearFdls!=null&&yearFdls.size()>0){
            historyFdlMapper.updateYearFdl(yearFdls,year+"");
        }
    }
}
