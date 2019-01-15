package tech.tengshe789.hello_netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @program: hello_netty
 * @description: 设置channel初始化器(channal注册后，会执行相应的初始化方法)
 * @author: tEngSHe789
 * @create: 2018-10-11 21:57
 **/
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    //每个channel由多个handler共同组成管道
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 通过socketChannel获得对应的管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 通过管道添加handler。
        //HttpServerCodec是netty提供的助手类，可以理解为拦截器
        //当请求到服务端，我们需要做编解码，相应到客户端可以做编码
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        //添加自定义助手类，返回hello
        pipeline.addLast("CustomHandler",new CustomHandler());
    }
}
