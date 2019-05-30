package com.pb.service;
import com.pb.enums.SocketEnum;
import com.pb.mapper.SheBeiMapper;
import com.pb.mapper.SheBeiCustomMapper;
import com.pb.mapper.ShiShiMapperCustom;
import com.pb.mapper.StationMapper;
import com.pb.pojo.SheBei;
import com.pb.pojo.Shishi;
import com.pb.pojo.Station;
import com.pb.utils.JsonUtils;
import com.pb.utils.MapCacheUtils;
import com.pb.vo.SheBeiVo;
import com.pb.vo.ShiShiSheBeiVo;
import com.pb.vo.StationShiShiVo;
import com.pb.websocket.DataHandler;
import com.pb.websocket.UserChannelRel;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.*;

/**
 * Created by Administrator on 2019/5/20.
 */
@Service
@Transactional
public class ShiShiService  {
    @Autowired
    private ShiShiMapperCustom shiShiMapperCustom;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private SheBeiMapper sheBeiMapper;
    @Autowired
    private SheBeiCustomMapper sheBeiMapperCustom;
    /**
     * 查询所有的电站和电站对应的设备
     */
    @Scheduled(initialDelay = 5000,fixedDelay = 70000)
    public void findAllStationAndSheBei(){
        List<Station> stations=stationMapper.selectAll();//查询所有的电站
        /**
         * 遍历循环电站，查询每个电站的设备
         */
        if(stations!=null && stations.size()>0){
            for(Station station:stations){
                SheBeiVo sheBeiVo=new SheBeiVo();
                //查询每个电站下的设备，包括设备基本信息和设备基本实时数据
                List<SheBei> sheBeis=sheBeiMapperCustom.selectSheBeiByStation(station.getZhanDianXuLieHao());
                if(sheBeis!=null&&sheBeis.size()>0){
                    List<SheBeiVo> sheBeiVos=new ArrayList<>();
                    for(SheBei sheBei:sheBeis){
                        if(StringUtils.isBlank(sheBeiVo.getSheBeiXuLieHao())){
                            sheBeiVo.setSheBeiXuLieHao(sheBei.getSheBeiXuLieHao());
                            sheBeiVo.setLeiXing(sheBei.getLeiXing());
                            sheBeiVo.setXingHao(sheBei.getXingHao());
                            sheBeiVo.setMingCheng(sheBei.getMingCheng());
                            setCeDianZhi(sheBeiVo,sheBei);
                        }else if(StringUtils.equals(sheBeiVo.getSheBeiXuLieHao(),sheBei.getSheBeiXuLieHao())){
                            setCeDianZhi(sheBeiVo,sheBei);
                        }else{
                            sheBeiVos.add(sheBeiVo);
                            sheBeiVo=new SheBeiVo();
                            setCeDianZhi(sheBeiVo,sheBei);
                        }
                    }
                    sheBeiVos.add(sheBeiVo);
                    Collections.sort(sheBeiVos, new Comparator<SheBeiVo>() {
                        @Override
                        public int compare(SheBeiVo o1, SheBeiVo o2) {
                            return o1.getMingCheng().compareTo(o2.getMingCheng());
                        }
                    });
                    MapCacheUtils.stationSheBeiMap.put(station.getZhanDianXuLieHao(),sheBeiVos);
                }
            }
        }
    }

    /**
     * 查询电站实时数据
     */
    @Scheduled(initialDelay = 10000,fixedDelay = 7000)
    public void selectStationShiShi(){
        //站点序列号和设备的对应
        Map<String,List<SheBeiVo>> stationSheBeiMap=MapCacheUtils.stationSheBeiMap;
        if(stationSheBeiMap!=null&&stationSheBeiMap.size()>0){
            for(Map.Entry<String,List<SheBeiVo>> entry:stationSheBeiMap.entrySet()){
                String stationXuLieHao=entry.getKey();
                List<SheBeiVo> sheBeiVos=entry.getValue();
                if(sheBeiVos!=null&&sheBeiVos.size()>0){
                    double rfdl=0;//站点日发电量
                    double leiJiFdl=0;//站点累计发电量
                    double lv=0;//站点累计功率
                    for(SheBeiVo sheBeiVo:sheBeiVos){
                        rfdl+=Double.parseDouble(sheBeiVo.getRfdl());
                        leiJiFdl+=Double.parseDouble(sheBeiVo.getLjFdl());
                        lv+=Double.parseDouble(sheBeiVo.getLv());
                    }
                    StationShiShiVo stationShiShiVo=new StationShiShiVo(rfdl,leiJiFdl,lv);
                    MapCacheUtils.shiShiMap.put(stationXuLieHao,stationShiShiVo);
                }
            }
        }
    }

