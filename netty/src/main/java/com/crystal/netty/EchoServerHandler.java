package com.crystal.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by xiaoou on 17/12/10 16:23.
 *
 * @version 1.0.0
 */
public class EchoServerHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        // 接收客户端发来的数据，使用buf.readableBytes()获取数据大小, 并转换成byte数组
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);

        // 将byte数组转成字符串，在控制台打印输出
        String body = new String(req, "UTF-8");
        System.out.println("receive data from client: " + body);

        // 将接收到的客户端发来的数据写回到客户端
        ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
        channelHandlerContext.write(resp);
    }

    /**
     * 发生异常，关闭链路
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 将发送缓冲区中的消息全部写入SocketChannel中
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
