package tech.tengshe789.im.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @program: hello_netty
 * @description: 处理消息的hanndler
 * TextWebSocketFrame :在netty中，用于为websocket专门处理文本的对象,frame是消息的载体
 * @author: tEngSHe789
 * @create: 2018-10-12 11:02
 **/
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //ChannelGroup可以记录管理对应的channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext context,
                                TextWebSocketFrame message) throws Exception {
        //获取客户端传输过来的消息
        String content = message.text();
        System.out.println("接受到的数据：" + content);

        for (Channel channels : clients){
            channels.writeAndFlush(new TextWebSocketFrame("[服务器接受到消息： ]"
            + LocalDateTime.now()+""+"接受的消息为： " + content));
        }

//        clients.writeAndFlush(new TextWebSocketFrame("[服务器接受到消息： ]"
//                + LocalDateTime.now()+""+"接受的消息为： " + content));
    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channnle，并且放到channel group中进行管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        //当触发handlerRemoved，channel group会自动移除对应客户端的channel
//        clients.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id是："
                +ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id是："+
                ctx.channel().id().asShortText());
    }
}