    /**
     * 查询设备实时数据
     */
    @Scheduled(initialDelay = 15000,fixedDelay = 12000)
    public void selectSheBeiShiShi(){
        String ceDianName="ceDianMingCheng";
        // Map<String ,List<SheBei>> mapStations=null;
        Map<String ,List<SheBeiVo>> mapStations= MapCacheUtils.stationSheBeiMap;
        if(mapStations!=null&&mapStations.size()>0){
            for(Map.Entry<String,List<SheBeiVo>> entry:mapStations.entrySet()){
                List<SheBeiVo> sheBeis=entry.getValue();
                if(sheBeis!=null&&sheBeis.size()>0){
                    for(SheBeiVo sheBei:sheBeis){
                        String sheBeiXuLieHao=sheBei.getSheBeiXuLieHao();
                        Example shiShiExample=new Example(Shishi.class);
                        Example.Criteria shiShiCriteria=shiShiExample.createCriteria();
                        shiShiCriteria.andEqualTo("sheBeiXuLieHao",sheBeiXuLieHao);
                        Example.Criteria c=shiShiExample.createCriteria();
                        c.orEqualTo(ceDianName,"开关机状态").orEqualTo(ceDianName,"逆变器状态")
                                .orEqualTo(ceDianName,"交流有功功率").orEqualTo(ceDianName,"交流无功功率")
                                .orEqualTo(ceDianName,"逆变器效率").orEqualTo(ceDianName,"当日发电量")
                                .orEqualTo(ceDianName,"累计发电量").orEqualTo(ceDianName,"Uca")
                                .orEqualTo(ceDianName,"机内温度").orEqualTo(ceDianName,"电网频率")
                                .orEqualTo(ceDianName,"Uab").orEqualTo(ceDianName,"Ubc").orEqualTo(ceDianName,"Ia")
                                .orEqualTo(ceDianName,"Ib").orEqualTo(ceDianName,"Ic").orEqualTo(ceDianName,"PV1_U")
                                .orEqualTo(ceDianName,"PV2_U").orEqualTo(ceDianName,"PV3_U").orEqualTo(ceDianName,"PV4_U")
                                .orEqualTo(ceDianName,"PV1_I").orEqualTo(ceDianName,"PV2_I").orEqualTo(ceDianName,"PV3_I")
                                .orEqualTo(ceDianName,"PV4_I").orEqualTo(ceDianName,"PV5_I").orEqualTo(ceDianName,"PV6_I")
                                .orEqualTo(ceDianName,"PV7_I").orEqualTo(ceDianName,"PV8_I").orEqualTo(ceDianName,"PV9_I")
                                .orEqualTo(ceDianName,"PV10_I").orEqualTo(ceDianName,"PV11_I").orEqualTo(ceDianName,"PV12_I")
                                .orEqualTo(ceDianName,"MPPT1_P").orEqualTo(ceDianName,"MPPT2_P").orEqualTo(ceDianName,"MPPT3_P")
                                .orEqualTo(ceDianName,"MPPT4_P");
                        shiShiExample.and(c);
                        List<Shishi> shishis=shiShiMapperCustom.selectByExample(shiShiExample);
                        ShiShiSheBeiVo shiShiSheBeiVo=new ShiShiSheBeiVo();//返回给前台的设备实时数据
                        shiShiSheBeiVo.setSheBeiXuLieHao(sheBeiXuLieHao);
                        shiShiSheBeiVo.setShishis(shishis);
                        MapCacheUtils.shiShiMap.put(sheBeiXuLieHao,shiShiSheBeiVo);
                    }
                }
            }
        }

    }

    @Scheduled(initialDelay = 20000,fixedDelay = 7000)
    public  void tuisong(){
        for(Map.Entry<String,Channel> entry: UserChannelRel.map.entrySet()){
            String userId=entry.getKey();
            //获取用户channel
            Channel channel=entry.getValue();
            if(channel!=null){
                Channel userChannel=DataHandler.users.find(channel.id());
                if(userChannel!=null){
                    int type= MapCacheUtils.typeMap.get(userId);
                    //用户第一次连接，发送电站数据
                    if(type== SocketEnum.CONNET.getType()){
                        userChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.serialize(MapCacheUtils.shiShiMap.get(MapCacheUtils.xuLeiHaoMap.get(userId)))));
                        //用户请求电站数据
                    }else if(type== SocketEnum.STATION.getType()){
                        userChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.serialize(MapCacheUtils.shiShiMap.get(MapCacheUtils.xuLeiHaoMap.get(userId)))));
                    }else if(type==SocketEnum.SHEBEI.getType()){
                        //用户请求设备列表
                        userChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.serialize(MapCacheUtils.stationSheBeiMap.get(MapCacheUtils.xuLeiHaoMap.get(userId)))));
                    }else {
                        //用户请求具体设备的数据
                        userChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.serialize(MapCacheUtils.shiShiMap.get(MapCacheUtils.xuLeiHaoMap.get(userId)))));
                    }
                }
            }
        }
    }

    private void setCeDianZhi(SheBeiVo sheBeiVo,SheBei sheBei){
        String ceDianMingCheng=sheBei.getCeDianMingCheng();
        String ceDianZhi=sheBei.getCeDianZhi();
        if(StringUtils.equals("当日发电量",ceDianMingCheng)){
            sheBeiVo.setRfdl(ceDianZhi);
            sheBeiVo.setUpdatetime(sheBei.getUpdatetime());
        }else if(StringUtils.equals("累计发电量",ceDianMingCheng)){
            sheBeiVo.setLjFdl(ceDianZhi);
        }else if(StringUtils.equals("交流有功功率",ceDianMingCheng)){
            sheBeiVo.setLv(ceDianZhi);
        }else  if(StringUtils.equals("开关机状态",ceDianMingCheng)){
            sheBeiVo.setKaiGuanZhuangTai(ceDianZhi);
        }else  if(StringUtils.equals("逆变器状态",ceDianMingCheng)){
            sheBeiVo.setNiBianQingZhuangTai(ceDianZhi);
        }
    }

    //public void
}
