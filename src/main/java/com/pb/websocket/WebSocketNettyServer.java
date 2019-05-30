package com.pb.websocket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;
/**
 * Created by Administrator on 2019/5/20.
 */
@Component
public class WebSocketNettyServer {
    private  static class SingleWebSocket{
        static final WebSocketNettyServer wsServer=new WebSocketNettyServer();
    }
    public static WebSocketNettyServer getInstance(){
        return SingleWebSocket.wsServer;
    }
    private EventLoopGroup bossGroup;//主线程池
    private EventLoopGroup workGroup;//工作线程池
    private ServerBootstrap server;//服务器
    private ChannelFuture future;//回调
    public void start(){
        this.future=server.bind(9001);
    }
    public WebSocketNettyServer(){
        bossGroup=new NioEventLoopGroup();
        workGroup=new NioEventLoopGroup();
        server=new ServerBootstrap();
        server.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebsocketInitializer());
    }
}
