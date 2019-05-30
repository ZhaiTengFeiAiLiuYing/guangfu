package com.pb.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by Administrator on 2019/5/20.
 */
public class WebsocketInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline=socketChannel.pipeline();
        //用于支持http协议，websocket基于http协议，需要有http编解码器
        pipeline.addLast(new HttpServerCodec());
        //对大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //添加对http请求和响应的的聚合器，只要是有netty进行http编程都需要使用
        //对httpMessage进行聚合，聚合成fullHttpRequest或者fullHttpResponse
        //在netty编程中都需要使用handler,用来解析post请求中的body信息
        pipeline.addLast(new HttpObjectAggregator(1024*64));
        //以上用于支持http协议

        //增加心跳支持
        //如果客户端1分钟没有向服务端发送读写心跳（ALL),则主动断开
        //如果读空闲或者写空闲不处理
        pipeline.addLast(new IdleStateHandler(50,50,60));
        //自定义空闲状态
        pipeline.addLast(new HeartBeatHandler());
        //以下用于支持httpwebsocket
        //websocket服务器处理的协议，用于指定给客户端连接访问的路由/ws
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //添加自定义的handler
        pipeline.addLast(new DataHandler());
    }
}
