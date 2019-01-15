package tech.tengshe789.im.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @program: hello_netty
 * @description:
 * @author: tEngSHe789
 * @create: 2018-10-12 10:13
 **/
public class WebSocketServerInitialzer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //==================== 用于支持http协议============================================
        //websocket基于http协议
        pipeline.addLast(new HttpServerCodec());
        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //聚合器(对httpmessage进行聚合，成FullHttpRequest或FullHttpRespone,几乎在netty中都会只用此handler)
        pipeline.addLast(new HttpObjectAggregator(1024*64));//消息长度
        //==================================================================================


        /*
         *websocket服务处理的协议，用于指定给客户端连接的访问路由"/"
         * 这个handler会帮忙处理繁重的事
         * （帮你处理握手动作：handshaking，譬如ping+pong=心跳）
         * 对于websocket来讲，都是以frames进行传输的，不同数据类型对应的frame也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //自定义处理器
        pipeline.addLast(new ChatHandler());
    }
}
