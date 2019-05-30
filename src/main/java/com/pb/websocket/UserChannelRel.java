package com.pb.websocket;
import io.netty.channel.Channel;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/20.
 */
public class UserChannelRel {
    public static Map<String,Channel> map=new Hashtable<>();
    public static void put(String userId, Channel channel){
        map.put(userId,channel);
    }
    public static Channel get(String userId){
        return map.get(userId);
    }

}
