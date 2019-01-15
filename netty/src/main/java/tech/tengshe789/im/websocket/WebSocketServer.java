package tech.tengshe789.im.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @program: hello_netty
 * @description:
 * @author: tEngSHe789
 * @create: 2018-10-12 10:06
 **/
public class WebSocketServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup slaverGroup = new NioEventLoopGroup();

        try {
        ServerBootstrap server =new ServerBootstrap();
        server.group(masterGroup,slaverGroup).channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketServerInitialzer());

        ChannelFuture future = server.bind(8080).sync();
        future.channel().closeFuture().sync();
        }finally {
            masterGroup.shutdownGracefully();
            slaverGroup.shutdownGracefully();
        }
    }
}
