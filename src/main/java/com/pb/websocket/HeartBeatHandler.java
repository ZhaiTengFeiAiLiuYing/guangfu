package com.pb.websocket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.Map;
/**
 * Created by Administrator on 2019/5/20.
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    //触发用户事件的时候进入
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断evt是否是IdleStateEvent(包含读空闲，写空闲，读写空闲)
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event=(IdleStateEvent) evt;
            //如果进入读写空闲则我们认为和前端的心调已经断开，我们就关闭对应的channel
            if(event.state()== IdleState.ALL_IDLE) {
                //获取心跳读写超时的客户端channel
                Channel channel=ctx.channel();
                //根据channel查找用户id并修改在线状态
                Map<String, Channel> map=UserChannelRel.map;
                String userId = null;
                for(Map.Entry<String, Channel> entry:map.entrySet()){
                    if(entry.getValue()==channel) {
                        userId=entry.getKey();
                    }
                }
                //移除userChannelRel中的channel
                UserChannelRel.map.remove(userId);
                //关闭channel
                channel.close();
            }
        }
    }
}
