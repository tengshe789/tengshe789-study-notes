package tech.tengshe789.hello_netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @program: hello_netty
 * @description: 自定义助手类
 * @author: tEngSHe789
 * @create: 2018-10-11 22:07
 **/
//SimpleChannelInboundHandler相当于入境
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject message) throws Exception {
        // 获取channel
        Channel channel = channelHandlerContext.channel();

        if (message instanceof HttpRequest){
            //显示客户端远程地址
            System.out.println(channel.remoteAddress());

            // 定义发送的数据消息
            // Unpooled.copiedBuffer 使用深拷贝复制缓冲区内容
            ByteBuf content = Unpooled.copiedBuffer("Hello netty~", CharsetUtil.UTF_8);

            //创建一个http response
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,//http协议
                    HttpResponseStatus.OK,//http response状态
                    content);//内容

            //为响应头增加数据类型
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/lain");//文本类型
            //为响应头增加内容长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //把响应刷到客户端
            channelHandlerContext.writeAndFlush(response);
        }
    }

    /**
     *  看netty生命周期（结果：并没有保持长连接）
     * @param ctx
     * @throws Exception
     */

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel移除");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel是活跃状态");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel不活跃");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel读数据读完");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("用户事件触发");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel可写事件被更改");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常的时候，捕获当前异常");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("助手类添加");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("助手类移除");
        super.handlerRemoved(ctx);
    }
}
