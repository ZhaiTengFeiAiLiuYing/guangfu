package com.pb.utils;
import com.google.common.collect.Sets;
import com.pb.vo.SheBeiVo;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by Administrator on 2019/5/17.
 */
public class MapCacheUtils {
    //用来存储设备序列号和设备数据、电站序列号和电站数据
    public static Map<String,Object> shiShiMap=new Hashtable<>();
    //存储userId和请求的数据类型的映射，用来给webSocket推送数据
    public static Map<String ,Integer> typeMap=new Hashtable<>();
    //存储userId和请求的电站序列号或设备序列号的映射，用来推送数据
    public static Map<String,String> xuLeiHaoMap=new Hashtable<>();
    //存储电站序列号和设备的映射
    public static Map<String ,List<SheBeiVo>> stationSheBeiMap=new Hashtable<>();
    //用来存储所有的电站序列号
    public static Set<String> stationXuLieHaoSet= Sets.newConcurrentHashSet();
    //用来存储所有的设备信息包括设备序列号，设备名称，和发电量功率
    public static  Set<SheBeiVo>  sheBeiXuLieHaoSet=Sets.newConcurrentHashSet();

}
