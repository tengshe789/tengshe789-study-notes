package tech.tengshe789.hello_netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @program: hello_netty
 * @description: 实现客户端发送一个请求，服务端会返回hello netty
 * @author: tEngSHe789
 * @create: 2018-10-11 21:40
 **/
public class HelloNetty {
    public static void main(String[] args)  {
        // 创建一对主从线程组
        EventLoopGroup masterGroup = new NioEventLoopGroup();//主线程不分配工作
        EventLoopGroup slaverGroup =new NioEventLoopGroup();//从线程干活

        try {
        //快速创建netty服务器,ServerBootstrap是启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(masterGroup,slaverGroup) //设置主从线程组
                .channel(NioServerSocketChannel.class) //设置nio双向通道
                .childHandler(new ServerInitializer()); //子处理器,用于处理从线程

        //启动server，并且设置8888为启动的端口号，同时启动方式为同步
        ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
        //监听关闭的channal，设置同步方式
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            masterGroup.shutdownGracefully();//优雅方式关闭
            slaverGroup.shutdownGracefully();
        }

    }
}
