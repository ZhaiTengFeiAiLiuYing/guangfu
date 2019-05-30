package com.pb.websocket;
import com.pb.bo.SocketDataBo;
import com.pb.enums.SocketEnum;
import com.pb.service.ShiShiService;
import com.pb.utils.JsonUtils;
import com.pb.utils.MapCacheUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2019/5/20.
 */
public class DataHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private ShiShiService shiShiService;
    //用来保存所有的客户端连接
    public static ChannelGroup users=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //客户端第一次连接
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //将新的通道加入到clients
        users.add(ctx.channel());
    }

    //客户端退出连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        remove(ctx);
    }
    //发生连接异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        remove(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //获取客户端传来的消息
        String text=textWebSocketFrame.text();
        SocketDataBo socketDataBo=JsonUtils.parse(text,SocketDataBo.class);
        int type=socketDataBo.getType();
        String userId=socketDataBo.getUserId();
        String xuLieHao=socketDataBo.getXuLieHao();
        Channel channel= channelHandlerContext.channel();
        MapCacheUtils.typeMap.put(userId,type);
        MapCacheUtils.xuLeiHaoMap.put(userId,xuLieHao);
        if(type==SocketEnum.CONNET.getType()){
            UserChannelRel.put(userId,channel);
            channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.serialize(JsonUtils.serialize(MapCacheUtils.shiShiMap.get(xuLieHao)))));
        }else if(type==SocketEnum.STATION.getType()){
            channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.serialize(JsonUtils.serialize(MapCacheUtils.shiShiMap.get(xuLieHao)))));
        }else if(type==SocketEnum.SHEBEI.getType()){
            channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.serialize(JsonUtils.serialize(MapCacheUtils.stationSheBeiMap.get(xuLieHao)))));
        }else {
            channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.serialize(MapCacheUtils.shiShiMap.get(xuLieHao))));
        }

        //channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("1"));
    }
    private void remove(ChannelHandlerContext ctx){
        Channel channel=ctx.channel();
        /*String userId=null;
        Map<String,Channel> map=UserChannelRel.map;
        for(Map.Entry<String,Channel> entry : map.entrySet()) {
            if(entry.getValue()==channel) {
                userId=entry.getKey();
                UserChannelRel.map.remove(userId);
            }
        }*/
        users.remove(channel);
        channel.close();
    }
}
