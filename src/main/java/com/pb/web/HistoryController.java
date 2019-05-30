package com.pb.web;
import com.pb.enums.ExceptionEnum;
import com.pb.enums.SuccessEnum;
import com.pb.exception.GfException;
import com.pb.pojo.History;
import com.pb.pojo.YearFdl;
import com.pb.service.HistoryService;
import com.pb.vo.HistoryVo;
import com.pb.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/28.
 */
@RestController
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    /**
     * 查询月中每日的发电量
     * @param month
     * @param xuLieHao
     * @return
     */
    @GetMapping("/yfdl/{month}/{xuLieHao}")
    public ResponseEntity<Result> selectYfdl(@PathVariable String month,@PathVariable String xuLieHao){
        if(StringUtils.isBlank(month)||StringUtils.isBlank(xuLieHao)){
            throw new GfException(ExceptionEnum.PARAMS_BE_NULL);
        }
        DateTime paramTime=DateTime.parse(month);
        //判断时间是否在当前时间后
        if(paramTime.isAfterNow()){
            throw new GfException(ExceptionEnum.TIME_BE_ERROR);
        }
        //未做，有时间实现，判断时间是否在有历史表之前
        List<History> historyFdls= historyService.selectYfdl(month,xuLieHao);
        if(historyFdls==null||historyFdls.isEmpty()){
            throw  new GfException(ExceptionEnum.SELECT_ERROR);
        }
        HistoryVo historyVo=new HistoryVo();
        List<String> countList= new LinkedList<>();
        List<String> timeList=new LinkedList<>();
        for(History historyFdl:historyFdls){
           countList.add(historyFdl.getCount()+"");
           timeList.add(historyFdl.getTime());
        }
        historyVo.setCount(countList);
        historyVo.setTime(timeList);
        return ResponseEntity.status(SuccessEnum.SUCCESS_SELECT.getCode())
                .body(new Result(SuccessEnum.SUCCESS_SELECT,historyVo));
    }

    @GetMapping("/gongLv/{date}/{xuLieHao}")
    public ResponseEntity<Result> selectGongLv(@PathVariable String date,@PathVariable String xuLieHao){
        if(StringUtils.isBlank(date)||StringUtils.isBlank(xuLieHao)){
            throw new GfException(ExceptionEnum.PARAMS_BE_NULL);
        }
        DateTime paramTime=DateTime.parse(date);
        //判断时间是否在当前时间后
        if(paramTime.isAfterNow()){
            throw new GfException(ExceptionEnum.TIME_BE_ERROR);
        }
        //未做，有时间实现，判断时间是否在有历史表之前
        List<History> histories=historyService.selectGongLv(date,xuLieHao);
        if(histories==null||histories.isEmpty()){
            throw  new GfException(ExceptionEnum.SELECT_ERROR);
        }
        HistoryVo historyVo=new HistoryVo();
        List<String> countList= new LinkedList<>();
        List<String> timeList=new LinkedList<>();
        for(History history:histories){
            countList.add(history.getCount()+"");
            timeList.add(history.getTime());
        }
        historyVo.setCount(countList);
        historyVo.setTime(timeList);
        return ResponseEntity.status(SuccessEnum.SUCCESS_SELECT.getCode())
                .body(new Result(SuccessEnum.SUCCESS_SELECT,historyVo));
    }

    @GetMapping("/fdl/{xuLieHao}")
    public ResponseEntity<Result> selectZongFdl(@PathVariable String xuLieHao){
        if(StringUtils.isBlank(xuLieHao)){
            throw new GfException(ExceptionEnum.PARAMS_BE_NULL);
        }

        List<YearFdl> yearFdls= historyService.selectZongFdl(xuLieHao);
        if(yearFdls==null||yearFdls.isEmpty()){
            throw  new GfException(ExceptionEnum.SELECT_ERROR);
        }
        HistoryVo historyVo=new HistoryVo();
        List<String> countList= new LinkedList<>();
        List<String> timeList=new LinkedList<>();
        for(YearFdl yearFdl:yearFdls){
            countList.add(yearFdl.getCount()+"");
            timeList.add(yearFdl.getYear()+"");
        }
        historyVo.setCount(countList);
        historyVo.setTime(timeList);
        return ResponseEntity.status(SuccessEnum.SUCCESS_SELECT.getCode())
                .body(new Result(SuccessEnum.SUCCESS_SELECT,historyVo));
    }
}
